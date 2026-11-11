package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.model.CardioSession
import com.example.model.LoggedMealItem
import com.example.model.WorkoutSession
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FitnessReportExporter {

    private fun getReportsDir(context: Context): File {
        val dir = File(context.cacheDir, "reports")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getTimestamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    private fun getHumanDate(): String {
        return SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date())
    }

    /**
     * Generates a comprehensive CSV export of workouts, cardio equipment logs, and nutrition meals.
     */
    fun exportToCsv(
        context: Context,
        workout: WorkoutSession,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>,
        waterIntakeMl: Int,
        weightKg: Double
    ): File {
        val file = File(getReportsDir(context), "JustGetFit_Report_${getTimestamp()}.csv")
        FileWriter(file).use { writer ->
            writer.append("=========================================\n")
            writer.append("JUSTGETFIT COMPREHENSIVE FITNESS & NUTRITION REPORT\n")
            writer.append("Export Date,${getHumanDate()}\n")
            writer.append("Current Body Weight (kg),$weightKg\n")
            writer.append("Daily Water Intake (ml),$waterIntakeMl\n")
            writer.append("=========================================\n\n")

            // 1. STRENGTH WORKOUT LOGS
            writer.append("[STRENGTH WORKOUT LOGS]\n")
            writer.append("Session Title,${workout.title}\n")
            writer.append("Date,${workout.dateString}\n")
            writer.append("Duration (min),${workout.durationMinutes}\n")
            writer.append("Total Volume (kg),${"%.1f".format(workout.totalVolumeKg)}\n")
            writer.append("Exercise Name,Gym Machine Equipment,Target Category,Equipment Type,Set Number,Weight (kg),Reps,RPE,Completed\n")

            workout.exercises.forEach { exLog ->
                val machineName = exLog.gymMachine?.let { "${it.machineName} (${it.everydayAlias})" } ?: "Standard Free Weight / Cable"
                exLog.sets.forEach { set ->
                    writer.append("\"${exLog.exercise.name}\",")
                    writer.append("\"$machineName\",")
                    writer.append("\"${exLog.exercise.category.displayName}\",")
                    writer.append("\"${exLog.exercise.equipmentType.displayName}\",")
                    writer.append("${set.setNumber},")
                    writer.append("${set.weightKg},")
                    writer.append("${set.reps},")
                    writer.append("${set.rpe},")
                    writer.append("${if (set.isCompleted) "Yes" else "No"}\n")
                }
            }
            writer.append("\n")

            // 2. CARDIO EQUIPMENT LOGS
            writer.append("[CARDIO & MACHINE TRAINING LOGS]\n")
            writer.append("Machine / Activity,Duration (min),Distance (km),Calories (kcal),Speed / Pace,Incline (%),Resistance,Avg HR (bpm),Date\n")
            cardioSessions.forEach { c ->
                val speedDisplay = when {
                    c.treadmillSpeedKmh > 0 -> "${c.treadmillSpeedKmh} km/h"
                    c.distanceKm > 0 && c.durationMinutes > 0 -> "%.1f km/h".format((c.distanceKm / c.durationMinutes) * 60)
                    else -> "-"
                }
                val inclineDisplay = if (c.treadmillInclinePercent > 0) "${c.treadmillInclinePercent}%" else "-"
                val resistanceDisplay = when {
                    c.ellipticalResistanceLevel > 0 -> "${c.ellipticalResistanceLevel}"
                    c.bikeResistanceLevel > 0 -> "${c.bikeResistanceLevel}"
                    c.rowingDamperSetting > 0 -> "Damper ${c.rowingDamperSetting}"
                    else -> "-"
                }

                writer.append("\"${c.machineType.displayName}\",")
                writer.append("${c.durationMinutes},")
                writer.append("${"%.2f".format(c.distanceKm)},")
                writer.append("${c.caloriesBurned},")
                writer.append("\"$speedDisplay\",")
                writer.append("\"$inclineDisplay\",")
                writer.append("\"$resistanceDisplay\",")
                writer.append("${c.avgHeartRateBpm},")
                writer.append("\"${c.dateString}\"\n")
            }
            writer.append("\n")

            // 3. NUTRITION & MACRONUTRIENT LOGS
            writer.append("[NUTRITION & MACRONUTRIENT LOGS]\n")
            writer.append("Meal Category,Food Item,Servings,Serving Unit,Calories (kcal),Protein (g),Carbs (g),Fat (g),Date\n")
            meals.forEach { m ->
                writer.append("\"${m.mealCategory.title}\",")
                writer.append("\"${m.foodItem.name}\",")
                writer.append("${m.numberOfServings},")
                writer.append("\"${m.foodItem.servingUnit}\",")
                writer.append("${m.totalCalories},")
                writer.append("${"%.1f".format(m.totalProtein)},")
                writer.append("${"%.1f".format(m.totalCarbs)},")
                writer.append("${"%.1f".format(m.totalFat)},")
                writer.append("\"${m.dateString}\"\n")
            }
            writer.append("\n")

            // Summary Totals
            val totalCal = meals.sumOf { it.totalCalories }
            val totalPro = meals.sumOf { it.totalProtein }
            val totalCarb = meals.sumOf { it.totalCarbs }
            val totalFat = meals.sumOf { it.totalFat }
            writer.append("DAILY TOTALS,Total Foods Logged: ${meals.size},,,$totalCal,${"%.1f".format(totalPro)},${"%.1f".format(totalCarb)},${"%.1f".format(totalFat)}\n")
        }
        return file
    }

    /**
     * Generates a beautifully formatted PDF fitness & nutrition report using Android's native PdfDocument.
     */
    fun exportToPdf(
        context: Context,
        workout: WorkoutSession,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>,
        waterIntakeMl: Int,
        weightKg: Double
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 dimensions in points
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Setup Paints
        val primaryPaint = Paint().apply {
            color = Color.rgb(15, 23, 42) // Slate 900
            style = Paint.Style.FILL
        }

        val headerPaint = Paint().apply {
            color = Color.rgb(16, 185, 129) // Emerald Mint
            textSize = 22f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val titlePaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 14f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val sectionPaint = Paint().apply {
            color = Color.rgb(255, 107, 74) // Coral Primary
            textSize = 12f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.rgb(51, 65, 85) // Slate 700
            textSize = 9.5f
            isAntiAlias = true
        }

        val smallBoldPaint = Paint().apply {
            color = Color.rgb(15, 23, 42)
            textSize = 9.5f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val dividerPaint = Paint().apply {
            color = Color.rgb(226, 232, 240) // Slate 200
            strokeWidth = 1f
        }

        val accentCardPaint = Paint().apply {
            color = Color.rgb(241, 245, 249) // Slate 100
            style = Paint.Style.FILL
        }

        // Top Banner Background
        canvas.drawRect(0f, 0f, 595f, 65f, primaryPaint)

        // Banner Text
        val bannerTitlePaint = Paint().apply {
            color = Color.rgb(16, 185, 129)
            textSize = 18f
            isFakeBoldText = true
            isAntiAlias = true
        }
        val bannerSubPaint = Paint().apply {
            color = Color.rgb(203, 213, 225)
            textSize = 10f
            isAntiAlias = true
        }
        canvas.drawText("JUSTGETFIT • ATHLETIC PERFORMANCE & NUTRITION REPORT", 30f, 32f, bannerTitlePaint)
        canvas.drawText("Generated on ${getHumanDate()} | Personal Progress Archive", 30f, 48f, bannerSubPaint)

        var y = 90f

        // Overview Highlights Card
        canvas.drawRoundRect(30f, y, 565f, y + 45f, 8f, 8f, accentCardPaint)
        canvas.drawText("CURRENT METRICS", 45f, y + 18f, sectionPaint)
        canvas.drawText("Weight: %.1f kg".format(weightKg), 45f, y + 34f, smallBoldPaint)
        canvas.drawText("Hydration: $waterIntakeMl ml", 160f, y + 34f, smallBoldPaint)
        canvas.drawText("Active Workout Volume: %.0f kg".format(workout.totalVolumeKg), 280f, y + 34f, smallBoldPaint)
        canvas.drawText("Cardio Sessions: ${cardioSessions.size}", 440f, y + 34f, smallBoldPaint)

        y += 65f

        // SECTION 1: STRENGTH WORKOUTS
        canvas.drawText("1. STRENGTH WORKOUT: ${workout.title.uppercase()} (${workout.durationMinutes} MINS)", 30f, y, sectionPaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        canvas.drawText("Exercise / Gym Machine", 30f, y, titlePaint.apply { textSize = 9.5f })
        canvas.drawText("Muscle Focus", 230f, y, titlePaint)
        canvas.drawText("Sets & Details", 360f, y, titlePaint)
        canvas.drawText("Total Vol", 510f, y, titlePaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        workout.exercises.take(6).forEach { exLog ->
            val setDetails = exLog.sets.filter { it.isCompleted }.joinToString(" • ") { "${it.weightKg.toInt()}kg×${it.reps}" }
                .ifEmpty { "${exLog.sets.size} sets planned" }
            val exVol = exLog.sets.filter { it.isCompleted }.sumOf { it.weightKg * it.reps }

            canvas.drawText(exLog.exercise.name.take(30), 30f, y, smallBoldPaint)
            canvas.drawText(exLog.exercise.category.displayName, 230f, y, bodyPaint)
            canvas.drawText(setDetails.take(28), 360f, y, bodyPaint)
            canvas.drawText("%.0f kg".format(exVol), 510f, y, smallBoldPaint)
            y += 15f
        }

        y += 12f

        // SECTION 2: CARDIO EQUIPMENT SESSIONS
        canvas.drawText("2. CARDIO EQUIPMENT & ENDURANCE SESSIONS", 30f, y, sectionPaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        canvas.drawText("Machine", 30f, y, titlePaint)
        canvas.drawText("Duration", 170f, y, titlePaint)
        canvas.drawText("Distance", 250f, y, titlePaint)
        canvas.drawText("Calories", 340f, y, titlePaint)
        canvas.drawText("Speed / Pace", 420f, y, titlePaint)
        canvas.drawText("Avg HR", 510f, y, titlePaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        cardioSessions.take(5).forEach { c ->
            val speedDisplay = when {
                c.treadmillSpeedKmh > 0 -> "${c.treadmillSpeedKmh} km/h"
                c.distanceKm > 0 && c.durationMinutes > 0 -> "%.1f km/h".format((c.distanceKm / c.durationMinutes) * 60)
                else -> "-"
            }
            canvas.drawText(c.machineType.displayName.take(20), 30f, y, smallBoldPaint)
            canvas.drawText("${c.durationMinutes} min", 170f, y, bodyPaint)
            canvas.drawText("%.2f km".format(c.distanceKm), 250f, y, bodyPaint)
            canvas.drawText("${c.caloriesBurned} kcal", 340f, y, smallBoldPaint)
            canvas.drawText(speedDisplay, 420f, y, bodyPaint)
            canvas.drawText("${c.avgHeartRateBpm} bpm", 510f, y, bodyPaint)
            y += 15f
        }

        y += 12f

        // SECTION 3: NUTRITION & MACROS
        val totalCalories = meals.sumOf { it.totalCalories }
        val totalProtein = meals.sumOf { it.totalProtein }
        val totalCarbs = meals.sumOf { it.totalCarbs }
        val totalFat = meals.sumOf { it.totalFat }

        canvas.drawText("3. NUTRITION & MACRONUTRIENT LOGS (TOTAL: $totalCalories KCAL • P: %.0fG • C: %.0fG • F: %.0fG)".format(totalProtein, totalCarbs, totalFat), 30f, y, sectionPaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        canvas.drawText("Meal / Food Item", 30f, y, titlePaint)
        canvas.drawText("Category", 230f, y, titlePaint)
        canvas.drawText("Serving", 330f, y, titlePaint)
        canvas.drawText("Calories", 420f, y, titlePaint)
        canvas.drawText("P / C / F", 500f, y, titlePaint)
        y += 6f
        canvas.drawLine(30f, y, 565f, y, dividerPaint)
        y += 14f

        meals.take(7).forEach { m ->
            canvas.drawText(m.foodItem.name.take(30), 30f, y, smallBoldPaint)
            canvas.drawText(m.mealCategory.title, 230f, y, bodyPaint)
            canvas.drawText("${m.numberOfServings} ${m.foodItem.servingUnit.take(10)}", 330f, y, bodyPaint)
            canvas.drawText("${m.totalCalories} kcal", 420f, y, smallBoldPaint)
            canvas.drawText("%.0f/%.0f/%.0fg".format(m.totalProtein, m.totalCarbs, m.totalFat), 500f, y, bodyPaint)
            y += 15f
        }

        // Footer
        canvas.drawLine(30f, 800f, 565f, 800f, dividerPaint)
        canvas.drawText("JustGetFit Workout & Nutrition Intelligence • Built for Athlete Progression", 30f, 815f, bodyPaint)

        pdfDocument.finishPage(page)

        val file = File(getReportsDir(context), "JustGetFit_Report_${getTimestamp()}.pdf")
        FileOutputStream(file).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()
        return file
    }

    /**
     * Launches the system share sheet with the generated report file.
     */
    fun shareReportFile(context: Context, file: File, mimeType: String, title: String) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "Here is my detailed fitness & nutrition export report from JustGetFit.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(sendIntent, title)
        context.startActivity(chooser)
    }
}
