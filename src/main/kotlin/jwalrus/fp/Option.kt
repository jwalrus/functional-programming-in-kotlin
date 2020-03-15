package jwalrus.fp

import kotlin.math.pow

sealed class Option<out A>

object None : Option<Nothing>()

data class Some<out A>(val get: A) : Option<A>()


fun <A, B> Option<A>.map(f: (A) -> B): Option<B> = when (this) {
    is None -> None
    is Some -> Some(f(this.get))
}

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = this.map(f).getOrElse { None }

fun <A> Option<A>.getOrElse(default: () -> A): A = when (this) {
    is None -> default()
    is Some -> this.get
}

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = this.map { Some(it) }.getOrElse { ob() }

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = this.flatMap { if (f(it)) Some(it) else None }

fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { oa -> oa.map(f) }

fun <A> catches(a: () -> A): Option<A> =
    try {
        Some(a())
    } catch (_: Throwable) {
        None
    }



// exercise 4.2
fun mean(xs: List<Double>): Option<Double> =
    if (xs.isEmpty()) None
    else Some(xs.sum() / xs.size)

fun variance(xs: List<Double>): Option<Double> = mean(xs).flatMap { mu -> mean(xs.map { (it - mu).pow(2) })  }

// exercise 4.3
fun <A, B, C> map2(oa: Option<A>, ob: Option<B>, f: (A, B) -> C): Option<C> = oa.flatMap { a -> ob.map { b -> f(a, b) } }

// exercise 4.4
fun <A> sequence(xs: MyList<Option<A>>): Option<MyList<A>> =
    foldRightL(xs, Some(MyList.empty<A>())) { oa: Option<A>, acc: Option<MyList<A>> ->
        map2(oa, acc) { a, b -> Cons(a, b) }
    }