package jwalrus.fp

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class ChapterTwoTest : StringSpec({

    "fibonacci" {
        forall(
            row(1, 0),
            row(2, 1),
            row(3, 1),
            row(4, 2),
            row(5, 3),
            row(6, 5)
        ) { n, exp -> fibonacci(n) shouldBe exp }
    }

    "isOrdered" {
        forall(
            row(listOf(), true),
            row(listOf(1), true),
            row(listOf(1, 2), true),
            row(listOf(1, 2, 3), true),
            row(listOf(2, 1), false),
            row(listOf(1, 2, 0), false),
            row(listOf(1, 1, 1), true)
        ) { aa, exp -> isSorted(aa) { a,b -> a <= b} shouldBe exp }
    }

    "curry" {
        curry { x: Int, y: Int -> x + y}(1)(2) shouldBe 3
        curry { x: String, y: String -> "$x, $y"}("hello")("world") shouldBe "hello, world"
    }

    "uncurry" {
        uncurry<Int, Int, Int> { a -> { b -> a + b} }(1, 2) shouldBe 3
        uncurry<String, String, String> { a -> { b -> "$a, $b"}  }("hello", "world") shouldBe "hello, world"
    }

    "compose" {
        val f = { x: String -> x.toInt() }
        val g = { x: Int -> x.toString() }
        compose(f, g)(1) shouldBe 1
    }
})