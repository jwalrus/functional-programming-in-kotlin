package jwalrus.fp

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class TreeSpec : StringSpec({

    "size" {
        forall(
            row(Leaf("a"), 1),
            row(Branch(Leaf("a"), Leaf("b")), 3),
            row(Branch(Leaf("a"), Branch(Leaf("b"), Leaf("c"))), 5)
        ) { tree, exp ->
            size(tree) shouldBe exp
            sizeF(tree) shouldBe exp
        }
    }

    "maximum" {
        forall(
            row(Leaf(1), 1),
            row(Branch(Leaf(5), Leaf(2)), 5),
            row(Branch(Leaf(3), Branch(Leaf(1), Leaf(5))), 5)
        ) { tree, exp ->
            maximum(tree) shouldBe exp
            maximumF(tree) shouldBe exp
        }
    }

    "depth" {
        forall(
            row(Leaf(1), 1),
            row(Branch(Leaf(5), Leaf(2)), 2),
            row(Branch(Leaf(3), Branch(Leaf(1), Leaf(5))), 3),
            row(Branch(Leaf(3), Branch(Branch(Leaf(1), Leaf(2)), Leaf(5))), 4)
        ) { tree, exp ->
            depth(tree) shouldBe exp
            depthF(tree) shouldBe exp
        }
    }

    "map" {
        forall(
            row(Leaf(1), Leaf(1)),
            row(Branch(Leaf(5), Leaf(2)), Branch(Leaf(25), Leaf(4))),
            row(Branch(Leaf(3), Branch(Leaf(1), Leaf(5))), Branch(Leaf(9), Branch(Leaf(1), Leaf(25))))
        ) { tree, exp ->
            map(tree) { it * it } shouldBe exp
            mapF(tree) { it * it } shouldBe exp
        }
    }
})