package com.example.data

import com.example.model.*
import com.example.ui.theme.AppColorTheme
import com.example.util.FitnessDateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class FitnessRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    // Theme Mode & Athletic Palette
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _currentColorTheme = MutableStateFlow(AppColorTheme.EMERALD_VITALITY)
    val currentColorTheme: StateFlow<AppColorTheme> = _currentColorTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.update { !it }
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    fun setColorTheme(theme: AppColorTheme) {
        _currentColorTheme.value = theme
    }

    // --- LLM CONFIGURATION (GEMINI & CHATGPT) ---
    private val _selectedLlmProvider = MutableStateFlow(LlmProvider.GEMINI)
    val selectedLlmProvider: StateFlow<LlmProvider> = _selectedLlmProvider.asStateFlow()

    private val _geminiApiKey = MutableStateFlow("")
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _chatGptApiKey = MutableStateFlow("")
    val chatGptApiKey: StateFlow<String> = _chatGptApiKey.asStateFlow()

    private val _customModelOverride = MutableStateFlow("")
    val customModelOverride: StateFlow<String> = _customModelOverride.asStateFlow()

    private val _defaultRestTimerSeconds = MutableStateFlow(90)
    val defaultRestTimerSeconds: StateFlow<Int> = _defaultRestTimerSeconds.asStateFlow()

    private val nutritionAiCalculator = NutritionAiCalculator()

    fun setLlmProvider(provider: LlmProvider) {
        _selectedLlmProvider.value = provider
    }

    fun setGeminiApiKey(key: String) {
        _geminiApiKey.value = key.trim()
    }

    fun setChatGptApiKey(key: String) {
        _chatGptApiKey.value = key.trim()
    }

    fun setCustomModelOverride(model: String) {
        _customModelOverride.value = model.trim()
    }

    fun setDefaultRestTimerSeconds(seconds: Int) {
        _defaultRestTimerSeconds.value = seconds
    }

    fun getActiveApiKey(): String {
        return when (_selectedLlmProvider.value) {
            LlmProvider.GEMINI -> _geminiApiKey.value
            LlmProvider.CHATGPT -> _chatGptApiKey.value
        }
    }

    suspend fun calculateFoodNutritionWithAi(foodDescription: String): Result<NutritionAiResult> {
        return nutritionAiCalculator.calculateNutrition(
            foodDescription = foodDescription,
            provider = _selectedLlmProvider.value,
            apiKey = getActiveApiKey(),
            customModelOverride = _customModelOverride.value.ifBlank { null }
        )
    }

    suspend fun testLlmConnection(): Result<String> {
        return nutritionAiCalculator.testApiConnection(
            provider = _selectedLlmProvider.value,
            apiKey = getActiveApiKey(),
            customModelOverride = _customModelOverride.value.ifBlank { null }
        )
    }

    // Daily Motivational Quote
    private val _currentQuote = MutableStateFlow(MotivationQuoteEngine.getRandomDailyQuote())
    val currentQuote: StateFlow<MotivationQuote> = _currentQuote.asStateFlow()

    fun refreshDailyQuote() {
        _currentQuote.value = MotivationQuoteEngine.getRandomDailyQuote()
    }

    // Weight Records & Goals
    private val _weightRecords = MutableStateFlow(PresetFitnessData.historicalWeightData)
    val weightRecords: StateFlow<List<WeightRecord>> = _weightRecords.asStateFlow()

    private val _weightGoal = MutableStateFlow(WeightGoal())
    val weightGoal: StateFlow<WeightGoal> = _weightGoal.asStateFlow()

    fun logWeight(weightKg: Double, dateStr: String = "Today") {
        val newRecord = WeightRecord(dateString = dateStr, weightKg = weightKg)
        _weightRecords.update { it + newRecord }
        _weightGoal.update { it.copy(currentWeightKg = weightKg) }
    }

    // Workouts History by Date & Active Selected Workout
    private val _workoutsByDate = MutableStateFlow<Map<String, WorkoutSession>>(PresetFitnessData.createInitialWorkoutsHistory())
    val workoutsByDate: StateFlow<Map<String, WorkoutSession>> = _workoutsByDate.asStateFlow()

    private val _selectedWorkoutDate = MutableStateFlow<String>(FitnessDateUtils.getTodayDateString())
    val selectedWorkoutDate: StateFlow<String> = _selectedWorkoutDate.asStateFlow()

    private val _activeWorkout = MutableStateFlow<WorkoutSession>(
        _workoutsByDate.value[FitnessDateUtils.getTodayDateString()]
            ?: _workoutsByDate.value["Today"]
            ?: PresetFitnessData.createInitialWorkoutSession()
    )
    val activeWorkout: StateFlow<WorkoutSession> = _activeWorkout.asStateFlow()

    private val _exerciseCatalog = MutableStateFlow(PresetFitnessData.exerciseCatalog)
    val exerciseCatalog: StateFlow<List<StrengthExercise>> = _exerciseCatalog.asStateFlow()

    private val _gymMachinesCatalog = MutableStateFlow(PresetFitnessData.gymMachinesCatalog)
    val gymMachinesCatalog: StateFlow<List<GymMachineInfo>> = _gymMachinesCatalog.asStateFlow()

    private fun syncActiveWorkoutToHistory(session: WorkoutSession) {
        _activeWorkout.value = session
        val dateKey = FitnessDateUtils.getNormalizedDate(session.dateString)
        _workoutsByDate.update { current ->
            val updated = current.toMutableMap()
            updated[dateKey] = session
            if (FitnessDateUtils.isDateToday(dateKey)) {
                updated["Today"] = session
            }
            if (FitnessDateUtils.isDateYesterday(dateKey)) {
                updated["Yesterday"] = session
            }
            updated
        }
    }

    fun selectWorkoutDate(rawDateStr: String) {
        val normalized = FitnessDateUtils.getNormalizedDate(rawDateStr)
        _selectedWorkoutDate.value = normalized
        val existing = _workoutsByDate.value[normalized] ?: _workoutsByDate.value[rawDateStr]
        if (existing != null) {
            _activeWorkout.value = existing
        } else {
            val emptySession = WorkoutSession(
                title = "Workout (${FitnessDateUtils.getRelativeLabel(normalized)})",
                splitCategory = "Custom Split",
                dateString = normalized,
                durationMinutes = 0,
                exercises = mutableListOf(),
                isCompleted = false,
                totalVolumeKg = 0.0
            )
            syncActiveWorkoutToHistory(emptySession)
        }
    }

    fun addGymMachineToWorkout(machine: GymMachineInfo) {
        addExerciseToWorkout(machine.toStrengthExercise())
    }

    private val _scheduledWorkouts = MutableStateFlow(PresetFitnessData.upcomingScheduledWorkouts)
    val scheduledWorkouts: StateFlow<List<ScheduledWorkout>> = _scheduledWorkouts.asStateFlow()

    // Dynamic Split Management (Options 1, 2, 3 and Custom)
    private val _currentSplitType = MutableStateFlow(SplitPlanType.PUSH_PULL_LEGS)
    val currentSplitType: StateFlow<SplitPlanType> = _currentSplitType.asStateFlow()

    private val _splitSchedule = MutableStateFlow(PresetFitnessData.option1PPLSchedule)
    val splitSchedule: StateFlow<List<SplitDaySchedule>> = _splitSchedule.asStateFlow()

    fun selectSplitPlan(type: SplitPlanType) {
        _currentSplitType.value = type
        _splitSchedule.value = when (type) {
            SplitPlanType.PUSH_PULL_LEGS -> PresetFitnessData.option1PPLSchedule
            SplitPlanType.ISOLATION_SPLIT -> PresetFitnessData.option2IsolationSchedule
            SplitPlanType.TWO_MUSCLES_ARMS -> PresetFitnessData.option3TwoMusclesSchedule
            SplitPlanType.CUSTOM_SPLIT -> _splitSchedule.value
        }
    }

    fun updateDayMuscleFocus(dayIndex: Int, newMuscle: ExerciseCategory, newTitle: String, newSummary: String, isRest: Boolean = false) {
        _splitSchedule.update { list ->
            list.mapIndexed { idx, day ->
                if (idx == dayIndex) {
                    day.copy(
                        focusTitle = newTitle,
                        primaryMuscle = newMuscle,
                        primaryMovementsSummary = newSummary,
                        isRestDay = isRest
                    )
                } else day
            }
        }
    }

    fun launchWorkoutFromSplitDay(daySchedule: SplitDaySchedule) {
        val matchingExercises = _exerciseCatalog.value.filter { it.category == daySchedule.primaryMuscle }.take(3)
        val selected = if (matchingExercises.isNotEmpty()) matchingExercises else _exerciseCatalog.value.take(3)

        val newSession = WorkoutSession(
            title = daySchedule.focusTitle,
            splitCategory = daySchedule.dayName,
            dateString = _selectedWorkoutDate.value,
            durationMinutes = 55,
            exercises = selected.map { ex ->
                ExerciseLog(
                    exercise = ex,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 32.5, reps = 8, rpe = 8, isCompleted = false),
                        WorkoutSet(3, weightKg = 35.0, reps = 6, rpe = 9, isCompleted = false)
                    )
                )
            }.toMutableList(),
            isCompleted = false,
            totalVolumeKg = 0.0
        )
        syncActiveWorkoutToHistory(newSession)
    }

    fun toggleSetCompleted(exerciseIndex: Int, setIndex: Int): String? {
        var cueToReturn: String? = null
        val session = _activeWorkout.value
        val updatedExercises = session.exercises.mapIndexed { eIdx, exLog ->
            if (eIdx == exerciseIndex) {
                val updatedSets = exLog.sets.mapIndexed { sIdx, set ->
                    if (sIdx == setIndex) {
                        val nextState = !set.isCompleted
                        if (nextState) {
                            cueToReturn = MotivationQuoteEngine.getExerciseCue(exLog.exercise.name)
                        }
                        set.copy(isCompleted = nextState)
                    } else set
                }.toMutableList()
                exLog.copy(sets = updatedSets)
            } else exLog
        }.toMutableList()

        var volume = 0.0
        updatedExercises.forEach { ex ->
            ex.sets.filter { it.isCompleted }.forEach { s ->
                volume += s.weightKg * s.reps
            }
        }

        val updated = session.copy(exercises = updatedExercises, totalVolumeKg = volume)
        syncActiveWorkoutToHistory(updated)
        return cueToReturn
    }

    fun updateSetValues(exerciseIndex: Int, setIndex: Int, weight: Double, reps: Int, rpe: Int) {
        val session = _activeWorkout.value
        val updatedExercises = session.exercises.mapIndexed { eIdx, exLog ->
            if (eIdx == exerciseIndex) {
                val updatedSets = exLog.sets.mapIndexed { sIdx, set ->
                    if (sIdx == setIndex) {
                        set.copy(weightKg = weight, reps = reps, rpe = rpe)
                    } else set
                }.toMutableList()
                exLog.copy(sets = updatedSets)
            } else exLog
        }.toMutableList()

        var volume = 0.0
        updatedExercises.forEach { ex ->
            ex.sets.filter { it.isCompleted }.forEach { s ->
                volume += s.weightKg * s.reps
            }
        }

        val updated = session.copy(exercises = updatedExercises, totalVolumeKg = volume)
        syncActiveWorkoutToHistory(updated)
    }

    fun addSetToExercise(exerciseIndex: Int) {
        val session = _activeWorkout.value
        val updatedExercises = session.exercises.mapIndexed { eIdx, exLog ->
            if (eIdx == exerciseIndex) {
                val lastSet = exLog.sets.lastOrNull()
                val newSet = WorkoutSet(
                    setNumber = exLog.sets.size + 1,
                    weightKg = lastSet?.weightKg ?: 60.0,
                    reps = lastSet?.reps ?: 10,
                    rpe = 8,
                    isCompleted = false
                )
                exLog.copy(sets = (exLog.sets + newSet).toMutableList())
            } else exLog
        }.toMutableList()

        syncActiveWorkoutToHistory(session.copy(exercises = updatedExercises))
    }

    fun deleteSetFromExercise(exerciseIndex: Int, setIndex: Int) {
        val session = _activeWorkout.value
        val updatedExercises = session.exercises.mapIndexed { eIdx, exLog ->
            if (eIdx == exerciseIndex) {
                val updatedSets = exLog.sets.filterIndexed { sIdx, _ -> sIdx != setIndex }
                    .mapIndexed { idx, s -> s.copy(setNumber = idx + 1) }
                    .toMutableList()
                exLog.copy(sets = updatedSets)
            } else exLog
        }.toMutableList()

        var volume = 0.0
        updatedExercises.forEach { ex ->
            ex.sets.filter { it.isCompleted }.forEach { s ->
                volume += s.weightKg * s.reps
            }
        }

        syncActiveWorkoutToHistory(session.copy(exercises = updatedExercises, totalVolumeKg = volume))
    }

    fun deleteExerciseFromWorkout(exerciseIndex: Int) {
        val session = _activeWorkout.value
        val updatedExercises = session.exercises.filterIndexed { idx, _ -> idx != exerciseIndex }.toMutableList()
        var volume = 0.0
        updatedExercises.forEach { ex ->
            ex.sets.filter { it.isCompleted }.forEach { s ->
                volume += s.weightKg * s.reps
            }
        }
        syncActiveWorkoutToHistory(session.copy(exercises = updatedExercises, totalVolumeKg = volume))
    }

    fun addExerciseAndMachineToWorkout(
        exercise: StrengthExercise,
        gymMachine: GymMachineInfo,
        initialSets: List<WorkoutSet>? = null
    ) {
        val session = _activeWorkout.value
        val setsToUse = initialSets?.toMutableList() ?: mutableListOf(
            WorkoutSet(setNumber = 1, weightKg = 40.0, reps = 10, rpe = 7, isCompleted = false),
            WorkoutSet(setNumber = 2, weightKg = 45.0, reps = 8, rpe = 8, isCompleted = false),
            WorkoutSet(setNumber = 3, weightKg = 45.0, reps = 8, rpe = 9, isCompleted = false)
        )
        val newLog = ExerciseLog(
            exercise = exercise,
            gymMachine = gymMachine,
            sets = setsToUse
        )
        syncActiveWorkoutToHistory(session.copy(exercises = (session.exercises + newLog).toMutableList()))
    }

    fun editExerciseAndMachineInWorkout(exerciseIndex: Int, newExercise: StrengthExercise, newGymMachine: GymMachineInfo) {
        val session = _activeWorkout.value
        val updated = session.exercises.mapIndexed { idx, log ->
            if (idx == exerciseIndex) {
                log.copy(
                    exercise = newExercise,
                    gymMachine = newGymMachine
                )
            } else log
        }.toMutableList()
        syncActiveWorkoutToHistory(session.copy(exercises = updated))
    }

    fun addExerciseToWorkout(exercise: StrengthExercise) {
        val session = _activeWorkout.value
        val newLog = ExerciseLog(
            exercise = exercise,
            gymMachine = null,
            sets = mutableListOf(
                WorkoutSet(setNumber = 1, weightKg = 40.0, reps = 10, rpe = 7, isCompleted = false),
                WorkoutSet(setNumber = 2, weightKg = 45.0, reps = 8, rpe = 8, isCompleted = false),
                WorkoutSet(setNumber = 3, weightKg = 45.0, reps = 8, rpe = 9, isCompleted = false)
            )
        )
        syncActiveWorkoutToHistory(session.copy(exercises = (session.exercises + newLog).toMutableList()))
    }

    fun completeWorkout(): String {
        val session = _activeWorkout.value
        syncActiveWorkoutToHistory(session.copy(isCompleted = true))
        if (FitnessDateUtils.isDateToday(session.dateString)) {
            _scheduledWorkouts.update { list ->
                list.map { if (it.isToday) it.copy(isCompleted = true) else it }
            }
        }
        return MotivationQuoteEngine.getRandomWorkoutCelebration()
    }

    // Cardio Sessions
    private val _cardioSessions = MutableStateFlow(PresetFitnessData.initialCardioSessions)
    val cardioSessions: StateFlow<List<CardioSession>> = _cardioSessions.asStateFlow()

    fun logCardioSession(session: CardioSession) {
        _cardioSessions.update { listOf(session) + it }
        // Also update wearable active burn
        _wearableTelemetry.update {
            it.copy(
                activeCaloriesBurned = it.activeCaloriesBurned + session.caloriesBurned,
                stepsTaken = if (session.machineType == CardioMachineType.TREADMILL || session.machineType == CardioMachineType.OUTDOOR_RUN) {
                    (it.stepsTaken + (session.distanceKm * 1250).toInt())
                } else it.stepsTaken
            )
        }
    }

    // Nutrition & Water with Date Management
    private val _loggedMeals = MutableStateFlow(PresetFitnessData.createInitialMealEntries())
    val loggedMeals: StateFlow<List<LoggedMealItem>> = _loggedMeals.asStateFlow()

    private val _selectedNutritionDate = MutableStateFlow<String>(FitnessDateUtils.getTodayDateString())
    val selectedNutritionDate: StateFlow<String> = _selectedNutritionDate.asStateFlow()

    private val _waterIntakeByDate = MutableStateFlow<Map<String, Int>>(PresetFitnessData.createInitialWaterIntake())
    val waterIntakeByDate: StateFlow<Map<String, Int>> = _waterIntakeByDate.asStateFlow()

    private val _foodsCatalog = MutableStateFlow(PresetFitnessData.commonFoodsCatalog)
    val foodsCatalog: StateFlow<List<FoodItem>> = _foodsCatalog.asStateFlow()

    private val _recipes = MutableStateFlow(PresetFitnessData.curatedRecipes)
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _waterIntakeMl = MutableStateFlow(2250)
    val waterIntakeMl: StateFlow<Int> = _waterIntakeMl.asStateFlow()

    private val _dailyCalorieTarget = MutableStateFlow(2400)
    val dailyCalorieTarget: Int get() = _dailyCalorieTarget.value
    val dailyCalorieTargetFlow: StateFlow<Int> = _dailyCalorieTarget.asStateFlow()

    private val _dailyProteinTargetG = MutableStateFlow(180.0)
    val dailyProteinTargetG: Double get() = _dailyProteinTargetG.value
    val dailyProteinTargetGFlow: StateFlow<Double> = _dailyProteinTargetG.asStateFlow()

    private val _dailyCarbsTargetG = MutableStateFlow(250.0)
    val dailyCarbsTargetG: Double get() = _dailyCarbsTargetG.value

    private val _dailyFatTargetG = MutableStateFlow(65.0)
    val dailyFatTargetG: Double get() = _dailyFatTargetG.value

    private val _dailyWaterTargetMl = MutableStateFlow(3500)
    val dailyWaterTargetMl: Int get() = _dailyWaterTargetMl.value
    val dailyWaterTargetMlFlow: StateFlow<Int> = _dailyWaterTargetMl.asStateFlow()

    fun setDailyCalorieTarget(target: Int) {
        _dailyCalorieTarget.value = target.coerceAtLeast(1000)
    }

    fun setDailyWaterTargetMl(target: Int) {
        _dailyWaterTargetMl.value = target.coerceAtLeast(500)
    }

    fun setDailyProteinTargetG(target: Double) {
        _dailyProteinTargetG.value = target.coerceAtLeast(20.0)
    }

    fun isDateToday(dateStr: String): Boolean = FitnessDateUtils.isDateToday(dateStr)

    fun selectNutritionDate(rawDateStr: String) {
        val normalized = FitnessDateUtils.getNormalizedDate(rawDateStr)
        _selectedNutritionDate.value = normalized
        val currentWater = _waterIntakeByDate.value[normalized]
            ?: _waterIntakeByDate.value[rawDateStr]
            ?: if (FitnessDateUtils.isDateToday(normalized)) 2250 else 2000
        _waterIntakeMl.value = currentWater
    }

    fun getMealsForDate(dateStr: String): List<LoggedMealItem> {
        val target = FitnessDateUtils.getNormalizedDate(dateStr)
        return _loggedMeals.value.filter {
            FitnessDateUtils.getNormalizedDate(it.dateString) == target
        }
    }

    fun getWaterForDate(dateStr: String): Int {
        val target = FitnessDateUtils.getNormalizedDate(dateStr)
        return _waterIntakeByDate.value[target] ?: _waterIntakeByDate.value[dateStr] ?: 0
    }

    fun logMeal(mealItem: LoggedMealItem) {
        _loggedMeals.update { listOf(mealItem) + it }
    }

    fun addFoodToDiary(food: FoodItem, category: MealCategory, servings: Double, targetDate: String? = null): String {
        val date = FitnessDateUtils.getNormalizedDate(targetDate ?: _selectedNutritionDate.value)
        val entry = LoggedMealItem(
            foodItem = food,
            mealCategory = category,
            numberOfServings = servings,
            dateString = date
        )
        _loggedMeals.update { it + entry }
        return MotivationQuoteEngine.getRandomMealAffirmation()
    }

    fun updateMealEntry(
        entryId: String,
        newServings: Double,
        newCategory: MealCategory,
        customCalories: Int? = null,
        customProtein: Double? = null,
        customCarbs: Double? = null,
        customFat: Double? = null
    ) {
        _loggedMeals.update { list ->
            list.map { entry ->
                if (entry.id == entryId) {
                    entry.copy(
                        numberOfServings = newServings,
                        mealCategory = newCategory,
                        customCaloriesOverride = customCalories,
                        customProteinOverride = customProtein,
                        customCarbsOverride = customCarbs,
                        customFatOverride = customFat
                    )
                } else entry
            }
        }
    }

    fun updateMealEntry(updatedItem: LoggedMealItem) {
        _loggedMeals.update { list ->
            list.map { if (it.id == updatedItem.id) updatedItem else it }
        }
    }

    fun removeMealEntry(entryId: String) {
        _loggedMeals.update { it.filterNot { entry -> entry.id == entryId } }
    }

    fun logRecipeToDiary(recipe: Recipe, category: MealCategory = MealCategory.LUNCH, targetDate: String? = null): String {
        val foodFromRecipe = FoodItem(
            name = recipe.title,
            brandOrCategory = "Chef Recipe",
            servingUnit = "1 portion",
            caloriesPerServing = recipe.calories,
            proteinGrams = recipe.proteinG,
            carbsGrams = recipe.carbsG,
            fatGrams = recipe.fatG
        )
        return addFoodToDiary(foodFromRecipe, category, 1.0, targetDate)
    }

    fun addCustomFood(
        name: String,
        calories: Int,
        protein: Double,
        carbs: Double,
        fat: Double,
        servingUnit: String = "1 serving"
    ) {
        val item = FoodItem(
            name = name,
            brandOrCategory = "Custom Food",
            servingUnit = servingUnit,
            caloriesPerServing = calories,
            proteinGrams = protein,
            carbsGrams = carbs,
            fatGrams = fat
        )
        _foodsCatalog.update { listOf(item) + it }
    }

    fun addWater(amountMl: Int, targetDate: String? = null) {
        val date = FitnessDateUtils.getNormalizedDate(targetDate ?: _selectedNutritionDate.value)
        val current = _waterIntakeByDate.value[date] ?: if (FitnessDateUtils.isDateToday(date)) 2250 else 0
        val newWater = (current + amountMl).coerceAtLeast(0)
        _waterIntakeByDate.update {
            val m = it.toMutableMap()
            m[date] = newWater
            if (FitnessDateUtils.isDateToday(date)) m["Today"] = newWater
            if (FitnessDateUtils.isDateYesterday(date)) m["Yesterday"] = newWater
            m
        }
        if (date == _selectedNutritionDate.value || (FitnessDateUtils.isDateToday(date) && FitnessDateUtils.isDateToday(_selectedNutritionDate.value))) {
            _waterIntakeMl.value = newWater
        }
    }

    fun setWater(amountMl: Int, targetDate: String? = null) {
        val date = FitnessDateUtils.getNormalizedDate(targetDate ?: _selectedNutritionDate.value)
        val newWater = amountMl.coerceAtLeast(0)
        _waterIntakeByDate.update {
            val m = it.toMutableMap()
            m[date] = newWater
            if (FitnessDateUtils.isDateToday(date)) m["Today"] = newWater
            if (FitnessDateUtils.isDateYesterday(date)) m["Yesterday"] = newWater
            m
        }
        if (date == _selectedNutritionDate.value || (FitnessDateUtils.isDateToday(date) && FitnessDateUtils.isDateToday(_selectedNutritionDate.value))) {
            _waterIntakeMl.value = newWater
        }
    }

    // Wearable & Recovery Telemetry
    private val _wearableTelemetry = MutableStateFlow(WearableTelemetry())
    val wearableTelemetry: StateFlow<WearableTelemetry> = _wearableTelemetry.asStateFlow()

    private val _recoveryInsight = MutableStateFlow(
        RecoveryInsight(
            readinessScorePercent = 88,
            statusHeadline = "Optimal Training Window",
            recoveryActionSummary = "CNS strain is low, deep sleep reached 1.8 hrs. High capacity for heavy strength sets or high-intensity intervals.",
            cnsStrainLevel = "Optimal (Green)",
            muscleSorenessAreas = listOf("Hamstrings (Mild)", "Chest (Recovered)", "Lats (Ready)"),
            recommendedWorkoutIntensity = "Push Volume & Heavy Loads (RPE 8-9)"
        )
    )
    val recoveryInsight: StateFlow<RecoveryInsight> = _recoveryInsight.asStateFlow()

    private var isSimulatingTelemetry = false

    fun toggleWearableLiveSync() {
        isSimulatingTelemetry = !isSimulatingTelemetry
        _wearableTelemetry.update { it.copy(isSyncingActive = isSimulatingTelemetry) }

        if (isSimulatingTelemetry) {
            scope.launch {
                while (isSimulatingTelemetry) {
                    delay(2500)
                    _wearableTelemetry.update { current ->
                        val hrFluctuation = Random.nextInt(-3, 4)
                        val newHr = (current.currentHeartRateBpm + hrFluctuation).coerceIn(60, 160)
                        val stepBump = Random.nextInt(5, 20)
                        current.copy(
                            currentHeartRateBpm = newHr,
                            stepsTaken = current.stepsTaken + stepBump,
                            activeCaloriesBurned = current.activeCaloriesBurned + (stepBump / 15)
                        )
                    }
                }
            }
        }
    }

    // Progress Photos & Measurements
    private val _progressPhotos = MutableStateFlow(PresetFitnessData.initialProgressPhotos)
    val progressPhotos: StateFlow<List<ProgressPhotoItem>> = _progressPhotos.asStateFlow()

    private val _circumferences = MutableStateFlow(PresetFitnessData.initialCircumferences)
    val circumferences: StateFlow<List<BodyCircumference>> = _circumferences.asStateFlow()

    fun addProgressPhoto(title: String, weightKg: Double, pose: PhotoPoseType, note: String) {
        val item = ProgressPhotoItem(
            dateString = "Nov 8, 2026",
            pose = pose,
            weightKg = weightKg,
            title = title,
            notes = note,
            accentHueSeed = Random.nextInt(0, 5)
        )
        _progressPhotos.update { listOf(item) + it }
    }

    fun addMeasurement(waist: Double, chest: Double, arms: Double, thighs: Double) {
        val item = BodyCircumference(
            dateString = "Nov 8",
            waistCm = waist,
            chestCm = chest,
            armsCm = arms,
            thighsCm = thighs
        )
        _circumferences.update { it + item }
    }

    // Reminders
    private val _reminders = MutableStateFlow(PresetFitnessData.defaultReminders)
    val reminders: StateFlow<List<CustomReminder>> = _reminders.asStateFlow()

    fun toggleReminder(id: String) {
        _reminders.update { list ->
            list.map {
                if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
            }
        }
    }

    fun addReminder(title: String, category: ReminderCategory, time: String, days: String) {
        val newReminder = CustomReminder(
            title = title,
            category = category,
            timeLabel = time,
            daysLabel = days,
            isEnabled = true
        )
        _reminders.update { it + newReminder }
    }
}
