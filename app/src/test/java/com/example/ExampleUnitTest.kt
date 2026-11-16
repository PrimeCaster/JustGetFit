package com.example

import com.example.data.MotivationQuoteEngine
import com.example.model.CardioMachineType
import com.example.model.CardioSession
import com.example.model.WorkoutSet
import com.example.util.ExportDateRange
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testOneRepMaxCalculation() {
        // 100kg for 1 rep should be 100kg
        val singleRepSet = WorkoutSet(setNumber = 1, weightKg = 100.0, reps = 1)
        assertEquals(100.0, singleRepSet.estimated1RM, 0.01)

        // Epley formula: 100 * (1 + 10/30) = 133.33kg
        val tenRepSet = WorkoutSet(setNumber = 2, weightKg = 100.0, reps = 10)
        assertEquals(133.33, tenRepSet.estimated1RM, 0.01)
    }

    @Test
    fun testMotivationQuoteEngine() {
        val quote = MotivationQuoteEngine.getRandomDailyQuote()
        assertNotNull(quote.text)
        assertTrue(quote.text.isNotEmpty())

        val squatCue = MotivationQuoteEngine.getExerciseCue("Barbell Back Squat")
        assertTrue(squatCue.isNotEmpty())
    }

    @Test
    fun testCardioSessionTelemetryParameters() {
        val session = CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = "Today",
            durationMinutes = 30,
            distanceKm = 5.0,
            caloriesBurned = 320,
            avgHeartRateBpm = 145,
            treadmillSpeedKmh = 10.0,
            treadmillInclinePercent = 2.0,
            speedUnit = "km/h",
            userWeightKg = 79.2,
            userAge = 28
        )
        assertEquals(CardioMachineType.TREADMILL, session.machineType)
        assertEquals(10.0, session.treadmillSpeedKmh, 0.01)
        assertEquals(2.0, session.treadmillInclinePercent, 0.01)
        assertEquals("km/h", session.speedUnit)
        assertEquals(79.2, session.userWeightKg, 0.01)
        assertEquals(28, session.userAge)
        assertEquals(320, session.caloriesBurned)
    }

    @Test
    fun testExportDateRanges() {
        val ranges = ExportDateRange.values()
        assertTrue(ranges.isNotEmpty())
        assertNotNull(ExportDateRange.CURRENT_WEEK)
        assertNotNull(ExportDateRange.CURRENT_MONTH)
        assertNotNull(ExportDateRange.SELECTED_MONTHS)
        assertNotNull(ExportDateRange.CURRENT_YEAR)
        assertNotNull(ExportDateRange.ALL_TIME)
        assertEquals(7, ExportDateRange.CURRENT_WEEK.daysLimit)
        assertEquals(30, ExportDateRange.CURRENT_MONTH.daysLimit)
        assertEquals(90, ExportDateRange.SELECTED_MONTHS.daysLimit)
    }
}
