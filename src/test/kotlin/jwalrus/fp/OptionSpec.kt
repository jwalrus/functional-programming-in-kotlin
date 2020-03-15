package jwalrus.fp

import io.kotlintest.data.suspend.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class OptionSpec : StringSpec({

    "map" {
        forall(
            row(None, None),
            row(Some(1), Some(2))
        ) { o: Option<Int>, exp: Option<Int> -> o.map { a -> a * 2 } shouldBe exp }
    }

    "flatMap" {
        forall(
            row(None, None),
            row(Some(1), Some(2))
        ) { o: Option<Int>, exp: Option<Int> ->
            o.flatMap { a -> Some(a + 1) } shouldBe exp
        }
    }

    "getOrElse" {
        forall(
            row(None, 1),
            row(Some(2), 2)
        ) { o: Option<Int>, exp: Int ->
            o.getOrElse { 1 } shouldBe exp
        }
    }

    "orElse" {
        forall(
            row(None, Some(1)),
            row(Some(2), Some(2))
        ) { o: Option<Int>, exp: Option<Int> ->
            o.orElse { Some(1) } shouldBe exp
        }
    }

    "filter" {
        forall(
            row(None, None),
            row(Some(1), None),
            row(Some(2), Some(2))
        ) { o: Option<Int>, exp: Option<Int> ->
            o.filter { a -> a % 2 == 0 } shouldBe exp
        }
    }

    "variance" {
        forall(
            row(listOf(), None),
            row(listOf(1.0), Some(0.0)),
            row(listOf(1.0, 3.0), Some(1.0))
        ) { xs: List<Double>, exp: Option<Double> ->
            variance(xs) shouldBe exp
        }
    }

    "map2" {
        forall(
            row(None, None, None),
            row(None, Some(1), None),
            row(Some(1), None, None),
            row(Some(2), Some(3), Some(6))
        ) { a, b, exp -> map2(a, b){ x, y -> x * y } shouldBe exp }
    }

    "sequence" {
        forall(
            row(MyList.of(None), None),
            row(MyList.of(Some(1), None), None),
            row(MyList.of(Some(1)), Some(MyList.of(1))),
            row(MyList.of(Some(1), Some(2)), Some(MyList.of(1, 2)))
        ) { listOfOps, opOfList -> sequence(listOfOps) shouldBe opOfList }
    }
})