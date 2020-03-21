package jwalrus.fp

import io.kotlintest.data.suspend.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class EitherSpec : StringSpec({

    "map" {
        forall(
            row(Left("Nothing"), Left("Nothing")),
            row(Right(2), Right(4))
        ) { ea, exp -> ea.map { a -> a * 2 } shouldBe exp }
    }

    "orElse" {
        forall(
            row(Left("Nothing"), Right(1)),
            row(Right(2), Right(2))
        ) { ea, exp -> ea.orElse { Right(1) } shouldBe exp }
    }

    "flatMap" {
        forall(
            row(Left("Nothing"), Left("Nothing")),
            row(Right(-1), Left("negative -1")),
            row(Right(2), Right(4))
        ) { ea, exp ->
            ea.flatMap { a -> if (a < 0) Left("negative $a") else Right(2 * a) } shouldBe exp
        }
    }

    "map2" {
        forall(
            row(Left("nothing"), Left("nada"), Left("nothing")),
            row(Left("nothing"), Right(1), Left("nothing")),
            row(Right(1), Left("nada"), Left("nada")),
            row(Right(1), Right(1), Right(2))
        ) { ea, eb, exp -> map2(ea, eb) { a, b -> a + b } shouldBe exp }
    }

    "sequence" {
        forall(
            row(List.of(Left("nothing")), Left("nothing")),
            row(List.of(Right(1), Left("nothing")), Left("nothing")),
            row(List.of(Right(1), Left("nothing"), Left("nada")), Left("nothing")),
            row(List.of(Right(1)), Right(List.of(1))),
            row(List.of(Right(1), Right(2)), Right(List.of(1, 2)))
        ) { lea, exp -> sequence(lea) shouldBe exp }
    }

    "traverse" {
        forall(
            row(List.empty(), Right(List.empty())),
            row(List.of(100), Right(List.of(1.0))),
            row(List.of(100, 50), Right(List.of(1.0, 2.0))),
            row(List.of(100, 0), Left("zero")),
            row(List.of(0, 100), Left("zero"))
        ) { xa: List<Int>, exp: Either<String, List<Double>> ->
            traversee(xa) { a -> if (a == 0) Left("zero") else Right(100.0 / a) } shouldBe exp
        }
    }
})