package miniprolog.interpreter

import miniprolog.syntax.lowlevel._

// There are three kinds of values in this language:
// 1.) Integers, represented by NumValue
// 2.) Structures which contain values, represented by Constructor
// 3.) A special placeholder for some other (possibly placeholder)
//     value.  This is represented by Placeholder.
sealed trait Value
case class NumValue(n: Int) extends Value
case class Constructor(name: String, values: List[Value]) extends Value
case class Placeholder(id: Int) extends Value

// Used to create a new Placeholder.  This should be the only
// place used to create new placeholders, which should all
// have unique ids.
object Placeholder {
  private var counter = 0
  def apply(): Placeholder = {
    val retval = Placeholder(counter)
    counter += 1
    retval
  }
}


// Representation of equivalence classes.  This maps
// placeholders to other, possibly placeholder, values.
// This allows for variable aliasing to occur.  For example, where
// P(1) means a placeholder with ID 1, with the following mapping:
// [P(1) -> P(2), P(2) -> P(3), P(4) -> NumValue(7)]
// ...P(1), P(2), and P(3) are all in the same equivalence class.
//
// Equivalence classes contain at most one non-placeholder value.
// It is guaranteed that a lookup on an equivalence class which
// contains a non-placeholder value will return the
// non-placeholder value.
//
// It is also assumed that whenever a variable lookup occurs, the
// equivalence relation will be consulted before the variable's value
// is considered.  For example, the environment may be like so:
// [Variable("X") -> P(0)]
// ...but the equivalence relation may be like so:
// [P(0) -> P(1), P(1) -> NumValue(42)]
// ...in this example, the value of the variable "X" should be
// the number 42, even though the environment doesn't map directly
// to 42 (it maps to 42 indirectly through the equivalence relation).
class EquivalenceRelation(val mapping: Map[Placeholder, Value]) {
  // Gets the set representative for the value `v`.
  // if `v` is a placeholder, then it should lookup whatever `v` maps
  // to recursively.  If `v` is a non-placeholder, then it should
  // simply return `v` as-is.
  def lookup(v: Value): Value = {
    v match {
      case ph: Placeholder => { 
      if (mapping.contains(ph))
        lookup(mapping(ph))
      else 
        ph
      }
      case _ => v
    }
  }

  // Adds a relation between `p` and `v`.  It should be the case that both
  // are set representatives.
  def addRelation(p: Placeholder, v: Value): EquivalenceRelation = new EquivalenceRelation(mapping + (p -> lookup(v)))
}

object Interpreter {
  import miniprolog.parser._
  import miniprolog.translator._

  // Takes a message to abort with.  The message is for your own debugging
  // purposes; we will not be using the message during grading
  def abortInterpreter(message: String): Nothing = {
    throw new Exception(message)
  }

  // Given a list of clauses, it returns a mapping of clause name, arity
  // pairs to clauses with that name and arity.  The clauses should be
  // in the same order as in the file.  It should be guaranteed that
  // there are no empty lists of clauses.
  def toClauseDB(clauses: Seq[Clause]): Map[(String, Int), List[Clause]] = {
    def matchNameArity(clause: Clause, clausedb: Map[(String,Int),List[Clause]]): Map[(String,Int),List[Clause]] = {
      val l: List[Clause] = clausedb.getOrElse((clause.name -> clause.params.length),List[Clause]()) :+ clause
      clausedb + ((clause.name -> clause.params.length) -> (clausedb.get((clause.name, clause.params.length)).fold(List(clause))(clause::_)))
    }
    val clausedb = clauses.foldRight(Map[(String, Int),List[Clause]]())(matchNameArity) 
    clausedb
  } 

  def makeQuery(rawQuery: String): Query =
    Translator.translateQuery(Parser.parseQuery(rawQuery))

  def makeInterpreter(clausesFile: String, query: String): Interpreter[NormalPrinter.type] = {
    import scala.io._
    val highlevel = Parser.parseClauses(Source.fromFile(clausesFile).mkString)
    val lowlevel = highlevel.map(Translator.translateClause)
    new Interpreter(Program(lowlevel, makeQuery(query)), NormalPrinter)
  }

  def main(args: Array[String]) {
    if (args.length != 2) {
      println(
        "Needs a file holding clauses and a query on the command line")
    } else {
      makeInterpreter(args(0), args(1)).run()
    }
  }
}

sealed trait Printer {
  def doPrint(what: String): Unit
}
object NormalPrinter extends Printer {
  def doPrint(what: String) {
    println(what)
  }
}
class TestingPrinter extends Printer {
  val outputs = scala.collection.mutable.ArrayBuffer.empty[String] // for tests generation

  def doPrint(what: String) {
    outputs += what
  }
}

// An interpreter is passed a program, and subsequently sets up
// an internal clause database.  The query held within the program
// is run by calling the run() method of the interpreter.
class Interpreter[P <: Printer](val program: Program, val printer: P) {
  import scala.collection.immutable.Stack
  import Interpreter.abortInterpreter

  // Type alias; with this declaration "Env" is synonymous with
  // the bulkier (and less informative) type Map[Variable, Value]
  type Env = Map[Variable, Value]

  // Representation for goals.  A goal is either a body (BodyGoal)
  // or the special restore goal (RestoreGoal) for restoring the
  // caller's environment after a clause call.
  sealed trait Goal
  case class BodyGoal(body: Body) extends Goal
  case class RestoreGoal(env: Env) extends Goal

  type GoalStack = Stack[Goal]
  type Choice = (Body, Env, EquivalenceRelation, GoalStack)
  type ChoiceStack = Stack[Choice]

  // the clause database
  val db: Map[(String, Int), List[Clause]] =
    Interpreter.toClauseDB(program.clauses)

  // Creates a new environment, given:
  // 1.) The local variables for the given environment
  // 2.) Variable/value pairs corresponding to values passed in.
  //     For example, for a clause foo(X, Y), if we call
  //     foo(3, 4), then we'd pass Seq(X -> 3, Y -> 4).
  //
  // It is assumed that there are no variables in common between the
  // local variables and the passed variables.  The translation guards
  // against this; for example, with the following:
  // foo(X) :- ... .
  // ?- X = 1, foo(X).
  // ...the translator performs certain renamings that prevent any
  // conflicts here.  It is, however, possible to pass incorrect
  // information to `newEnv`.
  def newEnv(local: Seq[Variable], passed: Seq[(Variable, Value)]): Env = {
    val newPlaceHolder = local.map ( 
                                      (v: Variable) => Placeholder()
                                   )    
    val zipped = local.zip(newPlaceHolder)
    val nEnv = (passed ++ zipped).toMap
    nEnv
  }

  // Attempts to unify v1 and v2, which are assumed to be set representatives.
  // Returns Some(equiv) if they could be unified; in this case, equiv is
  // the resulting possibly new equivalence relation.  Otherwise unify
  // returns None, indicating that v1 and v2 could not be unified.
  def unify(v1: Value, v2: Value, equiv: EquivalenceRelation): Option[EquivalenceRelation] = {

    val v1_equiv = equiv.lookup(v1)
    val v2_equiv = equiv.lookup(v2)
    (v1_equiv, v2_equiv) match { 
      case (x, y) if (x == y) => { 
        Some(equiv) 
      }
      case (ph: Placeholder, y) => { 
        Some(equiv.addRelation(ph,y)) 
      }
        case (x, ph: Placeholder) => { 
        Some(equiv.addRelation(ph,x)) 
      }
      case (Constructor(x, v3), Constructor(y, v4)) if ((x == y) && (v3.length == v4.length)) => 
      { 
        unifyTerms(v3 zip v4,equiv) 
      }
      case _ => 
      { 
        None 
      }
    }    
  } 

  // Unifies a sequence of possibly non-set representative values with
  // respect to the provided equivalence relation.  Behaves largely as
  // a helper function to unify.
  def unifyTerms(pairs: Seq[(Value, Value)], equiv: EquivalenceRelation): Option[EquivalenceRelation] = {
    pairs.foldLeft(Option(equiv))( (acc,v) => {
      acc match {
        case None => None
        case Some(newEquiv) => unify(v._1, v._2, newEquiv)
      }
    }
    )
  } 

  // Given some value which is assumed to be a set representative, it
  // will build a string representing what should be printed for the
  // value.  For a variable with ID 5, it will simply print _5.
  // For integers, it will print the integer.  For constructors, it
  // will print like so: constructorName(...), where ... represents
  // a recursive buildPrint call for each value.  Has special handling
  // for lists, to make the output easier to read (and for greater
  // consistency with Prolog).
  def buildPrint(v: Value, env: Env, equiv: EquivalenceRelation, inList: Boolean): String = {
    v match {
      case NumValue(n) => n.toString
      case Placeholder(n) => "_" + n.toString
      case Constructor(name, Nil) => {
        if (name == "[]") {
          if (inList) "]" else "[]"
        } else {
          name
        }
      }
      case Constructor(".", head :: tail :: Nil) => {
        val start = if (inList) ", " else "["
        (start + buildPrint(equiv.lookup(head), env, equiv, false) + 
         buildPrint(equiv.lookup(tail), env, equiv, true))
      }
      case Constructor(name, vars) => {
        (name + "(" + vars.map(v => buildPrint(equiv.lookup(v), env, equiv, false)).mkString(", ") + ")")
      }
    }
  }

  // Evaluates the given expression down to an integer.
  // If the expression involves a non-integer or an attempt
  // to divide by zero is made, then this calls `abortInterpreter`
  // with an appropriate message.
  def eval(exp: Exp, env: Env, equiv: EquivalenceRelation): Int = {
    //pattern mathc on exp
    // v match {
    //   case 
    // }
    exp match {
      case va @ Variable(name) => {
        val v = equiv.lookup(env(va))
        v match {
          case NumValue(num) => num
          case _ => abortInterpreter("val is not a number") 
        }    
      }
      case binop @ Binop(left, op, right) => {
        // val binop = equiv.lookup(env(bi))
        val l = eval(binop.left, env, equiv)
        val r = eval(binop.right, env, equiv)
        val o = binop.op
        o match {
          case Plus => l + r 
          case Minus => l - r 
          case Mul => l * r
          case Div => {
            if (r == 0)
              abortInterpreter("divide by zero")
            else
              l / r
          }  
        }
      }
    }
  }
  // Compares two integers with respect to the given operator.
  def compare(n1: Int, op: ROP, n2: Int): Boolean = {
    op match {
      case LT => n1 < n2
      case LE => n1 <= n2
      case GT => n1 > n2
      case GE => n1 >= n2
      case EQ => n1 == n2
      case NE => n1 != n2
    }
  }

  // Actually runs the interpreter's query.
  def run() {
    // setting up of local variables
    var env: Env = newEnv(program.query.vars, Seq())
    var equiv: EquivalenceRelation = new EquivalenceRelation(Map())
    var goalStack: GoalStack = Stack(BodyGoal(program.query.body))
    var choiceStack: ChoiceStack = Stack()

    while (goalStack.nonEmpty) {
      val (goal, newGoalStack) = goalStack.pop2
      goalStack = newGoalStack
      goal match {
        case BodyGoal(And(body1, body2)) => {
          goalStack = goalStack.push(BodyGoal(body2))
          goalStack = goalStack.push(BodyGoal(body1))
        }
        case BodyGoal(Or(body1, body2)) => {
          val choice: Choice = (body2, env, equiv, goalStack)
          choiceStack = choiceStack.push(choice)
          goalStack = goalStack.push(BodyGoal(body1))

        }

        case BodyGoal(Unify(x1, x2)) => {
          val v1 = equiv.lookup(env(x1))
          val v2 = equiv.lookup(env(x2))
          val nEquiv = unify(v1,v2,equiv) 
          nEquiv match {
            case Some(newEquiv) => {equiv = newEquiv}
            case None => {goalStack = goalStack.push(BodyGoal(False))}
          }
        }

        case BodyGoal(Check(name, vars)) => {
          val clauses = db((name,vars.size))

          if (clauses.isEmpty)
            abortInterpreter("clause is empty") 
          else {
            val clause1: Clause = clauses.head
            val clauset: List[Clause] = clauses.tail
            goalStack = goalStack.push(RestoreGoal(env))
            val vals = vars.map((v: Variable) => equiv.lookup(env(v)))
            clauset.reverse.foreach { (cl: Clause) =>
              val envC = newEnv(cl.localVars, cl.params zip vals)
              env = envC
              choiceStack = choiceStack.push((cl.body,env,equiv,goalStack))
            } // end of for each
            val envC = newEnv(clause1.localVars,clause1.params zip vals)
            env = envC
            goalStack = goalStack.push(BodyGoal(clause1.body))
          } // end of else
        }

        case RestoreGoal(envR) => {
          env = envR
        }

        case BodyGoal(Compare(x1, op, x2)) => {
          val v1 = equiv.lookup(env(x1))
          val v2 = equiv.lookup(env(x2))
          (v1, v2) match {
            case (NumValue(n1), NumValue(n2)) => {
              if (compare(n1, op, n2) == false)
                goalStack = goalStack.push(BodyGoal(False))
            }
            case (_,_) => abortInterpreter("v1 or v2 not a number")
          }
        }

        case BodyGoal(Bind(x, rhs)) => {
          val v1 = equiv.lookup(env(x))
          // val v2 = eval(rhs,env,equiv)
          v1 match {
            case ph: Placeholder => {
              rhs match {
                case ex: Exp => {
                  val v2 = NumValue(eval(ex, env, equiv))
                  equiv = equiv.addRelation(ph,v2)
                }
                case Num(n) => {
                  val v2 = NumValue(n)
                  equiv = equiv.addRelation(ph,v2)
                }
                case Structure(name, vars) => {
                  val v2 = Constructor(name,vars.map((va:Variable)=>equiv.lookup(env(va))))
                  equiv = equiv.addRelation(ph,v2)  
                }
              }   
            }
            case _ => //abortInterpreter("v1 not a placeholder")
          }
        } 

        case BodyGoal(True) => ()

        case BodyGoal(False) => {

          if (choiceStack.isEmpty) {
            env = Map() 
            goalStack = Stack() 
          }
          else {
            val ((body,envC,equivC,goalStackC),newChoiceStack) = choiceStack.pop2
            env = envC 
            equiv = equivC
            goalStack = goalStackC.push(BodyGoal(body))
            choiceStack = newChoiceStack
          }
        }
        case BodyGoal(Print(x)) => {
          printer.doPrint(buildPrint(equiv.lookup(env(x)), env, equiv, false))
        }
      } // goalStack.pop2
    } // while
  } // run
} // Interpreter

