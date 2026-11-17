package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FitnessRepository
import com.example.model.*
import com.example.ui.components.AppSettingsDialog
import com.example.ui.components.BodyMeasurementsTrendChart
import com.example.ui.components.EndurancePerformanceChart
import com.example.ui.components.WeightLossTrajectoryChart
import com.example.ui.components.bounceClick
import com.example.ui.components.cardBounceClick
import com.example.ui.theme.*

@Composable
fun ProgressScreen(
    repository: FitnessRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val weightRecords by repository.weightRecords.collectAsState()
    val weightGoal by repository.weightGoal.collectAsState()
    val progressPhotos by repository.progressPhotos.collectAsState()
    val circumferences by repository.circumferences.collectAsState()
    val reminders by repository.reminders.collectAsState()
    val activeWorkout by repository.activeWorkout.collectAsState()
    val cardioSessions by repository.cardioSessions.collectAsState()
    val loggedMeals by repository.loggedMeals.collectAsState()
    val waterIntakeMl by repository.waterIntakeMl.collectAsState()
    val waterIntakeByDate by repository.waterIntakeByDate.collectAsState()

    var showAddPhotoDialog by remember { mutableStateOf(false) }
    var showAddMeasurementDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showShareSuccessSnackbar by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Split-Screen Transformation Slider State (0.0 to 1.0)
    var splitSliderPosition by remember { mutableFloatStateOf(0.5f) }

    val baselinePhoto = progressPhotos.lastOrNull() ?: progressPhotos.firstOrNull()
    val currentPhoto = progressPhotos.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Progress & Transformation",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Body visualizer, milestone goals & habits",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Social Share Motivation Button
                        OutlinedButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "🔥 Crushing my fitness goals with JustGetFit! Lost %.1f kg, hit new strength PRs, and staying locked in on nutrition & recovery! #JustGetFit #FitnessJourney".format(
                                            weightGoal.startingWeightKg - weightGoal.currentWeightKg
                                        )
                                    )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Fitness Milestone")
                                try {
                                    context.startActivity(shareIntent)
                                } catch (_: Exception) {}
                                showShareSuccessSnackbar = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TrophyGold),
                            border = BorderStroke(1.dp, TrophyGold.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.bounceClick {
                                showShareSuccessSnackbar = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
        }

        // Share Confirmation Banner
        item {
            AnimatedVisibility(visible = showShareSuccessSnackbar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TrophyGold.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, TrophyGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Milestone card ready to share with friends & community!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { showShareSuccessSnackbar = false }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = TrophyGold)
                        }
                    }
                }
            }
        }

        // Weight Loss Trajectory Progress Graph
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
                            Text(
                                text = "Weight Loss Trajectory",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Starting %.1f kg → Target %.1f kg".format(weightGoal.startingWeightKg, weightGoal.targetWeightKg),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(MintPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "-%.1f kg Total".format(weightGoal.startingWeightKg - weightGoal.currentWeightKg),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    WeightLossTrajectoryChart(
                        records = weightRecords,
                        goal = weightGoal
                    )
                }
            }
        }

        // Long-term Body Transformation Split Visualizer
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
                            Text(
                                text = "Transformation Visualizer",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Drag horizontal handle to compare Before & After",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showAddPhotoDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.bounceClick { showAddPhotoDialog = true }
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Interactive Split Slider Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { change, _ ->
                                    val newFraction = (change.position.x / size.width).coerceIn(0.05f, 0.95f)
                                    splitSliderPosition = newFraction
                                }
                            }
                    ) {
                        // Left & Right Simulated Athletic Silhouettes / Photo Placeholders
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Before Side (Sep 1)
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(splitSliderPosition)
                                    .background(
                                        Brush.verticalGradient(listOf(Slate800, Slate700))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Slate600,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "BEFORE (Sep 1)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PureWhite
                                    )
                                    Text(
                                        text = "86.5 kg • 23.5% Body Fat",
                                        fontSize = 11.sp,
                                        color = PearlMuted
                                    )
                                }
                            }

                            // After Side (Nov 8)
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .background(
                                        Brush.verticalGradient(listOf(MintDark, Slate800))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = MintPrimary,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "CURRENT (Nov 8)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MintLight
                                    )
                                    Text(
                                        text = "79.2 kg • 19.1% Body Fat",
                                        fontSize = 11.sp,
                                        color = PureWhite
                                    )
                                }
                            }
                        }

                        // Divider Line & Center Draggable Knob
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(3.dp)
                                .offset(x = 0.dp)
                                .align(Alignment.CenterStart)
                                .padding(start = (320 * splitSliderPosition).dp) // approximate visual align
                                .background(PureWhite)
                        )

                        // Center slider pill indicator
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp)
                                .background(Obsidian950.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "◄ Drag to Compare ►",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date Photo Timeline Chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(progressPhotos) { photo ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Column {
                                    Text(photo.dateString, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("%.1f kg • ${photo.pose.label}".format(photo.weightKg), fontSize = 10.sp, color = CoralPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Body Circumference Measurements (Waist, Chest, Arms in ONE Graph!)
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
                            Text(
                                text = "Body Circumferences Evolution",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Waist, Chest & Arms in One Unified Graph",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = { showAddMeasurementDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AquaAccent.copy(alpha = 0.15f), contentColor = AquaAccent),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.bounceClick { showAddMeasurementDialog = true }
                        ) {
                            Text("+ Log cm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    BodyMeasurementsTrendChart(circumferences = circumferences)
                }
            }
        }

        // Endurance and Cardio Performance Tracking Graph
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
                            Text(
                                text = "Cardio & Endurance Performance",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Cardio equipment telemetry, distance & stamina",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(CardioGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${cardioSessions.size} Sessions",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CardioGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    EndurancePerformanceChart(cardioSessions = cardioSessions)
                }
            }
        }

        // Custom Reminders Manager
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
                            Text(
                                text = "Habit & Workout Reminders",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Never miss training or hydration",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showAddReminderDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            modifier = Modifier.bounceClick { showAddReminderDialog = true }
                        ) {
                            Text("+ Reminder", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    reminders.forEach { reminder ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (reminder.category) {
                                            ReminderCategory.WORKOUT -> Icons.Default.FitnessCenter
                                            ReminderCategory.HYDRATION -> Icons.Default.WaterDrop
                                            ReminderCategory.MEAL_LOG -> Icons.Default.Restaurant
                                            ReminderCategory.RECOVERY -> Icons.Default.Bedtime
                                        },
                                        contentDescription = null,
                                        tint = if (reminder.isEnabled) MintPrimary else Slate600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = reminder.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (reminder.isEnabled) MaterialTheme.colorScheme.onSurface else Slate600
                                    )
                                    Text(
                                        text = "${reminder.timeLabel} • ${reminder.daysLabel}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = reminder.isEnabled,
                                onCheckedChange = { repository.toggleReminder(reminder.id) },
                                colors = SwitchDefaults.colors(checkedThumbColor = MintPrimary, checkedTrackColor = MintPrimary.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Photo Dialog
    if (showAddPhotoDialog) {
        var photoTitle by remember { mutableStateOf("Weekly Check-In") }
        var photoWeight by remember { mutableStateOf("79.0") }
        var photoNote by remember { mutableStateOf("Feeling energetic and lean.") }

        AlertDialog(
            onDismissRequest = { showAddPhotoDialog = false },
            title = { Text("Log Progress Photo", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Attach current date-stamped photo to timeline to visually monitor body recomposition.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    OutlinedTextField(value = photoTitle, onValueChange = { photoTitle = it }, label = { Text("Title") }, singleLine = true)
                    OutlinedTextField(value = photoWeight, onValueChange = { photoWeight = it }, label = { Text("Weight (kg)") }, singleLine = true)
                    OutlinedTextField(value = photoNote, onValueChange = { photoNote = it }, label = { Text("Reflection / Notes") }, maxLines = 2)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = photoWeight.toDoubleOrNull() ?: 79.0
                        repository.addProgressPhoto(photoTitle, w, PhotoPoseType.FRONT, photoNote)
                        showAddPhotoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Text("Save Photo Record", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPhotoDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Measurement Dialog
    if (showAddMeasurementDialog) {
        var waistVal by remember { mutableStateOf("84.5") }
        var chestVal by remember { mutableStateOf("106.5") }
        var armsVal by remember { mutableStateOf("38.5") }
        var thighsVal by remember { mutableStateOf("62.0") }

        AlertDialog(
            onDismissRequest = { showAddMeasurementDialog = false },
            title = { Text("Log Circumferences (cm)", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = waistVal, onValueChange = { waistVal = it }, label = { Text("Waist (cm)") })
                    OutlinedTextField(value = chestVal, onValueChange = { chestVal = it }, label = { Text("Chest (cm)") })
                    OutlinedTextField(value = armsVal, onValueChange = { armsVal = it }, label = { Text("Arms (cm)") })
                    OutlinedTextField(value = thighsVal, onValueChange = { thighsVal = it }, label = { Text("Thighs (cm)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        repository.addMeasurement(
                            waist = waistVal.toDoubleOrNull() ?: 85.0,
                            chest = chestVal.toDoubleOrNull() ?: 106.0,
                            arms = armsVal.toDoubleOrNull() ?: 38.0,
                            thighs = thighsVal.toDoubleOrNull() ?: 62.0
                        )
                        showAddMeasurementDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AquaAccent)
                ) {
                    Text("Save Metrics", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMeasurementDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Reminder Dialog
    if (showAddReminderDialog) {
        var reminderTitle by remember { mutableStateOf("Post-Workout Protein Shake") }
        var reminderTime by remember { mutableStateOf("06:30 PM") }

        AlertDialog(
            onDismissRequest = { showAddReminderDialog = false },
            title = { Text("Create Habit Reminder", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = reminderTitle, onValueChange = { reminderTitle = it }, label = { Text("Reminder Title") })
                    OutlinedTextField(value = reminderTime, onValueChange = { reminderTime = it }, label = { Text("Time (e.g. 07:00 AM)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reminderTitle.isNotBlank()) {
                            repository.addReminder(reminderTitle, ReminderCategory.MEAL_LOG, reminderTime, "Daily")
                            showAddReminderDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                ) {
                    Text("Set Reminder", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReminderDialog = false }) { Text("Cancel") }
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
}

@Composable
private fun MeasurementMetricCard(
    label: String,
    currentCm: Double,
    deltaCm: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text("%.1f cm".format(currentCm), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            val isPositiveChange = if (label == "Waist") deltaCm <= 0 else deltaCm >= 0
            Text(
                text = "%+.1f cm".format(deltaCm),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositiveChange) MintPrimary else CoralPrimary
            )
        }
    }
}

@Preview(name = "Progress Screen Preview", showBackground = true)
@Composable
fun ProgressScreenPreview() {
    JustGetFitTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProgressScreen(repository = FitnessRepository())
        }
    }
}
