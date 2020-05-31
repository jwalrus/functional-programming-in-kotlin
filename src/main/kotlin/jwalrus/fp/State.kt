package jwalrus.fp


data class State<S, out A>(val run: (S) -> Pair<A, S>) {

    companion object {
        fun <S, A> unit(a: A): State<S, A> = State { s -> Pair(a, s) }

        fun <S, A, B, C> map2(sa: State<S, A>, sb: State<S, B>, f: (A, B) -> C): State<S, C>
            = sa.flatMap { a ->
                sb.map { b ->
                    f(a, b)
                }
            }

        fun <S, A> sequence(fs: List<State<S, A>>): State<S, List<A>> =
                foldRight(fs, unit(List.empty())) { f, acc ->
                    map2(f, acc) { h, t -> Cons(h, t) }
                }
    }

    fun <B> map(f: (A) -> B): State<S, B> = this.flatMap { unit(f(it)) }

    fun <B> flatMap(f: (A) -> State<S, B>): State<S, B> = State { s0 ->
        val (a, s1) = this.run(s0)
        f(a).run(s1)
    }
}