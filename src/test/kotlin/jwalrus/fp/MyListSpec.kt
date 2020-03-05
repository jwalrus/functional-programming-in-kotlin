package jwalrus.fp

import io.kotlintest.data.suspend.forall
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
})
