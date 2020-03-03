package jwalrus.fp

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec


class HelloWorldSpec : ShouldSpec({
    should("say hello world") {
        "hello, world" shouldBe "hello, world"
    }
})