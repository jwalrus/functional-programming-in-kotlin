package jwalrus.fp

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

