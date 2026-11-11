package com.example

import com.example.data.MotivationQuoteEngine
import com.example.model.WorkoutSet
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
}
