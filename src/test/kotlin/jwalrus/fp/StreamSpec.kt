package jwalrus.fp

import io.kotlintest.should
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

    should("return first element in list") {
        Stream.of(1, 2).headOption().getOrElse { -1 } shouldBe 1
        Stream.of(1, 2).headOption0().getOrElse { -1 } shouldBe 1
    }

    should("return None when list is empty") {
        Stream.empty<Int>().headOption().getOrElse { 1 } shouldBe 1
        Stream.empty<Int>().headOption0().getOrElse { 1 } shouldBe 1
    }

    should("map over values") {
        Stream.of(1, 2, 3).map { it * 2 }.toList() shouldBe Stream.of(2, 4, 6).toList()
    }

    should("filter out odd numbers" ) {
        Stream.of(1, 2, 3, 4).filter { it % 2 == 0}.toList() shouldBe Stream.of(2, 4).toList()
    }

    should("flatMap x -> [x, x]") {
        Stream.of(1, 2).flatMap { x -> Stream.of(x, x) }.toList() shouldBe Stream.of(1, 1, 2, 2).toList()
    }

    should("append 2, 3 to 0, 1") {
        Stream.of(0, 1).append(Stream.of(2, 3)).toList() shouldBe Stream.of(0, 1, 2, 3).toList()
    }

    should("create a stream of As") {
        constant("A").take(3).toList() shouldBe Stream.of("A", "A", "A").toList()
    }

    should("create a sequence 3, 4, 5") {
        from(3).take(3).toList() shouldBe Stream.of(3, 4, 5).toList()
    }

    should("generate first 8 fibonacci numbers") {
        fibs().take(8).toList() shouldBe List.of(0, 1, 1, 2, 3, 5, 8, 13)
    }
})