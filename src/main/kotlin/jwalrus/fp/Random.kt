package jwalrus.fp

interface RNG {
    fun nextInt(): Pair<Int, RNG>
}

typealias Rand<A> = (RNG) -> Pair<A, RNG>

data class SimpleRNG(val seed: Long) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        val newSeed = (seed * 0x5DEECE66DL + 0xBL) and 0xFFFFFFFFFFFFL
        val nextRNG = SimpleRNG(newSeed)
        val n = (newSeed ushr 16).toInt()
        return Pair(n, nextRNG)
    }
}

// exercise 6.1
fun nonNegativeInt(rng: RNG): Pair<Int, RNG> {
    val (n, rng1) = rng.nextInt()
    val rnd = if (n < 0) -(n+1) else n
    return Pair(rnd, rng1)
}

// exercise 6.2
fun double(rng: RNG): Pair<Double, RNG> {
    val (n, rng1) = nonNegativeInt(rng)
    return Pair(n.toDouble()/(Int.MAX_VALUE+1.0), rng1)
}

// exercise 6.3
fun intDouble(rng: RNG): Pair<Pair<Int, Double>, RNG> {
    val (i, rng1) = rng.nextInt()
    val (d, rng2) = double(rng1)
    return Pair(Pair(i, d), rng2)
}

fun doubleInt(rng: RNG): Pair<Pair<Double, Int>, RNG> {
    val (id, rng1) = intDouble(rng)
    val (i, d) = id
    return Pair(Pair(d, i), rng1)
}

fun double3(rng: RNG): Pair<Triple<Double, Double, Double>, RNG> {
    val (d1, rng1) = double(rng)
    val (d2, rng2) = double(rng1)
    val (d3, rng3) = double(rng2)
    return Pair(Triple(d1, d2, d3), rng3)
}

// exercise 6.4
fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> {
    tailrec fun go(n: Int, acc: List<Int>, rand: RNG): Pair<List<Int>, RNG> {
        if (n == 0) return Pair(acc, rand)
        val (i, rand1) = rand.nextInt()
        return go(n-1, Cons(i, acc), rand1)
    }
    return go(count, List.empty(), rng)
}

val intR: Rand<Int> = { rng -> rng.nextInt() }
val doubleR: Rand<Double> = map(::nonNegativeInt) { it / (Int.MAX_VALUE.toDouble() + 1) }

fun <A> unit(a: A): Rand<A> = { rng -> Pair(a, rng) }

fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = { rng ->
    val (a, rng2) = s(rng)
    Pair(f(a), rng2)
}

fun nonNegativeEven(): Rand<Int> = map(::nonNegativeInt) { if (it % 2 == 0) it else it - 1 }

// exercise 6.6
fun <A, B, C> map2(ra: Rand<A>, rb: Rand<B>, f: (A, B) -> C): Rand<C> = { rng ->
    val (a, rng1) = ra(rng)
    val (b, rng2) = rb(rng1)
    Pair(f(a, b), rng2)
}

fun <A, B> both(ra: Rand<A>, rb: Rand<B>): Rand<Pair<A, B>> = map2(ra, rb) { a, b -> Pair(a, b) }

val intDoubleR: Rand<Pair<Int, Double>> = both(intR, doubleR)
val doubleIntR: Rand<Pair<Double, Int>> = both(doubleR, intR)

// exercise 6.7
fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> = { rng ->
    when (fs) {
        is Nil -> unit(List.empty<A>())(rng)
        is Cons -> {
            val (x, rng1) = fs.head(rng)
            val (y, rng2) = sequence(fs.tail)(rng1)
            Pair(Cons(x, y), rng2)
        }
    }
}

fun <A> sequence2(fs: List<Rand<A>>): Rand<List<A>> =
    foldRight(fs, unit(List.empty<A>()), { f, acc ->
        map2(f, acc, { h, t -> Cons(h, t) })
    })

fun ints2(count: Int, rng: RNG): Pair<List<Int>, RNG> {
    fun go(n: Int): List<Rand<Int>> =
            if (n == 0) Nil
            else Cons({ r -> Pair(1, r) }, go(n - 1))
    return sequence2(go(count))(rng)
}

// exercise 6.8
fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = TODO()