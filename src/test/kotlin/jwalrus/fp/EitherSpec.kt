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
            row(MyList.of(Left("nothing")), Left("nothing")),
            row(MyList.of(Right(1), Left("nothing")), Left("nothing")),
            row(MyList.of(Right(1), Left("nothing"), Left("nada")), Left("nothing")),
            row(MyList.of(Right(1)), Right(MyList.of(1))),
            row(MyList.of(Right(1), Right(2)), Right(MyList.of(1, 2)))
        ) { lea, exp -> sequence(lea) shouldBe exp }
    }

    "traverse" {
        forall(
            row(MyList.empty(), Right(MyList.empty())),
            row(MyList.of(100), Right(MyList.of(1.0))),
            row(MyList.of(100, 50), Right(MyList.of(1.0, 2.0))),
            row(MyList.of(100, 0), Left("zero")),
            row(MyList.of(0, 100), Left("zero"))
        ) { xa: MyList<Int>, exp: Either<String, MyList<Double>> ->
            traversee(xa) { a -> if (a == 0) Left("zero") else Right(100.0 / a) } shouldBe exp
        }
    }
})