package jwalrus.fp

import io.kotlintest.data.suspend.forall
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class ListSpec : StringSpec({

    "tail" {
        forall(
            row(List.of(), Nil),
            row(List.of(1), Nil),
            row(List.of(1, 2), List.of(2)),
            row(List.of(1, 2, 3), List.of(2, 3))
        ) { xs, exp -> tail(xs) shouldBe exp }
    }

    "setHead" {
        forall(
            row(List.of(), 1, List.of(1)),
            row(List.of(1), 2, List.of(2)),
            row(List.of(1, 2), 2, List.of(2, 2)),
            row(List.of(1, 2, 3), 2, List.of(2, 2, 3))
        ) { xs, x, exp -> setHead(xs, x) shouldBe exp }
    }

    "drop" {
        forall(
            row(List.of(), 0, List.of()),
            row(List.of(), 1, List.of()),
            row(List.of(1), 0, List.of(1)),
            row(List.of(1), 1, List.of()),
            row(List.of(1, 2, 3), 1, List.of(2, 3)),
            row(List.of(1, 2, 3), 2, List.of(3))
        ) { xs, n, exp -> drop(xs, n) shouldBe exp }
    }

    "dropWhile" {
        forall(
            row(List.of(), 0, List.of()),
            row(List.of(), 1, List.of()),
            row(List.of(1), 0, List.of(1)),
            row(List.of(1), 1, List.of()),
            row(List.of(1, 2, 3), 1, List.of(2, 3)),
            row(List.of(1, 2, 3), 2, List.of(3))
        ) { xs, n, exp -> dropWhile(xs) { it <= n } shouldBe exp }
    }

    "init" {
        forall(
            row(List.of(), List.of()),
            row(List.of(1), List.of()),
            row(List.of(1, 2), List.of(1)),
            row(List.of(1, 2, 3), List.of(1, 2)),
            row(List.of(1, 2, 3, 4), List.of(1, 2, 3))
        ) { xs, exp -> init(xs) shouldBe exp }
    }

    "sum with foldRight" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            List.sum(xs) shouldBe foldRight(xs, 0) { x, acc -> x + acc}
        }
    }

    "product with foldRight" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            List.product(xs) shouldBe foldRight(xs, 1) { x, acc -> x * acc}
        }
    }

    "exercise 3.7" {
        foldRight(List.of(1, 2, 3), List.empty<Int>(), { x, acc -> Cons(x, acc)}) shouldBe List.of(1, 2, 3)
    }

    "length" {
        assertAll { l: kotlin.collections.List<Boolean> ->
            val xs = List.of(*l.toTypedArray())
            length(xs) shouldBe l.size
        }
    }

    "foldLeft verified with foldRight" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            val left = foldLeft(xs, 1, {acc, x -> acc * x})
            val right = foldRight(xs, 1, {x, acc -> x * acc})
            left shouldBe right
        }
    }

    "reverse" {
        forall(
            row(List.of(), List.of()),
            row(List.of('a'), List.of('a')),
            row(List.of('a', 'b'), List.of('b', 'a')),
            row(List.of('a', 'b', 'c'), List.of('c', 'b', 'a'))
        ) { xs, exp -> reverse(xs) shouldBe exp }
    }

    "foldLeft vs foldLeftR" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            val left = foldLeft(xs, 0, {acc, x -> acc + x})
            val leftR = foldLeftR(xs, 0, {acc, x -> acc + x})
            left shouldBe leftR
        }
    }

    "foldRight vs foldRightL" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            val right = foldRight(xs, 0, {x, acc -> acc + x})
            val rightL = foldRightL(xs, 0, {x, acc -> acc + x})
            right shouldBe rightL
        }
    }

    "append" {
        forall(
            row(List.of(), List.of(), List.of()),
            row(List.of(), List.of(1), List.of(1)),
            row(List.of(1), List.of(), List.of(1)),
            row(List.of(1), List.of(1), List.of(1, 1)),
            row(List.of(1, 2), List.of(3, 4), List.of(1, 2, 3, 4))
        ) { a1, a2, exp -> append(a1, a2) shouldBe exp }
    }

    "concat" {
        val aaa = List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6))
        concat(aaa) shouldBe List.of(1, 2, 3, 4, 5, 6)
    }

    "increment" {
        forall(
            row(List.of(), List.of()),
            row(List.of(1), List.of(2)),
            row(List.of(1, 2), List.of(2, 3))
        ) { xs, exp -> increment(xs) shouldBe exp }
    }

    "double2string" {
        forall(
            row(List.of(), List.of()),
            row(List.of(1.0), List.of("1.0")),
            row(List.of(1.0, 1.1), List.of("1.0", "1.1"))
        ) { xs, exp -> double2string(xs) shouldBe exp }
    }

    "map" {
        assertAll { l: kotlin.collections.List<Int> ->
            val xs = List.of(*l.toTypedArray())
            map(xs) { it + 1 } shouldBe increment(xs)
        }

        assertAll { d: kotlin.collections.List<Double> ->
            val xs = List.of(*d.toTypedArray())
            map(xs) { it.toString() } shouldBe double2string(xs)
        }
    }

    "filter" {
        forall(
            row(List.of(), { x: Int -> x % 2 == 0 }, List.of()),
            row(List.of(1), { x: Int -> x % 2 == 0 }, List.of()),
            row(List.of(1, 2), { x: Int -> x % 2 == 0 }, List.of(2)),
            row(List.of(2, 4, 6), { x: Int -> x % 2 == 0 }, List.of(2, 4, 6))
        ) { xs, p, exp ->
            filter(xs, p) shouldBe exp
            filterFlatMap(xs, p) shouldBe filter(xs, p)
        }
    }

    "flatMap" {
        flatMap(List.of(1, 2, 3)) { i -> List.of(i, i) } shouldBe List.of(1, 1, 2, 2, 3, 3)
    }

    "zipInts and zipWith" {
        val x1 = List.of(1, 2, 3)
        val x2 = List.of(4, 5, 6, 7)
        zipInts(x1, x2) shouldBe List.of(5, 7, 9)
        zipWith(x1, x2) { a,b -> a + b } shouldBe zipInts(x1, x2)
    }

    "hasSubsequence" {
        forall(
            row(List.of(), List.of(), false),
            row(List.of(), List.of(1), false),
            row(List.of(1), List.of(2), false),
            row(List.of(1, 2), List.of(2), true),
            row(List.of(1, 2, 3), List.of(2, 3), true),
            row(List.of(1, 2, 3, 4), List.of(2, 3), true),
            row(List.of(1, 2, 3, 4), List.of(2, 3, 2), false)
        ) { xs, sub, exp -> hasSubsequence(xs, sub) shouldBe exp }
    }
})
