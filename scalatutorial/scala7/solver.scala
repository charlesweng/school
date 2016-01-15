package miniprologfd.solver

// note the renaming that is being done by these imports
import scala.collection.mutable.{ Map => MMap }
import miniprologfd.interpreter.{ Value, NumValue, SymPlaceholder => Variable }
import miniprologfd.syntax.lowlevel.{ SymbolicROP => ROP, SymEQ => EQ,
  SymNE => NE, SymLT => LT, SymLE => LE, SymGT => GT, SymGE => GE }


// an interval of integers from 0 to Intervals.maxValue, inclusive
case class Interval(low:Int, hi:Int) {
  assert(low >= 0 && hi >= low && hi <= Intervals.maxValue)
  override def toString = "[" + low + ".." + hi + "]"
}


// represents the possible values of a variable as a list of
// intervals. by construction this list contains non-overlapping
// intervals in ascending order.
case class Intervals(ns:List[Interval]) {
  // are there no possible values?
  def isEmpty:Boolean =
    ns.isEmpty

  // apply function f to every possible value
  def foreach( f:Int => Unit ): Unit =
  {

    ns.foreach(i => 
    {
      i.low.to(i.hi).foreach(
        f
      )
    }
      )
  }

  // compute the intersection of these possible values and another set
  // of possible values
  def intersect( i:Intervals ): Intervals =
  {

    def min(a: Int, b: Int): Int =
    {
      if (a < b)
        a
      else
        b
    }

    def max(a: Int, b: Int): Int =
    {
      if (a > b)
        a
      else
        b
    }

    val newns: List[Interval] = for {
        ii <- i.ns
        nsi <- ns
        l = max(ii.low, nsi.low) 
        r = min(ii.hi, nsi.hi) 
        if (l <= r)
    } yield Interval(l, r) 
    Intervals(newns)
  }

  // is there only a single possible value?
  def singleton:Boolean =
  {
    if (ns.size == 1) {
      if (ns.head.low == ns.head.hi)
        true
      else
        false
    }
    else
      false
  }

  // retrieve the single possible value (only valid if there is only a
  // single possible value to retrieve)
  def getSingleton:Int =
  {
    if (singleton == true)
      ns.head.low
    else
      throw new Exception()
  }

  // remove a possible value from the current set of possible values
  def -( n:Int ): Intervals =
  {
    //use intersect to do this 
    //you want to either eliminate the endingpoint or startpoint of each interval
    // intersect(Intervals(List[Interval](Interval(0,n-1),Interval(n+1,Intervals.maxValue))))
        intersect(Intervals.getIntervals(miniprologfd.syntax.lowlevel.SymNE, n))
  }

  // return the lowest currently possible value
  def lowest:Int =
  {
    ns.head.low 
  }

  // return the highest currently possible value
  def highest:Int =
  {
    ns.last.hi
  }

  // returns all currently possible values < n
  def LT( n:Int ): Intervals =
  {
      // if (n < 0)
      // {
      //   Intervals(List[Interval]());
      // }
      // else if (!ns.isEmpty)
      // {
        intersect(Intervals.getIntervals(miniprologfd.syntax.lowlevel.SymLT, n))
      // intersect(Intervals(List[Interval](Interval(0,n-1))))
      // }
      // else
      // {
      //   Intervals(List[Interval](Interval(0,n-1)))
      // }
  }

  // returns all currently possible values ≤ n
  def LE( n:Int ): Intervals =
  {
      // if (n < 0)
      // {
      //   Intervals(List[Interval]());
      // }
      // else if (!ns.isEmpty)
      // {
        intersect(Intervals(List[Interval](Interval(0,n))))
      // }
      // else
      // {
      //   Intervals(List[Interval](Interval(0,n)))
      // }

  }

  // returns all currently possible values > n
  def GT( n:Int ): Intervals =
  {
      // if (n >= Intervals.maxValue)
      // {
      //   Intervals(List[Interval]());
      // }
      // else if (!ns.isEmpty)
      // {
        intersect(Intervals(List[Interval](Interval(n+1,Intervals.maxValue))))
      // }
      // else
      // {
      //   Intervals(List[Interval](Interval(n+1,Intervals.maxValue)))
      // }

  }

  // returns all currently possible values ≥ n
  def GE( n:Int ): Intervals =
  {
      // if (n >= Intervals.maxValue)
      // {
      //   Intervals(List[Interval]());
      // }
      // else if (!ns.isEmpty)
      // {
      intersect(Intervals(List[Interval](Interval(n,Intervals.maxValue))))
      // }
      // else
      // {
      //   Intervals(List[Interval](Interval(n,Intervals.maxValue)))
      // }

  }

  override def toString =
    ns.mkString(", ")
}


// companion object with useful functions
object Intervals {
  // maximum possible value
  val maxValue = 500

  // default Intervals value, i.e., the default set of possible values
  // for any variable
  val default = Intervals(List(Interval(0, maxValue)))

  // create an Intervals with a single possible value
  def apply( n:Int ): Intervals =
    Intervals(List[Interval](Interval(n,n)))

  // create an Intervals with a set of contiguous possible values in
  // the range low to hi
  def apply( low:Int, hi:Int ): Intervals =
    Intervals(List[Interval](Interval(low,hi)))

  // Create an Intervals containing all possible values that have the
  // given relation op with the given integer n.  `max` below refers
  // to the `maxValue` constant defined above.
  //
  // examples: getIntervals(<,  10) = [[0..9]]
  //           getIntervals(=,  10) = [[10..10]]
  //           getIntervals(≠,  10) = [[0..9], [11..max]]
  //           getIntervals(<,   0) = []          // less than minimum
  //           getIntervals(>, max) = []          // greater than maximum
  //           getIntervals(=,  -1) = []          // out of range
  //           getIntervals(>,  -5) = [[0..max]]  // the whole valid range
  def getIntervals( op:ROP, n:Int ): Intervals =
  {
    val ivs = op match 
    {
      case EQ => 
      {
        if (n < 0 || n > maxValue)
          Intervals(List[Interval]())
        else
          Intervals(List[Interval](Interval(n,n)))
      }
      case NE => 
      {
        if (n <= 0 || n >= maxValue)
          Intervals(List[Interval]())
        else
          Intervals(List[Interval](Interval(0,n-1),Interval(n+1, maxValue)))
      }
      case LT => 
      {
        if (n <= 0)
          Intervals(List[Interval]()) 
        else if (n > maxValue)
          Intervals(List[Interval](Interval(0,maxValue)))
        else
          Intervals(List[Interval](Interval(0,n-1)))
      }
      case LE =>
      {
        if (n <= 0)
          Intervals(List[Interval]()) 
        else if (n >= maxValue)
          Intervals(List[Interval](Interval(0,maxValue)))
        else
          Intervals(List[Interval](Interval(0,n)))
      }
      case GT => 
      {
        if (n >= maxValue)
          Intervals(List[Interval]()) 
        else if (n < 0)
          Intervals(List[Interval](Interval(0,maxValue)))
        else
          Intervals(List[Interval](Interval(n+1,maxValue)))
      }
      case GE => 
      {
        if (n > maxValue)
          Intervals(List[Interval]()) 
        else if (n < 0)
          Intervals(List[Interval](Interval(0,maxValue)))
        else
          Intervals(List[Interval](Interval(n,maxValue)))
      }
    }
    ivs
  }
}


// constraints between variables
case class Constraint(op:ROP, x1:Variable, x2:Variable)


// a map from variables to their possible values
case class Values( vs:Map[Variable, Intervals] = Map() ) {
  // retrieve the possible values for the given variable; if the
  // variable isn't in vs then return the default set of possible
  // values
  def apply( x:Variable ): Intervals =
  {
    vs.getOrElse(x,Intervals.default)
  }

  // return a mutable copy of this map
  def mutableCopy:MValues =
  {
    var mmap = MMap() ++ vs
    MValues(mmap)
  }
}


// a mutable map from variables to their possible values
case class MValues( vs:MMap[Variable, Intervals] = MMap() ) {
  // retrieve the possible values for the given variable; if the
  // variable isn't in vs then return the default set of possible
  // values
  def apply( x:Variable ): Intervals =
  {
    vs.getOrElse(x, Intervals.default)
  }

  // update the possible values of the given variable
  def update( x:Variable, i:Intervals ) {
    vs.update(x,i) 
  }

  // clear the map
  def clear() {
    vs.clear
  }

  // is the map empty?
  def isEmpty:Boolean =
    vs.isEmpty

  // return an immutable copy of this map
  def immutableCopy:Values =
  {
    val immap = Map() ++ vs
    Values(immap)
  }
}


// a map from variables to the constraints mentioning that variable
case class Constraints( cs:Map[Variable, Set[Constraint]] = Map() ) {
  // retrieve the constraint set for the given variable; if the
  // variable isn't in cs then return the empty set
  def apply( x:Variable ): Set[Constraint] =
  {
    cs.getOrElse(x, Set[Constraint]()) 
  }

  // add constraint c to the constraints sets for variables x1 and x2
  // mentioned in constraint c
  def +( c:Constraint ): Constraints = 
  {
    val setx1 = cs.getOrElse(c.x1,Set[Constraint]())
    val setx2 = cs.getOrElse(c.x2,Set[Constraint]())
    val newcs1:Map[Variable, Set[Constraint]] = (cs + (c.x1 -> (setx1.toList:+c).toSet ))
    val newcs2:Map[Variable, Set[Constraint]] = (newcs1 + (c.x2 ->(setx2.toList:+c).toSet))
    Constraints(newcs2)
  }
}


// this is the actual solver, including an API used by the
// miniprolog-fd interpreter to insert constraints and query for
// satisfiable values.
case class ConstraintStore(
  values:Values = Values(),
  constraints:Constraints = Constraints()
) {

  ////////// API FOR INTERPRETER //////////

  // insert a new constraint into the constraint store. the return
  // value is None if the new constraint is inconsistent with existing
  // constraints, otherwise it is Some(the updated constraint store).
  def insert(op:ROP, v1:Value, v2:Value): Option[ConstraintStore] =
  {
      var vmc = values.mutableCopy

      val nConstraintStore = (v1, v2) match {
        case (x1:NumValue, x2:Variable) =>
        {
          val v1Interval = Intervals.getIntervals(reverseOp(op), x1.n)
          propagate(vmc,x2,v1Interval)
          if (vmc.isEmpty)
            return None
          else
            Some(ConstraintStore(vmc.immutableCopy,constraints))
        }
        case (x1:Variable, x2:NumValue) =>
        {
          val v2Interval = Intervals.getIntervals(op, x2.n)
          val vmc = values.mutableCopy
          propagate(vmc,x1,v2Interval)
          if (vmc.isEmpty)
            return None
          else
            Some(ConstraintStore(vmc.immutableCopy,constraints))

        }
        case (x1:Variable, x2:Variable) =>
        {
         if(values(x1).singleton == true && values(x2).singleton == true )
        {
          val s1 = values(x1).getSingleton
          val s2 = values(x2).getSingleton
          op match 
          {
            case (EQ) => 
            {
              if(s1 == s2)
                return Some(this)
              else
                return None
            }

            case (NE) =>
            {
              if(s1 != s2)
                return Some(this)
              else
                return None
            }

            case(LT) =>
            {
              if(s1 < s2)
                return Some(this)
              else
                return None
            }

            case(LE) =>
            {
              if(s1 <= s2)
                return Some(this)
              else
                return None
            }
            case(GT) =>
            {
              if(s1 > s2)
                return Some(this)
              else
                return None
            }
            case(GE) =>
            {
              if(s1 >= s2)
                return Some(this)
              else
                return None
            }
          }
          
        }
        else
        {
          {
          val newConstraint = Constraint(op,x1,x2)
          val newConstraints = ConstraintStore(values,constraints + newConstraint)
          val solvedConstraint = newConstraints.solve(newConstraints.values,List[Variable](x1, x2))
          if (solvedConstraint.isEmpty)
            return None 
          else
            Some(newConstraints)
          }
        }
      }
        case _ => None
      }
      nConstraintStore
  }

  // query the constraint store for satisfying values for the given
  // list of variables
  def SAT(xs:List[Variable]): List[NumValue] =
  {


    solve(values, xs) match {
      case None => 
      {
        List[NumValue]()
      }
      case Some(Values(sm)) =>
      {
        val numList = xs.map( (va: Variable) =>
          NumValue(sm(va).getSingleton))
        return numList
      }
    }
  }


  ////////// INTERNAL METHODS //////////


  // given the current possible values for all the variables, compute
  // satisfying values for the given list of variables
  def solve( currValues:Values, xs:List[Variable] ): Option[Values] =
  {
    val x = xs.head
    val tl = xs.tail
    for (v <- currValues(x))
    {
      val mutableCV = currValues.mutableCopy
      propagate(mutableCV, x, Intervals(v))
      if (!mutableCV.vs.isEmpty)
      {
        val currValuesN = mutableCV.immutableCopy
        if (!tl.isEmpty)
        {
          val solveVal = solve(currValuesN, tl)
          solveVal match {
            case None => ()
            case Some(cv) => return Some(cv)
          }//end of match
        }//end of tail empty
        else
        {
          return Some(currValuesN)
        }
      }//end of mutableCV empty
    }//end of for
    return None
  }  
  // constraint propagation: restrict the possible values of x to only
  // include values in ranges. if that makes x unsatisfiable then
  // return; if that changes the possible values of x then recursively
  // propagate those changes to the other variables.
  //
  // IMPORTANT NOTE: because mutableCV returns a default set of
  // possible values for any variable not contained in the mapping, we
  // can't just iterate through all the constraints as done in the
  // pseudocode (it would incorrectly propagate default values if
  // mutableCV is made empty in some recursive call to
  // propagate). instead, we need to explicitly check after each
  // recursive call to propagate whether mutableCV is empty, and if so
  // then explicitly return from the function.
  def propagate( mutableCV:MValues, x:Variable, ranges:Intervals) {
     val oldmutableCV = mutableCV.immutableCopy
     mutableCV.update(x,mutableCV(x).intersect(ranges))
     if (mutableCV(x).isEmpty)
     {
       mutableCV.clear
     }
     else if (mutableCV(x) != oldmutableCV(x))
     {
       for (c <- constraints(x)) {
          if (!mutableCV.isEmpty)
          {
          if (x == c.x1)
          {
            val x2 = c.x2
            val r2 = narrow(x2, mutableCV(x2), c, mutableCV(x))
            propagate(mutableCV,x2,r2)
            if (mutableCV.isEmpty)
              return
          }
          else
          {
            val x2 = c.x1
            val r2 = narrow(x2, mutableCV(x2), c, mutableCV(x))
            propagate(mutableCV,x2,r2)
            if (mutableCV.isEmpty)
              return
          }
        }
        else return
        }
      }
  }

  // narrow the current possible values of x1 (given as rangesX1)
  // according to the constraint c, which is between x1 and some
  // variable x2 (where rangesX2 gives the possible values of x2)
  def narrow( x1:Variable, rangesX1:Intervals, c:Constraint, rangesX2:Intervals ): Intervals =
  {
    val ivs: Intervals = c match 
    {
      case Constraint(EQ, `x1`, _) | Constraint(EQ, _, `x1`) =>
      {
        rangesX1.intersect(rangesX2)
      }
      case Constraint(NE, `x1`, _) | Constraint(NE, _, `x1`) =>
      {
        if (rangesX2.singleton)
        {
          rangesX1 - rangesX2.getSingleton
        }
        else
        {
          rangesX1
        }
      }
      case Constraint(LT, `x1`, _) | Constraint(GT, _, `x1`) =>
      {
        rangesX1.LT(rangesX2.highest)
      }
      case Constraint(LE, `x1`, _) | Constraint(GE, _, `x1`) =>
      {
        rangesX1.LE(rangesX2.highest)
      }
      case Constraint(GT, `x1`, _) | Constraint(LT, _, `x1`) =>
      {
        rangesX1.GT(rangesX2.lowest)
      }
      case Constraint(GE, `x1`, _) | Constraint(LE, _, `x1`) =>
      {
        rangesX1.GE(rangesX2.lowest)
      }
    }
    ivs
  }

  // reverse the meaning of a relational operator (e.g., < becomes >
  // and ≤ becomes ≥)
  def reverseOp( op:ROP ): ROP =
    op match {
      case EQ => EQ
      case NE => NE
      case LT => GT
      case LE => GE
      case GT => LT
      case GE => LE
    }
}
