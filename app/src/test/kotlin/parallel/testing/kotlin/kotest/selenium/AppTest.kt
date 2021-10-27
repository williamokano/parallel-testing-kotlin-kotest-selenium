package parallel.testing.kotlin.kotest.selenium

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test
import kotlin.test.assertNotNull

class AppTest : FunSpec({
    val underTest = App()

    test("test app has a greeting") {
        underTest.greeting shouldNotBe null
    }

    test("test app message should be hello world") {
        underTest.greeting shouldBe "Hello World!"
    }
})
