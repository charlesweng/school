import scala.io._
import cs162.assign3.syntax._
import Aliases._
import scala.io.Source.fromFile

//——————————————————————————————————————————————————————————————————————————————
// Main entry point

object Checker {
  type TypeEnv = scala.collection.immutable.HashMap[Var, Type]
  object Illtyped extends Exception

  var typeDefs = Set[TypeDef]()

  def main( args:Array[String] ) {
    val filename = args(0)
    val input = fromFile(filename).mkString
    Parsers.program.run(input, filename) match {
      case Left(e) => println(e)
      case Right(program) =>
        val prettied = Pretty.prettySyntax(program)
        typeDefs = program.typedefs

       // try {
          getType( program.e, new TypeEnv())
          println("This program is well-typed:\n")
          println(Pretty.prettySyntax(program))
       // }

        //catch { case Illtyped => println("program is ill-typed") }
    }
  }

  // Gets all the constructors associated with a given type name.
  // For example, consider the following typedefs:
  //
  // type Either = Left num | Right bool
  // type Maybe = Some num | None
  //
  // With respect to the above typedefs, `constructors` will return
  // the following underneath the given arguments:
  //
  // constructors(Label("Either")) = Map(Label("Left") -> NumT, Label("Right") -> BoolT)
  // constructors(Label("Maybe")) = Map(Label("Some") -> NumT, Label("None") -> UnitT)
  // constructors(Label("Fake")) throws Illtyped
  //
  def constructors(name: Label): Map[Label, Type] =
    typeDefs.find(_.name == name).map(_.constructors).getOrElse(throw Illtyped)

  def getType( e:Exp, env:TypeEnv ): Type =
    e match {
      // variables
      case x:Var => env.getOrElse(x,throw Illtyped)
      // numeric literals
      case _:Num => NumT

      // boolean literals
      case _:Bool => BoolT

      // `nil` - the literal for unit
      case _: NilExp => UnitT

      // builtin arithmetic operators
      case Plus | Minus | Times | Divide => FunT(Seq(NumT,NumT), NumT)

      // builtin relational operators
      case LT | EQ => FunT(Seq(NumT,NumT),BoolT)

      // builtin logical operators
      case And | Or => FunT(Seq(BoolT,BoolT),BoolT)

      // builtin logical operators
      case Not => FunT(Seq(BoolT),BoolT)

      // function creation
      case Fun(params, body) => { 
       val newEnv: TypeEnv = env ++ params
       val Tr = getType(body, newEnv)
       FunT(params.map(_._2),Tr)
      }
      // function call
      case Call(fun, args) => {
        val Tr: Type = getType(fun, env) 
        Tr match {
          case FunT(formalParams,returnType) => {
            val Ti = args.map((args:Exp) => getType(args,env))
            if (Ti == formalParams) {
              returnType 
            }
            else {
              throw Illtyped
            }
          } 
          case _ => throw Illtyped
        }
      } 

      // conditionals 
      case If(e1, e2, e3) => {
        val x1: Type = getType(e1, env)
        x1 match {
          case BoolT => {
            val x2 = getType(e2, env)
            val x3 = getType(e3, env)
            if (x2 == x3)
              x2
            else
              throw Illtyped
          }
          case _ => throw Illtyped
        }
      } 

      // let binding
      case Let(x, e1, e2) =>  {
        val t1: (Var, Type) = (x, getType(e1, env))
        getType(e2, env + t1)
      }

      // recursive binding
      case Rec(x, t1, e1, e2) => {
        val tx: (Var, Type) = (x, t1)
        val te1 = getType(e1,env + tx)
        if (te1 == t1) {
          getType(e2, env + tx)
        }
        else {
          throw Illtyped
        }
      } 

      // record literals
      case Record(fields) => {
        RcdT(fields.mapValues((ei: Exp) => getType(ei, env))) 
      } 

      // record access
      case Access(e, field) => {
        val et = getType(e, env) 
        et match {
          case RcdT(fields) => fields.getOrElse(field,throw Illtyped)
          case _ => throw Illtyped 
        }
      }

      // constructor use
      case Construct(name, constructor, e) => {
        val tname = constructors(name)
        val cons = tname.getOrElse(constructor, throw Illtyped)
        val mye = getType(e,env)
        cons match {
          case `mye` => TypT(name)
          case _ => throw Illtyped
        } 
        // if (cons == mye)
        //   TypT(name)
        // else
        //   throw Illtyped 
      }
      // pattern matching (case ... of ...)
      case Match(e, cases) => {
        val en = getType(e, env)  
        en match {
          case TypT(name) => {
            val tname = constructors(name)
            val conslabel = tname.map(_._1).toSeq
            val caselabel = cases.map(_._1).toSeq
            if (conslabel.toSet != caselabel.toSet || conslabel.length != caselabel.length) 
            {
              throw Illtyped
            }
            else
            {
             val mapcase = cases.map( ( c: (Label,Var,Exp) ) => {
              getType(c._3, env+(c._2->tname(c._1)))   
             }).distinct
             if(mapcase.length==1){
               mapcase.head
             }
             else throw Illtyped
            }

          }
          case _ => throw Illtyped
        }   
      } 
    }
}
