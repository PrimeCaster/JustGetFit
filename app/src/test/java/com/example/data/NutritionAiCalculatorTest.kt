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
        assertEquals("gemini-1.5-flash", LlmProvider.GEMINI.defaultModel)
        assertEquals("claude-3-5-sonnet-20241022", LlmProvider.CLAUDE.defaultModel)
        assertEquals("gpt-4o-mini", LlmProvider.CHATGPT.defaultModel)
        assertTrue(LlmProvider.GEMINI.keyHelpUrl.contains("aistudio.google.com"))
        assertTrue(LlmProvider.CLAUDE.keyHelpUrl.contains("anthropic.com"))
        assertTrue(LlmProvider.CHATGPT.keyHelpUrl.contains("openai.com"))
        assertTrue(LlmProvider.CLAUDE.keyPlaceholder.startsWith("sk-ant"))
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

    @Test
    fun testOfflineSmartNutritionEstimator() {
        // Test 200g chicken breast and 1 cup rice
        val chickenAndRice = calculator.estimateNutritionOffline("200g chicken breast with 1 cup white rice")
        assertTrue(chickenAndRice.calories > 350)
        assertTrue(chickenAndRice.proteinGrams >= 50.0)
        assertTrue(chickenAndRice.carbsGrams >= 30.0)

        // Test 2 scrambled eggs & toast
        val eggsAndToast = calculator.estimateNutritionOffline("2 scrambled eggs with 1 slice whole wheat toast")
        assertTrue(eggsAndToast.calories > 180)
        assertTrue(eggsAndToast.proteinGrams >= 12.0)
    }

    @Test
    fun testCalculateNutritionWithFallbackWhenNoKey() = runBlocking {
        val result = calculator.calculateNutritionWithFallback(
            foodDescription = "200g chicken breast and sweet potato",
            provider = LlmProvider.GEMINI,
            apiKey = ""
        )
        assertTrue(result.isSuccess)
        val data = result.getOrNull()
        assertNotNull(data)
        assertTrue(data!!.calories > 0)
        assertTrue(data.proteinGrams > 0)
        assertTrue(data.notes.contains("Smart Offline"))
    }
}

