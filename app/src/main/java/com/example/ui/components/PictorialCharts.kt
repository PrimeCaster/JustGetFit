package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
                    .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "-%.1f kg lost".format(lostKg),
                    color = MintPrimary,
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
                        color = CoralPrimary
                    )
                    Text(
                        text = " kg",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = CoralPrimary.copy(alpha = 0.8f),
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
                    color = MintPrimary.copy(alpha = 0.15f),
                    radius = r1,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = MintPrimary,
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
                    color = ProteinBlue.copy(alpha = 0.15f),
                    radius = r2,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = ProteinBlue,
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
                    color = CarbsOrange.copy(alpha = 0.15f),
                    radius = r3,
                    style = Stroke(strokeWidth)
                )
                drawArc(
                    color = CarbsOrange,
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
