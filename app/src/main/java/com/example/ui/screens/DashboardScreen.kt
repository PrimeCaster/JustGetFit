package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FitnessRepository
import com.example.model.ScheduledWorkout
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    repository: FitnessRepository,
    onNavigateToWorkouts: () -> Unit,
    onNavigateToCardio: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme by repository.isDarkTheme.collectAsState()
    val currentColorTheme by repository.currentColorTheme.collectAsState()
    val weightRecords by repository.weightRecords.collectAsState()
    val weightGoal by repository.weightGoal.collectAsState()
    val scheduledWorkouts by repository.scheduledWorkouts.collectAsState()
    val loggedMeals by repository.loggedMeals.collectAsState()
    val cardioSessions by repository.cardioSessions.collectAsState()
    val waterIntakeMl by repository.waterIntakeMl.collectAsState()

    var showWeighInDialog by remember { mutableStateOf(false) }
    var newWeightInput by remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val upcomingWorkouts = scheduledWorkouts

    val consumedCalories = loggedMeals.sumOf { it.totalCalories }
    val consumedProtein = loggedMeals.sumOf { it.totalProtein }
    val consumedCarbs = loggedMeals.sumOf { it.totalCarbs }
    val consumedFat = loggedMeals.sumOf { it.totalFat }

    val weeklyCardioMinutes = cardioSessions.sumOf { it.durationMinutes } + 45
    val weeklyRunningKm = cardioSessions.filter { it.distanceKm > 0 }.sumOf { it.distanceKm } + 12.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "JustGetFit",
                            tint = PureWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "JustGetFit",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(TrophyGold.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TrophyGold
                                )
                            }
                        }
                        Text(
                            text = "Daily Wellness & Fitness Tracking",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Top Burger Menu Settings Button (Consolidates Themes, Dark/Light Mode, LLM Keys & Preferences)
                IconButton(
                    onClick = { showSettingsDialog = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .bounceClick { showSettingsDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Settings Menu",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Quick Athletic Color Theme Switcher Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COLOR PALETTE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${currentColorTheme.emoji} ${currentColorTheme.displayName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { showSettingsDialog = true }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(AppColorTheme.values()) { themeOption ->
                        val isSelected = currentColorTheme == themeOption
                        Surface(
                            onClick = { repository.setColorTheme(themeOption) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.bounceClick { repository.setColorTheme(themeOption) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // 3 color dots preview
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    themeOption.previewColors.forEach { dotColor ->
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(dotColor)
                                        )
                                    }
                                }
                                Text(
                                    text = "${themeOption.emoji} ${themeOption.displayName}",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1. DIET & DAILY MACRO TARGETS (TOP OF PAGE)
        item {
            DailyWaterAndMacroProgressRingCard(
                consumedCalories = consumedCalories,
                targetCalories = repository.dailyCalorieTarget,
                proteinG = consumedProtein,
                proteinTargetG = repository.dailyProteinTargetG,
                carbsG = consumedCarbs,
                carbsTargetG = repository.dailyCarbsTargetG,
                fatG = consumedFat,
                fatTargetG = repository.dailyFatTargetG,
                waterIntakeMl = waterIntakeMl,
                waterTargetMl = repository.dailyWaterTargetMl,
                onNavigateToDiary = onNavigateToNutrition,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }

        // 2. ENDURANCE, CARDIO & RUNNING TRACKING (FOLLOWING MACROS)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = AquaAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Endurance, Cardio & Running",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = onNavigateToCardio,
                            modifier = Modifier
                                .height(36.dp)
                                .bounceClick { onNavigateToCardio() },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AquaAccent.copy(alpha = 0.15f),
                                contentColor = AquaAccent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Log Cardio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Multi-Sport Pictorial Progress Bars
                    EnduranceAndMultiSportCard(
                        weeklyCardioMinutes = weeklyCardioMinutes,
                        weeklyRunningKm = weeklyRunningKm,
                        strengthVolumeTonnes = 14.8,
                        enduranceIndexPercent = 84,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 3. PICTORIAL WEIGHT LOSS & GOAL TRAJECTORY
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                contentDescription = null,
                                tint = MintPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Weight Loss & Goal Trajectory",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = { showWeighInDialog = true },
                            modifier = Modifier
                                .height(36.dp)
                                .bounceClick { showWeighInDialog = true },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Log Weight", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Canvas Pictorial Chart
                    WeightLossTrajectoryChart(
                        records = weightRecords,
                        goal = weightGoal,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 4. UPCOMING WORKOUTS SECTION (PLACED AT LAST ON TODAY PAGE)
        item {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "UPCOMING WORKOUT SCHEDULE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Open Splits >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary,
                        modifier = Modifier.clickable { onNavigateToWorkouts() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(upcomingWorkouts) { item ->
                        UpcomingWorkoutMiniCard(item = item, onClick = onNavigateToWorkouts)
                    }
                }
            }
        }
    }

    // Quick Log Weight Dialog
    if (showWeighInDialog) {
        AlertDialog(
            onDismissRequest = { showWeighInDialog = false },
            title = {
                Text(
                    text = "Log Weigh-In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter current weigh-in to update progress charts and milestone calculations.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newWeightInput,
                        onValueChange = { newWeightInput = it },
                        label = { Text("Weight (kg)") },
                        placeholder = { Text("e.g. 78.8") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val weight = newWeightInput.toDoubleOrNull()
                        if (weight != null && weight > 30) {
                            repository.logWeight(weight)
                        }
                        showWeighInDialog = false
                    },
                    modifier = Modifier.bounceClick {
                        val weight = newWeightInput.toDoubleOrNull()
                        if (weight != null && weight > 30) {
                            repository.logWeight(weight)
                        }
                        showWeighInDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save Weigh-in", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeighInDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // App Appearance & Color Theme Customizer Dialog
    // Settings & Preferences Menu Dialog (Burger Menu)
    if (showSettingsDialog) {
        AppSettingsDialog(
            repository = repository,
            onDismiss = { showSettingsDialog = false }
        )
    }
}

@Composable
fun UpcomingWorkoutMiniCard(
    item: ScheduledWorkout,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(210.dp)
            .cardBounceClick(onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(CoralPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.dayOfWeek.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary
                    )
                }
                Text(
                    text = item.date,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.focusArea,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.estimatedMinutes} min",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(name = "Dashboard Preview", showBackground = true)
@Composable
fun DashboardScreenPreview() {
    HomeScreenPreview()
}

@Preview(name = "Home Screen Preview", showBackground = true)
@Composable
fun HomeScreenPreview() {
    JustGetFitTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(
                repository = FitnessRepository(),
                onNavigateToWorkouts = {},
                onNavigateToCardio = {},
                onNavigateToNutrition = {}
            )
        }
    }
}
