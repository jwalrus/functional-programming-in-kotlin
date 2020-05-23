package jwalrus.fp

import io.kotlintest.matchers.numerics.shouldBeExactly
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.numerics.shouldBeLessThanOrEqual
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

class RandomSpec : StringSpec({

    "nextInt should return the same pseudo-random number sequence" {
        val r10 = SimpleRNG(1)
        val r20 = SimpleRNG(1)

        val (n10, r11) = r10.nextInt()
        val (n20, r21) = r20.nextInt()
        n10 shouldBeExactly n20

        val (n11, _) = r11.nextInt()
        val (n21, _) = r21.nextInt()
        n11 shouldBeExactly n21
    }

    "nonNegativeInt should only return non-negative integers" {
        forAll<Long> { seed ->
            val (n, _) = nonNegativeInt(SimpleRNG(seed))
            n >= 0 && n <= Int.MAX_VALUE
        }
    }

    "double should return values between 0 and 1 (exclusive)" {
        forAll<Long> { seed ->
            val (n, _) = double(SimpleRNG(seed))
            n >= 0.0 && n < 1.0
        }
    }

    "ints should generate a list of random integers" {
        val rnd = SimpleRNG(2)
        val (l1, rnd1) = ints(0, rnd)
        val (l2, rnd2) = ints(1, rnd1)
        val (l3, _) = ints(10, rnd2)

        length(l1) shouldBeExactly 0
        length(l2) shouldBeExactly 1
        length(l3) shouldBeExactly 10
    }
})