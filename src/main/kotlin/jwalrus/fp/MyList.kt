package jwalrus.fp

sealed class MyList<out A> {
    companion object {
        fun <A> of(vararg aa: A): MyList<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }

        fun <A> empty(): MyList<A> = Nil

        fun sum(ints: MyList<Int>): Int = when (ints) {
            is Nil -> 0
            is Cons -> ints.head + sum(ints.tail)
        }

        fun product(ints: MyList<Int>): Int = when (ints) {
            is Nil -> 1
            is Cons -> if (ints.head == 0) 0 else ints.head * product(ints.tail)
        }
    }
}

object Nil: MyList<Nothing>()

data class Cons<out A>(val head: A, val tail: MyList<A>): MyList<A>()

// exercise 3.1
fun <A> tail(xs: MyList<A>): MyList<A> = when (xs) {
    is Nil -> Nil
    is Cons -> xs.tail
}

// exercise 3.2
fun <A> setHead(xs: MyList<A>, x: A): MyList<A> = when (xs) {
    is Nil -> Cons(x, Nil)
    is Cons -> Cons(x, xs.tail)
}

// exercise 3.3
fun <A> drop(xs: MyList<A>, n: Int): MyList<A> = when (xs) {
    is Nil -> Nil
    is Cons -> if (n == 0) xs else drop(xs.tail, n - 1)
}

// exercise 3.4
fun <A> dropWhile(xs: MyList<A>, p: (A) -> Boolean): MyList<A> = when (xs) {
    is Nil -> Nil
    is Cons -> if (p(xs.head)) dropWhile(xs.tail, p) else xs
}

// exercise 3.5
fun <A> init(xs: MyList<A>): MyList<A> = when (xs) {
    is Nil -> Nil
    is Cons -> when (xs.tail) {
        is Nil -> Nil
        is Cons -> Cons(xs.head, init(xs.tail))
    }
}

fun <A, B> foldRight(xs: MyList<A>, z: B, f: (A, B) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> f(xs.head, foldRight(xs.tail, z, f))
}

// exercise 3.6
// Q: short-circuit foldRight?
// A: No. evaluates to end of list

// exercise 3.7
// see test
