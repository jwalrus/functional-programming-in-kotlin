package jwalrus.fp

import jwalrus.fp.Stream.Companion.empty
import jwalrus.fp.Stream.Companion.kons

sealed class Stream<out A> {
    companion object {
        // smart constructors
        fun <A> kons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
            val head: A by lazy { hd() }
            val tail: Stream<A> by lazy { tl() }
            return Kons({ head }, { tail })
        }

        fun <A> empty(): Stream<A> = Empty

        fun <A> of(vararg xs: A): Stream<A> =
                if (xs.isEmpty()) empty()
                else kons({ xs[0] }, { of(*xs.sliceArray(1 until xs.size)) })
    }
}

data class Kons<out A>(val h: () -> A, val t: () -> Stream<A>) : Stream<A>()
object Empty : Stream<Nothing>()

fun <A> Stream<A>.headOption0() : Option<A> = when (this) {
    is Empty -> None
    is Kons -> Some(h())
}

fun <A> Stream<A>.headOption(): Option<A> = TODO()


fun <A, B> Stream<A>.foldRight(z: () -> B, f: (A, () -> B) -> B): B = when (this) {
    is Kons -> f(this.h()) { t().foldRight(z, f) }
    else -> z()
}

// exercise 5.1
fun <A> Stream<A>.toList(): List<A> {
    tailrec fun go(xs: Stream<A>, acc: List<A>): List<A> = when (xs) {
        is Empty -> acc
        is Kons -> go(xs.t(), Cons(xs.h(), acc))
    }
    return reverse(go(this, Nil))
}

// exercise 5.2
fun <A> Stream<A>.take(n: Int): Stream<A> {
    fun go(xs: Stream<A>, m: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is Kons ->
            if (m == 0) empty()
            else kons(xs.h, { go(xs.t(), m - 1) })
    }
    return go(this, n)
}

// exercise 5.2
fun <A> Stream<A>.drop(n: Int): Stream<A> {
    fun go(xs: Stream<A>, m: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is Kons ->
            if (m == 0) xs
            else go(xs.t(), m - 1)
    }
    return go(this, n)
}

// exercise 5.3
fun <A> Stream<A>.takeWhile0(p: (A) -> Boolean): Stream<A> = when (this) {
    is Empty -> empty()
    is Kons ->
        if (p(this.h())) kons(this.h, { this.t().takeWhile0(p) })
        else empty()
}

// exercise 5.5
fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A>
    = foldRight({ empty() }) { a, b -> if (p(a)) kons({ a }, b) else b() }

// exercise 5.4
fun <A> Stream<A>.exists(p: (A) -> Boolean): Boolean = foldRight({ false }) { a, b -> p(a) || b() }

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean = foldRight({ true }) { a, b -> p(a) && b() }

// exercise 5.5
