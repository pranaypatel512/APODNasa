package com.pranay.apodnasa

import java.time.Duration
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun time_different_of_hour_is_positive() {
        val targetTime = LocalTime.of(2, 0, 0) //find diff for 6am using target hour time e.g 2am
        val morningTime = LocalTime.of(6, 0, 0) //6am using target hour time of day
        assertEquals(true, Duration.between(targetTime, morningTime).toHours() > 0)
    }

    @Test
    fun time_different_of_hour_is_negative() {
        val targetTime = LocalTime.of(8, 0, 0) //find diff for 6am using target hour time e.g 8am
        //val targetTime = LocalTime.now() //find diff for 6am using target hour time e.g Current time
        val morningTime = LocalTime.of(6, 0, 0) //6am using target hour time of day
        assertEquals(true, Duration.between(targetTime, morningTime).toHours() < 0)
    }
}