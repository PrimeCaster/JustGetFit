package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FitnessRepository
import com.example.model.*
import com.example.ui.components.AiNutritionLoggingDialog
import com.example.ui.components.AppSettingsDialog
import com.example.ui.components.MacroNutrientRings
import com.example.ui.components.bounceClick
import com.example.ui.components.cardBounceClick
import com.example.ui.theme.*
import com.example.util.FitnessDateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    repository: FitnessRepository,
    modifier: Modifier = Modifier
) {
    val loggedMeals by repository.loggedMeals.collectAsState()
    val foodsCatalog by repository.foodsCatalog.collectAsState()
    val recipes by repository.recipes.collectAsState()
    val waterIntakeMl by repository.waterIntakeMl.collectAsState()
    val selectedNutritionDate by repository.selectedNutritionDate.collectAsState()

    var activeNutritionToast by remember { mutableStateOf<String?>(null) }

    // Dialogs & Sheets
    var showFoodSearchSheet by remember { mutableStateOf(false) }
    var selectedMealCategoryForAdd by remember { mutableStateOf(MealCategory.BREAKFAST) }
    var showRecipeDetailDialog by remember { mutableStateOf<Recipe?>(null) }
    var showAiFoodDialog by remember { mutableStateOf(false) }
    var aiFoodInitialQuery by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showNutritionDatePickerDialog by remember { mutableStateOf(false) }
    var editingMealEntry by remember { mutableStateOf<LoggedMealItem?>(null) }
    var showEditWaterDialog by remember { mutableStateOf(false) }

    // Search query
    var searchQuery by remember { mutableStateOf("") }

    val filteredFoods = remember(searchQuery, foodsCatalog) {
        if (searchQuery.isBlank()) foodsCatalog
        else foodsCatalog.filter { it.name.contains(searchQuery, ignoreCase = true) || it.brandOrCategory.contains(searchQuery, ignoreCase = true) }
    }

    val mealsForDate = remember(loggedMeals, selectedNutritionDate) {
        repository.getMealsForDate(selectedNutritionDate)
    }

    val totalCalories = mealsForDate.sumOf { it.totalCalories }
    val totalProtein = mealsForDate.sumOf { it.totalProtein }
    val totalCarbs = mealsForDate.sumOf { it.totalCarbs }
    val totalFat = mealsForDate.sumOf { it.totalFat }

    val caloriesRemaining = (repository.dailyCalorieTarget - totalCalories).coerceAtLeast(0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nutrition & Diet",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Daily macro tracking & AI precision diet calculator",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AI / Custom Food Button
                    Button(
                        onClick = {
                            aiFoodInitialQuery = ""
                            showAiFoodDialog = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.bounceClick {
                            aiFoodInitialQuery = ""
                            showAiFoodDialog = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Food", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Top Burger Menu Settings Button
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .bounceClick { showSettingsDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Settings Menu",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Date Navigation Bar (Load and Edit Previous Days, Weeks & Older Nutrition)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val prev = FitnessDateUtils.shiftDate(selectedNutritionDate, -1)
                                repository.selectNutritionDate(prev)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day", tint = CoralPrimary)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showNutritionDatePickerDialog = true }
                        ) {
                            Text(
                                text = FitnessDateUtils.getRelativeLabel(selectedNutritionDate),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tap to pick any previous day or week",
                                fontSize = 11.sp,
                                color = CoralPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        IconButton(
                            onClick = {
                                val next = FitnessDateUtils.shiftDate(selectedNutritionDate, 1)
                                repository.selectNutritionDate(next)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day", tint = CoralPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Quick Date Pills
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(FitnessDateUtils.getRecentDateOptions(7)) { option ->
                            val isSelected = FitnessDateUtils.getNormalizedDate(selectedNutritionDate) == option.dateString
                            FilterChip(
                                selected = isSelected,
                                onClick = { repository.selectNutritionDate(option.dateString) },
                                label = {
                                    Text(
                                        option.displayLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CoralPrimary,
                                    selectedLabelColor = PureWhite
                                )
                            )
                        }
                    }
                }
            }
        }

        // Encouraging Nutrition Toast
        item {
            AnimatedVisibility(visible = activeNutritionToast != null) {
                activeNutritionToast?.let { toast ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(1.dp, MintPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = toast, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            IconButton(onClick = { activeNutritionToast = null }, modifier = Modifier.size(20.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MintPrimary)
                            }
                        }
                    }
                }
            }
        }

        // Daily Macro Rings & Budget Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CALORIE BUDGET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$caloriesRemaining kcal left", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MintPrimary)
                        }
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Goal: ${repository.dailyCalorieTarget} kcal", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    MacroNutrientRings(
                        consumedCalories = totalCalories,
                        targetCalories = repository.dailyCalorieTarget,
                        proteinG = totalProtein,
                        proteinTargetG = repository.dailyProteinTargetG,
                        carbsG = totalCarbs,
                        carbsTargetG = repository.dailyCarbsTargetG,
                        fatG = totalFat,
                        fatTargetG = repository.dailyFatTargetG,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Water Hydration Logger
        item {
            val currentWater = repository.getWaterForDate(selectedNutritionDate)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(WaterCyan.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = WaterCyan, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Hydration Tracker", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { showEditWaterDialog = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Water", tint = WaterCyan, modifier = Modifier.size(14.dp))
                                }
                            }
                            Text("$currentWater / ${repository.dailyWaterTargetMl} ml", fontSize = 12.sp, color = WaterCyan, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { repository.addWater(250, selectedNutritionDate) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WaterCyan.copy(alpha = 0.15f), contentColor = WaterCyan),
                            modifier = Modifier.bounceClick { repository.addWater(250, selectedNutritionDate) }
                        ) {
                            Text("+250ml", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { repository.addWater(500, selectedNutritionDate) },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WaterCyan),
                            modifier = Modifier.bounceClick { repository.addWater(500, selectedNutritionDate) }
                        ) {
                            Text("+500ml", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Meal Diary by Categories
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MEAL DIARY • ${FitnessDateUtils.getRelativeLabel(selectedNutritionDate).uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "$totalCalories kcal logged",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralPrimary
                )
            }
        }

        if (mealsForDate.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RestaurantMenu,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No meals logged for ${FitnessDateUtils.getRelativeLabel(selectedNutritionDate)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Forgot to log that day? Tap \"Add Food\" under any meal category above to retroactively record your meals, snacks, or water!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        items(MealCategory.values()) { category ->
            val categoryMeals = mealsForDate.filter { it.mealCategory == category }
            val categoryCalories = categoryMeals.sumOf { it.totalCalories }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(category.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("$categoryCalories kcal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Button(
                            onClick = {
                                selectedMealCategoryForAdd = category
                                showFoodSearchSheet = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                            modifier = Modifier.bounceClick {
                                selectedMealCategoryForAdd = category
                                showFoodSearchSheet = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Food", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (categoryMeals.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        categoryMeals.forEach { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(entry.foodItem.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                        if (entry.numberOfServings != 1.0) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "(${entry.numberOfServings}x)",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CoralPrimary
                                            )
                                        }
                                    }
                                    Text(
                                        "${entry.foodItem.servingUnit} • P: %.0fg | C: %.0fg | F: %.0fg".format(entry.totalProtein, entry.totalCarbs, entry.totalFat),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${entry.totalCalories} kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { editingMealEntry = entry },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Meal", tint = MintPrimary, modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(
                                        onClick = { repository.removeMealEntry(entry.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Slate600, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Robust Recipe Database Section
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ATHLETE RECIPE DATABASE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${recipes.size} Curated Recipes",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        items(recipes) { recipe ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .cardBounceClick { showRecipeDetailDialog = recipe }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipe.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(recipe.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${recipe.calories} kcal • P: %.0fg • C: %.0fg • F: %.0fg".format(recipe.proteinG, recipe.carbsG, recipe.fatG),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TrophyGold
                        )
                    }

                    Button(
                        onClick = {
                            val toast = repository.logRecipeToDiary(recipe, MealCategory.LUNCH, selectedNutritionDate)
                            activeNutritionToast = toast
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                        modifier = Modifier.bounceClick {
                            val toast = repository.logRecipeToDiary(recipe, MealCategory.LUNCH, selectedNutritionDate)
                            activeNutritionToast = toast
                        }
                    ) {
                        Text("+ Log", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Food Search Sheet Dialog
    if (showFoodSearchSheet) {
        AlertDialog(
            onDismissRequest = { showFoodSearchSheet = false },
            title = {
                Text(
                    text = "Add to ${selectedMealCategoryForAdd.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search chicken, oats, whey, eggs...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // AI Quick Calculation Banner Shortcut
                    Surface(
                        onClick = {
                            aiFoodInitialQuery = searchQuery
                            showFoodSearchSheet = false
                            showAiFoodDialog = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                aiFoodInitialQuery = searchQuery
                                showFoodSearchSheet = false
                                showAiFoodDialog = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(
                                text = if (searchQuery.isNotBlank()) "✨ Calculate \"$searchQuery\" with AI" else "✨ Describe food & calculate macros with AI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(modifier = Modifier.height(240.dp)) {
                        items(filteredFoods) { food ->
                            ListItem(
                                headlineContent = { Text(food.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                                supportingContent = {
                                    Text("${food.servingUnit} • P: %.0fg | C: %.0fg | F: %.0fg".format(food.proteinGrams, food.carbsGrams, food.fatGrams), fontSize = 11.sp)
                                },
                                trailingContent = {
                                    Text("${food.caloriesPerServing} kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoralPrimary)
                                },
                                modifier = Modifier
                                    .clickable {
                                        val toast = repository.addFoodToDiary(food, selectedMealCategoryForAdd, 1.0, selectedNutritionDate)
                                        activeNutritionToast = toast
                                        showFoodSearchSheet = false
                                        searchQuery = ""
                                    }
                                    .padding(vertical = 2.dp)
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showFoodSearchSheet = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Recipe Detail Dialog
    showRecipeDetailDialog?.let { recipe ->
        AlertDialog(
            onDismissRequest = { showRecipeDetailDialog = null },
            title = {
                Text(recipe.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    item {
                        Text(
                            "${recipe.calories} kcal • P: %.0fg • C: %.0fg • F: %.0fg".format(recipe.proteinG, recipe.carbsG, recipe.fatG),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Ingredients:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        recipe.ingredients.forEach { ing ->
                            Text("• $ing", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Instructions:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        recipe.instructions.forEachIndexed { idx, step ->
                            Text("${idx + 1}. $step", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toast = repository.logRecipeToDiary(recipe, MealCategory.LUNCH, selectedNutritionDate)
                        activeNutritionToast = toast
                        showRecipeDetailDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    modifier = Modifier.bounceClick {
                        val toast = repository.logRecipeToDiary(recipe, MealCategory.LUNCH, selectedNutritionDate)
                        activeNutritionToast = toast
                        showRecipeDetailDialog = null
                    }
                ) {
                    Text("Log to Lunch", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecipeDetailDialog = null }) {
                    Text("Close")
                }
            }
        )
    }

    // AI-Powered Meal & Food Logging Dialog
    if (showAiFoodDialog) {
        AiNutritionLoggingDialog(
            repository = repository,
            initialMealCategory = selectedMealCategoryForAdd,
            initialQuery = aiFoodInitialQuery,
            onOpenSettings = {
                showAiFoodDialog = false
                showSettingsDialog = true
            },
            onDismiss = { showAiFoodDialog = false },
            onSuccess = { toastMsg ->
                activeNutritionToast = toastMsg
            }
        )
    }

    // App Settings & Preferences Menu Dialog (Burger Menu)
    if (showSettingsDialog) {
        AppSettingsDialog(
            repository = repository,
            onDismiss = { showSettingsDialog = false }
        )
    }

    // Edit Logged Meal Dialog
    editingMealEntry?.let { meal ->
        EditMealDialog(
            meal = meal,
            onSave = { updatedMeal ->
                repository.updateMealEntry(updatedMeal)
                editingMealEntry = null
                activeNutritionToast = "Updated ${updatedMeal.foodItem.name}!"
            },
            onDelete = {
                repository.removeMealEntry(meal.id)
                editingMealEntry = null
                activeNutritionToast = "Removed ${meal.foodItem.name}"
            },
            onDismiss = { editingMealEntry = null }
        )
    }

    // Nutrition Date Picker Dialog (Past days, weeks, months)
    if (showNutritionDatePickerDialog) {
        NutritionDatePickerDialog(
            currentDate = selectedNutritionDate,
            onSelectDate = { pickedDate ->
                repository.selectNutritionDate(pickedDate)
                showNutritionDatePickerDialog = false
            },
            onDismiss = { showNutritionDatePickerDialog = false }
        )
    }

    // Water Intake Editor Dialog
    if (showEditWaterDialog) {
        val currentWater = repository.getWaterForDate(selectedNutritionDate)
        EditWaterDialog(
            currentWaterMl = currentWater,
            date = selectedNutritionDate,
            onSave = { newAmount ->
                repository.setWater(newAmount, selectedNutritionDate)
                showEditWaterDialog = false
                activeNutritionToast = "Hydration set to ${newAmount}ml for ${FitnessDateUtils.getRelativeLabel(selectedNutritionDate)}"
            },
            onDismiss = { showEditWaterDialog = false }
        )
    }
}

/**
 * Dialog allowing user to anytime edit servings, calories, protein, carbs, or fats for any logged meal entry.
 */
@Composable
fun EditMealDialog(
    meal: LoggedMealItem,
    onSave: (LoggedMealItem) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var servingsInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.2f", meal.numberOfServings)) }
    var caloriesOverride by remember { mutableStateOf(meal.totalCalories.toString()) }
    var proteinOverride by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", meal.totalProtein)) }
    var carbsOverride by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", meal.totalCarbs)) }
    var fatOverride by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", meal.totalFat)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Meal Entry", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "${meal.foodItem.name} (${meal.mealCategory.title})",
                    fontSize = 12.sp,
                    color = CoralPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Servings Stepper
                Text("SERVING MULTIPLIER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("-0.5", "-0.25", "+0.25", "+0.5").forEach { deltaStr ->
                        val delta = deltaStr.toDouble()
                        OutlinedButton(
                            onClick = {
                                val current = servingsInput.toDoubleOrNull() ?: 1.0
                                val updated = (current + delta).coerceAtLeast(0.25)
                                servingsInput = String.format(java.util.Locale.US, "%.2f", updated)
                                // Auto recalculate macros based on base food
                                caloriesOverride = (meal.foodItem.caloriesPerServing * updated).toInt().toString()
                                proteinOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.proteinGrams * updated)
                                carbsOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.carbsGrams * updated)
                                fatOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.fatGrams * updated)
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(deltaStr, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedTextField(
                    value = servingsInput,
                    onValueChange = { input ->
                        servingsInput = input
                        input.toDoubleOrNull()?.let { updated ->
                            caloriesOverride = (meal.foodItem.caloriesPerServing * updated).toInt().toString()
                            proteinOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.proteinGrams * updated)
                            carbsOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.carbsGrams * updated)
                            fatOverride = String.format(java.util.Locale.US, "%.1f", meal.foodItem.fatGrams * updated)
                        }
                    },
                    label = { Text("Servings (e.g., 1.5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Custom Macro & Calorie Overrides
                Text("CUSTOM MACROS & CALORIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = caloriesOverride,
                    onValueChange = { caloriesOverride = it },
                    label = { Text("Calories (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = proteinOverride,
                        onValueChange = { proteinOverride = it },
                        label = { Text("Protein (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = carbsOverride,
                        onValueChange = { carbsOverride = it },
                        label = { Text("Carbs (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fatOverride,
                        onValueChange = { fatOverride = it },
                        label = { Text("Fat (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE53935)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete This Meal Entry", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalServings = servingsInput.toDoubleOrNull() ?: meal.numberOfServings
                    val finalCalories = caloriesOverride.toIntOrNull()
                    val finalProtein = proteinOverride.toDoubleOrNull()
                    val finalCarbs = carbsOverride.toDoubleOrNull()
                    val finalFat = fatOverride.toDoubleOrNull()

                    val updatedMeal = meal.copy(
                        numberOfServings = finalServings,
                        customCaloriesOverride = finalCalories,
                        customProteinOverride = finalProtein,
                        customCarbsOverride = finalCarbs,
                        customFatOverride = finalFat
                    )
                    onSave(updatedMeal)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Date picker dialog for Nutrition Screen: Jump to past days, weeks, months or enter custom date.
 */
@Composable
fun NutritionDatePickerDialog(
    currentDate: String,
    onSelectDate: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customDateInput by remember { mutableStateOf(FitnessDateUtils.getNormalizedDate(currentDate)) }
    val presets = remember { FitnessDateUtils.getQuickJumpPresets() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Nutrition Log Date", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Jump to previous days or weeks to view and retroactively log/edit food, water, and macro intake:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text("QUICK JUMP PRESETS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                presets.forEach { preset ->
                    val isCurrent = FitnessDateUtils.getNormalizedDate(currentDate) == preset.dateString
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) CoralPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isCurrent) BorderStroke(1.dp, CoralPrimary) else null,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectDate(preset.dateString) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(preset.displayLabel, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text(preset.dateString, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (isCurrent) {
                                Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = CoralPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                Text("OR CUSTOM DATE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = customDateInput,
                    onValueChange = { customDateInput = it },
                    label = { Text("Date (e.g. Oct 28, 2026)") },
                    placeholder = { Text("MMM d, yyyy") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (customDateInput.isNotBlank()) {
                        onSelectDate(customDateInput.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
            ) {
                Text("Load Date", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Dialog to view and directly edit water intake for selected date.
 */
@Composable
fun EditWaterDialog(
    currentWaterMl: Int,
    date: String,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var waterInput by remember { mutableStateOf(currentWaterMl.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Hydration", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "For ${FitnessDateUtils.getRelativeLabel(date)}",
                    fontSize = 12.sp,
                    color = WaterCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("QUICK WATER PRESETS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(0, 1500, 2500, 3500).forEach { ml ->
                        OutlinedButton(
                            onClick = { waterInput = ml.toString() },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (ml == 0) "0ml" else "${ml}ml", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("+250ml", "+500ml", "+750ml", "+1000ml").forEach { label ->
                        val addVal = label.replace("+", "").replace("ml", "").toInt()
                        Button(
                            onClick = {
                                val current = waterInput.toIntOrNull() ?: 0
                                waterInput = (current + addVal).toString()
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WaterCyan.copy(alpha = 0.2f), contentColor = WaterCyan),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = waterInput,
                    onValueChange = { waterInput = it },
                    label = { Text("Exact Water Intake (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = waterInput.toIntOrNull() ?: currentWaterMl
                    onSave(amount.coerceAtLeast(0))
                },
                colors = ButtonDefaults.buttonColors(containerColor = WaterCyan)
            ) {
                Text("Save Hydration", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(name = "Nutrition Screen Preview", showBackground = true)
@Composable
fun NutritionScreenPreview() {
    JustGetFitTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NutritionScreen(repository = FitnessRepository())
        }
    }
}

