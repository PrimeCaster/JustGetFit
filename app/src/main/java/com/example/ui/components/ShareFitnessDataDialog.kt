package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.FitnessRepository
import com.example.ui.theme.CoralPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.PureWhite
import com.example.util.ExportDateRange
import com.example.util.FitnessReportExporter

enum class ShareContentType(val label: String, val icon: String) {
    BOTH("Both Workouts & Nutrition", "📊"),
    WORKOUTS_ONLY("Workouts Only", "🏋️"),
    NUTRITION_ONLY("Nutrition Only", "🥗")
}

enum class ShareFormat(val label: String, val subtitle: String) {
    TEXT_TABLE("Readable Text & Tables", "Clean emojis & structured summary for WhatsApp/Email"),
    CSV("Comma-Separated (CSV)", "Data table format for spreadsheets & archiving")
}

@Composable
fun ShareFitnessDataDialog(
    repository: FitnessRepository,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedRange by remember { mutableStateOf(ExportDateRange.TODAY) }
    var selectedContentType by remember { mutableStateOf(ShareContentType.BOTH) }
    var selectedFormat by remember { mutableStateOf(ShareFormat.TEXT_TABLE) }

    val activeWorkout by repository.activeWorkout.collectAsState()
    val workoutsHistory by repository.workoutsByDate.collectAsState()
    val cardioSessions by repository.cardioSessions.collectAsState()
    val meals by repository.loggedMeals.collectAsState()
    val waterIntakeByDate by repository.waterIntakeByDate.collectAsState()
    val weightGoal by repository.weightGoal.collectAsState()
    val dailyWaterTargetMl by repository.dailyWaterTargetMlFlow.collectAsState()

    // Formatted content preview
    val previewContent = remember(selectedRange, selectedContentType, selectedFormat, activeWorkout, meals, cardioSessions) {
        val incWorkouts = selectedContentType == ShareContentType.BOTH || selectedContentType == ShareContentType.WORKOUTS_ONLY
        val incNutrition = selectedContentType == ShareContentType.BOTH || selectedContentType == ShareContentType.NUTRITION_ONLY

        if (selectedFormat == ShareFormat.TEXT_TABLE) {
            FitnessReportExporter.buildShareText(
                range = selectedRange,
                includeWorkouts = incWorkouts,
                includeNutrition = incNutrition,
                workout = activeWorkout,
                workoutsHistory = workoutsHistory,
                cardioSessions = cardioSessions,
                meals = meals,
                waterIntakeByDate = waterIntakeByDate,
                weightGoal = weightGoal,
                dailyWaterTargetMl = dailyWaterTargetMl
            )
        } else {
            FitnessReportExporter.buildShareCsv(
                range = selectedRange,
                includeWorkouts = incWorkouts,
                includeNutrition = incNutrition,
                workout = activeWorkout,
                workoutsHistory = workoutsHistory,
                cardioSessions = cardioSessions,
                meals = meals
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Share Fitness & Nutrition",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Export via WhatsApp, Email or copy text",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Controls & Preview
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. SELECT TIME PERIOD
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "1. SELECT TIME PERIOD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                ExportDateRange.TODAY to "Today",
                                ExportDateRange.CURRENT_WEEK to "This Week",
                                ExportDateRange.CURRENT_MONTH to "This Month"
                            ).forEach { (range, label) ->
                                val isSelected = selectedRange == range
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .border(
                                            1.dp,
                                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedRange = range }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // 2. SELECT CONTENT
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "2. CONTENT TO INCLUDE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ShareContentType.values().forEach { cType ->
                                val isSelected = selectedContentType == cType
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) CoralPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .border(
                                            1.dp,
                                            if (isSelected) CoralPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedContentType = cType }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${cType.icon} ${cType.name.split('_').first().lowercase().replaceFirstChar { it.uppercase() }}",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // 3. SELECT FORMAT
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "3. SHARING FORMAT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ShareFormat.values().forEach { fmt ->
                                val isSelected = selectedFormat == fmt
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ElectricBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .border(
                                            1.dp,
                                            if (isSelected) ElectricBlue else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedFormat = fmt }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = fmt.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. PREVIEW BOX
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MESSAGE PREVIEW",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Tap to Copy",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("JustGetFit Report", previewContent))
                                    Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = previewContent,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = PureWhite,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: WhatsApp & Email
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // WhatsApp
                    Button(
                        onClick = {
                            FitnessReportExporter.shareViaWhatsApp(context, previewContent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Text("💬 WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }

                    // Email
                    Button(
                        onClick = {
                            val subject = "JustGetFit Progress Report - ${selectedRange.displayName}"
                            FitnessReportExporter.shareViaEmail(context, subject, previewContent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("✉️ Email", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                    }

                    // More Chooser
                    IconButton(
                        onClick = {
                            FitnessReportExporter.shareViaChooser(context, "Share JustGetFit Report", previewContent)
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Share Options", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}
