package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class MacroRingType(
    val title: String,
    val color: Color,
    val unit: String
) {
    WATER("Water", WaterCyan, "ml"),
    CALORIES("Calories", MintPrimary, "kcal"),
    PROTEIN("Protein", ProteinBlue, "g"),
    CARBS("Carbs", CarbsOrange, "g"),
    FAT("Fats", FatGold, "g")
}

@Composable
fun DailyWaterAndMacroProgressRingCard(
    consumedCalories: Int,
    targetCalories: Int,
    proteinG: Double,
    proteinTargetG: Double,
    carbsG: Double,
    carbsTargetG: Double,
    fatG: Double,
    fatTargetG: Double,
    waterIntakeMl: Int,
    waterTargetMl: Int,
    onNavigateToDiary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMetric by remember { mutableStateOf<MacroRingType?>(null) }

    // Progress fractions
    val waterFraction = (waterIntakeMl.toFloat() / waterTargetMl.coerceAtLeast(1)).coerceIn(0f, 1f)
    val calFraction = (consumedCalories.toFloat() / targetCalories.coerceAtLeast(1)).coerceIn(0f, 1f)
    val proFraction = (proteinG.toFloat() / proteinTargetG.coerceAtLeast(1.0).toFloat()).coerceIn(0f, 1f)
    val carbFraction = (carbsG.toFloat() / carbsTargetG.coerceAtLeast(1.0).toFloat()).coerceIn(0f, 1f)
    val fatFraction = (fatG.toFloat() / fatTargetG.coerceAtLeast(1.0).toFloat()).coerceIn(0f, 1f)

    // Overall daily alignment average
    val overallScore = ((waterFraction + calFraction + proFraction + carbFraction + fatFraction) / 5f * 100).toInt()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .cardBounceClick(onNavigateToDiary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(WaterCyan.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DonutLarge,
                            contentDescription = null,
                            tint = WaterCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Daily Hydration & Macros",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Concentric Intake Target Progress",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Daily Alignment Badge
                Box(
                    modifier = Modifier
                        .background(
                            if (overallScore >= 75) MintPrimary.copy(alpha = 0.15f) else TrophyGold.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$overallScore% on target",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (overallScore >= 75) MintPrimary else TrophyGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Rings Visualizer + Metric Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Concentric Multi-Ring Component (5 Rings: Water, Calories, Protein, Carbs, Fat)
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ConcentricProgressRingsCanvas(
                        waterProgress = waterFraction,
                        calProgress = calFraction,
                        proteinProgress = proFraction,
                        carbsProgress = carbFraction,
                        fatProgress = fatFraction,
                        highlightedType = selectedMetric,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Center Hub Content (Dynamic based on selected ring or default summary)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        when (selectedMetric) {
                            MacroRingType.WATER -> {
                                Text(text = "${(waterFraction * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = WaterCyan)
                                Text(text = "Water", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            MacroRingType.CALORIES -> {
                                Text(text = "${(calFraction * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = MintPrimary)
                                Text(text = "Energy", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            MacroRingType.PROTEIN -> {
                                Text(text = "${(proFraction * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ProteinBlue)
                                Text(text = "Protein", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            MacroRingType.CARBS -> {
                                Text(text = "${(carbFraction * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = CarbsOrange)
                                Text(text = "Carbs", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            MacroRingType.FAT -> {
                                Text(text = "${(fatFraction * 100).toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = FatGold)
                                Text(text = "Fat", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            null -> {
                                Text(
                                    text = "${(targetCalories - consumedCalories).coerceAtLeast(0)}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "kcal left",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Detailed Legend & Remaining Target List
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Water
                    MacroProgressLegendRow(
                        type = MacroRingType.WATER,
                        current = "$waterIntakeMl",
                        target = "$waterTargetMl",
                        progressFraction = waterFraction,
                        isSelected = selectedMetric == MacroRingType.WATER,
                        onClick = {
                            selectedMetric = if (selectedMetric == MacroRingType.WATER) null else MacroRingType.WATER
                        }
                    )

                    // Calories
                    MacroProgressLegendRow(
                        type = MacroRingType.CALORIES,
                        current = "$consumedCalories",
                        target = "$targetCalories",
                        progressFraction = calFraction,
                        isSelected = selectedMetric == MacroRingType.CALORIES,
                        onClick = {
                            selectedMetric = if (selectedMetric == MacroRingType.CALORIES) null else MacroRingType.CALORIES
                        }
                    )

                    // Protein
                    MacroProgressLegendRow(
                        type = MacroRingType.PROTEIN,
                        current = "%.0f".format(proteinG),
                        target = "%.0f".format(proteinTargetG),
                        progressFraction = proFraction,
                        isSelected = selectedMetric == MacroRingType.PROTEIN,
                        onClick = {
                            selectedMetric = if (selectedMetric == MacroRingType.PROTEIN) null else MacroRingType.PROTEIN
                        }
                    )

                    // Carbs
                    MacroProgressLegendRow(
                        type = MacroRingType.CARBS,
                        current = "%.0f".format(carbsG),
                        target = "%.0f".format(carbsTargetG),
                        progressFraction = carbFraction,
                        isSelected = selectedMetric == MacroRingType.CARBS,
                        onClick = {
                            selectedMetric = if (selectedMetric == MacroRingType.CARBS) null else MacroRingType.CARBS
                        }
                    )

                    // Fat
                    MacroProgressLegendRow(
                        type = MacroRingType.FAT,
                        current = "%.0f".format(fatG),
                        target = "%.0f".format(fatTargetG),
                        progressFraction = fatFraction,
                        isSelected = selectedMetric == MacroRingType.FAT,
                        onClick = {
                            selectedMetric = if (selectedMetric == MacroRingType.FAT) null else MacroRingType.FAT
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConcentricProgressRingsCanvas(
    waterProgress: Float,
    calProgress: Float,
    proteinProgress: Float,
    carbsProgress: Float,
    fatProgress: Float,
    highlightedType: MacroRingType?,
    modifier: Modifier = Modifier
) {
    var isLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLoaded = true
    }

    // Cascading animated sweeps with smooth easing
    val animWater by animateFloatAsState(
        targetValue = if (isLoaded) waterProgress else 0f,
        animationSpec = tween(durationMillis = 850, delayMillis = 60, easing = FastOutSlowInEasing),
        label = "animWater"
    )
    val animCal by animateFloatAsState(
        targetValue = if (isLoaded) calProgress else 0f,
        animationSpec = tween(durationMillis = 850, delayMillis = 140, easing = FastOutSlowInEasing),
        label = "animCal"
    )
    val animPro by animateFloatAsState(
        targetValue = if (isLoaded) proteinProgress else 0f,
        animationSpec = tween(durationMillis = 850, delayMillis = 220, easing = FastOutSlowInEasing),
        label = "animPro"
    )
    val animCarb by animateFloatAsState(
        targetValue = if (isLoaded) carbsProgress else 0f,
        animationSpec = tween(durationMillis = 850, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "animCarb"
    )
    val animFat by animateFloatAsState(
        targetValue = if (isLoaded) fatProgress else 0f,
        animationSpec = tween(durationMillis = 850, delayMillis = 380, easing = FastOutSlowInEasing),
        label = "animFat"
    )

    Canvas(modifier = modifier) {
        val strokeWidth = 5.dp.toPx()
        val gap = 2.5.dp.toPx()

        // Ring 1 (Outermost - Water)
        val rWater = (size.minDimension / 2) - (strokeWidth / 2)
        drawRing(
            radius = rWater,
            strokeWidth = strokeWidth,
            progress = animWater,
            color = WaterCyan,
            isDimmed = highlightedType != null && highlightedType != MacroRingType.WATER
        )

        // Ring 2 (Calories)
        val rCal = rWater - strokeWidth - gap
        drawRing(
            radius = rCal,
            strokeWidth = strokeWidth,
            progress = animCal,
            color = MintPrimary,
            isDimmed = highlightedType != null && highlightedType != MacroRingType.CALORIES
        )

        // Ring 3 (Protein)
        val rPro = rCal - strokeWidth - gap
        drawRing(
            radius = rPro,
            strokeWidth = strokeWidth,
            progress = animPro,
            color = ProteinBlue,
            isDimmed = highlightedType != null && highlightedType != MacroRingType.PROTEIN
        )

        // Ring 4 (Carbs)
        val rCarb = rPro - strokeWidth - gap
        drawRing(
            radius = rCarb,
            strokeWidth = strokeWidth,
            progress = animCarb,
            color = CarbsOrange,
            isDimmed = highlightedType != null && highlightedType != MacroRingType.CARBS
        )

        // Ring 5 (Innermost - Healthy Fat)
        val rFat = rCarb - strokeWidth - gap
        drawRing(
            radius = rFat,
            strokeWidth = strokeWidth,
            progress = animFat,
            color = FatGold,
            isDimmed = highlightedType != null && highlightedType != MacroRingType.FAT
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRing(
    radius: Float,
    strokeWidth: Float,
    progress: Float,
    color: Color,
    isDimmed: Boolean
) {
    val trackAlpha = if (isDimmed) 0.05f else 0.15f
    val strokeColor = if (isDimmed) color.copy(alpha = 0.35f) else color

    // Background track
    drawCircle(
        color = color.copy(alpha = trackAlpha),
        radius = radius,
        style = Stroke(strokeWidth)
    )

    // Active sweep arc
    if (progress > 0f) {
        drawArc(
            color = strokeColor,
            startAngle = -90f,
            sweepAngle = (360f * progress).coerceAtMost(360f),
            useCenter = false,
            style = Stroke(strokeWidth, cap = StrokeCap.Round),
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )
    }
}

@Composable
private fun MacroProgressLegendRow(
    type: MacroRingType,
    current: String,
    target: String,
    progressFraction: Float,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) type.color.copy(alpha = 0.12f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(type.color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = type.title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$current / $target ${type.unit}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = type.color
            )
        }
    }
}
