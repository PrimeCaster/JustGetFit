package com.example.data

import com.example.model.LlmProvider
import com.example.model.NutritionAiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class NutritionAiCalculator(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()
) {
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun calculateNutrition(
        foodDescription: String,
        provider: LlmProvider,
        apiKey: String,
        customModelOverride: String? = null
    ): Result<NutritionAiResult> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException(
                    "No API key provided for ${provider.displayName}.\n" +
                    "Please configure your API key in Settings (☰ burger menu at top of app)."
                )
            )
        }

        val trimmed = foodDescription.trim()
        if (trimmed.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a food description to analyze."))
        }

        try {
            when (provider) {
                LlmProvider.GEMINI -> callGeminiApi(trimmed, apiKey, customModelOverride)
                LlmProvider.CHATGPT -> callChatGptApi(trimmed, apiKey, customModelOverride)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun callGeminiApi(
        description: String,
        apiKey: String,
        customModelOverride: String?
    ): Result<NutritionAiResult> {
        val model = if (!customModelOverride.isNullOrBlank()) customModelOverride.trim() else LlmProvider.GEMINI.defaultModel
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

        val promptText = """
            You are a professional certified sports dietitian and precision nutrition calculation engine.
            Analyze the following food item or meal accurately: "$description".
            Estimate the total nutritional breakdown for the entire described portion.
            Return ONLY a valid JSON object with exactly these keys and no markdown wrapping or preamble:
            {
              "foodName": "Short descriptive food/meal title",
              "servingUnit": "Estimated serving or portion size",
              "calories": 350,
              "proteinG": 25.0,
              "carbsG": 30.0,
              "fatG": 12.0,
              "fiberG": 4.5,
              "notes": "Short breakdown of constituent items"
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            val contents = JSONArray().apply {
                val turn = JSONObject().apply {
                    put("role", "user")
                    val parts = JSONArray().apply {
                        val part = JSONObject().apply {
                            put("text", promptText)
                        }
                        put(part)
                    }
                    put("parts", parts)
                }
                put(turn)
            }
            put("contents", contents)

            val genConfig = JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.2)
            }
            put("generationConfig", genConfig)
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                val errorMsg = parseErrorMessage(responseBody, response.code, "Gemini")
                return Result.failure(Exception(errorMsg))
            }

            return parseGeminiResponse(responseBody, description)
        }
    }

    private fun parseGeminiResponse(jsonString: String, originalQuery: String): Result<NutritionAiResult> {
        return try {
            val root = JSONObject(jsonString)
            val candidates = root.getJSONArray("candidates")
            if (candidates.length() == 0) {
                return Result.failure(Exception("Gemini returned no candidates for this food item."))
            }
            val content = candidates.getJSONObject(0).getJSONObject("content")
            val parts = content.getJSONArray("parts")
            val rawText = parts.getJSONObject(0).getString("text")

            parseNutritionJson(rawText, originalQuery)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to parse Gemini response: ${e.message}"))
        }
    }

    private fun callChatGptApi(
        description: String,
        apiKey: String,
        customModelOverride: String?
    ): Result<NutritionAiResult> {
        val model = if (!customModelOverride.isNullOrBlank()) customModelOverride.trim() else LlmProvider.CHATGPT.defaultModel
        val url = "https://api.openai.com/v1/chat/completions"

        val promptText = """
            You are a professional nutrition calculation engine.
            Analyze this food/meal description: "$description".
            Return a valid JSON object ONLY with exactly these keys:
            {
              "foodName": "Short descriptive food/meal title",
              "servingUnit": "Estimated serving or portion size",
              "calories": 350,
              "proteinG": 25.0,
              "carbsG": 30.0,
              "fatG": 12.0,
              "fiberG": 4.5,
              "notes": "Short explanation or ingredients"
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            put("model", model)
            val messages = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "You are a clinical macro calculator that always outputs strict JSON.")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", promptText)
                })
            }
            put("messages", messages)
            put("temperature", 0.2)
            put("response_format", JSONObject().apply { put("type", "json_object") })
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer ${apiKey.trim()}")
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                val errorMsg = parseErrorMessage(responseBody, response.code, "ChatGPT")
                return Result.failure(Exception(errorMsg))
            }

            return parseChatGptResponse(responseBody, description)
        }
    }

    private fun parseChatGptResponse(jsonString: String, originalQuery: String): Result<NutritionAiResult> {
        return try {
            val root = JSONObject(jsonString)
            val choices = root.getJSONArray("choices")
            if (choices.length() == 0) {
                return Result.failure(Exception("ChatGPT returned no response choices."))
            }
            val message = choices.getJSONObject(0).getJSONObject("message")
            val content = message.getString("content")

            parseNutritionJson(content, originalQuery)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to parse ChatGPT response: ${e.message}"))
        }
    }

    internal fun parseNutritionJson(rawJson: String, originalQuery: String): Result<NutritionAiResult> {
        return try {
            // Clean any accidental markdown backticks ```json ... ```
            var clean = rawJson.trim()
            if (clean.startsWith("```json")) {
                clean = clean.removePrefix("```json")
            }
            if (clean.startsWith("```")) {
                clean = clean.removePrefix("```")
            }
            if (clean.endsWith("```")) {
                clean = clean.removeSuffix("```")
            }
            clean = clean.trim()

            val obj = JSONObject(clean)
            val foodName = obj.optString("foodName").ifBlank { originalQuery.replaceFirstChar { it.uppercase() } }
            val servingUnit = obj.optString("servingUnit").ifBlank { "1 serving" }
            val calories = obj.optInt("calories", (obj.optDouble("calories", 0.0)).toInt())
            val protein = obj.optDouble("proteinG", obj.optDouble("protein", 0.0))
            val carbs = obj.optDouble("carbsG", obj.optDouble("carbs", 0.0))
            val fat = obj.optDouble("fatG", obj.optDouble("fat", 0.0))
            val fiber = obj.optDouble("fiberG", obj.optDouble("fiber", 0.0))
            val notes = obj.optString("notes", "")

            Result.success(
                NutritionAiResult(
                    foodName = foodName,
                    servingUnit = servingUnit,
                    calories = calories.coerceAtLeast(0),
                    proteinGrams = (Math.round(protein * 10.0) / 10.0).coerceAtLeast(0.0),
                    carbsGrams = (Math.round(carbs * 10.0) / 10.0).coerceAtLeast(0.0),
                    fatGrams = (Math.round(fat * 10.0) / 10.0).coerceAtLeast(0.0),
                    fiberGrams = (Math.round(fiber * 10.0) / 10.0).coerceAtLeast(0.0),
                    notes = notes
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("Could not extract macro values from AI response: ${e.message}"))
        }
    }

    private fun parseErrorMessage(responseBody: String, statusCode: Int, providerName: String): String {
        return try {
            val root = JSONObject(responseBody)
            val errorObj = root.optJSONObject("error")
            val message = errorObj?.optString("message") ?: ""
            if (message.isNotBlank()) {
                "$providerName Error ($statusCode): $message"
            } else {
                "$providerName request failed ($statusCode)"
            }
        } catch (_: Exception) {
            when (statusCode) {
                401 -> "Invalid or expired $providerName API Key (401 Unauthorized)."
                403 -> "Permission denied for $providerName API Key (403 Forbidden)."
                429 -> "Rate limit or quota exceeded for $providerName (429)."
                else -> "$providerName HTTP request failed with code $statusCode."
            }
        }
    }

    suspend fun testApiConnection(
        provider: LlmProvider,
        apiKey: String,
        customModelOverride: String? = null
    ): Result<String> {
        val testResult = calculateNutrition("1 large apple", provider, apiKey, customModelOverride)
        return if (testResult.isSuccess) {
            Result.success("Success: Connected to ${provider.displayName}! (${testResult.getOrNull()?.calories} kcal)")
        } else {
            Result.failure(testResult.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}

