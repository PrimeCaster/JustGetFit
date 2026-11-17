package com.example.data

import com.example.util.FitnessDateUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FitnessRepositoryDateNavTest {

    private lateinit var repository: FitnessRepository

    @Before
    fun setUp() {
        repository = FitnessRepository()
    }

    @Test
    fun testWorkoutDateNavigationBackwards7Days() {
        val todayStr = repository.selectedWorkoutDate.value
        assertEquals("Today", FitnessDateUtils.getMainDateHeading(todayStr))
        
        var currentDate = todayStr
        for (day in 1..7) {
            val prevDate = FitnessDateUtils.shiftDate(currentDate, -1)
            assertNotEquals("Shift should change date", currentDate, prevDate)
            
            repository.selectWorkoutDate(prevDate)
            assertEquals(prevDate, repository.selectedWorkoutDate.value)
            
            val activeWorkout = repository.activeWorkout.value
            println("Day $day: date=$prevDate heading=${FitnessDateUtils.getMainDateHeading(prevDate)} workout=${activeWorkout.title} exercises=${activeWorkout.exercises.size}")
            
            assertTrue("Workout on day $day must have exercises! Title: ${activeWorkout.title}", activeWorkout.exercises.isNotEmpty())
            assertTrue("Volume on day $day must be > 0", activeWorkout.totalVolumeKg > 0.0)
            
            val options = FitnessDateUtils.getDynamicDateOptions(prevDate)
            assertEquals("Day $day must show ${day + 1} options", day + 1, options.size)
            
            currentDate = prevDate
        }
    }

    @Test
    fun testNutritionDateNavigationBackwards7Days() {
        val todayStr = repository.selectedNutritionDate.value
        assertEquals("Today", FitnessDateUtils.getMainDateHeading(todayStr))
        
        var currentDate = todayStr
        for (day in 1..7) {
            val prevDate = FitnessDateUtils.shiftDate(currentDate, -1)
            assertNotEquals("Shift should change date", currentDate, prevDate)
            
            repository.selectNutritionDate(prevDate)
            assertEquals(prevDate, repository.selectedNutritionDate.value)
            
            val meals = repository.getMealsForDate(prevDate)
            println("Day $day: date=$prevDate heading=${FitnessDateUtils.getMainDateHeading(prevDate)} mealsCount=${meals.size}")
            
            assertTrue("Meals on day $day must exist! Date: $prevDate", meals.isNotEmpty())
            
            val water = repository.getWaterForDate(prevDate)
            assertTrue("Water on day $day must be > 0", water > 0)
            
            currentDate = prevDate
        }
    }
}
