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
    val tabTitles = listOf("Strength Session", "Cardio Machines", "Schedule & Splits")

    val activeWorkout by repository.activeWorkout.collectAsState()
    val exerciseCatalog by repository.exerciseCatalog.collectAsState()
    val gymMachinesCatalog by repository.gymMachinesCatalog.collectAsState()
    val cardioSessions by repository.cardioSessions.collectAsState()
    val currentQuote by repository.currentQuote.collectAsState()
    val currentSplitType by repository.currentSplitType.collectAsState()
    val splitSchedule by repository.splitSchedule.collectAsState()

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
                    text = "Strength sets, cardio equipment & dynamic splits",
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
                onSelectDate = { dateStr -> repository.selectWorkoutDate(dateStr) },
                onShiftDate = { delta ->
                    val newDate = FitnessDateUtils.shiftDate(selectedWorkoutDate, delta)
                    repository.selectWorkoutDate(newDate)
                },
                onOpenDatePicker = { showWorkoutDatePickerDialog = true },
                onToggleSet = { exIdx, setIdx ->
                    val cue = repository.toggleSetCompleted(exIdx, setIdx)
                    if (cue != null) {
                        activeMotivationalToast = cue
                        triggerVibration()
                        initialRestSeconds = activeWorkout.exercises.getOrNull(exIdx)?.exercise?.defaultRestSeconds ?: 90
                        restSecondsLeft = initialRestSeconds
                        showRestTimer = true
                        isTimerRunning = true
                    }
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
                onSaveCardio = { session ->
                    repository.logCardioSession(session)
                    activeMotivationalToast = "Cardio session logged! Excellent cardiovascular stimulus."
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
                    activeMotivationalToast = "Updated to ${finalExercise.name} on ${finalMachine.machineName}!"
                } else {
                    repository.addExerciseAndMachineToWorkout(
                        exercise = finalExercise,
                        gymMachine = finalMachine,
                        initialSets = initialSets
                    )
                    activeMotivationalToast = "Logged ${finalExercise.name} on ${finalMachine.machineName} (${finalMachine.everydayAlias})!"
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
                onDismiss = { editingSetTarget = null },
                onSave = { newWeight, newReps, newRpe ->
                    repository.updateSetValues(exIdx, setIdx, newWeight, newReps, newRpe)
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
    onSave: (StrengthExercise, GymMachineInfo, List<WorkoutSet>?) -> Unit
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
                                                text = "${selectedMachine!!.machineName} (${selectedMachine!!.everydayAlias}) (${selectedMachine!!.targetMuscle})",
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
                                                text = "Select Gym Machine from dropdown...",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                                                text = "${machine.machineName} (${machine.everydayAlias}) (${machine.targetMuscle})",
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
                                                text = "Select Exercise movement from dropdown...",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        if (isBothSelected) {
                            val finalMachine = if (isManualMachine) {
                                GymMachineInfo(
                                    machineName = manualMachineName.trim(),
                                    everydayAlias = manualMachineAlias.ifBlank { manualMachineName.trim() },
                                    targetMuscle = manualTargetMuscle.ifBlank { manualMachineCategory.displayName },
                                    category = manualMachineCategory,
                                    tip = "Custom gym machine logged manually."
                                )
                            } else {
                                selectedMachine!!
                            }

                            val finalExercise = if (isManualExercise) {
                                StrengthExercise(
                                    name = manualExerciseName.trim(),
                                    category = manualExerciseCategory,
                                    equipmentType = if (finalMachine.machineName.contains("Dumbbell", ignoreCase = true)) EquipmentType.DUMBBELL else EquipmentType.GYM_MACHINE,
                                    techniqueTip = "Custom exercise logged manually.",
                                    encouragingCue = "Great work pushing through this movement!"
                                )
                            } else {
                                selectedExercise!!
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
                        }
                    },
                    enabled = isBothSelected,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp)
                        .bounceClick { },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBothSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isBothSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = if (isEditing) "Save Changes" else "Log Exercise",
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
    onSelectDate: (String) -> Unit,
    onShiftDate: (Int) -> Unit,
    onOpenDatePicker: () -> Unit,
    onToggleSet: (Int, Int) -> Unit,
    onAddSet: (Int) -> Unit,
    onEditSet: (Int, Int, WorkoutSet) -> Unit,
    onDeleteExercise: (Int) -> Unit,
    onAddExerciseClick: () -> Unit,
    onEditExerciseClick: (Int) -> Unit,
    onFinishWorkout: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. DATE NAVIGATION BAR (Load and Edit Previous Days, Weeks & Older Workouts)
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
                            onClick = { onShiftDate(-1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day", tint = CoralPrimary)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onOpenDatePicker() }
                        ) {
                            Text(
                                text = FitnessDateUtils.getRelativeLabel(selectedDate),
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
                            onClick = { onShiftDate(1) },
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
                            val isSelected = FitnessDateUtils.getNormalizedDate(selectedDate) == option.dateString
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectDate(option.dateString) },
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
                onToggleSet = { setIdx -> onToggleSet(exIdx, setIdx) },
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

@Composable
private fun ExerciseLoggingCard(
    exerciseLog: ExerciseLog,
    onToggleSet: (Int) -> Unit,
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
                        Text(
                            text = exerciseLog.exercise.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = exerciseLog.exercise.category.displayName,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Dedicated Gym Machine Display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = exerciseLog.gymMachine?.dropdownLabel ?: "Guided Station (${exerciseLog.exercise.category.displayName})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CoralPrimary,
                            maxLines = 2
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Action: Edit Exercise & Machine Selection Button
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

                    // Action: Delete Exercise Button
                    IconButton(
                        onClick = onDeleteExercise,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Exercise",
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
                Text("SET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(28.dp))
                Text("WEIGHT (KG)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(72.dp))
                Text("REPS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(55.dp))
                Text("RPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(40.dp))
                Text("ACTIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(72.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

            // Set Rows with Anytime Editable Weight and Reps
            exerciseLog.sets.forEachIndexed { sIdx, setItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${setItem.setNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.width(28.dp)
                    )

                    // Clickable Weight Pill (Anytime Edit)
                    Surface(
                        onClick = { onEditSet(sIdx, setItem) },
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.width(72.dp)
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("kg", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Clickable Reps Pill (Anytime Edit)
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("r", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // RPE
                    Text(
                        text = "${setItem.rpe}/10",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(40.dp)
                    )

                    // Action Icons: Quick Edit + Done Checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.width(72.dp)
                    ) {
                        IconButton(
                            onClick = { onEditSet(sIdx, setItem) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Set Values",
                                tint = CoralPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        IconButton(
                            onClick = { onToggleSet(sIdx) },
                            modifier = Modifier
                                .size(36.dp)
                                .bounceClick { onToggleSet(sIdx) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (setItem.isCompleted) MintPrimary else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (setItem.isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = PureWhite,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Add Set button
            OutlinedButton(
                onClick = onAddSet,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .bounceClick(onClick = onAddSet),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add Set", fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
    onDismiss: () -> Unit,
    onSave: (Double, Int, Int) -> Unit,
    onDeleteSet: () -> Unit
) {
    var weightStr by remember { mutableStateOf(if (initialWeight % 1.0 == 0.0) initialWeight.toInt().toString() else initialWeight.toString()) }
    var repsStr by remember { mutableStateOf(initialReps.toString()) }
    var rpeVal by remember { mutableIntStateOf(initialRpe.coerceIn(1, 10)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Edit Set #$setNumber", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(exerciseName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (gymMachineName != null) {
                    Text(gymMachineName, fontSize = 11.sp, color = CoralPrimary, fontWeight = FontWeight.SemiBold)
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
                                    selectedContainerColor = CoralPrimary,
                                    selectedLabelColor = PureWhite
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
                        onSave(finalWeight, finalReps, rpeVal)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save Set", fontWeight = FontWeight.Bold, color = Obsidian950)
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
    onSaveCardio: (CardioSession) -> Unit
) {
    var selectedMachine by remember { mutableStateOf(CardioMachineType.TREADMILL) }

    var durationMins by remember { mutableStateOf("30") }
    var distanceKm by remember { mutableStateOf("5.0") }
    var caloriesStr by remember { mutableStateOf("320") }
    var heartRateStr by remember { mutableStateOf("145") }

    var treadmillSpeed by remember { mutableStateOf("10.0") }
    var treadmillIncline by remember { mutableStateOf("2.0") }

    var ellipticalResistance by remember { mutableStateOf("8") }
    var ellipticalSpms by remember { mutableStateOf("130") }

    var rowingSplitMins by remember { mutableStateOf("2:05") }
    var rowingStrokesPerMin by remember { mutableStateOf("28") }
    var rowingDamper by remember { mutableStateOf("6") }
    var rowingWatts by remember { mutableStateOf("210") }

    var bikeCadence by remember { mutableStateOf("85") }
    var bikeWatts by remember { mutableStateOf("180") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                    Text(
                        text = "Log ${selectedMachine.displayName} Session",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = durationMins,
                            onValueChange = { durationMins = it },
                            label = { Text("Duration (min)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = distanceKm,
                            onValueChange = { distanceKm = it },
                            label = { Text("Distance (km)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = caloriesStr,
                            onValueChange = { caloriesStr = it },
                            label = { Text("Calories (kcal)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = heartRateStr,
                            onValueChange = { heartRateStr = it },
                            label = { Text("Avg HR (BPM)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Machine Parameters",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when (selectedMachine) {
                        CardioMachineType.TREADMILL -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = treadmillSpeed,
                                    onValueChange = { treadmillSpeed = it },
                                    label = { Text("Speed (km/h)") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = treadmillIncline,
                                    onValueChange = { treadmillIncline = it },
                                    label = { Text("Incline (%)") },
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
                                    label = { Text("Resistance Level") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = ellipticalSpms,
                                    onValueChange = { ellipticalSpms = it },
                                    label = { Text("Strides/min (SPM)") },
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
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = rowingWatts,
                                    onValueChange = { rowingWatts = it },
                                    label = { Text("Power (Watts)") },
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
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = bikeWatts,
                                    onValueChange = { bikeWatts = it },
                                    label = { Text("Avg Watts") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        else -> {
                            Text(
                                text = "Recording outdoor running GPS pace and cadence.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            val session = CardioSession(
                                machineType = selectedMachine,
                                dateString = "Today",
                                durationMinutes = durationMins.toIntOrNull() ?: 30,
                                distanceKm = distanceKm.toDoubleOrNull() ?: 5.0,
                                caloriesBurned = caloriesStr.toIntOrNull() ?: 300,
                                avgHeartRateBpm = heartRateStr.toIntOrNull() ?: 140,
                                treadmillSpeedKmh = treadmillSpeed.toDoubleOrNull() ?: 10.0,
                                treadmillInclinePercent = treadmillIncline.toDoubleOrNull() ?: 2.0,
                                ellipticalResistanceLevel = ellipticalResistance.toIntOrNull() ?: 8,
                                ellipticalStridesPerMin = ellipticalSpms.toIntOrNull() ?: 130,
                                rowingStrokesPerMin = rowingStrokesPerMin.toIntOrNull() ?: 28,
                                rowingDamperSetting = rowingDamper.toIntOrNull() ?: 6,
                                rowingWatts = rowingWatts.toIntOrNull() ?: 200,
                                bikeCadenceRpm = bikeCadence.toIntOrNull() ?: 85,
                                bikeWatts = bikeWatts.toIntOrNull() ?: 180
                            )
                            onSaveCardio(session)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .bounceClick {
                                val session = CardioSession(
                                    machineType = selectedMachine,
                                    dateString = "Today",
                                    durationMinutes = durationMins.toIntOrNull() ?: 30,
                                    distanceKm = distanceKm.toDoubleOrNull() ?: 5.0,
                                    caloriesBurned = caloriesStr.toIntOrNull() ?: 300,
                                    avgHeartRateBpm = heartRateStr.toIntOrNull() ?: 140
                                )
                                onSaveCardio(session)
                            },
                        colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Cardio Equipment Session", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AquaAccent.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null, tint = AquaAccent, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(session.machineType.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${session.dateString} • ${session.durationMinutes} min • %.1f km".format(session.distanceKm), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("${session.caloriesBurned} kcal", fontWeight = FontWeight.Bold, color = CoralPrimary, fontSize = 14.sp)
                        Text("${session.avgHeartRateBpm} BPM", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
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
