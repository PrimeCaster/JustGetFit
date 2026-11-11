package com.example

import com.example.model.FoodItem
import com.example.model.LoggedMealItem
import com.example.model.MealCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleRobolectricTest {

    @Test
    fun testMealLoggingCalculation() {
        val chicken = FoodItem(
            name = "Chicken Breast",
            brandOrCategory = "Lean",
            caloriesPerServing = 200,
            proteinGrams = 40.0,
            carbsGrams = 0.0,
            fatGrams = 4.0
        )
        val entry = LoggedMealItem(
            foodItem = chicken,
            mealCategory = MealCategory.LUNCH,
            numberOfServings = 1.5,
            dateString = "Today"
        )
        assertEquals(300, entry.totalCalories)
        assertEquals(60.0, entry.totalProtein, 0.01)
        assertEquals(6.0, entry.totalFat, 0.01)
    }
}
