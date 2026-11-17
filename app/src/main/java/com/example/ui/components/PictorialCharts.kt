package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BodyCircumference
import com.example.model.CardioMachineType
import com.example.model.CardioSession
import com.example.model.WeightGoal
import com.example.model.WeightRecord
import com.example.ui.theme.*

@Composable
fun WeightLossTrajectoryChart(
    records: List<WeightRecord>,
    goal: WeightGoal,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    targetLineColor: Color = MaterialTheme.colorScheme.secondary
) {
    if (records.isEmpty()) return

    val minWeight = minOf(goal.targetWeightKg - 2.0, records.minOf { it.weightKg } - 1.0)
    val maxWeight = maxOf(goal.startingWeightKg + 1.0, records.maxOf { it.weightKg } + 1.0)
    val weightRange = (maxWeight - minWeight).coerceAtLeast(1.0)

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 900),
        label = "weightChartAnimation"
    )

    Column(modifier = modifier) {
        // Quick Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Current Weight",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "%.1f".format(goal.currentWeightKg),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            // Delta Badge
            val lostKg = goal.startingWeightKg - goal.currentWeightKg
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "-%.1f kg lost".format(lostKg),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Target Goal",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "%.1f".format(goal.targetWeightKg),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = " kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Canvas Line Graph
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val paddingBottom = 20f
                val effectiveHeight = height - paddingBottom

                // Draw Target Horizontal Dashed Line
                val targetY = effectiveHeight - ((goal.targetWeightKg - minWeight) / weightRange * effectiveHeight).toFloat()
                drawLine(
                    color = targetLineColor.copy(alpha = 0.6f),
                    start = Offset(0f, targetY),
                    end = Offset(width, targetY),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                )

                // Build line & fill paths
                val points = records.mapIndexed { index, record ->
                    val x = (index.toFloat() / (records.size - 1).coerceAtLeast(1)) * width
                    val normalizedY = ((record.weightKg - minWeight) / weightRange).toFloat()
                    val y = effectiveHeight - (normalizedY * effectiveHeight * animatedProgress)
                    Offset(x, y)
                }

                if (points.isNotEmpty()) {
                    val path = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            val p0 = points[i - 1]
                            val p1 = points[i]
                            val controlX = (p0.x + p1.x) / 2
                            cubicTo(controlX, p0.y, controlX, p1.y, p1.x, p1.y)
                        }
                    }

                    // Gradient Fill under path
                    val fillPath = Path().apply {
                        addPath(path)
                        lineTo(points.last().x, effectiveHeight)
                        lineTo(points.first().x, effectiveHeight)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor.copy(alpha = 0.35f), lineColor.copy(alpha = 0.0f)),
                            startY = 0f,
                            endY = effectiveHeight
                        )
                    )

                    // Stroke line
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw points
                    points.forEachIndexed { idx, point ->
                        val isLast = idx == points.size - 1
                        drawCircle(
                            color = if (isLast) lineColor else lineColor.copy(alpha = 0.8f),
                            radius = if (isLast) 5.dp.toPx() else 3.5.dp.toPx(),
                            center = point
                        )
                        if (isLast) {
                            drawCircle(
                                color = PureWhite,
                                radius = 2.dp.toPx(),
                                center = point
                            )
                        }
                    }
                }
            }
        }

        // Timeline Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            records.forEach {
                Text(
                    text = it.dateString,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EnduranceAndMultiSportCard(
    weeklyCardioMinutes: Int = 145,
    cardioGoalMinutes: Int = 180,
    weeklyRunningKm: Double = 22.4,
    runningGoalKm: Double = 30.0,
    strengthVolumeTonnes: Double = 14.8,
    enduranceIndexPercent: Int = 84,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Multi-discipline insight metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cardio Minutes Bar
            MetricProgressBar(
                title = "Cardio Time",
                currentStr = "${weeklyCardioMinutes}m",
                goalStr = "/ ${cardioGoalMinutes}m",
                progress = (weeklyCardioMinutes.toFloat() / cardioGoalMinutes).coerceIn(0f, 1f),
                color = CardioGreen,
                modifier = Modifier.weight(1f)
            )

            // Running Distance
            MetricProgressBar(
                title = "Running Dist.",
                currentStr = "%.1f".format(weeklyRunningKm),
                goalStr = "km",
                progress = (weeklyRunningKm.toFloat() / runningGoalKm.toFloat()).coerceIn(0f, 1f),
                color = CoralPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Strength Volume
            MetricProgressBar(
                title = "Strength Vol.",
                currentStr = "%.1ft".format(strengthVolumeTonnes),
                goalStr = "lifted",
                progress = 0.82f,
                color = TrophyGold,
                modifier = Modifier.weight(1f)
            )

            // Endurance Capacity Index
            MetricProgressBar(
                title = "Aerobic Engine",
                currentStr = "$enduranceIndexPercent%",
                goalStr = "Index",
                progress = enduranceIndexPercent / 100f,
                color = AquaAccent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricProgressBar(
    title: String,
    currentStr: String,
    goalStr: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = currentStr,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = goalStr,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            // Progress Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(color, CircleShape)
                )
            }
        }
    }
}

@Composable
fun MacroNutrientRings(
    consumedCalories: Int,
    targetCalories: Int,
    proteinG: Double,
    proteinTargetG: Double,
    carbsG: Double,
    carbsTargetG: Double,
    fatG: Double,
    fatTargetG: Double,
    modifier: Modifier = Modifier
) {
    val calorieProgress = (consumedCalories.toFloat() / targetCalories).coerceIn(0f, 1f)
    val proteinProgress = (proteinG.toFloat() / proteinTargetG.toFloat()).coerceIn(0f, 1f)
    val carbsProgress = (carbsG.toFloat() / carbsTargetG.toFloat()).coerceIn(0f, 1f)
    val fatProgress = (fatG.toFloat() / fatTargetG.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.secondary
        val tertiaryColor = MaterialTheme.colorScheme.tertiary

        // Concentric Rings Canvas
        Box(
            modifier = Modifier.size(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 7.dp.toPx()
                val gap = 4.dp.toPx()

                // Ring 1 (Outer - Calories)
                val r1 = (size.minDimension / 2) - (strokeWidth / 2)
                drawCircle(
                    color = primaryColor.copy(alpha = 0.15f),
                    radius = r1,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * calorieProgress,
                    useCenter = false,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    size = Size(r1 * 2, r1 * 2),
                    topLeft = Offset(center.x - r1, center.y - r1)
                )

                // Ring 2 (Protein)
                val r2 = r1 - strokeWidth - gap
                drawCircle(
                    color = secondaryColor.copy(alpha = 0.15f),
                    radius = r2,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = secondaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * proteinProgress,
                    useCenter = false,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    size = Size(r2 * 2, r2 * 2),
                    topLeft = Offset(center.x - r2, center.y - r2)
                )

                // Ring 3 (Carbs)
                val r3 = r2 - strokeWidth - gap
                drawCircle(
                    color = tertiaryColor.copy(alpha = 0.15f),
                    radius = r3,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = tertiaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * carbsProgress,
                    useCenter = false,
                    style = Stroke(strokeWidth, cap = StrokeCap.Round),
                    size = Size(r3 * 2, r3 * 2),
                    topLeft = Offset(center.x - r3, center.y - r3)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$consumedCalories",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "kcal",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Legend Breakdown Column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MacroLegendItem(
                label = "Protein",
                current = "%.0fg".format(proteinG),
                target = "/ %.0fg".format(proteinTargetG),
                color = ProteinBlue
            )
            MacroLegendItem(
                label = "Carbs",
                current = "%.0fg".format(carbsG),
                target = "/ %.0fg".format(carbsTargetG),
                color = CarbsOrange
            )
            MacroLegendItem(
                label = "Fats",
                current = "%.0fg".format(fatG),
                target = "/ %.0fg".format(fatTargetG),
                color = FatGold
            )
        }
    }
}

@Composable
private fun MacroLegendItem(
    label: String,
    current: String,
    target: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            Text(
                text = current,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = target,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HeartRatePulseGraph(
    bpm: Int,
    restingBpm: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(CoralPrimary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Live Heart Rate",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Resting: $restingBpm BPM",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pulse wave Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val midY = height / 2

                // Draw ECG style line
                val path = Path().apply {
                    moveTo(0f, midY)
                    lineTo(width * 0.2f, midY)
                    lineTo(width * 0.25f, midY - 6f)
                    lineTo(width * 0.3f, midY + 8f)
                    lineTo(width * 0.35f, midY - height * 0.42f)
                    lineTo(width * 0.4f, midY + height * 0.35f)
                    lineTo(width * 0.45f, midY)
                    lineTo(width * 0.55f, midY)
                    lineTo(width * 0.6f, midY - 5f)
                    lineTo(width * 0.65f, midY)
                    lineTo(width * 0.72f, midY)
                    lineTo(width * 0.77f, midY - height * 0.45f)
                    lineTo(width * 0.82f, midY + height * 0.38f)
                    lineTo(width * 0.87f, midY)
                    lineTo(width, midY)
                }

                drawPath(
                    path = path,
                    color = CoralPrimary,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

/**
 * 7-Day Hydration Trend Chart tracking daily water intake vs daily goal with visual water bars.
 */
@Composable
fun WaterIntakeTrendChart(
    weeklyIntake: List<Pair<String, Int>>,
    targetWaterMl: Int,
    selectedDayIndex: Int = -1,
    modifier: Modifier = Modifier
) {
    val maxIntake = maxOf(targetWaterMl * 1.15f, (weeklyIntake.maxOfOrNull { it.second } ?: targetWaterMl).toFloat() * 1.1f)
    val themePrimary = MaterialTheme.colorScheme.primary
    val themeSecondary = MaterialTheme.colorScheme.secondary

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(themePrimary.copy(alpha = 0.18f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💧", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Hydration 7-Day Trend",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Target: ${targetWaterMl}ml / day",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val avg = if (weeklyIntake.isNotEmpty()) weeklyIntake.map { it.second }.average().toInt() else 0
            Box(
                modifier = Modifier
                    .background(themePrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "Avg: ${avg}ml",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = themePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Canvas 7-Day Bar Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val barCount = weeklyIntake.size.coerceAtLeast(1)
                val spacing = 12.dp.toPx()
                val totalSpacing = spacing * (barCount + 1)
                val barWidth = ((w - totalSpacing) / barCount).coerceIn(16.dp.toPx(), 44.dp.toPx())

                // Draw Target Dotted Line
                val targetRatio = (targetWaterMl / maxIntake).coerceIn(0.1f, 0.95f)
                val targetY = h - (h * targetRatio)
                drawLine(
                    color = themeSecondary.copy(alpha = 0.6f),
                    start = Offset(0f, targetY),
                    end = Offset(w, targetY),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                )

                weeklyIntake.forEachIndexed { idx, (_, ml) ->
                    val x = spacing + idx * (barWidth + spacing)
                    val ratio = (ml / maxIntake).coerceIn(0.04f, 1.0f)
                    val barH = h * ratio
                    val y = h - barH

                    // Background Track
                    drawRoundRect(
                        color = themePrimary.copy(alpha = 0.08f),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, h),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    // Active Water Fill Bar
                    val isSelected = idx == selectedDayIndex
                    val isAboveTarget = ml >= targetWaterMl
                    val barBrush = Brush.verticalGradient(
                        colors = listOf(
                            if (isAboveTarget) themePrimary else themePrimary.copy(alpha = 0.85f),
                            themePrimary.copy(alpha = 0.45f)
                        ),
                        startY = y,
                        endY = h
                    )

                    drawRoundRect(
                        brush = barBrush,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barH),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    if (isSelected) {
                        drawRoundRect(
                            color = PureWhite.copy(alpha = 0.9f),
                            topLeft = Offset(x - 2.dp.toPx(), y - 2.dp.toPx()),
                            size = Size(barWidth + 4.dp.toPx(), barH + 4.dp.toPx()),
                            style = Stroke(width = 2.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(7.dp.toPx(), 7.dp.toPx())
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Days of week labels & ML numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weeklyIntake.forEachIndexed { idx, (dayLabel, ml) ->
                val isSelected = idx == selectedDayIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = dayLabel,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (isSelected) WaterCyan else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (ml >= 1000) "%.1fL".format(ml / 1000.0) else "${ml}",
                        fontSize = 9.sp,
                        color = if (ml >= targetWaterMl) MintPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Body Circumference Multi-Measurement Graph:
 * Plots Waist, Chest, and Arms measurements simultaneously in ONE unified graph with distinct colors:
 * - Waist (Coral/Orange): Decreasing trend (waist slimming / fat loss)
 * - Chest (Cyan/Sky Blue): Hypertrophy upward progression
 * - Arms (Emerald/Mint): Arm circumference progression
 */
@Composable
fun BodyMeasurementsTrendChart(
    circumferences: List<BodyCircumference>,
    modifier: Modifier = Modifier
) {
    if (circumferences.isEmpty()) return

    val waistColor = Color(0xFFFF5722) // Coral / Orange
    val chestColor = Color(0xFF00E5FF) // Cyan / Sky Blue
    val armsColor = Color(0xFF10B981)  // Emerald / Mint

    val baseline = circumferences.first()
    val latest = circumferences.last()

    val waistDelta = latest.waistCm - baseline.waistCm
    val chestDelta = latest.chestCm - baseline.chestCm
    val armsDelta = latest.armsCm - baseline.armsCm

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "bodyChartAnimation"
    )

    Column(modifier = modifier) {
        // Summary Header Badges (Waist, Chest, Arms)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MeasurementPillBadge(
                label = "Waist",
                valueCm = latest.waistCm,
                deltaCm = waistDelta,
                color = waistColor,
                isLossGood = true,
                modifier = Modifier.weight(1f)
            )
            MeasurementPillBadge(
                label = "Chest",
                valueCm = latest.chestCm,
                deltaCm = chestDelta,
                color = chestColor,
                isLossGood = false,
                modifier = Modifier.weight(1f)
            )
            MeasurementPillBadge(
                label = "Arms",
                valueCm = latest.armsCm,
                deltaCm = armsDelta,
                color = armsColor,
                isLossGood = false,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Unified Multi-Line Canvas (Arms ~37-39 cm, Waist ~85-94 cm, Chest ~104-107 cm)
        val minScale = 30.0
        val maxScale = 115.0
        val scaleRange = maxScale - minScale

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val paddingBottom = 16f
                val effectiveH = h - paddingBottom

                // Draw Horizontal Reference Guidelines at 40cm, 60cm, 80cm, 100cm
                val gridLevels = listOf(40.0, 60.0, 80.0, 100.0)
                gridLevels.forEach { level ->
                    val y = effectiveH - ((level - minScale) / scaleRange * effectiveH).toFloat()
                    drawLine(
                        color = Color.White.copy(alpha = 0.08f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )
                }

                val n = circumferences.size.coerceAtLeast(1)
                fun getX(index: Int): Float = if (n > 1) (index.toFloat() / (n - 1)) * w else w / 2

                val waistPoints = circumferences.mapIndexed { idx, item ->
                    val x = getX(idx)
                    val normY = ((item.waistCm - minScale) / scaleRange).toFloat()
                    val y = effectiveH - (normY * effectiveH * animatedProgress)
                    Offset(x, y)
                }

                val chestPoints = circumferences.mapIndexed { idx, item ->
                    val x = getX(idx)
                    val normY = ((item.chestCm - minScale) / scaleRange).toFloat()
                    val y = effectiveH - (normY * effectiveH * animatedProgress)
                    Offset(x, y)
                }

                val armsPoints = circumferences.mapIndexed { idx, item ->
                    val x = getX(idx)
                    val normY = ((item.armsCm - minScale) / scaleRange).toFloat()
                    val y = effectiveH - (normY * effectiveH * animatedProgress)
                    Offset(x, y)
                }

                fun drawMetricLine(points: List<Offset>, color: Color) {
                    if (points.isEmpty()) return

                    val path = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            val p0 = points[i - 1]
                            val p1 = points[i]
                            val controlX = (p0.x + p1.x) / 2
                            cubicTo(controlX, p0.y, controlX, p1.y, p1.x, p1.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    points.forEachIndexed { idx, pt ->
                        val isLast = idx == points.size - 1
                        drawCircle(
                            color = color.copy(alpha = 0.35f),
                            radius = if (isLast) 7.dp.toPx() else 4.5.dp.toPx(),
                            center = pt
                        )
                        drawCircle(
                            color = color,
                            radius = if (isLast) 4.5.dp.toPx() else 3.dp.toPx(),
                            center = pt
                        )
                        if (isLast) {
                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }

                // Draw all 3 lines in ONE graph
                drawMetricLine(chestPoints, chestColor)
                drawMetricLine(waistPoints, waistColor)
                drawMetricLine(armsPoints, armsColor)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Date timeline labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            circumferences.forEach {
                Text(
                    text = it.dateString,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChartLegendPill(color = waistColor, label = "Waist (Slimming)")
            ChartLegendPill(color = chestColor, label = "Chest (Hypertrophy)")
            ChartLegendPill(color = armsColor, label = "Arms (Growth)")
        }
    }
}

/**
 * Endurance and Cardio Performance Tracking Graph:
 * Visualizes cardio sessions, duration progression, distance covered, calories burned,
 * and aerobic heart rate telemetry across treadmill, rowing, bike & elliptical.
 */
@Composable
fun EndurancePerformanceChart(
    cardioSessions: List<CardioSession>,
    modifier: Modifier = Modifier
) {
    val totalMinutes = cardioSessions.sumOf { it.durationMinutes }
    val totalDistanceKm = cardioSessions.sumOf { it.distanceKm }
    val totalCalories = cardioSessions.sumOf { it.caloriesBurned }
    val avgHeartRate = if (cardioSessions.isNotEmpty()) cardioSessions.map { it.avgHeartRateBpm }.average().toInt() else 138

    Column(modifier = modifier) {
        // Quick Telemetry Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardioStatTile(
                title = "Cardio Time",
                value = "${totalMinutes}m",
                icon = "⏱",
                color = CardioGreen,
                modifier = Modifier.weight(1f)
            )
            CardioStatTile(
                title = "Distance",
                value = "%.1f km".format(totalDistanceKm),
                icon = "🏃",
                color = CoralPrimary,
                modifier = Modifier.weight(1f)
            )
            CardioStatTile(
                title = "Calories",
                value = "$totalCalories",
                icon = "🔥",
                color = TrophyGold,
                modifier = Modifier.weight(1f)
            )
            CardioStatTile(
                title = "Avg HR",
                value = "$avgHeartRate",
                icon = "❤️",
                color = AquaAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-Bar Endurance Output Canvas
        val maxDuration = (cardioSessions.maxOfOrNull { it.durationMinutes } ?: 60).coerceAtLeast(45).toFloat() * 1.15f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val count = cardioSessions.size.coerceAtLeast(1)
                val spacing = 16.dp.toPx()
                val totalSpacing = spacing * (count + 1)
                val barWidth = ((w - totalSpacing) / count).coerceIn(24.dp.toPx(), 60.dp.toPx())

                // Draw Aerobic Target Reference Line (30 mins)
                val targetMinRatio = (30f / maxDuration).coerceIn(0.1f, 0.9f)
                val targetY = h - (h * targetMinRatio)
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(0f, targetY),
                    end = Offset(w, targetY),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                )

                cardioSessions.forEachIndexed { idx, session ->
                    val x = spacing + idx * (barWidth + spacing)
                    val ratio = (session.durationMinutes / maxDuration).coerceIn(0.08f, 1.0f)
                    val barH = h * ratio
                    val y = h - barH

                    // Background Track
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.04f),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, h),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )

                    val barColor = when (session.machineType) {
                        CardioMachineType.TREADMILL -> Color(0xFFFF5722)
                        CardioMachineType.ROWING_MACHINE -> Color(0xFF00E5FF)
                        CardioMachineType.ELLIPTICAL -> Color(0xFF10B981)
                        CardioMachineType.STATIONARY_BIKE -> Color(0xFFFFB800)
                        CardioMachineType.STAIR_CLIMBER -> Color(0xFFA855F7)
                        CardioMachineType.OUTDOOR_RUN -> Color(0xFF3B82F6)
                    }

                    // Bar Gradient
                    val barBrush = Brush.verticalGradient(
                        colors = listOf(barColor, barColor.copy(alpha = 0.45f)),
                        startY = y,
                        endY = h
                    )

                    drawRoundRect(
                        brush = barBrush,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barH),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Session Date & Machine Type Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            cardioSessions.forEach { session ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = session.dateString,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${session.durationMinutes}m • ${session.distanceKm}k",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cardio Machine Badges Breakdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            cardioSessions.map { it.machineType }.distinct().forEach { machine ->
                val badgeColor = when (machine) {
                    CardioMachineType.TREADMILL -> Color(0xFFFF5722)
                    CardioMachineType.ROWING_MACHINE -> Color(0xFF00E5FF)
                    CardioMachineType.ELLIPTICAL -> Color(0xFF10B981)
                    else -> MaterialTheme.colorScheme.primary
                }
                Box(
                    modifier = Modifier
                        .background(badgeColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = machine.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor
                    )
                }
            }
        }
    }
}

@Composable
private fun MeasurementPillBadge(
    label: String,
    valueCm: Double,
    deltaCm: Double,
    color: Color,
    isLossGood: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "%.1f cm".format(valueCm),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            val isProgress = if (isLossGood) deltaCm <= 0 else deltaCm >= 0
            Text(
                text = "%+.1f cm".format(deltaCm),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isProgress) MintPrimary else CoralPrimary
            )
        }
    }
}

@Composable
private fun ChartLegendPill(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CardioStatTile(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(icon, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(title, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
