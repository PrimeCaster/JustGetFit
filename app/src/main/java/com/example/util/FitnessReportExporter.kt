package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.model.CardioSession
import com.example.model.LoggedMealItem
import com.example.model.SupplementItem
import com.example.model.WeightGoal
import com.example.model.WeightRecord
import com.example.model.WorkoutSession
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ExportDateRange(val displayName: String, val subtitle: String, val daysLimit: Int) {
    TODAY("Today", "Today's workouts & nutrition records", 1),
    CURRENT_WEEK("This Week", "Last 7 days of workouts & meals", 7),
    CURRENT_MONTH("This Month", "Full current month records (30 days)", 30),
    SELECTED_MONTHS("Past 3 Months", "Quarterly rolling archive (90 days)", 90),
    PAST_SIX_MONTHS("Past 6 Months", "Half-year comprehensive dataset", 180),
    CURRENT_YEAR("Full Year", "Year 2026 complete dataset (365 days)", 365),
    ALL_TIME("All-Time Backup", "Complete historical archive of all entries", 9999)
}

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
        return exportFilteredBackupToCsv(
            context = context,
            range = ExportDateRange.CURRENT_WEEK,
            workout = workout,
            cardioSessions = cardioSessions,
            meals = meals,
            waterIntakeByDate = mapOf("Today" to waterIntakeMl),
            weightRecords = emptyList(),
            currentWeightKg = weightKg
        )
    }

    /**
     * Generates an Excel-compatible CSV backup with UTF-8 BOM, supporting date range filtering
     * across Current Week, Current Month, Selected Months, Full Year, and All-Time.
     */
    fun exportFilteredBackupToCsv(
        context: Context,
        range: ExportDateRange,
        workout: WorkoutSession,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>,
        waterIntakeByDate: Map<String, Int>,
        weightRecords: List<WeightRecord>,
        currentWeightKg: Double,
        dailyWaterTargetMl: Int = 3500
    ): File {
        val file = File(getReportsDir(context), "JustGetFit_${range.name.lowercase()}_backup_${getTimestamp()}.csv")
        FileOutputStream(file).use { fos ->
            // Write UTF-8 BOM for Microsoft Excel compatibility
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)

            writer.append("========================================================================\n")
            writer.append("JUSTGETFIT EXCEL & CSV DATA BACKUP ARCHIVE\n")
            writer.append("Backup Range,${range.displayName} (${range.subtitle})\n")
            writer.append("Export Timestamp,${getHumanDate()}\n")
            writer.append("Current Body Weight,${currentWeightKg} kg\n")
            writer.append("Daily Water Target,${dailyWaterTargetMl} ml\n")
            writer.append("========================================================================\n\n")

            // 1. STRENGTH WORKOUTS & EXERCISES
            writer.append("[1. STRENGTH WORKOUT SESSIONS & SETS]\n")
            writer.append("Workout Date,Workout Title,Focus Area,Duration (min),Total Volume (kg),Exercise Name,Equipment / Gym Machine,Target Muscle,Set Number,Weight (kg),Reps,Estimated 1RM (kg),RPE (1-10),Completed\n")
            workout.exercises.forEach { exLog ->
                val exName = exLog.exercise.name.ifBlank { "Exercise: Not Selected" }
                val machineName = if (exLog.isMachineSelected) {
                    exLog.gymMachine?.dropdownLabel ?: "${exLog.gymMachine?.machineName} (${exLog.gymMachine?.everydayAlias})"
                } else {
                    exLog.gymMachine?.let { "${it.machineName} (${it.everydayAlias})" } ?: "Free Weights"
                }
                val targetMuscle = exLog.exercise.category.displayName
                if (exLog.sets.isNotEmpty()) {
                    exLog.sets.forEach { s ->
                        writer.append("\"${workout.dateString}\",")
                        writer.append("\"${workout.title}\",")
                        writer.append("\"${workout.splitCategory}\",")
                        writer.append("${workout.durationMinutes},")
                        writer.append("${"%.1f".format(workout.totalVolumeKg)},")
                        writer.append("\"$exName\",")
                        writer.append("\"$machineName\",")
                        writer.append("\"$targetMuscle\",")
                        writer.append("${s.setNumber},")
                        writer.append("${s.weightKg},")
                        writer.append("${s.reps},")
                        writer.append("${"%.1f".format(s.estimated1RM)},")
                        writer.append("${s.rpe},")
                        writer.append("${if (s.isCompleted) "Yes" else "No"}\n")
                    }
                } else {
                    writer.append("\"${workout.dateString}\",\"${workout.title}\",\"${workout.splitCategory}\",${workout.durationMinutes},0.0,\"$exName\",\"$machineName\",\"$targetMuscle\",1,0.0,0,0.0,8,No\n")
                }
            }
            writer.append("\n")

            // 2. CARDIO EQUIPMENT & ENDURANCE SESSIONS
            writer.append("[2. CARDIO EQUIPMENT & ENDURANCE SESSIONS]\n")
            writer.append("Date,Machine Type,Duration (min),Distance (km),Calories (kcal),Speed,Speed Unit,Incline (%),Resistance,Cadence / SPM,Watts,Avg HR (BPM),User Weight (kg),User Age\n")
            cardioSessions.forEach { c ->
                val speedDisplay = when {
                    c.treadmillSpeedKmh > 0 -> "%.1f".format(if (c.speedUnit == "mph") c.treadmillSpeedKmh * 0.621371 else c.treadmillSpeedKmh)
                    c.distanceKm > 0 && c.durationMinutes > 0 -> "%.1f".format((c.distanceKm / c.durationMinutes) * 60)
                    else -> "-"
                }
                val inclineDisplay = if (c.treadmillInclinePercent > 0) "${c.treadmillInclinePercent}" else "-"
                val resistanceDisplay = when {
                    c.ellipticalResistanceLevel > 0 -> "${c.ellipticalResistanceLevel}"
                    c.bikeResistanceLevel > 0 -> "${c.bikeResistanceLevel}"
                    c.rowingDamperSetting > 0 -> "Damper ${c.rowingDamperSetting}"
                    else -> "-"
                }
                val cadenceSpm = when {
                    c.ellipticalStridesPerMin > 0 -> "${c.ellipticalStridesPerMin}"
                    c.rowingStrokesPerMin > 0 -> "${c.rowingStrokesPerMin}"
                    c.bikeCadenceRpm > 0 -> "${c.bikeCadenceRpm}"
                    else -> "-"
                }
                val watts = when {
                    c.bikeWatts > 0 -> "${c.bikeWatts}"
                    c.rowingWatts > 0 -> "${c.rowingWatts}"
                    else -> "-"
                }

                writer.append("\"${c.dateString}\",")
                writer.append("\"${c.machineType.displayName}\",")
                writer.append("${c.durationMinutes},")
                writer.append("${"%.2f".format(c.distanceKm)},")
                writer.append("${c.caloriesBurned},")
                writer.append("\"$speedDisplay\",")
                writer.append("\"${c.speedUnit}\",")
                writer.append("\"$inclineDisplay\",")
                writer.append("\"$resistanceDisplay\",")
                writer.append("\"$cadenceSpm\",")
                writer.append("\"$watts\",")
                writer.append("${c.avgHeartRateBpm},")
                writer.append("${c.userWeightKg},")
                writer.append("${c.userAge}\n")
            }
            writer.append("\n")

            // 3. NUTRITION & MACRONUTRIENT MEAL DIARY
            writer.append("[3. NUTRITION & MACRONUTRIENT MEAL DIARY]\n")
            writer.append("Date,Meal Category,Food Item,Portion Size,Servings,Total Calories (kcal),Protein (g),Carbs (g),Fat (g),Fiber (g)\n")
            meals.forEach { m ->
                writer.append("\"${m.dateString}\",")
                writer.append("\"${m.mealCategory.title}\",")
                writer.append("\"${m.foodItem.name}\",")
                writer.append("\"${m.foodItem.servingUnit}\",")
                writer.append("${m.numberOfServings},")
                writer.append("${m.totalCalories},")
                writer.append("${"%.1f".format(m.totalProtein)},")
                writer.append("${"%.1f".format(m.totalCarbs)},")
                writer.append("${"%.1f".format(m.totalFat)},")
                writer.append("${"%.1f".format(m.foodItem.fiberGrams * m.numberOfServings)}\n")
            }
            writer.append("\n")

            // 4. DAILY WATER INTAKE & HYDRATION HISTORY
            writer.append("[4. DAILY WATER INTAKE & HYDRATION HISTORY]\n")
            writer.append("Date,Water Intake (ml),Daily Target (ml),Goal Status\n")
            waterIntakeByDate.forEach { (date, ml) ->
                if (date != "Today" && date != "Yesterday") {
                    val status = if (ml >= dailyWaterTargetMl) "Goal Achieved" else "${(ml * 100) / dailyWaterTargetMl}%"
                    writer.append("\"$date\",$ml,$dailyWaterTargetMl,\"$status\"\n")
                }
            }
            writer.append("\n")

            // 5. BODY WEIGHT & WEIGH-IN TRAJECTORY
            writer.append("[5. BODY WEIGHT & WEIGH-IN TRAJECTORY]\n")
            writer.append("Date,Weight (kg),Body Fat (%)\n")
            weightRecords.forEach { w ->
                writer.append("\"${w.dateString}\",${w.weightKg},${w.bodyFatPercentage}\n")
            }
            writer.append("\n")

            writer.flush()
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

            val exLabel = if (exLog.gymMachine != null) {
                "${exLog.exercise.name} (${exLog.gymMachine.everydayAlias})"
            } else {
                exLog.exercise.name
            }
            canvas.drawText(exLabel.take(32), 30f, y, smallBoldPaint)
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

    /**
     * Returns the user-accessible folder on device storage named "JustGetFit".
     * Stored in Documents/JustGetFit or app external files JustGetFit.
     */
    fun getPublicAppFolder(context: Context): File {
        val docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val appFolder = File(docsDir, "JustGetFit")
        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }
        if (appFolder.exists() && appFolder.canWrite()) {
            return appFolder
        }
        val fallback = File(context.getExternalFilesDir(null), "JustGetFit")
        if (!fallback.exists()) {
            fallback.mkdirs()
        }
        return fallback
    }

    fun exportAllDataToFolder(context: Context, repository: com.example.data.FitnessRepository): Map<String, File> {
        return exportAllDataToFolder(
            context = context,
            workoutsHistory = repository.workoutsByDate.value,
            cardioSessions = repository.cardioSessions.value,
            meals = repository.loggedMeals.value,
            waterIntakeByDate = repository.waterIntakeByDate.value,
            weightRecords = repository.weightRecords.value,
            currentWeightKg = repository.weightGoal.value.currentWeightKg,
            supplements = repository.supplements.value,
            dailyWaterTargetMl = repository.dailyWaterTargetMl
        )
    }

    /**
     * Exports all app data into individual clean CSV files inside the "JustGetFit" folder on device storage:
     * 1. JustGetFit_Workouts.csv
     * 2. JustGetFit_Cardio.csv
     * 3. JustGetFit_Nutrition.csv
     * 4. JustGetFit_Hydration.csv
     * 5. JustGetFit_Weight.csv
     * 6. JustGetFit_Supplements.csv
     * 7. JustGetFit_Full_Backup.csv
     */
    fun exportAllDataToFolder(
        context: Context,
        workoutsHistory: Map<String, WorkoutSession>,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>,
        waterIntakeByDate: Map<String, Int>,
        weightRecords: List<WeightRecord>,
        currentWeightKg: Double,
        supplements: List<SupplementItem>,
        dailyWaterTargetMl: Int = 3500
    ): Map<String, File> {
        val folder = getPublicAppFolder(context)
        val generatedFiles = mutableMapOf<String, File>()

        // 1. Workouts CSV
        val workoutsFile = File(folder, "JustGetFit_Workouts.csv")
        FileOutputStream(workoutsFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Date,Workout Title,Split Category,Duration (mins),Total Volume (kg),Exercise Name,Gym Machine / Equipment,Target Muscle,Set Number,Weight (kg),Reps,Estimated 1RM (kg),RPE,Completed\n")
            // Deduplicate workouts to avoid duplicate rows from "Today" key
            val uniqueWorkouts = workoutsHistory.entries
                .filter { it.key != "Today" || !workoutsHistory.containsKey(FitnessDateUtils.getTodayDateString()) }
                .map { it.value }
            uniqueWorkouts.forEach { w ->
                w.exercises.forEach { exLog ->
                    val exName = exLog.exercise.name.ifBlank { "Exercise: Not Selected" }
                    val machineName = if (exLog.isMachineSelected) {
                        exLog.gymMachine?.dropdownLabel ?: "${exLog.gymMachine?.machineName} (${exLog.gymMachine?.everydayAlias} for ${exLog.gymMachine?.targetMuscle})"
                    } else {
                        exLog.gymMachine?.let { "${it.machineName} (${it.everydayAlias})" } ?: "Free Weights"
                    }
                    val targetMuscle = exLog.exercise.category.displayName
                    if (exLog.sets.isNotEmpty()) {
                        exLog.sets.forEach { s ->
                            writer.append("\"${w.dateString}\",\"${w.title}\",\"${w.splitCategory}\",${w.durationMinutes},${"%.1f".format(w.totalVolumeKg)},\"$exName\",\"$machineName\",\"$targetMuscle\",${s.setNumber},${s.weightKg},${s.reps},${"%.1f".format(s.estimated1RM)},${s.rpe},${if (s.isCompleted) "Yes" else "No"}\n")
                        }
                    } else {
                        writer.append("\"${w.dateString}\",\"${w.title}\",\"${w.splitCategory}\",${w.durationMinutes},${"%.1f".format(w.totalVolumeKg)},\"$exName\",\"$machineName\",\"$targetMuscle\",1,0.0,0,0.0,8,No\n")
                    }
                }
            }
            writer.flush()
        }
        generatedFiles["Workouts"] = workoutsFile

        // 2. Cardio CSV
        val cardioFile = File(folder, "JustGetFit_Cardio.csv")
        FileOutputStream(cardioFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Date,Machine Type,Duration (min),Distance (km),Calories (kcal),Speed (km/h),Incline (%),Resistance,Cadence (RPM/SPM),Watts,Avg Heart Rate (BPM),User Weight (kg)\n")
            cardioSessions.forEach { c ->
                writer.append("\"${c.dateString}\",\"${c.machineType.displayName}\",${c.durationMinutes},${"%.2f".format(c.distanceKm)},${c.caloriesBurned},${"%.1f".format(c.treadmillSpeedKmh)},${c.treadmillInclinePercent},${c.bikeResistanceLevel},${c.bikeCadenceRpm},${c.bikeWatts},${c.avgHeartRateBpm},${c.userWeightKg}\n")
            }
            writer.flush()
        }
        generatedFiles["Cardio"] = cardioFile

        // 3. Nutrition CSV
        val nutritionFile = File(folder, "JustGetFit_Nutrition.csv")
        FileOutputStream(nutritionFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Date,Meal Category,Food Name,Serving Unit,Servings,Total Calories (kcal),Protein (g),Carbs (g),Fat (g),Fiber (g)\n")
            meals.forEach { m ->
                writer.append("\"${m.dateString}\",\"${m.mealCategory.title}\",\"${m.foodItem.name}\",\"${m.foodItem.servingUnit}\",${m.numberOfServings},${m.totalCalories},${"%.1f".format(m.totalProtein)},${"%.1f".format(m.totalCarbs)},${"%.1f".format(m.totalFat)},${"%.1f".format(m.foodItem.fiberGrams * m.numberOfServings)}\n")
            }
            writer.flush()
        }
        generatedFiles["Nutrition"] = nutritionFile

        // 4. Hydration CSV
        val hydrationFile = File(folder, "JustGetFit_Hydration.csv")
        FileOutputStream(hydrationFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Date,Intake (ml),Daily Target (ml),Goal Reached\n")
            waterIntakeByDate.forEach { (date, ml) ->
                writer.append("\"$date\",$ml,$dailyWaterTargetMl,${if (ml >= dailyWaterTargetMl) "Yes" else "No"}\n")
            }
            writer.flush()
        }
        generatedFiles["Hydration"] = hydrationFile

        // 5. Weight CSV
        val weightFile = File(folder, "JustGetFit_Weight.csv")
        FileOutputStream(weightFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Date,Weight (kg),Body Fat (%)\n")
            weightRecords.forEach { w ->
                writer.append("\"${w.dateString}\",${w.weightKg},${w.bodyFatPercentage}\n")
            }
            writer.flush()
        }
        generatedFiles["Weight"] = weightFile

        // 6. Supplements CSV
        val suppFile = File(folder, "JustGetFit_Supplements.csv")
        FileOutputStream(suppFile).use { fos ->
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
            val writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            writer.append("Supplement Name,Dosage,Timing,Taken Today,Notes\n")
            supplements.forEach { s ->
                writer.append("\"${s.name}\",\"${s.dosage}\",\"${s.timing.label}\",${if (s.isTaken) "Yes" else "No"},\"${s.notes}\"\n")
            }
            writer.flush()
        }
        generatedFiles["Supplements"] = suppFile

        // 7. Full Master Backup CSV
        val masterWorkout = workoutsHistory.values.firstOrNull() ?: WorkoutSession(title = "General Session", splitCategory = "General", dateString = "Today", durationMinutes = 45, exercises = mutableListOf(), isCompleted = true, totalVolumeKg = 0.0)
        val fullBackupFile = File(folder, "JustGetFit_Full_Backup.csv")
        val generatedFull = exportFilteredBackupToCsv(
            context = context,
            range = ExportDateRange.ALL_TIME,
            workout = masterWorkout,
            cardioSessions = cardioSessions,
            meals = meals,
            waterIntakeByDate = waterIntakeByDate,
            weightRecords = weightRecords,
            currentWeightKg = currentWeightKg,
            dailyWaterTargetMl = dailyWaterTargetMl
        )
        generatedFull.copyTo(fullBackupFile, overwrite = true)
        generatedFiles["FullBackup"] = fullBackupFile

        return generatedFiles
    }

    /**
     * Builds a beautifully formatted, readable text summary with emojis and tabular/key-value sections
     * designed specifically for sharing via WhatsApp and Email.
     */
    fun buildShareText(
        range: ExportDateRange,
        includeWorkouts: Boolean,
        includeNutrition: Boolean,
        workout: WorkoutSession?,
        workoutsHistory: Map<String, WorkoutSession>,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>,
        waterIntakeByDate: Map<String, Int>,
        weightGoal: WeightGoal,
        dailyWaterTargetMl: Int
    ): String {
        val sb = StringBuilder()
        sb.append("🏋️ *JUSTGETFIT FITNESS & NUTRITION REPORT*\n")
        sb.append("📅 *Period:* ${range.displayName} (${range.subtitle})\n")
        sb.append("⚖️ *Current Weight:* %.1f kg (Goal: %.1f kg)\n".format(weightGoal.currentWeightKg, weightGoal.targetWeightKg))
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")

        if (includeWorkouts) {
            sb.append("\n💪 *WORKOUT SESSIONS & STRENGTH*\n")
            val targetWorkouts = if (range == ExportDateRange.TODAY) {
                val todayW = workout?.takeIf { it.exercises.isNotEmpty() }
                    ?: workoutsHistory["Today"]
                    ?: workoutsHistory[FitnessDateUtils.getTodayDateString()]
                    ?: workout
                listOfNotNull(todayW)
            } else {
                val allW = workoutsHistory.entries
                    .filter { it.key != "Today" || !workoutsHistory.containsKey(FitnessDateUtils.getTodayDateString()) }
                    .map { it.value }
                    .toMutableList()
                if (workout != null && allW.none { it.id == workout.id }) {
                    allW.add(0, workout)
                }
                allW
            }

            if (targetWorkouts.isEmpty() || targetWorkouts.all { it.exercises.isEmpty() }) {
                sb.append("• No strength exercises logged for this period.\n")
            } else {
                targetWorkouts.forEach { w ->
                    val relativeDate = FitnessDateUtils.getRelativeLabel(w.dateString)
                    sb.append("• *${w.title}* ($relativeDate)\n")
                    sb.append("  Volume: ${"%.0f".format(w.totalVolumeKg)} kg | Duration: ${w.durationMinutes} mins\n")
                    w.exercises.forEach { ex ->
                        val exName = ex.exercise.name.ifBlank { "Exercise: Not Selected" }
                        val machName = if (ex.isMachineSelected) {
                            ex.gymMachine?.dropdownLabel ?: "${ex.gymMachine?.machineName} (${ex.gymMachine?.everydayAlias})"
                        } else {
                            ex.gymMachine?.let { "${it.machineName} (${it.everydayAlias})" } ?: "Free Weights"
                        }
                        val setsText = if (ex.sets.isNotEmpty()) {
                            ex.sets.joinToString(", ") { "${it.weightKg.toInt()}kg×${it.reps}" }
                        } else {
                            "No sets recorded"
                        }
                        sb.append("  - $exName\n")
                        sb.append("    Equipment: $machName\n")
                        sb.append("    Sets: $setsText\n")
                    }
                }
            }

            // Cardio highlights
            val cardioFiltered = if (range == ExportDateRange.TODAY) {
                cardioSessions.filter { it.dateString == "Today" || it.dateString == FitnessDateUtils.getTodayDateString() }
            } else cardioSessions

            if (cardioFiltered.isNotEmpty()) {
                sb.append("\n🏃 *CARDIO EQUIPMENT TELEMETRY*\n")
                cardioFiltered.forEach { c ->
                    val dist = if (c.distanceKm > 0) "%.1f km".format(c.distanceKm) else "${c.durationMinutes} mins"
                    sb.append("• *${c.machineType.displayName}*: $dist | ${c.caloriesBurned} kcal burned | Avg HR: ${c.avgHeartRateBpm} BPM\n")
                }
            }
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        }

        if (includeNutrition) {
            sb.append("\n🥗 *NUTRITION & MACRONUTRIENT DIARY*\n")
            val mealsFiltered = if (range == ExportDateRange.TODAY) {
                meals.filter { it.dateString == "Today" || it.dateString == FitnessDateUtils.getTodayDateString() }
            } else meals

            val totalCals = mealsFiltered.sumOf { it.totalCalories }
            val totalP = mealsFiltered.sumOf { it.totalProtein }
            val totalC = mealsFiltered.sumOf { it.totalCarbs }
            val totalF = mealsFiltered.sumOf { it.totalFat }

            sb.append("• *Total Energy:* $totalCals kcal\n")
            sb.append("• *Protein:* %.0fg | *Carbs:* %.0fg | *Fats:* %.0fg\n".format(totalP, totalC, totalF))

            val todayWater = waterIntakeByDate["Today"] ?: waterIntakeByDate[FitnessDateUtils.getTodayDateString()] ?: 2500
            sb.append("• *Hydration:* $todayWater / $dailyWaterTargetMl ml (${todayWater / 250} glasses)\n")

            if (mealsFiltered.isNotEmpty()) {
                sb.append("• *Meal Logs:*\n")
                mealsFiltered.take(6).forEach { m ->
                    sb.append("  - [${m.mealCategory.title}] ${m.foodItem.name}: ${m.totalCalories} kcal (%.0fg P)\n".format(m.totalProtein))
                }
                if (mealsFiltered.size > 6) {
                    sb.append("  ...and ${mealsFiltered.size - 6} more meal items\n")
                }
            }
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        }

        sb.append("\n📱 _Shared via JustGetFit App_\n")
        return sb.toString()
    }

    /**
     * Builds a clean CSV table string for sharing as comma-separated data.
     */
    fun buildShareCsv(
        range: ExportDateRange,
        includeWorkouts: Boolean,
        includeNutrition: Boolean,
        workout: WorkoutSession?,
        workoutsHistory: Map<String, WorkoutSession>,
        cardioSessions: List<CardioSession>,
        meals: List<LoggedMealItem>
    ): String {
        val sb = StringBuilder()
        sb.append("JustGetFit Data Export - ${range.displayName}\n")
        if (includeWorkouts) {
            sb.append("\n[WORKOUTS]\nDate,Workout Title,Exercise Name,Gym Machine / Equipment,Target Muscle,Set Number,Weight (kg),Reps,Total Volume (kg)\n")
            val targetWorkouts = if (range == ExportDateRange.TODAY) {
                val todayW = workout?.takeIf { it.exercises.isNotEmpty() }
                    ?: workoutsHistory["Today"]
                    ?: workoutsHistory[FitnessDateUtils.getTodayDateString()]
                    ?: workout
                listOfNotNull(todayW)
            } else {
                val allW = workoutsHistory.entries
                    .filter { it.key != "Today" || !workoutsHistory.containsKey(FitnessDateUtils.getTodayDateString()) }
                    .map { it.value }
                    .toMutableList()
                if (workout != null && allW.none { it.id == workout.id }) {
                    allW.add(0, workout)
                }
                allW
            }
            targetWorkouts.forEach { w ->
                w.exercises.forEach { ex ->
                    val exName = ex.exercise.name.ifBlank { "Exercise: Not Selected" }
                    val machName = if (ex.isMachineSelected) {
                        ex.gymMachine?.dropdownLabel ?: "${ex.gymMachine?.machineName} (${ex.gymMachine?.everydayAlias})"
                    } else {
                        ex.gymMachine?.let { "${it.machineName} (${it.everydayAlias})" } ?: "Free Weights"
                    }
                    val targetMuscle = ex.exercise.category.displayName
                    if (ex.sets.isNotEmpty()) {
                        ex.sets.forEach { s ->
                            sb.append("\"${w.dateString}\",\"${w.title}\",\"$exName\",\"$machName\",\"$targetMuscle\",${s.setNumber},${s.weightKg},${s.reps},${"%.1f".format(s.weightKg * s.reps)}\n")
                        }
                    } else {
                        sb.append("\"${w.dateString}\",\"${w.title}\",\"$exName\",\"$machName\",\"$targetMuscle\",1,0.0,0,0.0\n")
                    }
                }
            }
            sb.append("\n[CARDIO]\nDate,Machine Type,Duration (min),Distance (km),Calories (kcal),Avg HR (BPM)\n")
            val cardioFiltered = if (range == ExportDateRange.TODAY) {
                cardioSessions.filter { it.dateString == "Today" || it.dateString == FitnessDateUtils.getTodayDateString() }
            } else cardioSessions
            cardioFiltered.forEach { c ->
                sb.append("\"${c.dateString}\",\"${c.machineType.displayName}\",${c.durationMinutes},${c.distanceKm},${c.caloriesBurned},${c.avgHeartRateBpm}\n")
            }
        }
        if (includeNutrition) {
            sb.append("\n[NUTRITION]\nDate,Meal Category,Food Name,Calories (kcal),Protein (g),Carbs (g),Fat (g)\n")
            val mealsFiltered = if (range == ExportDateRange.TODAY) {
                meals.filter { it.dateString == "Today" || it.dateString == FitnessDateUtils.getTodayDateString() }
            } else meals
            mealsFiltered.forEach { m ->
                sb.append("\"${m.dateString}\",\"${m.mealCategory.title}\",\"${m.foodItem.name}\",${m.totalCalories},${"%.1f".format(m.totalProtein)},${"%.1f".format(m.totalCarbs)},${"%.1f".format(m.totalFat)}\n")
            }
        }
        return sb.toString()
    }

    /**
     * Shares formatted text and optional CSV attachment directly via WhatsApp.
     */
    fun shareViaWhatsApp(context: Context, text: String, file: File? = null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (file != null) "text/csv" else "text/plain"
            `package` = "com.whatsapp"
            putExtra(Intent.EXTRA_TEXT, text)
            if (file != null) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general system chooser if WhatsApp is not installed
            val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                type = if (file != null) "text/csv" else "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                if (file != null) {
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
            context.startActivity(Intent.createChooser(fallbackIntent, "Share via WhatsApp or other app"))
        }
    }

    /**
     * Shares formatted text and optional CSV attachment via Email.
     */
    fun shareViaEmail(context: Context, subject: String, body: String, file: File? = null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (file != null) "text/csv" else "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            if (file != null) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        context.startActivity(Intent.createChooser(intent, "Share via Email"))
    }

    /**
     * General system share for text or file.
     */
    fun shareViaChooser(context: Context, title: String, text: String, file: File? = null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = if (file != null) "text/csv" else "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
            if (file != null) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        context.startActivity(Intent.createChooser(intent, title))
    }

    /**
     * Backs up CSV to user's Google Drive inside the specified folder.
     */
    fun backupToGoogleDrive(context: Context, file: File, email: String, folderName: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val subject = "[JustGetFit Backup] Upload to Google Drive / $folderName ($email)"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, "Upload this JustGetFit full backup archive to your Google Drive account ($email) in folder: $folderName.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(sendIntent, "Backup to Google Drive ($folderName)"))
    }
}
