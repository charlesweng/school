import scala.io._
import cs162.assign4.syntax._
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
          println(Pretty.prettySyntax(program))
          getType( program.e, new TypeEnv())
          println("This program is well-typed")
          ///println(Pretty.prettySyntax(program))
       // } catch { case Illtyped => println("failed.") }
    }
  }

  // Gets a listing of the constructor names associated with a given type definition.
  // For example, consider the following type definition:
  //
  // type Either['A, 'B] = Left 'A | Right 'B
  //
  // Some example calls to `constructors`, along with return values:
  //
  // constructors("Either") = Set("Left", "Right")
  // constructors("Foo") = a thrown Illtyped exception
  //
  def constructors(name: Label): Set[Label] =
    typeDefs.find(_.name == name).map(_.constructors.keySet).getOrElse(throw Illtyped)

  // Takes the following parameters:
  // -The name of a user-defined type
  // -The name of a user-defined constructor in that user-defined type
  // -The types which we wish to apply to the constructor
  // Returns the type that is held within the constructor.
  //
  // For example, consider the following type definition:
  //
  // type Either['A, 'B] = Left 'A | Right 'B
  //
  // Some example calls to `constructorType`, along with return values:
  //
  // constructorType("Either", "Left", Seq(NumT)) = NumT
  // constructorType("Either", "Right", Seq(BoolT)) = BoolT
  // constructorType("Either", "Left", Seq(NumT, NumT)) = a thrown Illtyped exception
  // constructorType("Either", "Foo", Seq(UnitT)) = a thrown Illtyped exception
  // constructorType("Bar", "Left", Seq(UnitT)) = a thrown Illtyped exception
  //
  def constructorType(name: Label, constructor: Label, types: Seq[Type]): Type = 
    (for {
      td <- typeDefs.find(_.name == name)
      rawType <- td.constructors.get(constructor)
      if (types.size == td.tvars.size)
    } yield replace(rawType, td.tvars.zip(types).toMap)).getOrElse(throw Illtyped)

  // Given a type and a mapping of type variables to other types, it
  // will recursively replace the type variables in `t` with the
  // types in `tv2t`, if possible.  If a type variable isn't
  // in `tv2t`, it should simply return the original type.  If a
  // `TFunT` is encountered, then whatever type variables it defines
  // (the first parameter in the `TFunT`) should overwrite whatever is in
  // `tv2t` right before a recursive `replace` call.  In other words,
  // type variables can shadow other type variables.
  //
  def replace( t:Type, tv2t:Map[TVar, Type] ): Type =
    t match {
      case NumT | BoolT | UnitT => t 

      case FunT(params, ret) => {
        FunT(params.map((myt: Type) => replace(myt,tv2t)),replace(ret,tv2t))
      }
      //((A)->B)[A->bool,B->num] = (bool->num)

      case RcdT(fields) => {
        RcdT(fields.mapValues((ei: Type) => replace(ei, tv2t)))
      } 
      //(|x:A,y:B|)[A->bool,B->num] = (|x:bool, y:num|)

      case TypT(name, typs) => {
        TypT(name, typs.map((myt: Type) => replace(myt, tv2t)))
      } 
      //(Either[A,B])[A->bool,B->num] = Either[bool,num]

      case tv:TVar => tv2t.getOrElse(tv,t) 
      //A[..,A->num,...] = num
      //B[A->num] = B 

      case TFunT(tvars, funt) => {
        val newtv2t = tv2t -- tvars 
        // val myfunt: FunT = replace(funt, newtv2t)
        TFunT(tvars,replace(funt,newtv2t).asInstanceOf[FunT])
      } 
      //[A]((x:A)=>x)
      //    |_______|
      //        A->A

    }
    // [a,b]((a)->b*c)[a|->bool,b|->num,c|->unit]
    // = [a,b]((a)->b*unit)
  // HINT - the bulk of this remains unchanged from the previous assignment.
  // Feel free to copy and paste code from your last submission into here.
  def getType( e:Exp, env:TypeEnv ): Type =
    e match {
      // variables
      case x:Var => env.getOrElse(x,throw Illtyped)
      // numeric literals
      case _:Num => NumT

      // boolean literals
      case _:Bool => BoolT

      // `nil` - the literal for unit
      case _:Unit => UnitT

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
      case c @ Construct(name, constructor, typs, e) => {
        // val tname = constructorsType()
        // val cons = tname.getOrElse(constructor, throw Illtyped)
        // val mye = getType(e,env)
        // cons match {
        //   case `mye` => TypT(name)
        //   case _ => throw Illtyped
        // } 
        // if (cons == mye)
        //   TypT(name)
        // else
        //   throw Illtyped 
        val tname = constructorType(name,constructor,typs)

        val mye = getType(e,env)

        tname match {
          case `mye` => TypT(name,typs)
          case _ => throw Illtyped
        }
      }
      // pattern matching (case ... of ...)
      case Match(e, cases) => {
        val en = getType(e, env)  
        en match {
          case TypT(name,typs) => { 
            val setlabel = constructors(name)
            val caselabel = cases.map(_._1).toSeq
            if (setlabel != caselabel.toSet || setlabel.toSeq.length != caselabel.length) 
            {
              throw Illtyped
            }
            else
            {
             val mapcase = cases.map( ( c: (Label,Var,Exp) ) => {
              getType(c._3, env+(c._2->constructorType(name,c._1,typs)))   
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

      case TAbs(tvars, fun) => {
        TFunT(tvars, getType(fun,env).asInstanceOf[FunT])
      }

      case TApp(e, typs) => {
        val mye = getType(e,env)
        mye match {
          case TFunT(tvars, funt) => {
            replace(funt, tvars.zip(typs).toMap)
          }
          case _ => {
            throw Illtyped
          }
        }
      }//end of TApp

    }
}
