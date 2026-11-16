package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FitnessRepository
import com.example.model.SupplementItem
import com.example.model.SupplementTiming
import com.example.ui.components.bounceClick
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.PureWhite
import java.util.Locale

@Composable
fun TargetsScreen(
    repository: FitnessRepository,
    modifier: Modifier = Modifier
) {
    val weightGoal by repository.weightGoal.collectAsState()
    val dailyCalorieTarget by repository.dailyCalorieTargetFlow.collectAsState()
    val dailyProteinTargetG by repository.dailyProteinTargetGFlow.collectAsState()
    val dailyCarbsTargetG by repository.dailyCarbsTargetGFlow.collectAsState()
    val dailyFatTargetG by repository.dailyFatTargetGFlow.collectAsState()
    val dailyWaterTargetMl by repository.dailyWaterTargetMlFlow.collectAsState()
    val supplements by repository.supplements.collectAsState()

    // Local editing state for Weight Targets
    var curWeightInput by remember(weightGoal.currentWeightKg) { mutableDoubleStateOf(weightGoal.currentWeightKg) }
    var targetWeightInput by remember(weightGoal.targetWeightKg) { mutableDoubleStateOf(weightGoal.targetWeightKg) }

    // Macro editing state
    var isWeeklyMacroMode by remember { mutableStateOf(false) }
    var proteinInput by remember(dailyProteinTargetG) { mutableDoubleStateOf(dailyProteinTargetG) }
    var carbsInput by remember(dailyCarbsTargetG) { mutableDoubleStateOf(dailyCarbsTargetG) }
    var fatInput by remember(dailyFatTargetG) { mutableDoubleStateOf(dailyFatTargetG) }

    // Water editing state
    var waterInput by remember(dailyWaterTargetMl) { mutableIntStateOf(dailyWaterTargetMl) }

    // Toast state
    var feedbackToast by remember { mutableStateOf<String?>(null) }

    // Supplement dialog state
    var showAddSupplementDialog by remember { mutableStateOf(false) }
    var editingSupplement by remember { mutableStateOf<SupplementItem?>(null) }

    val morningSupplements = remember(supplements) {
        supplements.filter { it.timing == SupplementTiming.MORNING }
    }
    val eveningSupplements = remember(supplements) {
        supplements.filter { it.timing == SupplementTiming.EVENING }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. SCREEN HEADER ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Targets & Goals",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Weight, nutrition macros, daily water & supplement routine",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = "Targets Hub",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Feedback banner
                AnimatedVisibility(visible = feedbackToast != null) {
                    feedbackToast?.let { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .border(1.dp, MintPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "✓ $msg",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MintPrimary
                                )
                                IconButton(
                                    onClick = { feedbackToast = null },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = MintPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 2. WEIGHT TARGETS SECTION ---
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CoralPrimary.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonitorWeight,
                                    contentDescription = null,
                                    tint = CoralPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Weight Targets",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Starting: ${weightGoal.startingWeightKg} kg • Pace: ${weightGoal.weeklyPaceKg} kg/wk",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        val weightDiff = curWeightInput - targetWeightInput
                        val diffText = if (weightDiff > 0) String.format(Locale.US, "-%.1f kg to goal", weightDiff)
                        else if (weightDiff < 0) String.format(Locale.US, "+%.1f kg to goal", -weightDiff)
                        else "Goal reached!"
                        Box(
                            modifier = Modifier
                                .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = diffText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Current Weight & Target Weight Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Current Weight
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CURRENT WEIGHT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(Locale.US, "%.1f kg", curWeightInput),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { curWeightInput = (curWeightInput - 0.2).coerceAtLeast(30.0) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "-0.2kg", modifier = Modifier.size(14.dp))
                                }
                                IconButton(
                                    onClick = { curWeightInput = (curWeightInput + 0.2).coerceAtMost(250.0) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "+0.2kg", modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        // Target Weight
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TARGET WEIGHT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(Locale.US, "%.1f kg", targetWeightInput),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { targetWeightInput = (targetWeightInput - 0.5).coerceAtLeast(30.0) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "-0.5kg", modifier = Modifier.size(14.dp))
                                }
                                IconButton(
                                    onClick = { targetWeightInput = (targetWeightInput + 0.5).coerceAtMost(250.0) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "+0.5kg", modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress bar between start and target
                    val totalLossNeeded = (weightGoal.startingWeightKg - targetWeightInput).coerceAtLeast(0.1)
                    val currentLoss = (weightGoal.startingWeightKg - curWeightInput).coerceAtLeast(0.0)
                    val progressFraction = (currentLoss / totalLossNeeded).coerceIn(0.0, 1.0).toFloat()
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CoralPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            repository.updateWeightTargets(curWeightInput, targetWeightInput)
                            feedbackToast = String.format(Locale.US, "Weight targets updated: %.1f kg current, %.1f kg target", curWeightInput, targetWeightInput)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                repository.updateWeightTargets(curWeightInput, targetWeightInput)
                                feedbackToast = String.format(Locale.US, "Weight targets updated: %.1f kg current, %.1f kg target", curWeightInput, targetWeightInput)
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                    ) {
                        Text("Save Weight Targets", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        // --- 3. MACRONUTRIENT & CALORIE TARGETS SECTION (DAILY / WEEKLY TOGGLE) ---
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(ElectricBlue.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Macronutrient Targets",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isWeeklyMacroMode) "Viewing 7-day cumulative intake targets" else "Viewing single-day intake targets",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Daily vs Weekly Toggle
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (!isWeeklyMacroMode) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable { isWeeklyMacroMode = false }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Daily",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isWeeklyMacroMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isWeeklyMacroMode) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable { isWeeklyMacroMode = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Weekly",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isWeeklyMacroMode) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val computedDailyCalories = ((proteinInput * 4) + (carbsInput * 4) + (fatInput * 9)).toInt()
                    val displayCalories = if (isWeeklyMacroMode) computedDailyCalories * 7 else computedDailyCalories
                    val displayProtein = if (isWeeklyMacroMode) proteinInput * 7 else proteinInput
                    val displayCarbs = if (isWeeklyMacroMode) carbsInput * 7 else carbsInput
                    val displayFat = if (isWeeklyMacroMode) fatInput * 7 else fatInput

                    // Calorie Summary Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isWeeklyMacroMode) "WEEKLY ENERGY TARGET" else "DAILY ENERGY TARGET",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "$displayCalories kcal",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PureWhite
                                )
                            }
                            Text(
                                text = if (isWeeklyMacroMode) "7-Day Total" else "${computedDailyCalories} kcal/day",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 1. Protein Target
                    MacroTargetAdjusterRow(
                        label = "Protein Target",
                        amount = displayProtein,
                        dailyAmount = proteinInput,
                        unit = "g",
                        isWeekly = isWeeklyMacroMode,
                        color = CoralPrimary,
                        onDecrement = { proteinInput = (proteinInput - 5.0).coerceAtLeast(30.0) },
                        onIncrement = { proteinInput = (proteinInput + 5.0).coerceAtMost(400.0) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Carbs Target
                    MacroTargetAdjusterRow(
                        label = "Carbohydrates Target",
                        amount = displayCarbs,
                        dailyAmount = carbsInput,
                        unit = "g",
                        isWeekly = isWeeklyMacroMode,
                        color = ElectricBlue,
                        onDecrement = { carbsInput = (carbsInput - 10.0).coerceAtLeast(20.0) },
                        onIncrement = { carbsInput = (carbsInput + 10.0).coerceAtMost(600.0) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. Fats Target
                    MacroTargetAdjusterRow(
                        label = "Fats Target",
                        amount = displayFat,
                        dailyAmount = fatInput,
                        unit = "g",
                        isWeekly = isWeeklyMacroMode,
                        color = MintPrimary,
                        onDecrement = { fatInput = (fatInput - 5.0).coerceAtLeast(15.0) },
                        onIncrement = { fatInput = (fatInput + 5.0).coerceAtMost(200.0) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            repository.updateMacroTargets(proteinInput, carbsInput, fatInput, computedDailyCalories)
                            feedbackToast = "Nutrition targets saved: ${proteinInput.toInt()}g P • ${carbsInput.toInt()}g C • ${fatInput.toInt()}g F ($computedDailyCalories kcal)"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                repository.updateMacroTargets(proteinInput, carbsInput, fatInput, computedDailyCalories)
                                feedbackToast = "Nutrition targets saved: ${proteinInput.toInt()}g P • ${carbsInput.toInt()}g C • ${fatInput.toInt()}g F ($computedDailyCalories kcal)"
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save Macro Targets", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        // --- 4. DAILY WATER TARGET SECTION ---
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(ElectricBlue.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Daily Water Target",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Optimal cellular hydration & metabolic performance",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = String.format(Locale.US, "%.1f L", waterInput / 1000.0),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "DAILY INTAKE TARGET",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$waterInput ml",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = PureWhite
                            )
                            Text(
                                text = "≈ ${(waterInput / 250)} standard glasses (250 ml)",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { waterInput = (waterInput - 250).coerceAtLeast(500) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "-250ml", modifier = Modifier.size(16.dp))
                            }
                            IconButton(
                                onClick = { waterInput = (waterInput + 250).coerceAtMost(8000) },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "+250ml", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Preset Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(2500, 3000, 3500, 4000).forEach { preset ->
                            val isSelected = waterInput == preset
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { waterInput = preset }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${preset / 1000.0}L",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            repository.setDailyWaterTargetMl(waterInput)
                            feedbackToast = "Water target updated to $waterInput ml (${waterInput / 1000.0}L/day)!"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick {
                                repository.setDailyWaterTargetMl(waterInput)
                                feedbackToast = "Water target updated to $waterInput ml (${waterInput / 1000.0}L/day)!"
                            },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text("Save Water Target", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        // --- 5. SUPPLEMENTS REGIMEN (MORNING & EVENING) ---
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MintPrimary.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Medication,
                                    contentDescription = null,
                                    tint = MintPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Supplements Routine",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Morning & Evening timing, dosage & tracking",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = {
                                editingSupplement = null
                                showAddSupplementDialog = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // A. MORNING SUPPLEMENTS
                    val morningTakenCount = morningSupplements.count { it.isTaken }
                    SupplementGroupCard(
                        title = "Morning Regimen",
                        icon = "🌅",
                        accentColor = CoralPrimary,
                        progressText = "$morningTakenCount/${morningSupplements.size} Taken",
                        supplements = morningSupplements,
                        onToggle = { id -> repository.toggleSupplementTaken(id) },
                        onEdit = { item -> editingSupplement = item },
                        onDelete = { id ->
                            repository.deleteSupplement(id)
                            feedbackToast = "Supplement removed."
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // B. EVENING SUPPLEMENTS
                    val eveningTakenCount = eveningSupplements.count { it.isTaken }
                    SupplementGroupCard(
                        title = "Evening Regimen",
                        icon = "🌙",
                        accentColor = ElectricBlue,
                        progressText = "$eveningTakenCount/${eveningSupplements.size} Taken",
                        supplements = eveningSupplements,
                        onToggle = { id -> repository.toggleSupplementTaken(id) },
                        onEdit = { item -> editingSupplement = item },
                        onDelete = { id ->
                            repository.deleteSupplement(id)
                            feedbackToast = "Supplement removed."
                        }
                    )
                }
            }
        }
    }

    // --- DIALOGS: ADD / EDIT SUPPLEMENT ---
    if (showAddSupplementDialog || editingSupplement != null) {
        val isEdit = editingSupplement != null
        var name by remember { mutableStateOf(editingSupplement?.name ?: "") }
        var dosage by remember { mutableStateOf(editingSupplement?.dosage ?: "") }
        var timing by remember { mutableStateOf(editingSupplement?.timing ?: SupplementTiming.MORNING) }
        var notes by remember { mutableStateOf(editingSupplement?.notes ?: "") }

        AlertDialog(
            onDismissRequest = {
                showAddSupplementDialog = false
                editingSupplement = null
            },
            title = {
                Text(
                    text = if (isEdit) "Edit Supplement" else "Add New Supplement",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Supplement Name", fontSize = 11.sp) },
                        placeholder = { Text("e.g. Creatine, Ashwagandha", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosage / Instructions", fontSize = 11.sp) },
                        placeholder = { Text("e.g. 5g with water, 400mg", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Timing Regimen",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SupplementTiming.values().forEach { t ->
                            val selected = timing == t
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { timing = t }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${t.iconEmoji} ${t.label}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (Optional)", fontSize = 11.sp) },
                        placeholder = { Text("e.g. Take with food", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            if (isEdit) {
                                repository.updateSupplement(editingSupplement!!.id, name, dosage, timing, notes)
                                feedbackToast = "Updated supplement: $name"
                            } else {
                                repository.addSupplement(name, dosage, timing, notes)
                                feedbackToast = "Added supplement: $name"
                            }
                            showAddSupplementDialog = false
                            editingSupplement = null
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (isEdit) "Save Changes" else "Add Supplement", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddSupplementDialog = false
                    editingSupplement = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun MacroTargetAdjusterRow(
    label: String,
    amount: Double,
    dailyAmount: Double,
    unit: String,
    isWeekly: Boolean,
    color: Color,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = if (isWeekly) "Daily: ${dailyAmount.toInt()}$unit • Weekly: ${amount.toInt()}$unit" else "${amount.toInt()}$unit / day",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(28.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
            }
            Text(
                text = "${amount.toInt()}$unit",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PureWhite,
                modifier = Modifier.widthIn(min = 45.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(28.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun SupplementGroupCard(
    title: String,
    icon: String,
    accentColor: Color,
    progressText: String,
    supplements: List<SupplementItem>,
    onToggle: (String) -> Unit,
    onEdit: (SupplementItem) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Box(
                modifier = Modifier
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = progressText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (supplements.isEmpty()) {
            Text(
                text = "No supplements added for $title. Tap \"+ Add\" above.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                supplements.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                            .border(
                                1.dp,
                                if (item.isTaken) MintPrimary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onToggle(item.id) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Checkbox toggle
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        if (item.isTaken) MintPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                        CircleShape
                                    )
                                    .border(
                                        1.dp,
                                        if (item.isTaken) MintPrimary else MaterialTheme.colorScheme.outlineVariant,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (item.isTaken) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Taken",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isTaken) PureWhite else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = item.dosage + if (item.notes.isNotBlank()) " • ${item.notes}" else "",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            IconButton(
                                onClick = { onEdit(item) },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Supplement",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            IconButton(
                                onClick = { onDelete(item.id) },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Supplement",
                                    tint = CoralPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
