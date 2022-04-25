package com.asoul.asasfans

import kotlinx.coroutines.delay
import org.junit.Test

import org.junit.Assert.*
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun random_test() {
        for (i in 0 until 10) {
            println((0 until 20).random())
            Thread.sleep(500)
        }
    }
}