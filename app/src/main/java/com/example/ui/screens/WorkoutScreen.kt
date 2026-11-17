package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FitnessRepository
import com.example.model.*
import com.example.ui.components.AppSettingsDialog
import com.example.ui.components.ShareFitnessDataDialog
import com.example.ui.components.bounceClick
import com.example.ui.components.cardBounceClick
import com.example.ui.theme.*
import com.example.util.FitnessDateUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    repository: FitnessRepository,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(initialTab) }
    val tabTitles = listOf("Strength", "Cardio", "Schedule & Splits")

    val activeWorkout by repository.activeWorkout.collectAsState()
    val exerciseCatalog by repository.exerciseCatalog.collectAsState()
    val gymMachinesCatalog by repository.gymMachinesCatalog.collectAsState()
    val cardioSessions by repository.cardioSessions.collectAsState()
    val currentQuote by repository.currentQuote.collectAsState()
    val currentSplitType by repository.currentSplitType.collectAsState()
    val splitSchedule by repository.splitSchedule.collectAsState()
    val weightGoal by repository.weightGoal.collectAsState()

    // Exercise Cue & Motivation Toast
    var activeMotivationalToast by remember { mutableStateOf<String?>(null) }

    // Rest Timer State
    var showRestTimer by remember { mutableStateOf(false) }
    var restSecondsLeft by remember { mutableIntStateOf(90) }
    var initialRestSeconds by remember { mutableIntStateOf(90) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Dialogs
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var editingExerciseIndex by remember { mutableStateOf<Int?>(null) }
    var showWorkoutFinishedDialog by remember { mutableStateOf(false) }
    var workoutCelebrationMessage by remember { mutableStateOf("") }
    var editingSplitDayIndex by remember { mutableStateOf<Int?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }

    val selectedWorkoutDate by repository.selectedWorkoutDate.collectAsState()
    var showWorkoutDatePickerDialog by remember { mutableStateOf(false) }
    var editingSetTarget by remember { mutableStateOf<Triple<Int, Int, WorkoutSet>?>(null) }

    fun triggerVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(200)
            }
        } catch (_: Exception) {}
    }

    // Timer Countdown Coroutine
    LaunchedEffect(isTimerRunning, restSecondsLeft) {
        if (isTimerRunning && restSecondsLeft > 0) {
            delay(1000)
            restSecondsLeft -= 1
            if (restSecondsLeft == 0) {
                isTimerRunning = false
                triggerVibration()
                activeMotivationalToast = "Rest complete! Step up for your next set."
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Workout Hub",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Strength, cardio & dynamic splits",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quick Rest Timer Floating Indicator
                if (showRestTimer && restSecondsLeft > 0) {
                    Box(
                        modifier = Modifier
                            .background(TrophyGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                            .border(1.dp, TrophyGold, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { isTimerRunning = !isTimerRunning }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = TrophyGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${restSecondsLeft}s",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrophyGold
                            )
                        }
                    }
                }

                // Share Fitness Data Button
                IconButton(
                    onClick = { showShareDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .bounceClick { showShareDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Workouts",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(19.dp)
                    )
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

        // DATE NAVIGATION BAR (Today, Yesterday & Past Days - Like MyFitnessPal)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val newDate = FitnessDateUtils.shiftDate(selectedWorkoutDate, -1)
                            repository.selectWorkoutDate(newDate)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day", tint = CoralPrimary)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { showWorkoutDatePickerDialog = true }
                    ) {
                        Text(
                            text = FitnessDateUtils.getRelativeLabel(selectedWorkoutDate),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tap to pick any past day or week",
                            fontSize = 11.sp,
                            color = CoralPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(
                        onClick = {
                            val newDate = FitnessDateUtils.shiftDate(selectedWorkoutDate, 1)
                            repository.selectWorkoutDate(newDate)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day", tint = CoralPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Quick Date Pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(FitnessDateUtils.getRecentDateOptions(7)) { option ->
                        val isSelected = FitnessDateUtils.getNormalizedDate(selectedWorkoutDate) == option.dateString
                        FilterChip(
                            selected = isSelected,
                            onClick = { repository.selectWorkoutDate(option.dateString) },
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

        // MOTIVATIONAL QUOTES SECTION (TRANSFERRED TO LOGGING PAGE)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .cardBounceClick { repository.refreshDailyQuote() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(CoralPrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Lift Inspiration",
                        tint = CoralPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\"${currentQuote.text}\"",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "— ${currentQuote.author} • ${currentQuote.contextTag}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary
                    )
                }
                IconButton(
                    onClick = { repository.refreshDailyQuote() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "New Quote",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Segmented Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = CoralPrimary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Active Set Cues & Motivational Toast
        AnimatedVisibility(visible = activeMotivationalToast != null) {
            activeMotivationalToast?.let { toastText ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
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
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = MintPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = toastText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { activeMotivationalToast = null },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = MintPrimary)
                        }
                    }
                }
            }
        }

        // Content per Tab
        when (selectedTabIndex) {
            0 -> StrengthWorkoutLoggingTab(
                workout = activeWorkout,
                selectedDate = selectedWorkoutDate,
                gymMachinesCatalog = gymMachinesCatalog,
                exerciseCatalog = exerciseCatalog,
                onSelectDate = { dateStr -> repository.selectWorkoutDate(dateStr) },
                onShiftDate = { delta ->
                    val newDate = FitnessDateUtils.shiftDate(selectedWorkoutDate, delta)
                    repository.selectWorkoutDate(newDate)
                },
                onOpenDatePicker = { showWorkoutDatePickerDialog = true },
                onDeleteSet = { exIdx, setIdx ->
                    repository.deleteSetFromExercise(exIdx, setIdx)
                },
                onAddSet = { exIdx -> repository.addSetToExercise(exIdx) },
                onEditSet = { exIdx, setIdx, setItem ->
                    editingSetTarget = Triple(exIdx, setIdx, setItem)
                },
                onDeleteExercise = { exIdx ->
                    repository.deleteExerciseFromWorkout(exIdx)
                },
                onAddExerciseClick = {
                    editingExerciseIndex = null
                    showAddExerciseDialog = true
                },
                onLogSetWithDropdowns = { machine, exercise, weight, reps, rpe ->
                    repository.logStrengthSetWithDropdowns(machine, exercise, weight, reps, rpe)
                    activeMotivationalToast = if (machine != null && exercise != null) {
                        "Logged ${exercise.name} set on ${machine.machineName}!"
                    } else if (machine == null && exercise == null) {
                        "Logged set with Machine: Not Selected and Exercise: Not Selected."
                    } else if (machine == null) {
                        "Logged set with Machine: Not Selected."
                    } else {
                        "Logged set with Exercise: Not Selected."
                    }
                },
                onEditExerciseClick = { exIdx ->
                    editingExerciseIndex = exIdx
                    showAddExerciseDialog = true
                },
                onFinishWorkout = {
                    workoutCelebrationMessage = repository.completeWorkout()
                    showWorkoutFinishedDialog = true
                }
            )

            1 -> CardioMachineLoggingTab(
                cardioSessions = cardioSessions,
                userWeightKg = weightGoal.currentWeightKg,
                onSaveCardio = { session ->
                    repository.logCardioSession(session)
                    activeMotivationalToast = "Cardio session logged! Excellent cardiovascular stimulus."
                },
                onUpdateCardio = { session ->
                    repository.updateCardioSession(session)
                    activeMotivationalToast = "Cardio session updated!"
                },
                onDeleteCardio = { sessionId ->
                    repository.deleteCardioSession(sessionId)
                    activeMotivationalToast = "Cardio session deleted."
                }
            )

            2 -> WorkoutSplitPlannerTab(
                currentSplit = currentSplitType,
                schedule = splitSchedule,
                onSelectSplit = { repository.selectSplitPlan(it) },
                onEditDay = { dayIdx -> editingSplitDayIndex = dayIdx },
                onStartSplitDay = { daySchedule ->
                    repository.launchWorkoutFromSplitDay(daySchedule)
                    selectedTabIndex = 0 // Navigate to active strength session tab
                    activeMotivationalToast = "Loaded ${daySchedule.focusTitle}! Start your sets below."
                }
            )
        }
    }

    // Add or Edit Exercise and Gym Machine Dialog (Two Dropdowns with Manual Entry Options)
    if (showAddExerciseDialog) {
        val existingLog = editingExerciseIndex?.let { activeWorkout.exercises.getOrNull(it) }

        AddOrEditExerciseMachineDialog(
            isEditing = editingExerciseIndex != null,
            existingExercise = existingLog?.exercise,
            existingMachine = existingLog?.gymMachine,
            gymMachinesCatalog = gymMachinesCatalog,
            exerciseCatalog = exerciseCatalog,
            onDismiss = {
                showAddExerciseDialog = false
                editingExerciseIndex = null
            },
            onSave = { finalExercise, finalMachine, initialSets ->
                if (editingExerciseIndex != null) {
                    repository.editExerciseAndMachineInWorkout(
                        exerciseIndex = editingExerciseIndex!!,
                        newExercise = finalExercise,
                        newGymMachine = finalMachine
                    )
                    activeMotivationalToast = "Updated exercise log!"
                } else {
                    repository.addExerciseAndMachineToWorkout(
                        exercise = finalExercise,
                        gymMachine = finalMachine,
                        initialSets = initialSets
                    )
                    activeMotivationalToast = "Logged ${finalExercise.name} in workout!"
                }
                showAddExerciseDialog = false
                editingExerciseIndex = null
            }
        )
    }

    // Dynamic Edit Day Split Dialog (Plan which muscle which day on the fly)
    editingSplitDayIndex?.let { dayIdx ->
        val day = splitSchedule[dayIdx]
        var titleInput by remember { mutableStateOf(day.focusTitle) }
        var summaryInput by remember { mutableStateOf(day.primaryMovementsSummary) }
        var selectedMuscle by remember { mutableStateOf(day.primaryMuscle) }
        var isRestDay by remember { mutableStateOf(day.isRestDay) }

        AlertDialog(
            onDismissRequest = { editingSplitDayIndex = null },
            title = {
                Text(
                    text = "Edit ${day.dayName} Workout",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Customize this day to any muscle group or movement on the fly.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Workout Focus Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = summaryInput,
                        onValueChange = { summaryInput = it },
                        label = { Text("Primary Movements & Machines") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Text(
                        text = "Target Muscle Group",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary
                    )

                    // Muscle Group Chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(ExerciseCategory.values()) { category ->
                            FilterChip(
                                selected = selectedMuscle == category,
                                onClick = {
                                    selectedMuscle = category
                                    if (titleInput.isBlank() || titleInput.contains("Focus")) {
                                        titleInput = "${category.displayName} Focus"
                                    }
                                },
                                label = { Text(category.displayName, fontSize = 11.sp) }
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isRestDay,
                            onCheckedChange = { isRestDay = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mark as Rest / Recovery Day", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.updateDayMuscleFocus(
                            dayIndex = dayIdx,
                            newMuscle = selectedMuscle,
                            newTitle = titleInput,
                            newSummary = summaryInput,
                            isRest = isRestDay
                        )
                        editingSplitDayIndex = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                    modifier = Modifier.bounceClick {
                        repository.updateDayMuscleFocus(
                            dayIndex = dayIdx,
                            newMuscle = selectedMuscle,
                            newTitle = titleInput,
                            newSummary = summaryInput,
                            isRest = isRestDay
                        )
                        editingSplitDayIndex = null
                    }
                ) {
                    Text("Save Day Plan", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSplitDayIndex = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Workout Complete Dialog
    if (showWorkoutFinishedDialog) {
        AlertDialog(
            onDismissRequest = { showWorkoutFinishedDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = TrophyGold,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Workout Conquered!",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = workoutCelebrationMessage,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Volume: %.0f kg".format(activeWorkout.totalVolumeKg), fontWeight = FontWeight.Bold, color = MintPrimary)
                            Text("Duration: ${activeWorkout.durationMinutes} minutes", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Split: ${activeWorkout.title}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showWorkoutFinishedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    modifier = Modifier.bounceClick { showWorkoutFinishedDialog = false }
                ) {
                    Text("Awesome", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Edit Set Dialog (Weight kg, Reps & RPE anytime editing)
    editingSetTarget?.let { (exIdx, setIdx, targetSet) ->
        val exLog = activeWorkout.exercises.getOrNull(exIdx)
        if (exLog != null) {
            EditSetDialog(
                exerciseName = exLog.exercise.name,
                gymMachineName = exLog.gymMachine?.dropdownLabel,
                setNumber = targetSet.setNumber,
                initialWeight = targetSet.weightKg,
                initialReps = targetSet.reps,
                initialRpe = targetSet.rpe,
                initialCompleted = targetSet.isCompleted,
                onDismiss = { editingSetTarget = null },
                onSave = { newWeight, newReps, newRpe, newCompleted ->
                    repository.updateSetValues(exIdx, setIdx, newWeight, newReps, newRpe, newCompleted)
                    editingSetTarget = null
                    activeMotivationalToast = "Set #${targetSet.setNumber} updated: %.1f kg x %d reps".format(newWeight, newReps)
                },
                onDeleteSet = {
                    repository.deleteSetFromExercise(exIdx, setIdx)
                    editingSetTarget = null
                    activeMotivationalToast = "Set #${targetSet.setNumber} deleted."
                }
            )
        } else {
            editingSetTarget = null
        }
    }

    // Workout Date Picker Dialog
    if (showWorkoutDatePickerDialog) {
        WorkoutDatePickerDialog(
            selectedDate = selectedWorkoutDate,
            onSelectDate = { pickedDate ->
                repository.selectWorkoutDate(pickedDate)
                showWorkoutDatePickerDialog = false
                activeMotivationalToast = "Loaded workout for ${FitnessDateUtils.getRelativeLabel(pickedDate)}"
            },
            onDismiss = { showWorkoutDatePickerDialog = false }
        )
    }

    // App Settings & Preferences Menu Dialog (Burger Menu)
    if (showSettingsDialog) {
        AppSettingsDialog(
            repository = repository,
            onDismiss = { showSettingsDialog = false }
        )
    }

    // Share Fitness Data Dialog (WhatsApp, Email & System Chooser)
    if (showShareDialog) {
        ShareFitnessDataDialog(
            repository = repository,
            onDismiss = { showShareDialog = false }
        )
    }
}

// --- DUAL DROPDOWNS & MANUAL ENTRY EXERCISE / MACHINE LOGGING DIALOG ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrEditExerciseMachineDialog(
    isEditing: Boolean,
    existingExercise: StrengthExercise?,
    existingMachine: GymMachineInfo?,
    gymMachinesCatalog: List<GymMachineInfo>,
    exerciseCatalog: List<StrengthExercise>,
    onDismiss: () -> Unit,
    onSave: (StrengthExercise, GymMachineInfo?, List<WorkoutSet>?) -> Unit
) {
    // Mode states: Dropdown vs Manual
    var isManualMachine by remember {
        mutableStateOf(existingMachine != null && gymMachinesCatalog.none { it.machineName.equals(existingMachine.machineName, ignoreCase = true) })
    }
    var selectedMachine by remember {
        mutableStateOf<GymMachineInfo?>(
            existingMachine?.let { em -> gymMachinesCatalog.firstOrNull { it.machineName.equals(em.machineName, ignoreCase = true) } ?: em }
        )
    }
    var machineDropdownExpanded by remember { mutableStateOf(false) }
    var machineSearchQuery by remember { mutableStateOf("") }
    var manualMachineName by remember { mutableStateOf(existingMachine?.machineName ?: "") }
    var manualMachineAlias by remember { mutableStateOf(existingMachine?.everydayAlias ?: "") }
    var manualMachineCategory by remember { mutableStateOf(existingMachine?.category ?: ExerciseCategory.CHEST) }
    var manualTargetMuscle by remember { mutableStateOf(existingMachine?.targetMuscle ?: "Chest & Arms") }

    var isManualExercise by remember {
        mutableStateOf(existingExercise != null && exerciseCatalog.none { it.name.equals(existingExercise.name, ignoreCase = true) })
    }
    var selectedExercise by remember {
        mutableStateOf<StrengthExercise?>(
            existingExercise?.let { ee -> exerciseCatalog.firstOrNull { it.name.equals(ee.name, ignoreCase = true) } ?: ee }
        )
    }
    var exerciseDropdownExpanded by remember { mutableStateOf(false) }
    var exerciseSearchQuery by remember { mutableStateOf("") }
    var manualExerciseName by remember { mutableStateOf(existingExercise?.name ?: "") }
    var manualExerciseCategory by remember { mutableStateOf(existingExercise?.category ?: ExerciseCategory.CHEST) }

    // Initial sets setup (when logging a new exercise)
    var initialSetsCount by remember { mutableIntStateOf(3) }
    var initialWeightKg by remember { mutableDoubleStateOf(40.0) }
    var initialReps by remember { mutableIntStateOf(10) }

    // Filtered machines (includes Free Dumbbells and all gym machines)
    val filteredMachines = remember(machineSearchQuery, gymMachinesCatalog) {
        if (machineSearchQuery.isBlank()) gymMachinesCatalog
        else gymMachinesCatalog.filter {
            it.machineName.contains(machineSearchQuery, ignoreCase = true) ||
            it.everydayAlias.contains(machineSearchQuery, ignoreCase = true) ||
            it.targetMuscle.contains(machineSearchQuery, ignoreCase = true) ||
            it.category.displayName.contains(machineSearchQuery, ignoreCase = true)
        }
    }

    // Filtered exercises
    val filteredExercises = remember(exerciseSearchQuery, exerciseCatalog) {
        if (exerciseSearchQuery.isBlank()) exerciseCatalog
        else exerciseCatalog.filter {
            it.name.contains(exerciseSearchQuery, ignoreCase = true) ||
            it.category.displayName.contains(exerciseSearchQuery, ignoreCase = true) ||
            it.equipmentType.displayName.contains(exerciseSearchQuery, ignoreCase = true)
        }
    }

    // Validation: BOTH machine and exercise must be selected / entered
    val hasMachine = if (isManualMachine) manualMachineName.isNotBlank() else selectedMachine != null
    val hasExercise = if (isManualExercise) manualExerciseName.isNotBlank() else selectedExercise != null
    val isBothSelected = hasMachine && hasExercise

    val displayMachineName = if (isManualMachine) manualMachineName.trim() else (selectedMachine?.machineName ?: "")
    val displayExerciseName = if (isManualExercise) manualExerciseName.trim() else (selectedExercise?.name ?: "")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.EditNote else Icons.Default.PrecisionManufacturing,
                        contentDescription = null,
                        tint = CoralPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isEditing) "Edit Machine & Exercise" else "Log Machine & Exercise",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Both gym machine & exercise required",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ==========================================
                // 1. GYM MACHINE SELECTION SECTION
                // ==========================================
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (hasMachine) CoralPrimary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Section Header with Title & Validation Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "1. Gym Machine Equipment",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralPrimary
                                )
                            }
                            if (hasMachine) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintPrimary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(14.dp))
                                }
                            } else {
                                Text("Required", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CoralPrimary)
                            }
                        }

                        // Full-Width Mode Toggle (Never squished)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (!isManualMachine) CoralPrimary else Color.Transparent)
                                    .clickable { isManualMachine = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📋 Preset Dropdown",
                                    fontSize = 11.sp,
                                    fontWeight = if (!isManualMachine) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!isManualMachine) PureWhite else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isManualMachine) CoralPrimary else Color.Transparent)
                                    .clickable {
                                        isManualMachine = true
                                        machineDropdownExpanded = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✏️ Enter Manually",
                                    fontSize = 11.sp,
                                    fontWeight = if (isManualMachine) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isManualMachine) PureWhite else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (!isManualMachine) {
                            // --- DROPDOWN SELECTOR BOX ---
                            Surface(
                                onClick = { machineDropdownExpanded = !machineDropdownExpanded },
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (machineDropdownExpanded) CoralPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        if (selectedMachine != null) {
                                            Text(
                                                text = selectedMachine!!.dropdownLabel,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 2
                                            )
                                            Text(
                                                text = "Muscle: ${selectedMachine!!.targetMuscle} • ${selectedMachine!!.category.displayName}",
                                                fontSize = 10.sp,
                                                color = CoralPrimary
                                            )
                                        } else {
                                            Text(
                                                text = "-- None: Machine Not Selected --",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = CoralPrimary
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = if (machineDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "Toggle Machine Dropdown",
                                        tint = CoralPrimary
                                    )
                                }
                            }

                            // Expandable Dropdown Menu with Search & Free Dumbbells
                            AnimatedVisibility(visible = machineDropdownExpanded) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, CoralPrimary.copy(alpha = 0.4f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        // Search Bar inside Dropdown
                                        OutlinedTextField(
                                            value = machineSearchQuery,
                                            onValueChange = { machineSearchQuery = it },
                                            placeholder = { Text("Filter (e.g. Free Dumbbells, press, curl)...", fontSize = 11.sp) },
                                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(16.dp)) },
                                            trailingIcon = {
                                                if (machineSearchQuery.isNotBlank()) {
                                                    IconButton(onClick = { machineSearchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                                    }
                                                }
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = CoralPrimary,
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Prompt to enter machine manually if unsatisfied with dropdown
                                        Surface(
                                            onClick = {
                                                isManualMachine = true
                                                machineDropdownExpanded = false
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            color = CoralPrimary.copy(alpha = 0.12f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "✏️ Enter machine manually if not in dropdown",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = CoralPrimary
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // List of filtered machines formatted <technical name><everyday name><target musclegroup>
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(max = 190.dp)
                                        ) {
                                            item {
                                                Surface(
                                                    onClick = {
                                                        selectedMachine = null
                                                        machineDropdownExpanded = false
                                                    },
                                                    color = if (selectedMachine == null) CoralPrimary.copy(alpha = 0.15f) else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
                                                        Text(
                                                            text = "-- None: Machine Not Selected --",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CoralPrimary
                                                        )
                                                    }
                                                }
                                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            }
                                            items(filteredMachines) { machine ->
                                                val isSelected = selectedMachine?.machineName == machine.machineName
                                                Surface(
                                                    onClick = {
                                                        selectedMachine = machine
                                                        machineDropdownExpanded = false
                                                    },
                                                    color = if (isSelected) CoralPrimary.copy(alpha = 0.18f) else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = machine.dropdownLabel,
                                                                fontSize = 11.sp,
                                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                                color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.onSurface
                                                            )
                                                            Text(
                                                                text = "Target: ${machine.targetMuscle} • ${machine.category.displayName}",
                                                                fontSize = 9.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                        if (isSelected) {
                                                            Icon(Icons.Default.Check, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(16.dp))
                                                        }
                                                    }
                                                }
                                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // --- MANUAL MACHINE ENTRY ---
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = manualMachineName,
                                    onValueChange = { manualMachineName = it },
                                    label = { Text("Custom Gym Machine Name (Required)", fontSize = 11.sp) },
                                    placeholder = { Text("e.g. Free Dumbbells, Hammer Strength Iso-Press", fontSize = 11.sp) },
                                    leadingIcon = { Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoralPrimary),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = manualMachineAlias,
                                    onValueChange = { manualMachineAlias = it },
                                    label = { Text("Everyday Alias / Common Name (Optional)", fontSize = 11.sp) },
                                    placeholder = { Text("e.g. Incline Bench Machine", fontSize = 11.sp) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text("Target Muscle Group:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(ExerciseCategory.values()) { cat ->
                                        FilterChip(
                                            selected = manualMachineCategory == cat,
                                            onClick = {
                                                manualMachineCategory = cat
                                                manualTargetMuscle = cat.displayName
                                            },
                                            label = { Text(cat.displayName, fontSize = 10.sp) },
                                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CoralPrimary, selectedLabelColor = PureWhite)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 2. EXERCISE SELECTION SECTION
                // ==========================================
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (hasExercise) AquaAccent.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Section Header with Title & Validation Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "2. Exercise Movement",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AquaAccent
                                )
                            }
                            if (hasExercise) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintPrimary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintPrimary, modifier = Modifier.size(14.dp))
                                }
                            } else {
                                Text("Required", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CoralPrimary)
                            }
                        }

                        // Full-Width Mode Toggle (Never squished)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (!isManualExercise) AquaAccent else Color.Transparent)
                                    .clickable { isManualExercise = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📋 Preset Dropdown",
                                    fontSize = 11.sp,
                                    fontWeight = if (!isManualExercise) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!isManualExercise) Obsidian950 else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isManualExercise) AquaAccent else Color.Transparent)
                                    .clickable {
                                        isManualExercise = true
                                        exerciseDropdownExpanded = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✏️ Enter Manually",
                                    fontSize = 11.sp,
                                    fontWeight = if (isManualExercise) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isManualExercise) Obsidian950 else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (!isManualExercise) {
                            // --- DROPDOWN SELECTOR BOX ---
                            Surface(
                                onClick = { exerciseDropdownExpanded = !exerciseDropdownExpanded },
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (exerciseDropdownExpanded) AquaAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        if (selectedExercise != null) {
                                            Text(
                                                text = "${selectedExercise!!.name} (${selectedExercise!!.category.displayName})",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 2
                                            )
                                            Text(
                                                text = "${selectedExercise!!.category.displayName} • ${selectedExercise!!.equipmentType.displayName}",
                                                fontSize = 10.sp,
                                                color = AquaAccent
                                            )
                                        } else {
                                            Text(
                                                text = "-- None: Exercise Not Selected --",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                color = CoralPrimary
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = if (exerciseDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = "Toggle Exercise Dropdown",
                                        tint = AquaAccent
                                    )
                                }
                            }

                            // Expandable Dropdown Menu with Search
                            AnimatedVisibility(visible = exerciseDropdownExpanded) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, AquaAccent.copy(alpha = 0.4f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        // Search Bar inside Dropdown
                                        OutlinedTextField(
                                            value = exerciseSearchQuery,
                                            onValueChange = { exerciseSearchQuery = it },
                                            placeholder = { Text("Filter (e.g. bench press, squat, curl)...", fontSize = 11.sp) },
                                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(16.dp)) },
                                            trailingIcon = {
                                                if (exerciseSearchQuery.isNotBlank()) {
                                                    IconButton(onClick = { exerciseSearchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                                    }
                                                }
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = AquaAccent,
                                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Prompt to enter exercise manually if unsatisfied with dropdown
                                        Surface(
                                            onClick = {
                                                isManualExercise = true
                                                exerciseDropdownExpanded = false
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            color = AquaAccent.copy(alpha = 0.12f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "✏️ Enter exercise manually if not in dropdown",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AquaAccent
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // List of filtered exercises
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(max = 190.dp)
                                        ) {
                                            item {
                                                Surface(
                                                    onClick = {
                                                        selectedExercise = null
                                                        exerciseDropdownExpanded = false
                                                    },
                                                    color = if (selectedExercise == null) CoralPrimary.copy(alpha = 0.15f) else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
                                                        Text(
                                                            text = "-- None: Exercise Not Selected --",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = CoralPrimary
                                                        )
                                                    }
                                                }
                                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            }
                                            items(filteredExercises) { exercise ->
                                                val isSelected = selectedExercise?.name == exercise.name
                                                Surface(
                                                    onClick = {
                                                        selectedExercise = exercise
                                                        exerciseDropdownExpanded = false
                                                    },
                                                    color = if (isSelected) AquaAccent.copy(alpha = 0.18f) else Color.Transparent,
                                                    shape = RoundedCornerShape(6.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = "${exercise.name} (${exercise.category.displayName})",
                                                                fontSize = 11.sp,
                                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                                color = if (isSelected) AquaAccent else MaterialTheme.colorScheme.onSurface
                                                            )
                                                            Text(
                                                                text = "${exercise.category.displayName} • ${exercise.equipmentType.displayName}",
                                                                fontSize = 9.sp,
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                                            )
                                                        }
                                                        if (isSelected) {
                                                            Icon(Icons.Default.Check, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(16.dp))
                                                        }
                                                    }
                                                }
                                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // --- MANUAL EXERCISE ENTRY ---
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = manualExerciseName,
                                    onValueChange = { manualExerciseName = it },
                                    label = { Text("Custom Exercise Name (Required)", fontSize = 11.sp) },
                                    placeholder = { Text("e.g. Incline Neutral Grip Press, Single Leg Romanian Deadlift", fontSize = 11.sp) },
                                    leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(18.dp)) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AquaAccent),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text("Muscle Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(ExerciseCategory.values()) { cat ->
                                        FilterChip(
                                            selected = manualExerciseCategory == cat,
                                            onClick = { manualExerciseCategory = cat },
                                            label = { Text(cat.displayName, fontSize = 10.sp) },
                                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AquaAccent, selectedLabelColor = Obsidian950)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 3. INITIAL SETS & WEIGHT SETUP (WHEN ADDING)
                // ==========================================
                if (!isEditing) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Initial Sets & Weight (Editable Anytime Later)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Sets Stepper Card
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Sets", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Surface(
                                                onClick = { if (initialSetsCount > 1) initialSetsCount-- },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                            Text(
                                                text = "$initialSetsCount",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            Surface(
                                                onClick = { if (initialSetsCount < 10) initialSetsCount++ },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                        }
                                    }
                                }

                                // Weight Stepper Card
                                Card(
                                    modifier = Modifier.weight(1.3f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Weight", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Surface(
                                                onClick = { if (initialWeightKg >= 2.5) initialWeightKg -= 2.5 },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                            Text(
                                                text = "${if (initialWeightKg % 1.0 == 0.0) initialWeightKg.toInt() else initialWeightKg}k",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                            Surface(
                                                onClick = { initialWeightKg += 2.5 },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                        }
                                    }
                                }

                                // Reps Stepper Card
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("Reps", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Surface(
                                                onClick = { if (initialReps > 1) initialReps-- },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                            Text(
                                                text = "$initialReps",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            Surface(
                                                onClick = { initialReps++ },
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // 4. DUAL SELECTION VALIDATION BANNER
                // ==========================================
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isBothSelected) MintPrimary.copy(alpha = 0.12f) else CoralPrimary.copy(alpha = 0.10f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (isBothSelected) MintPrimary.copy(alpha = 0.6f) else CoralPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBothSelected) Icons.Default.Verified else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isBothSelected) MintPrimary else CoralPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            if (isBothSelected) {
                                Text(
                                    text = "Ready to Log: $displayExerciseName",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintPrimary
                                )
                                Text(
                                    text = "Equipment: $displayMachineName",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = when {
                                        !hasMachine && !hasExercise -> "Both items required: Please select/enter Gym Machine and Exercise."
                                        !hasMachine -> "Gym Machine missing: Select from dropdown or enter manually."
                                        else -> "Exercise missing: Select from dropdown or enter manually."
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CoralPrimary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancel", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Button(
                    onClick = {
                        val finalMachine: GymMachineInfo? = if (isManualMachine) {
                            if (manualMachineName.isNotBlank()) {
                                GymMachineInfo(
                                    machineName = manualMachineName.trim(),
                                    everydayAlias = manualMachineAlias.ifBlank { manualMachineName.trim() },
                                    targetMuscle = manualTargetMuscle.ifBlank { manualMachineCategory.displayName },
                                    category = manualMachineCategory,
                                    tip = "Custom gym machine logged manually."
                                )
                            } else null
                        } else {
                            selectedMachine
                        }

                        val finalExercise = if (isManualExercise) {
                            if (manualExerciseName.isNotBlank()) {
                                StrengthExercise(
                                    name = manualExerciseName.trim(),
                                    category = manualExerciseCategory,
                                    equipmentType = if (finalMachine?.machineName?.contains("Free Weights", ignoreCase = true) == true) EquipmentType.DUMBBELL else EquipmentType.GYM_MACHINE,
                                    techniqueTip = "Custom exercise logged manually.",
                                    encouragingCue = "Great work pushing through this movement!"
                                )
                            } else {
                                StrengthExercise(
                                    name = "Exercise Not Selected",
                                    category = finalMachine?.category ?: ExerciseCategory.CHEST,
                                    techniqueTip = "Exercise not selected in dropdown.",
                                    encouragingCue = "Remember to select an exercise!"
                                )
                            }
                        } else {
                            selectedExercise ?: StrengthExercise(
                                name = "Exercise Not Selected",
                                category = finalMachine?.category ?: ExerciseCategory.CHEST,
                                techniqueTip = "Exercise not selected in dropdown.",
                                encouragingCue = "Remember to select an exercise!"
                            )
                        }

                        val sets = if (!isEditing) {
                            (1..initialSetsCount).map { sNum ->
                                WorkoutSet(
                                    setNumber = sNum,
                                    weightKg = initialWeightKg,
                                    reps = initialReps,
                                    rpe = 8,
                                    isCompleted = false
                                )
                            }
                        } else null

                        onSave(finalExercise, finalMachine, sets)
                    },
                    enabled = true,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .bounceClick { },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBothSelected) MaterialTheme.colorScheme.primary else CoralPrimary,
                        contentColor = PureWhite
                    )
                ) {
                    Text(
                        text = if (isEditing) "Save Changes" else if (isBothSelected) "Log Exercise" else "Log (Unselected)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    )
}

// --- STRENGTH WORKOUT LOGGING TAB ---

@Composable
private fun StrengthWorkoutLoggingTab(
    workout: WorkoutSession,
    selectedDate: String,
    gymMachinesCatalog: List<GymMachineInfo>,
    exerciseCatalog: List<StrengthExercise>,
    onSelectDate: (String) -> Unit,
    onShiftDate: (Int) -> Unit,
    onOpenDatePicker: () -> Unit,
    onDeleteSet: (Int, Int) -> Unit,
    onAddSet: (Int) -> Unit,
    onEditSet: (Int, Int, WorkoutSet) -> Unit,
    onDeleteExercise: (Int) -> Unit,
    onAddExerciseClick: () -> Unit,
    onLogSetWithDropdowns: (GymMachineInfo?, StrengthExercise?, Double, Int, Int) -> Unit,
    onEditExerciseClick: (Int) -> Unit,
    onFinishWorkout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Session Date Header Indicator
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKOUT SESSION • ${FitnessDateUtils.getRelativeLabel(selectedDate).uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }
        }

        // 2. Workout Overview & Add Exercise Button
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = workout.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Total Volume: %.0f kg • %d Exercises Logged".format(workout.totalVolumeKg, workout.exercises.size),
                                fontSize = 12.sp,
                                color = MintPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (workout.isCompleted) {
                            Box(
                                modifier = Modifier
                                    .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .border(1.dp, MintPrimary, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("COMPLETED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MintPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Primary Add Button
                    Button(
                        onClick = onAddExerciseClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .bounceClick(onClick = onAddExerciseClick),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Add Exercise & Gym Machine",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }

        // Two Dropdowns Under Strength Sets (Gym Machine Selection with Free Weights + Type of Exercise)
        item {
            StrengthSetsDropdownLoggerCard(
                gymMachinesCatalog = gymMachinesCatalog,
                exerciseCatalog = exerciseCatalog,
                onLogSetWithDropdowns = onLogSetWithDropdowns
            )
        }

        // Empty state when viewing a forgotten day with no workout yet logged
        if (workout.exercises.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No workout logged for this date",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Forgot to log that day? Tap \"Add Exercise & Gym Machine\" above to retroactively record your weights, machines and reps!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // Exercises List
        itemsIndexed(workout.exercises) { exIdx, exerciseLog ->
            ExerciseLoggingCard(
                exerciseLog = exerciseLog,
                onDeleteSet = { setIdx -> onDeleteSet(exIdx, setIdx) },
                onAddSet = { onAddSet(exIdx) },
                onEditSet = { setIdx, setItem -> onEditSet(exIdx, setIdx, setItem) },
                onEditClick = { onEditExerciseClick(exIdx) },
                onDeleteExercise = { onDeleteExercise(exIdx) }
            )
        }

        // Finish Workout CTA (only if exercises present)
        if (workout.exercises.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onFinishWorkout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .bounceClick(onClick = onFinishWorkout),
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Complete & Save Workout",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Obsidian950
                    )
                }
            }
        }
    }
}

// --- TWO DROPDOWNS UNDER STRENGTH SETS COMPOSABLE ---

@Composable
private fun StrengthSetsDropdownLoggerCard(
    gymMachinesCatalog: List<GymMachineInfo>,
    exerciseCatalog: List<StrengthExercise>,
    onLogSetWithDropdowns: (GymMachineInfo?, StrengthExercise?, Double, Int, Int) -> Unit
) {
    var selectedMachine by remember { mutableStateOf<GymMachineInfo?>(null) }
    var machineDropdownExpanded by remember { mutableStateOf(false) }
    var machineSearchQuery by remember { mutableStateOf("") }

    var selectedExercise by remember { mutableStateOf<StrengthExercise?>(null) }
    var exerciseDropdownExpanded by remember { mutableStateOf(false) }
    var exerciseSearchQuery by remember { mutableStateOf("") }

    var weightKg by remember { mutableDoubleStateOf(40.0) }
    var reps by remember { mutableIntStateOf(10) }
    var rpe by remember { mutableIntStateOf(8) }

    val hasMachine = selectedMachine != null
    val hasExercise = selectedExercise != null
    val isBothSelected = hasMachine && hasExercise

    val filteredMachines = remember(machineSearchQuery, gymMachinesCatalog) {
        if (machineSearchQuery.isBlank()) gymMachinesCatalog
        else gymMachinesCatalog.filter {
            it.machineName.contains(machineSearchQuery, ignoreCase = true) ||
            it.everydayAlias.contains(machineSearchQuery, ignoreCase = true) ||
            it.targetMuscle.contains(machineSearchQuery, ignoreCase = true)
        }
    }

    val filteredExercises = remember(exerciseSearchQuery, exerciseCatalog, selectedMachine) {
        var baseList = exerciseCatalog
        if (selectedMachine != null && selectedMachine!!.category != ExerciseCategory.ARMS) {
            val matching = baseList.filter { it.category == selectedMachine!!.category }
            val other = baseList.filter { it.category != selectedMachine!!.category }
            baseList = matching + other
        }
        if (exerciseSearchQuery.isBlank()) baseList
        else baseList.filter {
            it.name.contains(exerciseSearchQuery, ignoreCase = true) ||
            it.category.displayName.contains(exerciseSearchQuery, ignoreCase = true)
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (isBothSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Log Strength Set",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (isBothSelected) {
                    Box(
                        modifier = Modifier
                            .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("✓ Both Selected", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MintPrimary)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(CoralPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (!hasMachine && !hasExercise) "Both Unselected" else if (!hasMachine) "Machine Unselected" else "Exercise Unselected",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralPrimary
                        )
                    }
                }
            }

            // 1. Gym Machine Dropdown
            Text(
                text = "1. Gym Machine Equipment (Technical Name (Everyday Name for Muscle Targeted))",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                onClick = { machineDropdownExpanded = !machineDropdownExpanded },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(
                    1.dp,
                    if (hasMachine) MaterialTheme.colorScheme.primary else CoralPrimary.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (selectedMachine != null) {
                            Text(
                                text = selectedMachine!!.dropdownLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 2
                            )
                        } else {
                            Text(
                                text = "-- None: Machine Not Selected --",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CoralPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = if (machineDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle Gym Machine Dropdown",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Expandable Gym Machine Dropdown Menu
            AnimatedVisibility(visible = machineDropdownExpanded) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = machineSearchQuery,
                            onValueChange = { machineSearchQuery = it },
                            placeholder = { Text("Search machine (e.g. Free Weights, press)...", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp)) },
                            trailingIcon = {
                                if (machineSearchQuery.isNotBlank()) {
                                    IconButton(onClick = { machineSearchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                            // Unselected / None Option
                            item {
                                Surface(
                                    onClick = {
                                        selectedMachine = null
                                        machineDropdownExpanded = false
                                    },
                                    color = if (selectedMachine == null) CoralPrimary.copy(alpha = 0.15f) else Color.Transparent,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
                                        Text(
                                            text = "-- None: Machine Not Selected --",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CoralPrimary
                                        )
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            }

                            // Gym Machines + Free Weights in specified format
                            items(filteredMachines) { machine ->
                                val isSelected = selectedMachine?.machineName == machine.machineName
                                Surface(
                                    onClick = {
                                        selectedMachine = machine
                                        machineDropdownExpanded = false
                                    },
                                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) else Color.Transparent,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = machine.dropdownLabel,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }

            // 2. Type of Exercise Dropdown
            Text(
                text = "2. Exercise Movement",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                onClick = { exerciseDropdownExpanded = !exerciseDropdownExpanded },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(
                    1.dp,
                    if (hasExercise) MaterialTheme.colorScheme.secondary else CoralPrimary.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (selectedExercise != null) {
                            Text(
                                text = "${selectedExercise!!.name} (${selectedExercise!!.category.displayName})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                        } else {
                            Text(
                                text = "-- None: Exercise Not Selected --",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CoralPrimary
                            )
                        }
                    }
                    Icon(
                        imageVector = if (exerciseDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle Exercise Dropdown",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // Expandable Exercise Dropdown Menu
            AnimatedVisibility(visible = exerciseDropdownExpanded) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = exerciseSearchQuery,
                            onValueChange = { exerciseSearchQuery = it },
                            placeholder = { Text("Search exercise (e.g. bench press, squat, curl)...", fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp)) },
                            trailingIcon = {
                                if (exerciseSearchQuery.isNotBlank()) {
                                    IconButton(onClick = { exerciseSearchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                            // Unselected / None Option
                            item {
                                Surface(
                                    onClick = {
                                        selectedExercise = null
                                        exerciseDropdownExpanded = false
                                    },
                                    color = if (selectedExercise == null) CoralPrimary.copy(alpha = 0.15f) else Color.Transparent,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp)) {
                                        Text(
                                            text = "-- None: Exercise Not Selected --",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CoralPrimary
                                        )
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            }

                            items(filteredExercises) { ex ->
                                val isSelected = selectedExercise?.name == ex.name
                                Surface(
                                    onClick = {
                                        selectedExercise = ex
                                        exerciseDropdownExpanded = false
                                    },
                                    color = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f) else Color.Transparent,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = ex.name,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${ex.category.displayName} • ${ex.equipmentType.displayName}",
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }

            // Steppers for Weight & Reps & RPE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weight Stepper
                Card(
                    modifier = Modifier.weight(1.2f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Weight", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { if (weightKg >= 2.5) weightKg -= 2.5 }, modifier = Modifier.size(24.dp)) {
                                Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "%.1f kg".format(weightKg),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            IconButton(onClick = { weightKg += 2.5 }, modifier = Modifier.size(24.dp)) {
                                Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Reps Stepper
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Reps", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { if (reps > 1) reps-- }, modifier = Modifier.size(24.dp)) {
                                Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$reps",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            IconButton(onClick = { reps++ }, modifier = Modifier.size(24.dp)) {
                                Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // RPE
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("RPE", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { if (rpe > 6) rpe-- }, modifier = Modifier.size(24.dp)) {
                                Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$rpe/10",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(onClick = { if (rpe < 10) rpe++ }, modifier = Modifier.size(24.dp)) {
                                Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Dual Selection Validation Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isBothSelected) MintPrimary.copy(alpha = 0.12f) else CoralPrimary.copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, if (isBothSelected) MintPrimary.copy(alpha = 0.6f) else CoralPrimary.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isBothSelected) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isBothSelected) MintPrimary else CoralPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when {
                            isBothSelected -> "✓ Ready to log: ${selectedExercise!!.name} on ${selectedMachine!!.machineName}"
                            !hasMachine && !hasExercise -> "⚠️ Both not selected — Logged data will show Machine: Not Selected & Exercise: Not Selected"
                            !hasMachine -> "⚠️ Gym machine not selected — Logged data will show Machine: Not Selected"
                            else -> "⚠️ Exercise not selected — Logged data will show Exercise: Not Selected"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isBothSelected) MintPrimary else CoralPrimary
                    )
                }
            }

            // Log Button
            Button(
                onClick = {
                    onLogSetWithDropdowns(selectedMachine, selectedExercise, weightKg, reps, rpe)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .bounceClick { },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+ Log Strength Set",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ExerciseLoggingCard(
    exerciseLog: ExerciseLog,
    onDeleteSet: (Int) -> Unit,
    onAddSet: () -> Unit,
    onEditSet: (Int, WorkoutSet) -> Unit,
    onEditClick: () -> Unit,
    onDeleteExercise: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header with Exercise Name, Gym Machine Name & Edit/Delete Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onEditClick() }
                ) {
                    // Exercise Movement Name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (exerciseLog.isExerciseSelected) {
                            Text(
                                text = exerciseLog.displayExerciseName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = exerciseLog.exercise.category.displayName,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(CoralPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .border(1.dp, CoralPrimary, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Exercise: Not Selected",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CoralPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Dedicated Gym Machine Display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (exerciseLog.isMachineSelected) {
                            Icon(
                                imageVector = Icons.Default.PrecisionManufacturing,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = exerciseLog.displayMachineName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 2
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(CoralPrimary.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                    .border(1.dp, CoralPrimary.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Machine: Not Selected",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CoralPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Exercise and Machine",
                            tint = CoralPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onDeleteExercise,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Exercise Section",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Table Header with Edit Tip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("SET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(44.dp))
                Text("WEIGHT (KG)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(76.dp))
                Text("REPS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(55.dp))
                Text("RPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(40.dp))
                Text("ACTIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(68.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

            // Set Rows with Anytime Editable Weight and Reps - NO CHECKBOXES, WHITE FONT FOR KG AND REPS
            exerciseLog.sets.forEachIndexed { sIdx, setItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Set ${setItem.setNumber}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(44.dp)
                    )

                    // Clickable Weight Pill (Anytime Edit) - WHITE FONT FOR KG & NUMBER
                    Surface(
                        onClick = { onEditSet(sIdx, setItem) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.width(76.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "%.1f".format(setItem.weightKg),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("kg", fontSize = 9.sp, color = PureWhite.copy(alpha = 0.85f))
                        }
                    }

                    // Clickable Reps Pill (Anytime Edit) - WHITE FONT
                    Surface(
                        onClick = { onEditSet(sIdx, setItem) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.width(55.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${setItem.reps}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("r", fontSize = 9.sp, color = PureWhite.copy(alpha = 0.85f))
                        }
                    }

                    // RPE
                    Text(
                        text = "${setItem.rpe}/10",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(40.dp)
                    )

                    // Action Icons: Quick Edit + DELETE ICON (NO CHECKBOX)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.width(68.dp)
                    ) {
                        IconButton(
                            onClick = { onEditSet(sIdx, setItem) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Set Values",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        IconButton(
                            onClick = { onDeleteSet(sIdx) },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Set",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Add Set button for this Machine & Exercise pair only
            OutlinedButton(
                onClick = onAddSet,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .bounceClick(onClick = onAddSet),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Set to this Machine & Exercise", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EditSetDialog(
    exerciseName: String,
    gymMachineName: String?,
    setNumber: Int,
    initialWeight: Double,
    initialReps: Int,
    initialRpe: Int,
    initialCompleted: Boolean,
    onDismiss: () -> Unit,
    onSave: (Double, Int, Int, Boolean) -> Unit,
    onDeleteSet: () -> Unit
) {
    var weightStr by remember { mutableStateOf(if (initialWeight % 1.0 == 0.0) initialWeight.toInt().toString() else initialWeight.toString()) }
    var repsStr by remember { mutableStateOf(initialReps.toString()) }
    var rpeVal by remember { mutableIntStateOf(initialRpe.coerceIn(1, 10)) }
    var isCompleted by remember { mutableStateOf(initialCompleted) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Set #$setNumber", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(exerciseName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (gymMachineName != null) {
                    Text(gymMachineName, fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Weight (kg) Input with Stepper Chips
                Column {
                    Text("WEIGHT (KG)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = weightStr,
                        onValueChange = { weightStr = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(-5.0, -2.5, 2.5, 5.0).forEach { delta ->
                            val label = if (delta > 0) "+$delta" else "$delta"
                            SuggestionChip(
                                onClick = {
                                    val current = weightStr.toDoubleOrNull() ?: 0.0
                                    val next = (current + delta).coerceAtLeast(0.0)
                                    weightStr = if (next % 1.0 == 0.0) next.toInt().toString() else "%.1f".format(next)
                                },
                                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }

                // Reps Input with Stepper Chips
                Column {
                    Text("REPETITIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = repsStr,
                        onValueChange = { repsStr = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(-2, -1, 1, 2, 5).forEach { delta ->
                            val label = if (delta > 0) "+$delta" else "$delta"
                            SuggestionChip(
                                onClick = {
                                    val current = repsStr.toIntOrNull() ?: 0
                                    val next = (current + delta).coerceAtLeast(0)
                                    repsStr = next.toString()
                                },
                                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }

                // RPE (Exertion 1-10)
                Column {
                    Text("EFFORT (RPE 1-10): $rpeVal/10", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(6, 7, 8, 9, 10).forEach { rpeNum ->
                            FilterChip(
                                selected = rpeVal == rpeNum,
                                onClick = { rpeVal = rpeNum },
                                label = { Text("$rpeNum", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = onDeleteSet,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Set")
                }
                Button(
                    onClick = {
                        val finalWeight = weightStr.toDoubleOrNull() ?: initialWeight
                        val finalReps = repsStr.toIntOrNull() ?: initialReps
                        onSave(finalWeight, finalReps, rpeVal, isCompleted)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save Set", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun WorkoutDatePickerDialog(
    selectedDate: String,
    onSelectDate: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var customDateInput by remember { mutableStateOf("") }
    val presets = remember { FitnessDateUtils.getQuickJumpPresets() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Workout Date", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Load past days, weeks, or older workout sessions to review or retroactively log forgotten training:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Jump Options
                presets.forEach { (label, dateKey) ->
                    val isCurrent = FitnessDateUtils.getNormalizedDate(selectedDate) == dateKey
                    Surface(
                        onClick = {
                            onSelectDate(dateKey)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isCurrent) CoralPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isCurrent) BorderStroke(1.dp, CoralPrimary) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isCurrent) CoralPrimary else MaterialTheme.colorScheme.onSurface)
                            Text(dateKey, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Custom Date Entry
                Column {
                    Text("CUSTOM DATE ENTRY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = customDateInput,
                        onValueChange = { customDateInput = it },
                        placeholder = { Text("e.g. Oct 28, 2026 or Sep 15") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            if (customDateInput.isNotBlank()) {
                Button(
                    onClick = {
                        onSelectDate(customDateInput)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Text("Load Date", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

// --- CARDIO MACHINE LOGGING TAB ---

@Composable
private fun CardioMachineLoggingTab(
    cardioSessions: List<CardioSession>,
    userWeightKg: Double = 79.2,
    onSaveCardio: (CardioSession) -> Unit,
    onUpdateCardio: (CardioSession) -> Unit,
    onDeleteCardio: (String) -> Unit
) {
    var editingCardioSession by remember { mutableStateOf<CardioSession?>(null) }
    var selectedMachine by remember { mutableStateOf(CardioMachineType.TREADMILL) }
    var speedUnit by remember { mutableStateOf("km/h") } // "km/h" or "mph"

    var durationMins by remember { mutableStateOf("30") }
    var distanceKm by remember { mutableStateOf("5.00") }
    var caloriesStr by remember { mutableStateOf("315") }
    var heartRateStr by remember { mutableStateOf("145") }

    var userWeightInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", userWeightKg)) }
    var userAgeInput by remember { mutableStateOf("28") }

    var treadmillSpeed by remember { mutableStateOf("10.0") }
    var treadmillIncline by remember { mutableStateOf("2.0") }

    var ellipticalResistance by remember { mutableStateOf("8") }
    var ellipticalSpms by remember { mutableStateOf("130") }

    var rowingSplitMins by remember { mutableStateOf("2:05") }
    var rowingStrokesPerMin by remember { mutableStateOf("28") }
    var rowingDamper by remember { mutableStateOf("6") }
    var rowingWatts by remember { mutableStateOf("210") }

    var bikeCadence by remember { mutableStateOf("85") }
    var bikeResistance by remember { mutableStateOf("7") }
    var bikeWatts by remember { mutableStateOf("180") }

    var stairFloors by remember { mutableStateOf("45") }

    // Live ACSM Calorie Calculation function
    fun computeCalories(): Int {
        val dur = durationMins.toDoubleOrNull() ?: 30.0
        val weight = userWeightInput.toDoubleOrNull() ?: userWeightKg
        val age = userAgeInput.toIntOrNull() ?: 28
        val isMph = speedUnit == "mph"
        val speedVal = treadmillSpeed.toDoubleOrNull() ?: 10.0
        val speedKmh = if (isMph) speedVal * 1.60934 else speedVal
        val speedMMin = speedKmh * 16.6667
        val inclineVal = (treadmillIncline.toDoubleOrNull() ?: 2.0).coerceAtLeast(0.0) / 100.0

        val vo2 = when (selectedMachine) {
            CardioMachineType.TREADMILL, CardioMachineType.OUTDOOR_RUN -> {
                if (speedMMin > 134.0) {
                    // Running formula: VO2 = (0.2 * S) + (0.9 * S * G) + 3.5
                    (0.2 * speedMMin) + (0.9 * speedMMin * inclineVal) + 3.5
                } else {
                    // Walking formula: VO2 = (0.1 * S) + (1.8 * S * G) + 3.5
                    (0.1 * speedMMin) + (1.8 * speedMMin * inclineVal) + 3.5
                }
            }
            CardioMachineType.ELLIPTICAL -> {
                val res = ellipticalResistance.toIntOrNull() ?: 8
                (5.0 + res * 0.4) * 3.5
            }
            CardioMachineType.STATIONARY_BIKE -> {
                val w = bikeWatts.toIntOrNull() ?: 180
                val res = bikeResistance.toIntOrNull() ?: 7
                if (w > 0) {
                    (1.8 * (w * 6.0) / weight) + 7.0
                } else {
                    (4.5 + res * 0.4) * 3.5
                }
            }
            CardioMachineType.ROWING_MACHINE -> {
                val w = rowingWatts.toIntOrNull() ?: 210
                (3.0 + w * 0.035) * 3.5
            }
            CardioMachineType.STAIR_CLIMBER -> {
                val fl = stairFloors.toIntOrNull() ?: 45
                (7.0 + (fl / dur.coerceAtLeast(1.0)) * 0.1) * 3.5
            }
        }

        val ageFactor = (1.0 - ((age - 25).coerceAtLeast(0) * 0.0025)).coerceIn(0.85, 1.15)
        val kcalPerMin = (vo2 * weight / 1000.0) * 5.0 * ageFactor
        return (kcalPerMin * dur).toInt().coerceAtLeast(10)
    }

    // Auto-update calories on parameter changes
    LaunchedEffect(
        selectedMachine, speedUnit, treadmillSpeed, treadmillIncline,
        durationMins, userWeightInput, userAgeInput, ellipticalResistance,
        bikeWatts, bikeResistance, rowingWatts, stairFloors
    ) {
        val estimated = computeCalories()
        caloriesStr = estimated.toString()

        val dur = durationMins.toDoubleOrNull() ?: 30.0
        val spd = treadmillSpeed.toDoubleOrNull() ?: 10.0
        val isMph = speedUnit == "mph"
        val dist = if (isMph) (spd * dur / 60.0) * 1.60934 else (spd * dur / 60.0)
        distanceKm = String.format(java.util.Locale.US, "%.2f", dist)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Prominent Cardio Machine Section Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "+ Add Cardio Machine",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Record treadmill, cycle, elliptical, rowing & climber with live ACSM calories",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Equipment Selector Carousel
        item {
            Text(
                text = "SELECT GYM CARDIO MACHINE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(CardioMachineType.values()) { machine ->
                    val isSelected = selectedMachine == machine
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) CoralPrimary else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedMachine = machine }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column {
                            Text(
                                text = machine.displayName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) PureWhite else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = machine.unitTag,
                                fontSize = 10.sp,
                                color = if (isSelected) PureWhite.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Comprehensive Machine Tracking Form Card
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
                        Text(
                            text = "Log ${selectedMachine.displayName} Session",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Speed Unit Toggle for Treadmill / Running
                        if (selectedMachine == CardioMachineType.TREADMILL || selectedMachine == CardioMachineType.OUTDOOR_RUN) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (speedUnit == "km/h") MaterialTheme.colorScheme.primary else Color.Transparent)
                                        .clickable {
                                            if (speedUnit != "km/h") {
                                                val cur = treadmillSpeed.toDoubleOrNull() ?: 6.2
                                                treadmillSpeed = String.format(java.util.Locale.US, "%.1f", cur * 1.60934)
                                                speedUnit = "km/h"
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "km/h",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (speedUnit == "km/h") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (speedUnit == "mph") MaterialTheme.colorScheme.primary else Color.Transparent)
                                        .clickable {
                                            if (speedUnit != "mph") {
                                                val cur = treadmillSpeed.toDoubleOrNull() ?: 10.0
                                                treadmillSpeed = String.format(java.util.Locale.US, "%.1f", cur * 0.621371)
                                                speedUnit = "mph"
                                            }
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "mph",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (speedUnit == "mph") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Machine Specific Primary Telemetry Fields
                    when (selectedMachine) {
                        CardioMachineType.TREADMILL, CardioMachineType.OUTDOOR_RUN -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = treadmillSpeed,
                                    onValueChange = { treadmillSpeed = it },
                                    label = { Text("Speed ($speedUnit)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = treadmillIncline,
                                    onValueChange = { treadmillIncline = it },
                                    label = { Text("Incline (%)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        CardioMachineType.ELLIPTICAL -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = ellipticalResistance,
                                    onValueChange = { ellipticalResistance = it },
                                    label = { Text("Resistance Level (1-20)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = ellipticalSpms,
                                    onValueChange = { ellipticalSpms = it },
                                    label = { Text("Strides/min (SPM)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        CardioMachineType.ROWING_MACHINE -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = rowingSplitMins,
                                    onValueChange = { rowingSplitMins = it },
                                    label = { Text("/500m Split") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = rowingStrokesPerMin,
                                    onValueChange = { rowingStrokesPerMin = it },
                                    label = { Text("Stroke Rate (SPM)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = rowingDamper,
                                    onValueChange = { rowingDamper = it },
                                    label = { Text("Damper (1-10)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = rowingWatts,
                                    onValueChange = { rowingWatts = it },
                                    label = { Text("Power (Watts)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        CardioMachineType.STATIONARY_BIKE -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = bikeCadence,
                                    onValueChange = { bikeCadence = it },
                                    label = { Text("Cadence (RPM)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = bikeWatts,
                                    onValueChange = { bikeWatts = it },
                                    label = { Text("Power (Watts)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        CardioMachineType.STAIR_CLIMBER -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = stairFloors,
                                    onValueChange = { stairFloors = it },
                                    label = { Text("Floors Climbed") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Duration and Distance Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = durationMins,
                            onValueChange = { durationMins = it },
                            label = { Text("Duration (min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = distanceKm,
                            onValueChange = { distanceKm = it },
                            label = { Text("Distance (km)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // User Profile Parameters (For Precision ACSM Calorie Estimation)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = userWeightInput,
                            onValueChange = { userWeightInput = it },
                            label = { Text("Weight (kg)") },
                            placeholder = { Text("e.g. 79.2") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = userAgeInput,
                            onValueChange = { userAgeInput = it },
                            label = { Text("Age (years)") },
                            placeholder = { Text("e.g. 28") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Calories (Auto Calculated + Fully Editable) and Avg Heart Rate
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = caloriesStr,
                                onValueChange = { caloriesStr = it },
                                label = { Text("Calories (kcal)") },
                                placeholder = { Text("e.g. 320") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "⚡ Auto-estimated (editable)",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = heartRateStr,
                                onValueChange = { heartRateStr = it },
                                label = { Text("Avg HR (BPM)") },
                                placeholder = { Text("e.g. 145") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Target: Zone 2 - 4",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Save Button
                    Button(
                        onClick = {
                            val dur = durationMins.toIntOrNull() ?: 30
                            val spdVal = treadmillSpeed.toDoubleOrNull() ?: 10.0
                            val spdKmh = if (speedUnit == "mph") spdVal * 1.60934 else spdVal
                            val session = CardioSession(
                                machineType = selectedMachine,
                                dateString = "Today",
                                durationMinutes = dur,
                                distanceKm = distanceKm.toDoubleOrNull() ?: 5.0,
                                caloriesBurned = caloriesStr.toIntOrNull() ?: 300,
                                avgHeartRateBpm = heartRateStr.toIntOrNull() ?: 140,
                                treadmillSpeedKmh = spdKmh,
                                treadmillInclinePercent = treadmillIncline.toDoubleOrNull() ?: 2.0,
                                speedUnit = speedUnit,
                                userWeightKg = userWeightInput.toDoubleOrNull() ?: userWeightKg,
                                userAge = userAgeInput.toIntOrNull() ?: 28,
                                ellipticalResistanceLevel = ellipticalResistance.toIntOrNull() ?: 8,
                                ellipticalStridesPerMin = ellipticalSpms.toIntOrNull() ?: 130,
                                rowingSplitSeconds500m = 125,
                                rowingStrokesPerMin = rowingStrokesPerMin.toIntOrNull() ?: 28,
                                rowingDamperSetting = rowingDamper.toIntOrNull() ?: 6,
                                rowingWatts = rowingWatts.toIntOrNull() ?: 210,
                                bikeCadenceRpm = bikeCadence.toIntOrNull() ?: 85,
                                bikeResistanceLevel = bikeResistance.toIntOrNull() ?: 7,
                                bikeWatts = bikeWatts.toIntOrNull() ?: 180,
                                stairFloorsClimbed = stairFloors.toIntOrNull() ?: 45
                            )
                            onSaveCardio(session)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("+ Add Cardio Machine Session", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Recent Cardio Sessions History
        item {
            Text(
                text = "RECENT EQUIPMENT SESSIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
        }

        items(cardioSessions) { session ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { editingCardioSession = session }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(session.machineType.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            val machineDetails = when (session.machineType) {
                                CardioMachineType.TREADMILL -> "Speed: %.1f %s • Incline: %.1f%%".format(
                                    if (session.speedUnit == "mph") session.treadmillSpeedKmh * 0.621371 else session.treadmillSpeedKmh,
                                    session.speedUnit,
                                    session.treadmillInclinePercent
                                )
                                CardioMachineType.ELLIPTICAL -> "Res: ${session.ellipticalResistanceLevel} • ${session.ellipticalStridesPerMin} SPM"
                                CardioMachineType.ROWING_MACHINE -> "Damper: ${session.rowingDamperSetting} • ${session.rowingWatts}W"
                                CardioMachineType.STATIONARY_BIKE -> "${session.bikeCadenceRpm} RPM • ${session.bikeWatts}W"
                                CardioMachineType.STAIR_CLIMBER -> "${session.stairFloorsClimbed} Floors"
                                else -> "Pace: %.1f km/h".format(if (session.durationMinutes > 0) (session.distanceKm / session.durationMinutes) * 60 else 0.0)
                            }
                            Text(
                                text = "${session.durationMinutes} min • %.2f km • %s".format(session.distanceKm, machineDetails),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${session.caloriesBurned} kcal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                            Text("${session.avgHeartRateBpm} BPM", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { editingCardioSession = session },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Cardio Session",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(
                            onClick = { onDeleteCardio(session.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Cardio Session",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit Cardio Session Dialog (Anytime Editing of all Parameters)
    editingCardioSession?.let { session ->
        EditCardioSessionDialog(
            session = session,
            onDismiss = { editingCardioSession = null },
            onSave = { updated ->
                onUpdateCardio(updated)
                editingCardioSession = null
            },
            onDelete = {
                onDeleteCardio(session.id)
                editingCardioSession = null
            }
        )
    }
}

/**
 * Dialog allowing user to anytime edit any logged cardio session parameter.
 */
@Composable
private fun EditCardioSessionDialog(
    session: CardioSession,
    onDismiss: () -> Unit,
    onSave: (CardioSession) -> Unit,
    onDelete: () -> Unit
) {
    var selectedMachine by remember { mutableStateOf(session.machineType) }
    var speedUnit by remember { mutableStateOf(session.speedUnit) }
    var durationMins by remember { mutableStateOf(session.durationMinutes.toString()) }
    var distanceKm by remember { mutableStateOf(String.format(java.util.Locale.US, "%.2f", session.distanceKm)) }
    var caloriesStr by remember { mutableStateOf(session.caloriesBurned.toString()) }
    var heartRateStr by remember { mutableStateOf(session.avgHeartRateBpm.toString()) }
    var userWeightInput by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", session.userWeightKg)) }
    var userAgeInput by remember { mutableStateOf(session.userAge.toString()) }
    var notesInput by remember { mutableStateOf(session.notes) }

    var treadmillSpeed by remember {
        val spd = if (session.speedUnit == "mph") session.treadmillSpeedKmh * 0.621371 else session.treadmillSpeedKmh
        mutableStateOf(if (spd > 0) String.format(java.util.Locale.US, "%.1f", spd) else "10.0")
    }
    var treadmillIncline by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", session.treadmillInclinePercent)) }
    var ellipticalResistance by remember { mutableStateOf(session.ellipticalResistanceLevel.toString()) }
    var ellipticalSpms by remember { mutableStateOf(session.ellipticalStridesPerMin.toString()) }
    var rowingSplitMins by remember { mutableStateOf("2:05") }
    var rowingStrokesPerMin by remember { mutableStateOf(session.rowingStrokesPerMin.toString()) }
    var rowingDamper by remember { mutableStateOf(session.rowingDamperSetting.toString()) }
    var rowingWatts by remember { mutableStateOf(session.rowingWatts.toString()) }
    var bikeCadence by remember { mutableStateOf(session.bikeCadenceRpm.toString()) }
    var bikeResistance by remember { mutableStateOf(session.bikeResistanceLevel.toString()) }
    var bikeWatts by remember { mutableStateOf(session.bikeWatts.toString()) }
    var stairFloors by remember { mutableStateOf(session.stairFloorsClimbed.toString()) }

    fun recomputeCalories(): Int {
        val dur = durationMins.toDoubleOrNull() ?: 30.0
        val weight = userWeightInput.toDoubleOrNull() ?: session.userWeightKg
        val age = userAgeInput.toIntOrNull() ?: session.userAge
        val isMph = speedUnit == "mph"
        val speedVal = treadmillSpeed.toDoubleOrNull() ?: 10.0
        val speedKmh = if (isMph) speedVal * 1.60934 else speedVal
        val speedMMin = speedKmh * 16.6667
        val inclineVal = (treadmillIncline.toDoubleOrNull() ?: 2.0).coerceAtLeast(0.0) / 100.0

        val vo2 = when (selectedMachine) {
            CardioMachineType.TREADMILL, CardioMachineType.OUTDOOR_RUN -> {
                if (speedMMin > 134.0) (0.2 * speedMMin) + (0.9 * speedMMin * inclineVal) + 3.5
                else (0.1 * speedMMin) + (1.8 * speedMMin * inclineVal) + 3.5
            }
            CardioMachineType.ELLIPTICAL -> (5.0 + (ellipticalResistance.toIntOrNull() ?: 8) * 0.4) * 3.5
            CardioMachineType.STATIONARY_BIKE -> {
                val w = bikeWatts.toIntOrNull() ?: 180
                val res = bikeResistance.toIntOrNull() ?: 7
                if (w > 0) (1.8 * (w * 6.0) / weight) + 7.0 else (4.5 + res * 0.4) * 3.5
            }
            CardioMachineType.ROWING_MACHINE -> (3.0 + (rowingWatts.toIntOrNull() ?: 210) * 0.035) * 3.5
            CardioMachineType.STAIR_CLIMBER -> (7.0 + ((stairFloors.toIntOrNull() ?: 45) / dur.coerceAtLeast(1.0)) * 0.1) * 3.5
        }
        val ageFactor = (1.0 - ((age - 25).coerceAtLeast(0) * 0.0025)).coerceIn(0.85, 1.15)
        val kcalPerMin = (vo2 * weight / 1000.0) * 5.0 * ageFactor
        return (kcalPerMin * dur).toInt().coerceAtLeast(10)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Cardio Session", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Update equipment parameters & telemetry anytime", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Machine Type Selection
                Text("EQUIPMENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CardioMachineType.values()) { m ->
                        FilterChip(
                            selected = selectedMachine == m,
                            onClick = { selectedMachine = m },
                            label = { Text(m.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                // Machine-specific fields
                when (selectedMachine) {
                    CardioMachineType.TREADMILL, CardioMachineType.OUTDOOR_RUN -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = treadmillSpeed,
                                onValueChange = { treadmillSpeed = it },
                                label = { Text("Speed ($speedUnit)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = treadmillIncline,
                                onValueChange = { treadmillIncline = it },
                                label = { Text("Incline (%)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    CardioMachineType.ELLIPTICAL -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ellipticalResistance,
                                onValueChange = { ellipticalResistance = it },
                                label = { Text("Resistance (1-20)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = ellipticalSpms,
                                onValueChange = { ellipticalSpms = it },
                                label = { Text("SPM (Strides/m)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    CardioMachineType.ROWING_MACHINE -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = rowingDamper,
                                onValueChange = { rowingDamper = it },
                                label = { Text("Damper (1-10)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = rowingWatts,
                                onValueChange = { rowingWatts = it },
                                label = { Text("Watts") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    CardioMachineType.STATIONARY_BIKE -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = bikeCadence,
                                onValueChange = { bikeCadence = it },
                                label = { Text("Cadence (RPM)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = bikeWatts,
                                onValueChange = { bikeWatts = it },
                                label = { Text("Watts") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    CardioMachineType.STAIR_CLIMBER -> {
                        OutlinedTextField(
                            value = stairFloors,
                            onValueChange = { stairFloors = it },
                            label = { Text("Floors Climbed") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Duration & Distance
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = durationMins,
                        onValueChange = { durationMins = it },
                        label = { Text("Duration (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = distanceKm,
                        onValueChange = { distanceKm = it },
                        label = { Text("Distance (km)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Calories & Heart Rate
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = caloriesStr,
                        onValueChange = { caloriesStr = it },
                        label = { Text("Calories (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = heartRateStr,
                        onValueChange = { heartRateStr = it },
                        label = { Text("Avg HR (BPM)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Recalculate button
                OutlinedButton(
                    onClick = {
                        val re = recomputeCalories()
                        caloriesStr = re.toString()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⚡ Recompute ACSM Calories: ${recomputeCalories()} kcal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Delete button
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete Cardio Session", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dur = durationMins.toIntOrNull() ?: session.durationMinutes
                    val spdVal = treadmillSpeed.toDoubleOrNull() ?: 10.0
                    val spdKmh = if (speedUnit == "mph") spdVal * 1.60934 else spdVal
                    val updated = session.copy(
                        machineType = selectedMachine,
                        durationMinutes = dur,
                        distanceKm = distanceKm.toDoubleOrNull() ?: session.distanceKm,
                        caloriesBurned = caloriesStr.toIntOrNull() ?: session.caloriesBurned,
                        avgHeartRateBpm = heartRateStr.toIntOrNull() ?: session.avgHeartRateBpm,
                        treadmillSpeedKmh = spdKmh,
                        treadmillInclinePercent = treadmillIncline.toDoubleOrNull() ?: session.treadmillInclinePercent,
                        speedUnit = speedUnit,
                        userWeightKg = userWeightInput.toDoubleOrNull() ?: session.userWeightKg,
                        userAge = userAgeInput.toIntOrNull() ?: session.userAge,
                        ellipticalResistanceLevel = ellipticalResistance.toIntOrNull() ?: session.ellipticalResistanceLevel,
                        ellipticalStridesPerMin = ellipticalSpms.toIntOrNull() ?: session.ellipticalStridesPerMin,
                        rowingStrokesPerMin = rowingStrokesPerMin.toIntOrNull() ?: session.rowingStrokesPerMin,
                        rowingDamperSetting = rowingDamper.toIntOrNull() ?: session.rowingDamperSetting,
                        rowingWatts = rowingWatts.toIntOrNull() ?: session.rowingWatts,
                        bikeCadenceRpm = bikeCadence.toIntOrNull() ?: session.bikeCadenceRpm,
                        bikeResistanceLevel = bikeResistance.toIntOrNull() ?: session.bikeResistanceLevel,
                        bikeWatts = bikeWatts.toIntOrNull() ?: session.bikeWatts,
                        stairFloorsClimbed = stairFloors.toIntOrNull() ?: session.stairFloorsClimbed,
                        notes = notesInput
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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

// --- DYNAMIC WORKOUT SPLIT PLANNER TAB (OPTIONS 1, 2, 3 & CUSTOM) ---

@Composable
private fun WorkoutSplitPlannerTab(
    currentSplit: SplitPlanType,
    schedule: List<SplitDaySchedule>,
    onSelectSplit: (SplitPlanType) -> Unit,
    onEditDay: (Int) -> Unit,
    onStartSplitDay: (SplitDaySchedule) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Split Type Selector Chips
        item {
            Text(
                text = "CHOOSE SPLIT STRATEGY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SplitPlanType.values().forEach { splitType ->
                    val isSelected = currentSplit == splitType
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSplit(splitType) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) CoralPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, CoralPrimary) else null,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = splitType.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = splitType.subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CoralPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Days Schedule List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WEEKLY SCHEDULE & MOVEMENTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Tap Day to Edit Focus",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        itemsIndexed(schedule) { dayIdx, dayItem ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (dayItem.isRestDay) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (dayItem.isRestDay) Slate600.copy(alpha = 0.2f) else CoralPrimary.copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = dayItem.dayName.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (dayItem.isRestDay) Slate600 else CoralPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dayItem.focusTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Edit Button
                        IconButton(
                            onClick = { onEditDay(dayIdx) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Day",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Primary: ${dayItem.primaryMovementsSummary}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!dayItem.isRestDay) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onStartSplitDay(dayItem) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                modifier = Modifier.bounceClick { onStartSplitDay(dayItem) }
                            ) {
                                Text("Log This Session", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Workout Screen Preview", showBackground = true)
@Composable
fun WorkoutScreenPreview() {
    JustGetFitTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            WorkoutScreen(repository = FitnessRepository())
        }
    }
}
