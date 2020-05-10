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

fun <A> Stream<A>.headOption0(): Option<A> = when (this) {
    is Empty -> None
    is Kons -> Some(h())
}


fun <A> Stream<A>.headOption(): Option<A> = foldRight({ Option.empty() }) { a, _ -> Some(a) }

// exercise 5.6
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
fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A> = foldRight({ empty() }) { a, b -> if (p(a)) kons({ a }, b) else b() }

// exercise 5.4
fun <A> Stream<A>.exists(p: (A) -> Boolean): Boolean = foldRight({ false }) { a, b -> p(a) || b() }

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean = foldRight({ true }) { a, b -> p(a) && b() }

// exercise 5.7
fun <A, B> Stream<A>.map(f: (A) -> B): Stream<B> = foldRight({ empty() }) { a, b -> kons({ f(a) }, b) }

fun <A> Stream<A>.filter(p: (A) -> Boolean): Stream<A> = foldRight({ empty() }) { a, b -> if (p(a)) kons({ a }, b) else b() }

fun <A, B> Stream<A>.flatMap(f: (A) -> Stream<B>): Stream<B>
        = foldRight({ empty() }) { a, acc1 ->
            f(a).foldRight(acc1) { b, acc2 -> kons({ b }, acc2) }
        }

fun <A> Stream<A>.append(xs: Stream<A>): Stream<A> = foldRight({ xs }) { a, b -> kons({ a }, b) }

// exercise 5.8
fun <A> constant(a: A): Stream<A> = kons({ a }, { constant(a) })

// exercise 5.9
fun from(n: Int): Stream<Int> = kons({ n }, { from(n+1) })

// exercise 5.10
fun fibs(): Stream<Int> {
    fun go(a: Int, b: Int): Stream<Int> = kons({ a }, { go(b, a + b) })
    return go(0, 1)
}

// exercise 5.11
fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A>
    = f(z).map { (a,b) -> kons({ a }, { unfold(b, f)}) }.getOrElse { empty() }

// exercise 5.12
fun ones_(): Stream<Int> = unfold(1) { Some(Pair(1, 1)) }

fun <A> constant_(a: A): Stream<A> = unfold(a) { b -> Some(Pair(b, b)) }

fun from_(n: Int): Stream<Int> = unfold(n) { a -> Some(Pair(a, a + 1)) }

fun fibs_(): Stream<Int> = unfold(Pair(0, 1)) { (a,b) -> Some(Pair(a, Pair(b, a + b))) }

// exercise 5.13
fun <A, B> Stream<A>.map_(f: (A) -> B): Stream<B>
    = unfold(this) { xs: Stream<A> ->
        when (xs) {
            is Empty -> None
            is Kons -> Some(Pair(f(xs.h()), xs.t()))
        }
    }

fun <A> Stream<A>.take_(n: Int): Stream<A>
    = unfold(this) { xs: Stream<A> ->
        when (xs) {
            is Kons -> if (n < 1) None else Some(Pair(xs.h(), xs.t().take_(n-1)))
            is Empty -> None
        }
    }

fun <A> Stream<A>.takeWhile_(p: (A) -> Boolean): Stream<A>
    = unfold(this) { xs: Stream<A> ->
        when (xs) {
            is Kons -> if (p(xs.h())) Some(Pair(xs.h(), xs.t())) else None
            is Empty -> None
        }
    }

fun <A, B, C> Stream<A>.zipWith(that: Stream<B>, f: (A,B) -> C): Stream<C>
        = unfold(Pair(this, that)) { (xs, ys) ->
            when (xs) {
                is Empty -> None
                is Kons -> when (ys) {
                    is Empty -> None
                    is Kons -> Some(Pair( f(xs.h(), ys.h()), Pair(xs.t(), ys.t())))
                }
            }
        }

fun <A, B> Stream<A>.zipAll(that: Stream<B>): Stream<Pair<Option<A>, Option<B>>>
    = unfold(Pair(this, that)) { (xs, ys) ->
        when (xs) {
            is Empty -> when (ys) {
                is Empty -> None
                is Kons -> Some(Pair(Pair(None, Some(ys.h())), Pair(empty<A>(), ys.t())))
            }
            is Kons -> when (ys) {
                is Empty -> Some(Pair(Pair(Some(xs.h()), None), Pair(xs.t(), empty<B>())))
                is Kons -> Some(Pair(Pair(Some(xs.h()), Some(ys.h())), Pair(xs.t(), ys.t())))
            }
        }
    }

// exercise 5.14
fun <A> Stream<A>.startsWith(that: Stream<A>): Boolean = this.zipAll(that)
    .takeWhile_ { (_, b) -> b !is None }
    .forAll { (a, b) -> a == b }

// exercise 5.15
fun <A> Stream<A>.tails(): Stream<Stream<A>> = unfold(this) { xs ->
    when (xs) {
        is Empty -> None
        is Kons -> Some(Pair(xs, xs.t()))
    }
}

// exercise 5.16
fun <A, B> Stream<A>.scanRight(z: B, f: (A, () -> B) -> B): Stream<B> = TODO()