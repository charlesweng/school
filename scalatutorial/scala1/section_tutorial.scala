// object Hello {
//   for main (args: Array[String]): Unit = {
//     println("Hello, world!")
//   }
//   def f(): Int = 42
// }

// val plus5 = ((x: Int) => ((y: Int) => x + y))(5)

// println(Hello.f)

// (1 to 10).map(println(_))

// def addi(l: List[Int]): List (Int) = {
//   if (l.size == 0) {
//     Nil
//   }
//   else {
//     (l.head + 1) :: addi(l.tail)
//   }
// }

// println(addi(List(1,2,3)))

// def addi(l: List(Int)): List[Int] = l match {
//   case Nil => Nil
//   case head :: tail => (head + 1) :: addi(tail)
// }

// println(addi(List(1,2,3)))

// def myMap[A,B](l: List[A])(f: A => B): = {
//   case Nil => Nil
//   case head :: tail => f(head) :: myMap(tail)(f)
// }

// println(myMap(List("asdf", "asdfhjdfjdj", ""))(()))

// println(List(1,2,3).filter(x => x %2 == 0))

// def getOdds(l: List[Int]): List[Int] = l match {
//   case Nil => Nil
//   case head :: tail => if (head % 2 == 1) (head :: getOdds(tail)) else getOdds(tail)
// }

// print(getOdds(List(1,2,3)))

// def divide(a: Int, b: Int): Option[Int] = {
//  b match {
//   case 0 => None
//   case _ => Some(a / b)
//  }
// }

// println(divide(10,2))
