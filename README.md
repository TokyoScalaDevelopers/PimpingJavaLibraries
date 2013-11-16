PimpingJavaLibraries
====================

Using many Java libraries from Scala is unpleasant at best. Fortunately, Implicit Conversions in Scala provide a great way to cut through the madness! Follow along (`git log -p`), as each commit explains what is being done, and why.

The "Pimp my Library" pattern was proposed by Martin Odersky in 2006. A couple examples were provided, here's one of them:

    class RichArray[T](value: Array[T]) {
      def append(other: Array[T]): Array[T] = {
        val result = new Array[T](value.length + other.length)
        Array.copy(value, 0, result, 0, value.length)
        Array.copy(other, 0, result, value.length, other.length)
        result
      }
    }

    implicit def enrichArray[T](xs: Array[T]) = new RichArray[T]

With these definitions, we can do:

    val first: Array[Int] = List(1,2,3).toArray
    val last: Array[Int] = List(4,5,6).toArray

    first.append(last)

which converts first into a `RichArray[T]`, then calls append, which returns a normal `Array[T]`

Current libraries:
  - Jackson JSON parser
