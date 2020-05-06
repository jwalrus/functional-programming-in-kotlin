package jwalrus.fp

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

class StreamSpec : ShouldSpec ({

    should("convert stream to list") {
        Stream.of(1, 2, 3).toList() shouldBe List.of(1, 2, 3)
    }

    should("take the first 2 elements of 1,2,3") {
        Stream.of(1, 2, 3).take(2).toList() shouldBe List.of(1, 2)
    }

    should("drop the first 2 elements of 1,2,3") {
        Stream.of(1, 2, 3).drop(2).toList() shouldBe List.of(3)
    }

    should("takeWhile numbers are less than 3") {
        Stream.of(1, 2, 3).takeWhile0 { it < 3 }.toList() shouldBe List.of(1, 2)
        Stream.of(1, 2, 3).takeWhile { it < 3 }.toList() shouldBe List.of(1, 2)
    }

    should("be true because 2 exists is in 1,2,3") {
        Stream.of(1, 2, 3).exists { it == 2 } shouldBe true
    }

    should("be false because 4 does not exist in 1,2,3") {
        Stream.of(1, 2, 3).exists { it == 4 } shouldBe false
    }

    should("be true because all are less than 5") {
        Stream.of(1, 2, 3).forAll { it < 5 } shouldBe true
    }

    should("be false because not all are greater than 2") {
        Stream.of(1, 2, 3).forAll { it > 2 } shouldBe false
    }
})