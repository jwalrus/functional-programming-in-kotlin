package jwalrus.fp

sealed class Either<out E, out A>

data class Left<out E>(val value: E) : Either<E, Nothing>()
data class Right<out A>(val value: A): Either<Nothing, A>()


// exercise 4.6
fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> = when (this) {
    is Left -> this
    is Right -> Right(f(value))
}

fun <E, A> Either<E, A>.orElse(f: () -> Either<E, A>): Either<E, A> = when (this) {
    is Left -> f()
    is Right -> this
}

fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> = when (this) {
    is Left -> this
    is Right -> f(value)
}

fun <E, A, B, C> map2(ae: Either<E, A>, be: Either<E, B>, f: (A, B) -> C): Either<E, C> =
    ae.flatMap { a ->
        be.map { b ->
            f(a, b)
        }
    }

// exercise 4.7
fun <E, A, B> traversee(xa: List<A>, f: (A) -> Either<E, B>): Either<E, List<B>> =
    when (xa) {
        is Nil -> Right(Nil)
        is Cons -> map2(f(xa.head), traversee(xa.tail, f)) { a, acc -> Cons(a, acc) }
    }

fun <E, A> sequence(xa: List<Either<E, A>>): Either<E, List<A>> = traversee(xa) { it }