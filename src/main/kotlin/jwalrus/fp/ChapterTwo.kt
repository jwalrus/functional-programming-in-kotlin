package jwalrus.fp

// exercise 2.1
// returns the nth number in the fibonacci series
fun fibonacci(n: Int): Long {
    tailrec fun loop(count: Int, a: Long, b: Long): Long =
        if (count == n) a
        else loop(count + 1, b, a + b)
    return loop(1, 0, 1)
}


// exercise 2.2
fun <A> isSorted(aa: List<A>, ordered: (A, A) -> Boolean): Boolean {
    tailrec fun loop(bb: List<A>):Boolean = when (bb.size) {
        0 -> true
        1 -> true
        else -> if (!ordered(bb[0], bb[1])) false else loop(bb.drop(1))
    }
    return loop(aa)
}