package com.memorizepi

import com.memorizepi.models.Round
import kotlin.test.Test
import kotlin.test.assertEquals

class RoundDateTest {
    @Test
    fun testGettingRoundDateString() {
        val round = Round(12L, 15, 1644871927000, 15L)
        assertEquals("14 FEBRUARY 2022", round.dateString)
    }
}