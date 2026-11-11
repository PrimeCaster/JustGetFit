package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.FitnessRepository
import com.example.model.FoodItem
import com.example.model.LlmProvider
import com.example.model.LoggedMealItem
import com.example.model.MealCategory
import kotlinx.coroutines.launch

@Composable
fun AiNutritionLoggingDialog(
    repository: FitnessRepository,
    initialMealCategory: MealCategory = MealCategory.BREAKFAST,
    initialQuery: String = "",
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedProvider by repository.selectedLlmProvider.collectAsState()
    val hasApiKey = repository.getActiveApiKey().isNotBlank()

    var mealCategory by remember { mutableStateOf(initialMealCategory) }
    var foodDescriptionInput by remember { mutableStateOf(initialQuery) }

    var isCalculating by remember { mutableStateOf(false) }
    var calculationError by remember { mutableStateOf<String?>(null) }
    var aiSuccessSource by remember { mutableStateOf<String?>(null) }
    var aiNotes by remember { mutableStateOf<String?>(null) }

    // Calculated / Editable fields
    var foodName by remember { mutableStateOf(initialQuery) }
    var servingUnit by remember { mutableStateOf("1 serving") }
    var caloriesInput by remember { mutableStateOf("") }
    var proteinInput by remember { mutableStateOf("") }
    var carbsInput by remember { mutableStateOf("") }
    var fatInput by remember { mutableStateOf("") }

    var logToDiary by remember { mutableStateOf(true) }
    var saveToCatalog by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Log Meal & AI Macro Calculator",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Powered by ${selectedProvider.displayName}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form Body
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Meal Category Selector
                    item {
                        Text(
                            text = "MEAL CATEGORY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            MealCategory.values().forEach { cat ->
                                val isSelected = mealCategory == cat
                                Surface(
                                    onClick = { mealCategory = cat },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = cat.title,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Natural Language Food Description Input
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "DESCRIBE WHAT YOU ATE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (hasApiKey) "✓ Key Ready" else "⚠️ Key Required",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasApiKey) MaterialTheme.colorScheme.primary else Color(0xFFF97316)
                                )
                            }

                            OutlinedTextField(
                                value = foodDescriptionInput,
                                onValueChange = {
                                    foodDescriptionInput = it
                                    calculationError = null
                                },
                                placeholder = {
                                    Text(
                                        "e.g. 2 scrambled eggs with 1 slice whole wheat toast and half an avocado",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(76.dp),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // AI Calculate Action Button
                            Button(
                                onClick = {
                                    if (foodDescriptionInput.isBlank()) {
                                        calculationError = "Please describe what food or ingredients you ate."
                                        return@Button
                                    }
                                    if (!hasApiKey) {
                                        calculationError = "No API key configured for ${selectedProvider.displayName}. Please tap below to configure your key in Settings."
                                        return@Button
                                    }

                                    isCalculating = true
                                    calculationError = null
                                    aiSuccessSource = null
                                    aiNotes = null

                                    coroutineScope.launch {
                                        val result = repository.calculateFoodNutritionWithAi(foodDescriptionInput)
                                        isCalculating = false
                                        if (result.isSuccess) {
                                            val data = result.getOrThrow()
                                            foodName = data.foodName
                                            servingUnit = data.servingUnit
                                            caloriesInput = data.calories.toString()
                                            proteinInput = data.proteinGrams.toString()
                                            carbsInput = data.carbsGrams.toString()
                                            fatInput = data.fatGrams.toString()
                                            aiSuccessSource = "Calculated via ${selectedProvider.displayName}"
                                            aiNotes = data.notes
                                        } else {
                                            calculationError = result.exceptionOrNull()?.message ?: "AI calculation failed."
                                        }
                                    }
                                },
                                enabled = !isCalculating && foodDescriptionInput.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                if (isCalculating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Analyzing with ${selectedProvider.displayName}...", fontSize = 12.sp)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("✨ Calculate Macros with AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Error or Missing Key Banner
                            AnimatedVisibility(visible = calculationError != null) {
                                calculationError?.let { err ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFEF4444).copy(alpha = 0.12f))
                                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = err, fontSize = 11.sp, color = Color(0xFFEF4444))
                                        if (!hasApiKey) {
                                            OutlinedButton(
                                                onClick = onOpenSettings,
                                                modifier = Modifier.height(32.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                            ) {
                                                Icon(Icons.Default.Settings, null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Open Settings to Enter API Key", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            // Success Source Chip
                            AnimatedVisibility(visible = aiSuccessSource != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "$aiSuccessSource ${if (!aiNotes.isNullOrBlank()) "• $aiNotes" else ""}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Editable Calculated Macro Values
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "NUTRITIONAL BREAKDOWN (EDITABLE)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Food Name & Portion
                            OutlinedTextField(
                                value = foodName,
                                onValueChange = { foodName = it },
                                label = { Text("Food / Recipe Title") },
                                placeholder = { Text("e.g. Scrambled Eggs & Avocado Toast") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = servingUnit,
                                onValueChange = { servingUnit = it },
                                label = { Text("Serving Portion") },
                                placeholder = { Text("e.g. 1 plate (200g)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // 4 Macro Boxes Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Calories
                                OutlinedTextField(
                                    value = caloriesInput,
                                    onValueChange = { caloriesInput = it },
                                    label = { Text("Calories") },
                                    placeholder = { Text("kcal") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                // Protein
                                OutlinedTextField(
                                    value = proteinInput,
                                    onValueChange = { proteinInput = it },
                                    label = { Text("Protein") },
                                    placeholder = { Text("g") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Carbs
                                OutlinedTextField(
                                    value = carbsInput,
                                    onValueChange = { carbsInput = it },
                                    label = { Text("Carbs") },
                                    placeholder = { Text("g") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                // Fat
                                OutlinedTextField(
                                    value = fatInput,
                                    onValueChange = { fatInput = it },
                                    label = { Text("Fats") },
                                    placeholder = { Text("g") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }

                    // Save Options Checkboxes
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { logToDiary = !logToDiary }
                            ) {
                                Checkbox(checked = logToDiary, onCheckedChange = { logToDiary = it })
                                Text(
                                    text = "Log directly to ${mealCategory.title} for today",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { saveToCatalog = !saveToCatalog }
                            ) {
                                Checkbox(checked = saveToCatalog, onCheckedChange = { saveToCatalog = it })
                                Text(
                                    text = "Save to Food Catalog for future quick-selection",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel", fontSize = 13.sp)
                    }

                    val canSave = foodName.isNotBlank() && caloriesInput.isNotBlank()
                    Button(
                        onClick = {
                            val cals = caloriesInput.toIntOrNull() ?: 0
                            val pro = proteinInput.toDoubleOrNull() ?: 0.0
                            val carbs = carbsInput.toDoubleOrNull() ?: 0.0
                            val fat = fatInput.toDoubleOrNull() ?: 0.0

                            val newFoodItem = FoodItem(
                                name = foodName.trim(),
                                brandOrCategory = "Custom / AI Logged",
                                servingUnit = servingUnit.trim().ifBlank { "1 serving" },
                                caloriesPerServing = cals,
                                proteinGrams = pro,
                                carbsGrams = carbs,
                                fatGrams = fat
                            )

                            if (saveToCatalog) {
                                repository.addCustomFood(
                                    name = newFoodItem.name,
                                    calories = newFoodItem.caloriesPerServing,
                                    protein = newFoodItem.proteinGrams,
                                    carbs = newFoodItem.carbsGrams,
                                    fat = newFoodItem.fatGrams,
                                    servingUnit = newFoodItem.servingUnit
                                )
                            }

                            if (logToDiary) {
                                val mealEntry = LoggedMealItem(
                                    foodItem = newFoodItem,
                                    mealCategory = mealCategory,
                                    numberOfServings = 1.0,
                                    dateString = repository.selectedNutritionDate.value
                                )
                                repository.logMeal(mealEntry)
                            }

                            onSuccess("Logged '$foodName' (${cals} kcal) to ${mealCategory.title}!")
                            onDismiss()
                        },
                        enabled = canSave,
                        modifier = Modifier
                            .weight(1.5f)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Save & Log Meal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

