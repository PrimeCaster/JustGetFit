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

    @Test
    fun testDemoDataCompletenessAcrossApp() {
        // 1. Weight Records
        val weights = repository.weightRecords.value
        assertTrue("Weight records should be comprehensive (>= 10 entries)", weights.size >= 10)
        assertTrue("Weight trend should show weight reduction", weights.first().weightKg > weights.last().weightKg)
        println("Weight entries: ${weights.size}, from ${weights.first().weightKg}kg to ${weights.last().weightKg}kg")

        // 2. Body Circumferences
        val circumferences = repository.circumferences.value
        assertTrue("Body measurements should have multiple milestones (>= 6)", circumferences.size >= 6)
        assertTrue("Waist measurement should show decrease", circumferences.first().waistCm > circumferences.last().waistCm)
        assertTrue("Chest measurement should show increase", circumferences.first().chestCm < circumferences.last().chestCm)
        println("Circumference milestones: ${circumferences.size}, waist ${circumferences.first().waistCm}cm -> ${circumferences.last().waistCm}cm")

        // 3. Progress Photos
        val photos = repository.progressPhotos.value
        assertTrue("Progress photos should have multiple milestone comparisons (>= 4)", photos.size >= 4)

        // 4. Cardio Sessions
        val cardio = repository.cardioSessions.value
        assertTrue("Cardio sessions should span endurance machines (>= 8)", cardio.size >= 8)

        // 5. Curated Recipes
        val recipes = repository.recipes.value
        assertTrue("Recipes catalog should have diverse high-protein meals (>= 5)", recipes.size >= 5)

        // 6. Supplements Stack
        val supplements = repository.supplements.value
        assertTrue("Supplement stack should include morning and evening items (>= 7)", supplements.size >= 7)

        // 7. Scheduled Weekly Workouts
        val scheduled = repository.scheduledWorkouts.value
        assertTrue("Scheduled split should cover weekly program (>= 5)", scheduled.size >= 5)
    }
}
