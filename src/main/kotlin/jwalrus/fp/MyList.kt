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

// exercise 3.8
fun <A> length(xs: MyList<A>): Int = foldRight(xs, 0, {_, acc -> acc + 1})

// exercise 3.9
tailrec fun <A, B> foldLeft(xs: MyList<A>, z: B, f: (B, A) -> B): B = when (xs) {
    is Nil -> z
    is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
}

// exercise 3.10
fun sum(xs: MyList<Int>): Int = foldLeft(xs, 0, {acc, x -> acc + x})
fun product(xs: MyList<Int>): Int = foldLeft(xs, 1, {acc, x -> acc + x})
fun lengthL(xs: MyList<Int>): Int = foldLeft(xs, 0, {acc, _ -> acc + 1})

// exercise 3.11
fun <A> reverse(xs: MyList<A>): MyList<A> = foldLeft(xs, MyList.empty(), { acc, x -> Cons(x, acc)})

// exercise 3.12
fun <A, B> foldLeftR(xs: MyList<A>, z: B, f: (B, A) -> B): B =
    foldRight(xs, {b: B -> b}, { a, g -> { b -> g(f(b, a)) } })(z)

fun <A, B> foldRightL(xs: MyList<A>, z: B, f: (A, B) -> B): B =
    foldLeft(xs, {b: B -> b}, {g, a -> { b -> g(f(a, b)) } })(z)

// exercise 3.13
fun <A> append(a1: MyList<A>, a2: MyList<A>): MyList<A> =
    foldRight(a1, a2, { a, acc -> Cons(a, acc)})

// exercise 3.14
fun <A> concat(aaa: MyList<MyList<A>>): MyList<A> =
    foldRightL(aaa, MyList.empty()) { a, acc -> append(a, acc) }

// exercise 3.15
fun increment(xs: MyList<Int>): MyList<Int> =
    foldRightL(xs, MyList.empty(), {a, acc -> Cons(a + 1, acc)})

// exercise 3.16
fun double2string(xs: MyList<Double>): MyList<String> =
    foldRight(xs, MyList.empty(), {a, acc -> Cons(a.toString(), acc)})

// exercise 3.17
fun <A, B> map(xs: MyList<A>, f: (A) -> B): MyList<B> =
    foldRightL(xs, MyList.empty(), {a, acc -> Cons(f(a), acc)})

// exercise 3.18
fun <A> filter(xs: MyList<A>, p: (A) -> Boolean): MyList<A> =
    foldRightL(xs, MyList.empty(), {a, acc -> if (p(a)) Cons(a, acc) else acc})

// exercise 3.19
fun <A, B> flatMap(xa: MyList<A>, f: (A) -> MyList<B>): MyList<B> = concat(map(xa, f))
/*
// probably better to use
fun<A, B> flatMap(xa: MyList<A>, f: (A) -> MyList<B>): MyList<B> =
    foldRight(xa, MyList.empty(), { a, acc ->
        foldRight(f(a), acc, { b, accb -> Cons(b, accb) })
    })
*/

// exercise 3.20
fun <A> filterFlatMap(xs: MyList<A>, p: (A) -> Boolean): MyList<A> =
    flatMap(xs) { if (p(it)) MyList.of(it) else MyList.empty() }

// exercise 3.21
fun zipInts(x1: MyList<Int>, x2: MyList<Int>): MyList<Int> =
    when (x1) {
        is Nil -> Nil
        is Cons -> when (x2) {
            is Nil -> Nil
            is Cons -> Cons(x1.head + x2.head, zipInts(x1.tail, x2.tail))
        }
    }

// exercise 3.22
fun <A, B> zipWith(x1: MyList<A>, x2: MyList<A>, f: (A, A) -> B): MyList<B> =
    when (x1) {
        is Nil -> Nil
        is Cons -> when (x2) {
            is Nil -> Nil
            is Cons -> Cons(f(x1.head, x2.head), zipWith(x1.tail, x2.tail, f))
        }
    }

// exercise 3.23
tailrec fun <A> hasSubsequence(xs: MyList<A>, sub: MyList<A>): Boolean {
    fun <A> go(l1: MyList<A>, l2: MyList<A>): Boolean = when(l1) {
        is Nil -> l2 == Nil
        is Cons -> when(l2) {
            is Nil -> true
            is Cons -> if (l1.head == l2.head) go(l1.tail, l2.tail) else false
        }
    }

    return when (sub) {
        is Nil -> false
        is Cons -> when (xs) {
            is Nil -> false
            is Cons -> if (go(xs, sub)) true
                       else hasSubsequence(xs.tail, sub)
        }
    }
}