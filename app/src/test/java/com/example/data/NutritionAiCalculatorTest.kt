package com.example.data

import com.example.model.LlmProvider
import com.example.model.NutritionAiResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NutritionAiCalculatorTest {

    private lateinit var calculator: NutritionAiCalculator

    @Before
    fun setup() {
        calculator = NutritionAiCalculator()
    }

    @Test
    fun testEmptyApiKeyReturnsFailure() = runBlocking {
        val result = calculator.calculateNutrition(
            foodDescription = "2 eggs and toast",
            provider = LlmProvider.GEMINI,
            apiKey = ""
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("No API key provided") == true)
    }

    @Test
    fun testEmptyFoodDescriptionReturnsFailure() = runBlocking {
        val result = calculator.calculateNutrition(
            foodDescription = "   ",
            provider = LlmProvider.CHATGPT,
            apiKey = "mock_dummy_test_key"
        )
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("enter a food description") == true)
    }

    @Test
    fun testLlmProviderDefaults() {
        assertEquals("gemini-2.5-flash", LlmProvider.GEMINI.defaultModel)
        assertEquals("gpt-4o-mini", LlmProvider.CHATGPT.defaultModel)
        assertTrue(LlmProvider.GEMINI.keyHelpUrl.contains("aistudio.google.com"))
        assertTrue(LlmProvider.CHATGPT.keyHelpUrl.contains("openai.com"))
    }

    @Test
    fun testNutritionAiResultDataModel() {
        val res = NutritionAiResult(
            foodName = "Oatmeal with Almond Milk",
            servingUnit = "1 bowl",
            calories = 280,
            proteinGrams = 8.0,
            carbsGrams = 45.0,
            fatGrams = 5.0,
            fiberGrams = 6.0,
            notes = "Healthy breakfast"
        )
        assertEquals(280, res.calories)
        assertEquals(8.0, res.proteinGrams, 0.01)
        assertEquals(45.0, res.carbsGrams, 0.01)
        assertEquals(5.0, res.fatGrams, 0.01)
        assertEquals(6.0, res.fiberGrams, 0.01)
    }
}

