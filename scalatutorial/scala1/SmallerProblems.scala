// // These problems are extracted from "Programming Scala", by
// // Dean Wampler.

object Problem1 {
    def main( args:Array[String] ) = {
      // Reverse each element of the args array and print out the
      // result. Note that the String class has a 'reverse' method.
      for (s <- args) {
        // s.reverse
        println(s.reverse) 
      }
    }
}

object Problem2 {
  // A binary tree node.  The field `ord` is declared with
  // `var`, so it is mutable.  For example, you can do:
  //
  // val n = Node(...)
  // n.ord = (1 -> 2)
  //
  // Because we introduced the `var`, you may modify _this_ `var`.
  // You may not introduce any other `var`s.
  case class Node(var ord:(Int,Int), 
                  left:Option[Node],
                  right:Option[Node])

  def main( args:Array[String] ) = {
    // example tree
    val tree = Node( (-1,-1), 
      Some(Node( (-1, -1), None, None)),
      Some(Node( (-1,-1),
                Some(Node( (-1,-1), None, None )),
                Some(Node( (-1,-1), Some(Node( (-1,-1), None, None )), None ))
      ))
    )
    
    // set the tree nodes' labels and print the tree. note that case
    // classes are automatically given a toString method, so we don't
    // need to define our own.
    // tree.ord = (1,0)
    // println(tree.ord._1)
    order( tree )
    println( tree )
  }

  def order( node:Node ) {
    // use a nested method inside this method as a helper function to
    // traverse the given tree and set each Node's 'ord' field to the
    // tuple '(preorder, postorder)', where 'preorder' is the Node's
    // preorder label and 'postorder' is the Node's postorder
    // label. For consistent numbers, visit left children before right
    // children. Labels should start at 0 (i.e., the root node's
    // preorder label should be 0).
    def preorder(n:Option[Node], next: Int): Int = {
      n match {
        case None => next// println("leafnode")
        case Some(n) => 
        {
          n.ord = (next, n.ord._2) 
          preorder(n.right, preorder(n.left, next + 1))
        }
      }
    }

    preorder(Some(node), 0)

    def postorder(n:Option[Node], next: Int): Int = {
      n match {
        case None => next
        case Some(n) =>
        {
          val nn: Int = postorder(n.left, next)
          val nnn: Int = postorder(n.right, nn)
          n.ord = (n.ord._1, nnn)
          nnn + 1
        }
      }
    }

    postorder(Some(node), 0)
    // postorder(Some(node), 0)
    /*
    PostOrder ==
    foo.bar() f(empty(next) = next
    f(n, next)
    postorderof(l, next)
    next3 = f(r,next)

    */
    // As a hint, you'll need to use recursion here.  The nested
    // method should have an auxilliary parameter, representing the
    // currently available ID.  The nested method should return the
    // next available ID.  This is equivalent to an approach of
    // having a mutable variable elsewhere and incrementing it
    // each time we need a new ID, which is likely a more obvious
    // solution if you're coming from an imperative background.  This
    // is equivalent, because the mutable variable sets up an implicit
    // data dependency between recursive calls, whereas with functional
    // purity we must make this data dependency explicit.
  }
}

object Problem3 {
  def main( args:Array[String] ) {
    val list = args.toList

    // Use the foldLeft method of list to print the following:
    //
    // 1. the largest element in args (using string comparison)
    // 2. args with no duplicate elements
    // 3. a run-length-encoded version of args
    /* Objective
    [[word, word], [otherwise]]
    [word, word, ]
    
    */

    println(list.foldLeft("")((s1: String, s2: String) =>
    {
      if (s1 > s2)
        s1
      else
        s2
    } 
    )
    )//end of println

    println( list.foldLeft(List[String]())( 
      (a,s1:String) => {if (a.contains(s1)) a else s1 :: a
    }
    ).reverse
    )//end of println


    println(
    list.foldLeft(List[List[String]]())((listoflists,s)=> 
    {
      if (listoflists.isEmpty)
      {
        List(List(s))
      }
      else if (listoflists.last.head == s)
      {
        listoflists.init ++ List(s :: listoflists.last)
      }
      else
      {
        listoflists ++ List(List(s))
      }
    }
    )
    )//end of println

    // NOTES
    //
    // If the initial value given to foldLeft is an empty List you
    // need to explicitly give the type of the List, e.g., List[Int]()
    // or List[String](), otherwise the compiler won't be able to
    // figure out the types.
    //
    // For run-length-encoding specifics, see
    // http://en.wikipedia.org/wiki/Run_length_encoding.
  }
}
