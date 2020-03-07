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
    foldRight(aaa, MyList.empty()) { a, acc -> append(a, acc) }