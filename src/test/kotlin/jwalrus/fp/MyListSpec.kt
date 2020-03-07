package jwalrus.fp

import io.kotlintest.data.suspend.forall
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class MyListSpec : StringSpec({

    "tail" {
        forall(
            row(MyList.of(), Nil),
            row(MyList.of(1), Nil),
            row(MyList.of(1, 2), MyList.of(2)),
            row(MyList.of(1, 2, 3), MyList.of(2, 3))
        ) { xs, exp -> tail(xs) shouldBe exp }
    }

    "setHead" {
        forall(
            row(MyList.of(), 1, MyList.of(1)),
            row(MyList.of(1), 2, MyList.of(2)),
            row(MyList.of(1, 2), 2, MyList.of(2, 2)),
            row(MyList.of(1, 2, 3), 2, MyList.of(2, 2, 3))
        ) { xs, x, exp -> setHead(xs, x) shouldBe exp }
    }

    "drop" {
        forall(
            row(MyList.of(), 0, MyList.of()),
            row(MyList.of(), 1, MyList.of()),
            row(MyList.of(1), 0, MyList.of(1)),
            row(MyList.of(1), 1, MyList.of()),
            row(MyList.of(1, 2, 3), 1, MyList.of(2, 3)),
            row(MyList.of(1, 2, 3), 2, MyList.of(3))
        ) { xs, n, exp -> drop(xs, n) shouldBe exp }
    }

    "dropWhile" {
        forall(
            row(MyList.of(), 0, MyList.of()),
            row(MyList.of(), 1, MyList.of()),
            row(MyList.of(1), 0, MyList.of(1)),
            row(MyList.of(1), 1, MyList.of()),
            row(MyList.of(1, 2, 3), 1, MyList.of(2, 3)),
            row(MyList.of(1, 2, 3), 2, MyList.of(3))
        ) { xs, n, exp -> dropWhile(xs) { it <= n } shouldBe exp }
    }

    "init" {
        forall(
            row(MyList.of(), MyList.of()),
            row(MyList.of(1), MyList.of()),
            row(MyList.of(1, 2), MyList.of(1)),
            row(MyList.of(1, 2, 3), MyList.of(1, 2)),
            row(MyList.of(1, 2, 3, 4), MyList.of(1, 2, 3))
        ) { xs, exp -> init(xs) shouldBe exp }
    }

    "sum with foldRight" {
        assertAll { l: List<Int> ->
            val xs = MyList.of(*l.toTypedArray())
            MyList.sum(xs) shouldBe foldRight(xs, 0) {x, acc -> x + acc}
        }
    }

    "product with foldRight" {
        assertAll { l: List<Int> ->
            val xs = MyList.of(*l.toTypedArray())
            MyList.product(xs) shouldBe foldRight(xs, 1) {x, acc -> x * acc}
        }
    }

    "exercise 3.7" {
        foldRight(MyList.of(1, 2, 3), MyList.empty<Int>(), {x, acc -> Cons(x, acc)}) shouldBe MyList.of(1, 2, 3)
    }

    "length" {
        assertAll { l: List<Boolean> ->
            val xs = MyList.of(*l.toTypedArray())
            length(xs) shouldBe l.size
        }
    }

    "foldLeft verified with foldRight" {
        assertAll { l: List<Int> ->
            val xs = MyList.of(*l.toTypedArray())
            val left = foldLeft(xs, 1, {acc, x -> acc * x})
            val right = foldRight(xs, 1, {x, acc -> x * acc})
            left shouldBe right
        }
    }

    "reverse" {
        forall(
            row(MyList.of(), MyList.of()),
            row(MyList.of('a'), MyList.of('a')),
            row(MyList.of('a', 'b'), MyList.of('b', 'a')),
            row(MyList.of('a', 'b', 'c'), MyList.of('c', 'b', 'a'))
        ) { xs, exp -> reverse(xs) shouldBe exp }
    }

    "foldLeft vs foldLeftR" {
        assertAll { l: List<Int> ->
            val xs = MyList.of(*l.toTypedArray())
            val left = foldLeft(xs, 0, {acc, x -> acc + x})
            val leftR = foldLeftR(xs, 0, {acc, x -> acc + x})
            left shouldBe leftR
        }
    }

    "foldRight vs foldRightL" {
        assertAll { l: List<Int> ->
            val xs = MyList.of(*l.toTypedArray())
            val right = foldRight(xs, 0, {x, acc -> acc + x})
            val rightL = foldRightL(xs, 0, {x, acc -> acc + x})
            right shouldBe rightL
        }
    }

    "append" {
        forall(
            row(MyList.of(), MyList.of(), MyList.of()),
            row(MyList.of(), MyList.of(1), MyList.of(1)),
            row(MyList.of(1), MyList.of(), MyList.of(1)),
            row(MyList.of(1), MyList.of(1), MyList.of(1, 1)),
            row(MyList.of(1, 2), MyList.of(3, 4), MyList.of(1, 2, 3, 4))
        ) { a1, a2, exp -> append(a1, a2) shouldBe exp }
    }

    "concat" {
        val aaa = MyList.of(MyList.of(1, 2), MyList.of(3, 4), MyList.of(5, 6))
        concat(aaa) shouldBe MyList.of(1, 2, 3, 4, 5, 6)
    }
})
