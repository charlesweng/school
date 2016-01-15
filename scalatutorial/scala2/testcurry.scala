def lift0[A](v: A)(l: List[A]): List[A] = v :: List[A]() 

// val l = List(1,2,3)

println(lift0(1)(List[Int]()))
