package com.example.util

import org.junit.Assert.*
import org.junit.Test

class FitnessDateUtilsTest {

    @Test
    fun testIsDateToday() {
        val todayStr = FitnessDateUtils.getTodayDateString()
        assertTrue(FitnessDateUtils.isDateToday("Today"))
        assertTrue(FitnessDateUtils.isDateToday(todayStr))

        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        assertFalse(FitnessDateUtils.isDateToday(yesterdayStr))
        assertFalse(FitnessDateUtils.isDateToday("Yesterday"))
    }

    @Test
    fun testGetDaysAgo() {
        val todayStr = FitnessDateUtils.getTodayDateString()
        assertEquals(0, FitnessDateUtils.getDaysAgo("Today"))
        assertEquals(0, FitnessDateUtils.getDaysAgo(todayStr))

        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        assertEquals(1, FitnessDateUtils.getDaysAgo("Yesterday"))
        assertEquals(1, FitnessDateUtils.getDaysAgo(yesterdayStr))

        val threeDaysAgoStr = FitnessDateUtils.getDateDaysAgo(3)
        assertEquals(3, FitnessDateUtils.getDaysAgo(threeDaysAgoStr))
    }

    @Test
    fun testGetDynamicDateOptionsForToday() {
        // When on Today, ONLY 1 option ([ ★ Today ]) must be returned
        val todayStr = FitnessDateUtils.getTodayDateString()
        val options = FitnessDateUtils.getDynamicDateOptions(todayStr)

        assertEquals(1, options.size)
        assertEquals(todayStr, options[0].dateString)
        assertEquals("★ Today", options[0].displayLabel)
        assertTrue(options[0].isToday)
        assertEquals(0, options[0].daysAgo)
    }

    @Test
    fun testGetDynamicDateOptionsForYesterday() {
        // When on Yesterday, 2 options must be returned: [ Yesterday ] and [ ★ Today ]
        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        val todayStr = FitnessDateUtils.getTodayDateString()

        val options = FitnessDateUtils.getDynamicDateOptions(yesterdayStr)

        assertEquals(2, options.size)
        assertEquals(yesterdayStr, options[0].dateString)
        assertEquals("Yesterday", options[0].displayLabel)
        assertEquals(1, options[0].daysAgo)

        assertEquals(todayStr, options[1].dateString)
        assertEquals("★ Today", options[1].displayLabel)
        assertEquals(0, options[1].daysAgo)
    }

    @Test
    fun testGetDynamicDateOptionsForThreeDaysAgo() {
        // When on 3 days ago, 4 options must be returned: [ 3 Days Ago ], [ 2 Days Ago ], [ Yesterday ], [ ★ Today ]
        val threeDaysAgoStr = FitnessDateUtils.getDateDaysAgo(3)
        val options = FitnessDateUtils.getDynamicDateOptions(threeDaysAgoStr)

        assertEquals(4, options.size)
        assertEquals(3, options[0].daysAgo)
        assertEquals(2, options[1].daysAgo)
        assertEquals(1, options[2].daysAgo)
        assertEquals(0, options[3].daysAgo)
        assertEquals("Yesterday", options[2].displayLabel)
        assertEquals("★ Today", options[3].displayLabel)
    }
}
