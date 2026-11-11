package com.example.model

import java.util.UUID

// --- STRENGTH WORKOUT MODELS ---

enum class ExerciseCategory(val displayName: String) {
    CHEST("Chest"),
    BACK("Back"),
    LEGS("Legs"),
    SHOULDERS("Shoulders"),
    ARMS("Arms"),
    CORE("Core"),
    CARDIO("Cardio")
}

enum class EquipmentType(val displayName: String) {
    DUMBBELL("Dumbbell"),
    BARBELL("Barbell"),
    CABLE_MACHINE("Cable Machine"),
    GYM_MACHINE("Gym Machine"),
    SMITH_MACHINE("Smith Machine"),
    BODYWEIGHT("Bodyweight"),
    CARDIO_MACHINE("Cardio Machine")
}

data class StrengthExercise(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: ExerciseCategory,
    val equipmentType: EquipmentType = EquipmentType.GYM_MACHINE,
    val defaultRestSeconds: Int = 90,
    val techniqueTip: String,
    val encouragingCue: String
)

data class GymMachineInfo(
    val id: String = UUID.randomUUID().toString(),
    val machineName: String,
    val everydayAlias: String,
    val targetMuscle: String,
    val category: ExerciseCategory,
    val defaultRestSeconds: Int = 90,
    val tip: String = "Maintain smooth controlled cadence through full range of motion."
) {
    val dropdownLabel: String
        get() = "$machineName ($everydayAlias) ($targetMuscle)"

    fun toStrengthExercise(): StrengthExercise {
        return StrengthExercise(
            name = "$machineName ($everydayAlias)",
            category = category,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = defaultRestSeconds,
            techniqueTip = "$tip Targets: $targetMuscle",
            encouragingCue = "Solid machine control! Own the contraction and slow down the negative."
        )
    }
}

enum class SplitPlanType(val title: String, val subtitle: String) {
    PUSH_PULL_LEGS("Option 1: Push/Pull/Legs", "Push, Pull, Legs, Push, Pull, Legs, Rest"),
    ISOLATION_SPLIT("Option 2: Isolation Split", "Chest, Back, Shoulders, Legs, Arms, Cardio, Rest"),
    TWO_MUSCLES_ARMS("Option 3: Two Muscles + Full Arms", "Chest/Tri, Back/Bi, Shoulders/Traps, Legs, Full Arms, Cardio, Rest"),
    CUSTOM_SPLIT("Custom Dynamic Split", "Assign any muscle to any day on the fly")
}

data class SplitDaySchedule(
    val dayName: String, // "Monday", "Tuesday", etc.
    val focusTitle: String,
    val primaryMuscle: ExerciseCategory,
    val primaryMovementsSummary: String,
    val isRestDay: Boolean = false,
    val isCardioOnly: Boolean = false
)

data class WorkoutSet(
    val setNumber: Int,
    var weightKg: Double,
    var reps: Int,
    var rpe: Int = 8, // Rate of Perceived Exertion (1-10)
    var isCompleted: Boolean = false,
    val isWarmup: Boolean = false
) {
    // Calculated 1 Rep Max using the Epley formula: 1RM = weight * (1 + reps / 30.0)
    val estimated1RM: Double
        get() = if (reps > 0 && weightKg > 0) {
            if (reps == 1) weightKg else (weightKg * (1.0 + reps / 30.0))
        } else 0.0
}

data class ExerciseLog(
    val exercise: StrengthExercise,
    val gymMachine: GymMachineInfo? = null,
    val sets: MutableList<WorkoutSet> = mutableListOf()
)

data class WorkoutSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val splitCategory: String, // e.g. "Push Day", "Pull Day", "Legs", "Upper Power"
    val dateString: String,
    val durationMinutes: Int = 0,
    val exercises: MutableList<ExerciseLog> = mutableListOf(),
    val isCompleted: Boolean = false,
    val totalVolumeKg: Double = 0.0,
    val personalRecordsBroken: Int = 0
)

data class ScheduledWorkout(
    val id: String = UUID.randomUUID().toString(),
    val dayOfWeek: String, // "Mon", "Tue", etc.
    val date: String,
    val title: String,
    val focusArea: String,
    val estimatedMinutes: Int,
    val isToday: Boolean = false,
    val isCompleted: Boolean = false
)

// --- CARDIO MACHINE MODELS ---

enum class CardioMachineType(val displayName: String, val unitTag: String) {
    TREADMILL("Treadmill", "Speed & Incline"),
    ELLIPTICAL("Elliptical", "Ramp & Resistance"),
    ROWING_MACHINE("Rowing Machine (Erg)", "/500m Split & SPM"),
    STATIONARY_BIKE("Stationary Bike", "Cadence & Watts"),
    STAIR_CLIMBER("Stair Climber", "Floors & Step Rate"),
    OUTDOOR_RUN("Running / Jogging", "Pace & Distance")
}

data class CardioSession(
    val id: String = UUID.randomUUID().toString(),
    val machineType: CardioMachineType,
    val dateString: String,
    val durationMinutes: Int,
    val distanceKm: Double = 0.0,
    val caloriesBurned: Int = 0,
    val avgHeartRateBpm: Int = 135,
    
    // Machine Specific Telemetry
    val treadmillSpeedKmh: Double = 0.0,
    val treadmillInclinePercent: Double = 0.0,
    
    val ellipticalResistanceLevel: Int = 0,
    val ellipticalStridesPerMin: Int = 0,
    
    val rowingSplitSeconds500m: Int = 0, // e.g. 120s = 2:00/500m
    val rowingStrokesPerMin: Int = 0,
    val rowingDamperSetting: Int = 5,
    val rowingWatts: Int = 0,
    
    val bikeCadenceRpm: Int = 0,
    val bikeResistanceLevel: Int = 0,
    val bikeWatts: Int = 0,
    
    val stairFloorsClimbed: Int = 0,
    val notes: String = ""
)

// --- LLM PROVIDER & AI NUTRITION MODELS ---

enum class LlmProvider(
    val id: String,
    val displayName: String,
    val defaultModel: String,
    val keyPlaceholder: String,
    val keyHelpUrl: String
) {
    GEMINI(
        id = "gemini",
        displayName = "Google Gemini",
        defaultModel = "gemini-2.5-flash",
        keyPlaceholder = "AIzaSy...",
        keyHelpUrl = "aistudio.google.com/apikey"
    ),
    CHATGPT(
        id = "chatgpt",
        displayName = "OpenAI ChatGPT",
        defaultModel = "gpt-4o-mini",
        keyPlaceholder = "sk-proj-...",
        keyHelpUrl = "platform.openai.com/api-keys"
    )
}

data class NutritionAiResult(
    val foodName: String,
    val servingUnit: String = "1 serving",
    val calories: Int,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double = 0.0,
    val notes: String = ""
)

// --- NUTRITION & RECIPE MODELS ---

enum class MealCategory(val title: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks")
}

data class FoodItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val brandOrCategory: String,
    val servingUnit: String = "100g",
    val caloriesPerServing: Int,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double = 0.0
)

data class LoggedMealItem(
    val id: String = UUID.randomUUID().toString(),
    val foodItem: FoodItem,
    val mealCategory: MealCategory,
    val numberOfServings: Double = 1.0,
    val dateString: String,
    val customCaloriesOverride: Int? = null,
    val customProteinOverride: Double? = null,
    val customCarbsOverride: Double? = null,
    val customFatOverride: Double? = null
) {
    val totalCalories: Int get() = customCaloriesOverride ?: (foodItem.caloriesPerServing * numberOfServings).toInt()
    val totalProtein: Double get() = customProteinOverride ?: (foodItem.proteinGrams * numberOfServings)
    val totalCarbs: Double get() = customCarbsOverride ?: (foodItem.carbsGrams * numberOfServings)
    val totalFat: Double get() = customFatOverride ?: (foodItem.fatGrams * numberOfServings)
}

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val prepTimeMinutes: Int,
    val calories: Int,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val ingredients: List<String>,
    val instructions: List<String>,
    val tags: List<String>
)

// --- RECOVERY & WEARABLE TELEMETRY MODELS ---

data class WearableTelemetry(
    val currentHeartRateBpm: Int = 72,
    val restingHeartRateBpm: Int = 58,
    val heartRateVariabilityMs: Int = 68,
    val activeCaloriesBurned: Int = 620,
    val stepsTaken: Int = 8450,
    val totalSleepHours: Double = 7.6,
    val deepSleepHours: Double = 1.8,
    val remSleepHours: Double = 2.1,
    val lightSleepHours: Double = 3.2,
    val awakeSleepHours: Double = 0.5,
    val sleepQualityScore: Int = 86, // 0-100
    val isSyncingActive: Boolean = false
)

data class RecoveryInsight(
    val readinessScorePercent: Int, // 0-100%
    val statusHeadline: String,
    val recoveryActionSummary: String,
    val cnsStrainLevel: String, // "Optimal", "Moderate", "High Strain"
    val muscleSorenessAreas: List<String>,
    val recommendedWorkoutIntensity: String
)

// --- WEIGHT TRACKING, BODY TRANSFORMATION & PHOTOS ---

data class WeightRecord(
    val id: String = UUID.randomUUID().toString(),
    val dateString: String,
    val weightKg: Double,
    val bodyFatPercentage: Double = 0.0
)

data class WeightGoal(
    val startingWeightKg: Double = 86.5,
    val currentWeightKg: Double = 79.2,
    val targetWeightKg: Double = 74.0,
    val targetMilestoneWeeks: Int = 8,
    val weeklyPaceKg: Double = 0.65
)

enum class PhotoPoseType(val label: String) {
    FRONT("Front View"),
    SIDE("Side Profile"),
    BACK("Back View")
}

data class ProgressPhotoItem(
    val id: String = UUID.randomUUID().toString(),
    val dateString: String,
    val pose: PhotoPoseType,
    val weightKg: Double,
    val title: String,
    val notes: String = "",
    val accentHueSeed: Int = 0
)

data class BodyCircumference(
    val id: String = UUID.randomUUID().toString(),
    val dateString: String,
    val waistCm: Double,
    val chestCm: Double,
    val armsCm: Double,
    val thighsCm: Double
)

// --- HABIT REMINDERS & MOTIVATION ---

enum class ReminderCategory(val title: String) {
    WORKOUT("Workout Session"),
    MEAL_LOG("Nutrition & Meal Log"),
    HYDRATION("Hydration Check"),
    RECOVERY("Wind-Down & Sleep")
}

data class CustomReminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: ReminderCategory,
    val timeLabel: String,
    val daysLabel: String,
    var isEnabled: Boolean = true
)

data class MotivationQuote(
    val text: String,
    val author: String,
    val contextTag: String
)
