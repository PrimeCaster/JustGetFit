package com.example.data

import com.example.model.*
import com.example.util.FitnessDateUtils

object PresetFitnessData {

    val exerciseCatalog = listOf(
        // CHEST
        StrengthExercise(
            name = "Flat Dumbbell Bench Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Retract scapulae, lower dumbbells until deep stretch at chest, press and converge at top.",
            encouragingCue = "Control the descent, explode through the chest press!"
        ),
        StrengthExercise(
            name = "Incline Dumbbell Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 75,
            techniqueTip = "30-degree incline, elbows at 45 degrees, squeeze upper clavicular pecs.",
            encouragingCue = "Solid stretch at bottom, squeeze upper chest at top!"
        ),
        StrengthExercise(
            name = "Incline Machine Chest Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Adjust seat height so handles align with mid-to-upper chest. Lock back against pad.",
            encouragingCue = "Drive through the handles! Continuous tension builds upper chest thickness."
        ),
        StrengthExercise(
            name = "Pec Deck Machine Flyes",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Slight bend in elbows, bring pads together and pause for 1 second peak contraction.",
            encouragingCue = "Squeeze like hugging a tree! Peak chest isolation."
        ),
        StrengthExercise(
            name = "Cable Chest Crossover Flyes",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Set pulleys at shoulder or high position, press down and across sternum.",
            encouragingCue = "Feel that deep outer chest stretch, lock down the contraction!"
        ),
        StrengthExercise(
            name = "Barbell Bench Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Plant feet firmly, touch mid-chest, drive upward with full body tightness.",
            encouragingCue = "Lock the lats, drive your feet through the floor!"
        ),

        // BACK
        StrengthExercise(
            name = "Lat Pulldowns",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Wide or neutral grip, slight lean back, pull bar to upper chest leading with elbows.",
            encouragingCue = "Pull your elbows into your back pockets! Flare those lats!"
        ),
        StrengthExercise(
            name = "Seated Cable Rows",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Chest tall, pull V-bar or wide handle to navel, squeeze rhomboids hard.",
            encouragingCue = "Retract and squeeze the shoulder blades! Pure back density."
        ),
        StrengthExercise(
            name = "Straight Arm Cable Pulldowns",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Slight hip hinge, arms locked straight, sweep bar in an arc down to thighs.",
            encouragingCue = "Isolate the lats without bicep involvement! Smooth arc."
        ),
        StrengthExercise(
            name = "Dumbbell Rows",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Hand on bench, flat spine, pull dumbbell up towards hip pocket.",
            encouragingCue = "Drive the elbow straight up! Unmatched unilateral lat recruitment."
        ),
        StrengthExercise(
            name = "Chest-Supported Machine Row",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Chest pinned to pad, pull handles back without using lower back momentum.",
            encouragingCue = "Pure upper back engagement with zero spinal fatigue!"
        ),
        StrengthExercise(
            name = "Assisted Pull-Up Machine",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Knees on pad, full dead-hang stretch, pull chin cleanly over handles.",
            encouragingCue = "Full range of motion builds authentic vertical pulling power!"
        ),
        StrengthExercise(
            name = "Conventional Deadlift",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 150,
            techniqueTip = "Pack lats, hinge hips back, squeeze glutes at the lockout.",
            encouragingCue = "Push the earth away! Unstoppable posterior chain power!"
        ),

        // SHOULDERS & TRAPS
        StrengthExercise(
            name = "Dumbbell Shoulder Press",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Upright bench, press dumbbells in a slight inward arc overhead without clanking.",
            encouragingCue = "Press with authority! Boulder delts in the making."
        ),
        StrengthExercise(
            name = "Seated Machine Shoulder Press",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Seat at chin height, press guided handles smoothly to lockout.",
            encouragingCue = "Safe, heavy delt overload! Squeeze at the top."
        ),
        StrengthExercise(
            name = "Dumbbell Lateral Raises",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Lead with elbows, raise arms in scapular plane to parallel with floor.",
            encouragingCue = "Build that 3D shoulder width! Slow, controlled descent."
        ),
        StrengthExercise(
            name = "Cable Lateral Raises",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Cable set at wrist level, pull across body for constant resistance curve.",
            encouragingCue = "Zero dead zones! Constant tension right on the side delt."
        ),
        StrengthExercise(
            name = "Reverse Pec Deck (Rear Delt Fly)",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Chest against pad, arms horizontal, sweep outwards focusing on rear deltoids.",
            encouragingCue = "Carve out those rear delts! Essential for shoulder posture."
        ),
        StrengthExercise(
            name = "Cable Face Pulls",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rope set at eye level, pull to nose while externally rotating hands back.",
            encouragingCue = "Pull apart the rope! Bulletproof rotator cuffs and upper back."
        ),
        StrengthExercise(
            name = "Dumbbell Shrugs",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Elevate shoulders straight towards ears, pause for 1 second, lower smoothly.",
            encouragingCue = "Heavy trap contraction! Do not roll your shoulders."
        ),

        // LEGS
        StrengthExercise(
            name = "Leg Press Machine (45 Degree)",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 120,
            techniqueTip = "Feet mid-platform, lower weight sled until 90-degree knee bend, press through heels.",
            encouragingCue = "Massive quad and glute drive! Don't lock knees out aggressively."
        ),
        StrengthExercise(
            name = "Leg Extension Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Knees aligned with machine pivot, kick out and squeeze teardrop quad for 1 sec.",
            encouragingCue = "Lock out and hold the peak quad burn! Pure isolation."
        ),
        StrengthExercise(
            name = "Seated Leg Curl Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 75,
            techniqueTip = "Thigh pad firmly clamped, curl roller down toward seat, slow 3-sec return.",
            encouragingCue = "Hamstrings locked in! Controlled eccentric for bulletproof knees."
        ),
        StrengthExercise(
            name = "Lying Leg Curl Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 75,
            techniqueTip = "Hips pinned to bench, curl ankles towards glutes without lifting pelvis.",
            encouragingCue = "Deep hamstring recruitment! Own the negative."
        ),
        StrengthExercise(
            name = "Calf Raise Machine (Standing/Seated)",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Balls of feet on block, deep ankle stretch at bottom, press high onto big toes.",
            encouragingCue = "Full ankle flexion and extension! Build diamond calves."
        ),
        StrengthExercise(
            name = "Hack Squat Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 120,
            techniqueTip = "Back against angled pad, feet slightly forward, sink deep into hips and quads.",
            encouragingCue = "Ultimate quad builder with locked-in spinal safety!"
        ),
        StrengthExercise(
            name = "Romanian Deadlift",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Soft knees, push hips backward to stretch hamstrings, keep dumbbells against legs.",
            encouragingCue = "Feel that deep hamstring tension and snap hips through!"
        ),
        StrengthExercise(
            name = "Walking Dumbbell Lunges",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Step forward, drop back knee gently towards floor, drive through front heel.",
            encouragingCue = "Unilateral balance and glute power with every step!"
        ),
        StrengthExercise(
            name = "Barbell Back Squat",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Descend until hip crease is below knee caps. Drive mid-foot into ground.",
            encouragingCue = "Drive the floor away! Proud chest, strong lockout!"
        ),

        // ARMS (TRICEPS, BICEPS, FOREARMS)
        StrengthExercise(
            name = "Cable Triceps Pushdowns",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Elbows pinned to sides, push straight or V-bar down and lock triceps out.",
            encouragingCue = "Lock out and feel that tricep horseshoe burn!"
        ),
        StrengthExercise(
            name = "Overhead Triceps Cable Extension",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rope on low or mid cable, lean forward, extend forearms overhead.",
            encouragingCue = "Full stretch on the long head of the tricep! Maximum arm size."
        ),
        StrengthExercise(
            name = "Skull Crushers (EZ-Bar)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Lower bar towards forehead or crown, keep upper arms vertical, extend up.",
            encouragingCue = "Strict elbow pivot! Massive triceps builder."
        ),
        StrengthExercise(
            name = "Dumbbell Bicep Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Pin elbows to ribs, supinate wrists at peak contraction.",
            encouragingCue = "Peak tension on the bicep! Don't swing the weights."
        ),
        StrengthExercise(
            name = "Dumbbell Hammer Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Neutral grip (palms facing each other), curl towards shoulders for brachialis.",
            encouragingCue = "Build forearm and bicep thickness with heavy hammer curls!"
        ),
        StrengthExercise(
            name = "Cable Bicep Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Straight bar on low cable, keep continuous tension through the entire curve.",
            encouragingCue = "Constant tension pump! Squeeze at the top."
        ),
        StrengthExercise(
            name = "Preacher Curl Machine",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Armpits snug on pad, extend arms without hyperextending, curl to top.",
            encouragingCue = "Strict bicep isolation with zero cheating!"
        ),
        StrengthExercise(
            name = "Reverse Cable Curls (Forearms)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Overhand pronated grip, curl bar to recruit brachioradialis and extensor muscles.",
            encouragingCue = "Ironclad grip and forearm vascularity!"
        ),
        StrengthExercise(
            name = "Wrist Curls (Dumbbell/Machine)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 45,
            techniqueTip = "Forearms resting on thighs or bench, curl wrists upward to full flexion.",
            encouragingCue = "Popeye forearm endurance and grip strength!"
        ),

        // CORE & CARDIO
        StrengthExercise(
            name = "Light Planks & Core Bracing",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Elbows under shoulders, lock glutes and quads, pull belly button to spine.",
            encouragingCue = "Solid pillar of core stability!"
        ),
        StrengthExercise(
            name = "Hanging Leg Raise",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Tilt pelvis up towards chin to fully recruit lower abdominals.",
            encouragingCue = "Total core control! Resisting momentum builds iron abs."
        ),
        StrengthExercise(
            name = "Inclined Treadmill Walking (60+ mins)",
            category = ExerciseCategory.CARDIO,
            equipmentType = EquipmentType.CARDIO_MACHINE,
            defaultRestSeconds = 0,
            techniqueTip = "Set incline to 10-15%, speed at 4.5-5.5 km/h, steady state fat burn.",
            encouragingCue = "Zone 2 aerobic base building! Low impact, high metabolic reward."
        )
    )

    // Option 1: Push / Pull / Legs Split
    val option1PPLSchedule = listOf(
        SplitDaySchedule(
            dayName = "Monday",
            focusTitle = "Push (Chest, Shoulders, Triceps)",
            primaryMuscle = ExerciseCategory.CHEST,
            primaryMovementsSummary = "Dumbbell Bench Press, Shoulder Press, Triceps Pushdowns"
        ),
        SplitDaySchedule(
            dayName = "Tuesday",
            focusTitle = "Pull (Back, Biceps)",
            primaryMuscle = ExerciseCategory.BACK,
            primaryMovementsSummary = "Lat Pulldowns, Seated Cable Rows, Bicep Curls"
        ),
        SplitDaySchedule(
            dayName = "Wednesday",
            focusTitle = "Legs",
            primaryMuscle = ExerciseCategory.LEGS,
            primaryMovementsSummary = "Leg Press, Leg Extensions, Calf Raises"
        ),
        SplitDaySchedule(
            dayName = "Thursday",
            focusTitle = "Push (Chest, Shoulders, Triceps)",
            primaryMuscle = ExerciseCategory.CHEST,
            primaryMovementsSummary = "Incline Dumbbell Press, Lateral Raises, Overhead Triceps Extensions"
        ),
        SplitDaySchedule(
            dayName = "Friday",
            focusTitle = "Pull (Back, Biceps)",
            primaryMuscle = ExerciseCategory.BACK,
            primaryMovementsSummary = "Dumbbell Rows, Face Pulls, Hammer Curls"
        ),
        SplitDaySchedule(
            dayName = "Saturday",
            focusTitle = "Legs & Core",
            primaryMuscle = ExerciseCategory.LEGS,
            primaryMovementsSummary = "Lunges, Leg Curls, Light Planks"
        ),
        SplitDaySchedule(
            dayName = "Sunday",
            focusTitle = "Rest & Mobility",
            primaryMuscle = ExerciseCategory.CARDIO,
            primaryMovementsSummary = "Complete rest or light walking",
            isRestDay = true
        )
    )

    // Option 2: Isolation Split (One Muscle Per Day)
    val option2IsolationSchedule = listOf(
        SplitDaySchedule(
            dayName = "Monday",
            focusTitle = "Chest Focus",
            primaryMuscle = ExerciseCategory.CHEST,
            primaryMovementsSummary = "Flat & Incline Dumbbell Press, Pec Deck Flyes"
        ),
        SplitDaySchedule(
            dayName = "Tuesday",
            focusTitle = "Back Focus",
            primaryMuscle = ExerciseCategory.BACK,
            primaryMovementsSummary = "Lat Pulldowns, Cable Rows, Straight Arm Pulldowns"
        ),
        SplitDaySchedule(
            dayName = "Wednesday",
            focusTitle = "Shoulders Focus",
            primaryMuscle = ExerciseCategory.SHOULDERS,
            primaryMovementsSummary = "Shoulder Press, Lateral Raises, Reverse Pec Deck"
        ),
        SplitDaySchedule(
            dayName = "Thursday",
            focusTitle = "Legs Focus",
            primaryMuscle = ExerciseCategory.LEGS,
            primaryMovementsSummary = "Leg Press, Leg Extensions, Seated Leg Curls"
        ),
        SplitDaySchedule(
            dayName = "Friday",
            focusTitle = "Arms (Bi & Tri)",
            primaryMuscle = ExerciseCategory.ARMS,
            primaryMovementsSummary = "Bicep Curls, Skull Crushers, Cable Pushdowns"
        ),
        SplitDaySchedule(
            dayName = "Saturday",
            focusTitle = "Cardio Only (60+ mins)",
            primaryMuscle = ExerciseCategory.CARDIO,
            primaryMovementsSummary = "60+ mins Inclined Treadmill",
            isCardioOnly = true
        ),
        SplitDaySchedule(
            dayName = "Sunday",
            focusTitle = "Rest & Recovery",
            primaryMuscle = ExerciseCategory.CARDIO,
            primaryMovementsSummary = "Complete rest",
            isRestDay = true
        )
    )

    // Option 3: Two Muscles + Full Arms
    val option3TwoMusclesSchedule = listOf(
        SplitDaySchedule(
            dayName = "Monday",
            focusTitle = "Chest & Triceps",
            primaryMuscle = ExerciseCategory.CHEST,
            primaryMovementsSummary = "Flat Dumbbell Press, Incline Machine Press, Cable Triceps Pushdowns"
        ),
        SplitDaySchedule(
            dayName = "Tuesday",
            focusTitle = "Back & Biceps",
            primaryMuscle = ExerciseCategory.BACK,
            primaryMovementsSummary = "Lat Pulldowns, Seated Cable Rows, Dumbbell Hammer Curls"
        ),
        SplitDaySchedule(
            dayName = "Wednesday",
            focusTitle = "Shoulders & Traps",
            primaryMuscle = ExerciseCategory.SHOULDERS,
            primaryMovementsSummary = "Seated Dumbbell Shoulder Press, Lateral Raises, Face Pulls, Shrugs"
        ),
        SplitDaySchedule(
            dayName = "Thursday",
            focusTitle = "Legs",
            primaryMuscle = ExerciseCategory.LEGS,
            primaryMovementsSummary = "Leg Press, Leg Extensions, Seated Leg Curls"
        ),
        SplitDaySchedule(
            dayName = "Friday",
            focusTitle = "Full Arms (Bi/Tri/Forearms)",
            primaryMuscle = ExerciseCategory.ARMS,
            primaryMovementsSummary = "Barbell/Cable Curls, Skull Crushers, Reverse Curls, Wrist Curls"
        ),
        SplitDaySchedule(
            dayName = "Saturday",
            focusTitle = "Active Recovery & Cardio",
            primaryMuscle = ExerciseCategory.CARDIO,
            primaryMovementsSummary = "45-60 mins Inclined Treadmill Cardio only",
            isCardioOnly = true
        ),
        SplitDaySchedule(
            dayName = "Sunday",
            focusTitle = "Rest, Sleep & Prep",
            primaryMuscle = ExerciseCategory.CARDIO,
            primaryMovementsSummary = "Complete rest, focus on sleep and meal prep",
            isRestDay = true
        )
    )

    fun createInitialWorkoutSession(): WorkoutSession {
        val bench = exerciseCatalog[0] // Flat DB Bench
        val shoulderPress = exerciseCatalog[13] // DB Shoulder Press
        val pushdowns = exerciseCatalog[29] // Cable Pushdowns

        return WorkoutSession(
            title = "Push (Chest, Shoulders, Triceps)",
            splitCategory = "Push",
            dateString = "Today",
            durationMinutes = 55,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = bench,
                    gymMachine = gymMachinesCatalog[0], // Chest Press Machine
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 32.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 34.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 36.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = shoulderPress,
                    gymMachine = gymMachinesCatalog[4], // Shoulder Press Machine
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 22.0, reps = 10, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 24.0, reps = 8, rpe = 9, isCompleted = false),
                        WorkoutSet(3, weightKg = 24.0, reps = 8, rpe = 9, isCompleted = false)
                    )
                ),
                ExerciseLog(
                    exercise = pushdowns,
                    gymMachine = gymMachinesCatalog[22], // Triceps Extension Machine
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 25.0, reps = 12, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 30.0, reps = 10, rpe = 9, isCompleted = false),
                        WorkoutSet(3, weightKg = 30.0, reps = 10, rpe = 9, isCompleted = false)
                    )
                )
            ),
            isCompleted = false,
            totalVolumeKg = 1880.0
        )
    }

    fun createInitialWorkoutsHistory(): Map<String, WorkoutSession> {
        val todayStr = FitnessDateUtils.getTodayDateString()
        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        val twoDaysAgoStr = FitnessDateUtils.getDateDaysAgo(2)
        val fourDaysAgoStr = FitnessDateUtils.getDateDaysAgo(4)
        val oneWeekAgoStr = FitnessDateUtils.getDateDaysAgo(7)
        val twoWeeksAgoStr = FitnessDateUtils.getDateDaysAgo(14)

        val todayWorkout = createInitialWorkoutSession()

        val yesterdayWorkout = WorkoutSession(
            title = "Legs (Quads, Hamstrings & Calves)",
            splitCategory = "Legs",
            dateString = yesterdayStr,
            durationMinutes = 65,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Press", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Press", ignoreCase = true) } ?: gymMachinesCatalog[11],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 180.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 200.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 220.0, reps = 8, rpe = 9, isCompleted = true),
                        WorkoutSet(4, weightKg = 240.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Extension", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Extension", ignoreCase = true) } ?: gymMachinesCatalog[12],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 60.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 65.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 70.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Calf", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Calf", ignoreCase = true) } ?: gymMachinesCatalog[17],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 75.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 80.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 85.0, reps = 10, rpe = 9, isCompleted = true),
                        WorkoutSet(4, weightKg = 90.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 8450.0
        )

        val twoDaysAgoWorkout = WorkoutSession(
            title = "Pull (Back & Biceps)",
            splitCategory = "Pull",
            dateString = twoDaysAgoStr,
            durationMinutes = 55,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Lat Pulldown", ignoreCase = true) } ?: exerciseCatalog[6],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Lat Pulldown", ignoreCase = true) } ?: gymMachinesCatalog[6],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 60.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 65.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 70.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Cable Row", ignoreCase = true) } ?: exerciseCatalog[7],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Cable Row", ignoreCase = true) } ?: gymMachinesCatalog[7],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 55.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 60.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 65.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bicep", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Bicep", ignoreCase = true) } ?: gymMachinesCatalog[19],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 14.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 16.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 16.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 4120.0
        )

        val fourDaysAgoWorkout = WorkoutSession(
            title = "Shoulders & Core Power",
            splitCategory = "Shoulders",
            dateString = fourDaysAgoStr,
            durationMinutes = 50,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Shoulder Press", ignoreCase = true) } ?: exerciseCatalog[13],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Shoulder Press", ignoreCase = true) } ?: gymMachinesCatalog[4],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 45.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 50.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 55.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Lateral", ignoreCase = true) } ?: exerciseCatalog[14],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Lateral", ignoreCase = true) } ?: gymMachinesCatalog[5],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 35.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 40.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 40.0, reps = 12, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 3890.0
        )

        val oneWeekAgoWorkout = WorkoutSession(
            title = "Chest & Triceps Hypertrophy",
            splitCategory = "Push",
            dateString = oneWeekAgoStr,
            durationMinutes = 60,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Incline", ignoreCase = true) } ?: exerciseCatalog[1],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Incline Chest", ignoreCase = true) } ?: gymMachinesCatalog[1],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 70.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 75.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 80.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Tricep", ignoreCase = true) } ?: exerciseCatalog[29],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Triceps", ignoreCase = true) } ?: gymMachinesCatalog[20],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 40.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 45.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 50.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 4680.0
        )

        val twoWeeksAgoWorkout = WorkoutSession(
            title = "Legs & Posterior Power",
            splitCategory = "Legs",
            dateString = twoWeeksAgoStr,
            durationMinutes = 60,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Hack Squat", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Hack Squat", ignoreCase = true) } ?: gymMachinesCatalog[14],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 100.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 120.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 140.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 6720.0
        )

        return mapOf(
            todayStr to todayWorkout,
            "Today" to todayWorkout,
            yesterdayStr to yesterdayWorkout,
            "Yesterday" to yesterdayWorkout,
            twoDaysAgoStr to twoDaysAgoWorkout,
            fourDaysAgoStr to fourDaysAgoWorkout,
            oneWeekAgoStr to oneWeekAgoWorkout,
            twoWeeksAgoStr to twoWeeksAgoWorkout
        )
    }

    val upcomingScheduledWorkouts = listOf(
        ScheduledWorkout(
            dayOfWeek = "Mon",
            date = "Nov 10",
            title = "Push (Chest, Shoulders, Triceps)",
            focusArea = "Flat DB Bench, Shoulder Press, Cable Pushdowns",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Tue",
            date = "Nov 11",
            title = "Pull (Back, Biceps)",
            focusArea = "Lat Pulldowns, Seated Cable Rows, Bicep Curls",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Wed",
            date = "Nov 12",
            title = "Legs (Quad & Calves)",
            focusArea = "Leg Press, Leg Extensions, Calf Raises",
            estimatedMinutes = 60,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Thu",
            date = "Nov 13",
            title = "Push Hypertrophy",
            focusArea = "Incline DB Press, Lateral Raises, Overhead Extensions",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Fri",
            date = "Nov 14",
            title = "Pull Density & Traps",
            focusArea = "Dumbbell Rows, Face Pulls, Hammer Curls",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        )
    )

    val commonFoodsCatalog = listOf(
        FoodItem(
            name = "Skinless Chicken Breast",
            brandOrCategory = "Lean Protein",
            servingUnit = "150g grilled",
            caloriesPerServing = 247,
            proteinGrams = 46.5,
            carbsGrams = 0.0,
            fatGrams = 5.4
        ),
        FoodItem(
            name = "Rolled Whole Oats",
            brandOrCategory = "Complex Carbs",
            servingUnit = "80g dry",
            caloriesPerServing = 303,
            proteinGrams = 10.8,
            carbsGrams = 54.0,
            fatGrams = 5.2,
            fiberGrams = 8.4
        ),
        FoodItem(
            name = "Whey Protein Isolate",
            brandOrCategory = "Supplements",
            servingUnit = "1 scoop (30g)",
            caloriesPerServing = 120,
            proteinGrams = 26.0,
            carbsGrams = 1.5,
            fatGrams = 0.5
        ),
        FoodItem(
            name = "Large Whole Eggs",
            brandOrCategory = "Protein & Fats",
            servingUnit = "2 whole eggs (100g)",
            caloriesPerServing = 143,
            proteinGrams = 12.6,
            carbsGrams = 0.7,
            fatGrams = 9.5
        ),
        FoodItem(
            name = "Wild Atlantic Salmon",
            brandOrCategory = "Healthy Fats & Protein",
            servingUnit = "150g fillet",
            caloriesPerServing = 280,
            proteinGrams = 34.0,
            carbsGrams = 0.0,
            fatGrams = 15.0
        ),
        FoodItem(
            name = "Cooked Jasmine Rice",
            brandOrCategory = "Carbohydrates",
            servingUnit = "150g cooked",
            caloriesPerServing = 195,
            proteinGrams = 4.2,
            carbsGrams = 42.0,
            fatGrams = 0.4
        ),
        FoodItem(
            name = "Greek Yogurt (0% Fat)",
            brandOrCategory = "Dairy Protein",
            servingUnit = "200g container",
            caloriesPerServing = 120,
            proteinGrams = 20.0,
            carbsGrams = 7.0,
            fatGrams = 0.0
        ),
        FoodItem(
            name = "Ripe Hass Avocado",
            brandOrCategory = "Monounsaturated Fat",
            servingUnit = "1/2 medium (75g)",
            caloriesPerServing = 120,
            proteinGrams = 1.5,
            carbsGrams = 6.0,
            fatGrams = 11.0,
            fiberGrams = 5.0
        ),
        FoodItem(
            name = "Fresh Blueberries",
            brandOrCategory = "Antioxidant Fruit",
            servingUnit = "100g bowl",
            caloriesPerServing = 57,
            proteinGrams = 0.7,
            carbsGrams = 14.5,
            fatGrams = 0.3,
            fiberGrams = 2.4
        ),
        FoodItem(
            name = "Natural Peanut Butter",
            brandOrCategory = "Healthy Fats",
            servingUnit = "2 tbsp (32g)",
            caloriesPerServing = 190,
            proteinGrams = 8.0,
            carbsGrams = 7.0,
            fatGrams = 16.0
        ),
        FoodItem(
            name = "Roasted Sweet Potato",
            brandOrCategory = "Complex Carbs",
            servingUnit = "150g baked",
            caloriesPerServing = 135,
            proteinGrams = 3.0,
            carbsGrams = 31.0,
            fatGrams = 0.2,
            fiberGrams = 4.5
        )
    )

    val curatedRecipes = listOf(
        Recipe(
            title = "Anabolic Power Protein Oatmeal",
            description = "Warm steel-cut oats whipped with whey protein, chia seeds, and fresh berries.",
            prepTimeMinutes = 8,
            calories = 480,
            proteinG = 38.0,
            carbsG = 62.0,
            fatG = 8.5,
            ingredients = listOf(
                "80g Rolled Oats",
                "1 Scoop Vanilla Whey Isolate",
                "150ml Unsweetened Almond Milk",
                "50g Fresh Blueberries",
                "1 tbsp Ground Chia Seeds"
            ),
            instructions = listOf(
                "Microwave oats and almond milk for 2 minutes until creamy.",
                "Stir in whey protein after heating to keep texture smooth.",
                "Top with fresh blueberries and chia seeds."
            ),
            tags = listOf("High Protein", "Breakfast", "Pre-Workout")
        ),
        Recipe(
            title = "Grilled Chimichurri Chicken Bowl",
            description = "Charred chicken breast with cilantro chimichurri, jasmine rice, and charred corn.",
            prepTimeMinutes = 20,
            calories = 540,
            proteinG = 52.0,
            carbsG = 56.0,
            fatG = 12.0,
            ingredients = listOf(
                "180g Boneless Chicken Breast",
                "150g Cooked Jasmine Rice",
                "1/4 Hass Avocado sliced",
                "Fresh Chimichurri (Parsley, Garlic, Olive Oil)",
                "Lime wedge"
            ),
            instructions = listOf(
                "Season chicken with smoked paprika, sea salt, and cumin.",
                "Grill on high heat for 6 minutes each side.",
                "Slice over jasmine rice and drizzle fresh chimichurri."
            ),
            tags = listOf("Lean Muscle", "Lunch", "Meal Prep")
        ),
        Recipe(
            title = "Crisp Salmon & Sweet Potato Mash",
            description = "Pan-seared Atlantic salmon fillet with creamy roasted sweet potato and steamed asparagus.",
            prepTimeMinutes = 25,
            calories = 580,
            proteinG = 42.0,
            carbsG = 44.0,
            fatG = 22.0,
            ingredients = listOf(
                "160g Atlantic Salmon Fillet",
                "180g Baked Sweet Potato",
                "100g Steamed Asparagus spears",
                "1 tsp Olive oil & Lemon zest"
            ),
            instructions = listOf(
                "Sear salmon skin-side down for 5 minutes until crispy.",
                "Mash baked sweet potato with a pinch of sea salt.",
                "Serve with steamed asparagus and fresh lemon."
            ),
            tags = listOf("Omega-3 Rich", "Dinner", "Recovery")
        )
    )

    fun createInitialMealEntries(): List<LoggedMealItem> {
        val todayStr = FitnessDateUtils.getTodayDateString()
        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        val twoDaysAgoStr = FitnessDateUtils.getDateDaysAgo(2)
        val oneWeekAgoStr = FitnessDateUtils.getDateDaysAgo(7)

        return listOf(
            // Today's Meals
            LoggedMealItem(
                foodItem = commonFoodsCatalog[1], // Oats
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 1.0,
                dateString = todayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[2], // Whey
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 1.0,
                dateString = todayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[0], // Chicken
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.2,
                dateString = todayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[5], // Rice
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.0,
                dateString = todayStr
            ),

            // Yesterday's Meals
            LoggedMealItem(
                foodItem = commonFoodsCatalog[3], // Eggs
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 2.0,
                dateString = yesterdayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[1], // Oats
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 1.0,
                dateString = yesterdayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[4], // Salmon
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.0,
                dateString = yesterdayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[7], // Sweet Potato
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.5,
                dateString = yesterdayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[0], // Chicken
                mealCategory = MealCategory.DINNER,
                numberOfServings = 1.5,
                dateString = yesterdayStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[8], // Greek Yogurt
                mealCategory = MealCategory.SNACKS,
                numberOfServings = 1.0,
                dateString = yesterdayStr
            ),

            // 2 Days Ago Meals
            LoggedMealItem(
                foodItem = commonFoodsCatalog[2], // Whey
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 1.5,
                dateString = twoDaysAgoStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[0], // Chicken
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.0,
                dateString = twoDaysAgoStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[5], // Rice
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.2,
                dateString = twoDaysAgoStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[4], // Salmon
                mealCategory = MealCategory.DINNER,
                numberOfServings = 1.0,
                dateString = twoDaysAgoStr
            ),

            // 1 Week Ago Meals
            LoggedMealItem(
                foodItem = commonFoodsCatalog[3], // Eggs
                mealCategory = MealCategory.BREAKFAST,
                numberOfServings = 2.0,
                dateString = oneWeekAgoStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[0], // Chicken
                mealCategory = MealCategory.LUNCH,
                numberOfServings = 1.5,
                dateString = oneWeekAgoStr
            ),
            LoggedMealItem(
                foodItem = commonFoodsCatalog[7], // Sweet Potato
                mealCategory = MealCategory.DINNER,
                numberOfServings = 1.0,
                dateString = oneWeekAgoStr
            )
        )
    }

    fun createInitialWaterIntake(): Map<String, Int> {
        val todayStr = FitnessDateUtils.getTodayDateString()
        val yesterdayStr = FitnessDateUtils.getDateDaysAgo(1)
        val twoDaysAgoStr = FitnessDateUtils.getDateDaysAgo(2)
        val fourDaysAgoStr = FitnessDateUtils.getDateDaysAgo(4)
        val oneWeekAgoStr = FitnessDateUtils.getDateDaysAgo(7)

        return mapOf(
            todayStr to 2250,
            "Today" to 2250,
            yesterdayStr to 3200,
            "Yesterday" to 3200,
            twoDaysAgoStr to 2800,
            fourDaysAgoStr to 3000,
            oneWeekAgoStr to 3400
        )
    }

    val historicalWeightData = listOf(
        WeightRecord(dateString = "Sep 1", weightKg = 86.5, bodyFatPercentage = 23.5),
        WeightRecord(dateString = "Sep 15", weightKg = 85.1, bodyFatPercentage = 22.8),
        WeightRecord(dateString = "Oct 1", weightKg = 83.7, bodyFatPercentage = 21.9),
        WeightRecord(dateString = "Oct 15", weightKg = 82.2, bodyFatPercentage = 20.8),
        WeightRecord(dateString = "Nov 1", weightKg = 80.4, bodyFatPercentage = 19.8),
        WeightRecord(dateString = "Nov 8", weightKg = 79.2, bodyFatPercentage = 19.1)
    )

    val initialProgressPhotos = listOf(
        ProgressPhotoItem(
            dateString = "Sep 1, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 86.5,
            title = "Starting Baseline",
            notes = "Day 1 of body recomposition. Prioritizing strength and 500 kcal deficit.",
            accentHueSeed = 0
        ),
        ProgressPhotoItem(
            dateString = "Oct 1, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 83.7,
            title = "Month 1 Milestone",
            notes = "Waist down 3.5cm. Visible shoulder and quad definition emerging.",
            accentHueSeed = 1
        ),
        ProgressPhotoItem(
            dateString = "Nov 8, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 79.2,
            title = "Current Progress (Week 10)",
            notes = "Abs clearly visible, bench 1RM reached 105kg.",
            accentHueSeed = 2
        )
    )

    val initialCircumferences = listOf(
        BodyCircumference(dateString = "Sep 1", waistCm = 94.0, chestCm = 104.0, armsCm = 37.0, thighsCm = 61.0),
        BodyCircumference(dateString = "Oct 1", waistCm = 90.5, chestCm = 105.0, armsCm = 37.5, thighsCm = 61.5),
        BodyCircumference(dateString = "Nov 8", waistCm = 85.0, chestCm = 106.5, armsCm = 38.5, thighsCm = 62.0)
    )

    val defaultReminders = listOf(
        CustomReminder(
            title = "Morning Hydration (500ml)",
            category = ReminderCategory.HYDRATION,
            timeLabel = "07:30 AM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Scheduled Workout Session",
            category = ReminderCategory.WORKOUT,
            timeLabel = "05:30 PM",
            daysLabel = "Mon, Tue, Thu, Fri, Sat",
            isEnabled = true
        ),
        CustomReminder(
            title = "Dinner & Macro Review",
            category = ReminderCategory.MEAL_LOG,
            timeLabel = "08:30 PM",
            daysLabel = "Everyday",
            isEnabled = true
        )
    )

    val initialCardioSessions = listOf(
        CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = "Yesterday",
            durationMinutes = 60,
            distanceKm = 6.5,
            caloriesBurned = 520,
            avgHeartRateBpm = 138,
            treadmillSpeedKmh = 5.2,
            treadmillInclinePercent = 12.0
        ),
        CardioSession(
            machineType = CardioMachineType.ROWING_MACHINE,
            dateString = "3 days ago",
            durationMinutes = 20,
            distanceKm = 4.8,
            caloriesBurned = 260,
            avgHeartRateBpm = 156,
            rowingSplitSeconds500m = 125,
            rowingStrokesPerMin = 28,
            rowingDamperSetting = 6,
            rowingWatts = 210
        ),
        CardioSession(
            machineType = CardioMachineType.ELLIPTICAL,
            dateString = "5 days ago",
            durationMinutes = 25,
            distanceKm = 4.1,
            caloriesBurned = 210,
            avgHeartRateBpm = 138,
            ellipticalResistanceLevel = 8,
            ellipticalStridesPerMin = 130
        )
    )

    val gymMachinesCatalog = listOf(
        // Free Weights & Dumbbells Option
        GymMachineInfo(
            machineName = "Free Dumbbells",
            everydayAlias = "Dumbbells / Free Weights",
            targetMuscle = "Full Body & All Muscle Groups",
            category = ExerciseCategory.ARMS,
            tip = "Versatile free-weight dumbbells for progressive overload, unilateral stability, and full range of motion."
        ),
        // Upper Body – Chest & Pushing
        GymMachineInfo(
            machineName = "Chest Press Machine",
            everydayAlias = "Seated Chest Press",
            targetMuscle = "Pectoralis Major, Anterior Deltoids & Triceps",
            category = ExerciseCategory.CHEST,
            tip = "Targets pectoralis major, anterior deltoids, and triceps with a fixed, guided pushing path."
        ),
        GymMachineInfo(
            machineName = "Incline Chest Press Machine",
            everydayAlias = "Upper Chest Machine",
            targetMuscle = "Upper Chest & Front Shoulders",
            category = ExerciseCategory.CHEST,
            tip = "Focuses on the upper chest (clavicular head) and front deltoids with an upward pressing angle."
        ),
        GymMachineInfo(
            machineName = "Decline Chest Press Machine",
            everydayAlias = "Lower Chest Machine",
            targetMuscle = "Lower Pectorals & Triceps",
            category = ExerciseCategory.CHEST,
            tip = "Emphasizes the lower portion of the pectoral muscles with reduced shoulder strain."
        ),
        GymMachineInfo(
            machineName = "Pec Deck Machine",
            everydayAlias = "Butterfly / Chest Fly Machine",
            targetMuscle = "Chest & Pecs",
            category = ExerciseCategory.CHEST,
            tip = "Isolates the chest muscles through a hugging/adduction motion without triceps takeover."
        ),
        GymMachineInfo(
            machineName = "Shoulder Press Machine",
            everydayAlias = "Seated Overhead Press Machine",
            targetMuscle = "Front & Lateral Deltoids",
            category = ExerciseCategory.SHOULDERS,
            tip = "Targets the front and lateral deltoids of the shoulders along a stable overhead path."
        ),
        GymMachineInfo(
            machineName = "Lateral Raise Machine",
            everydayAlias = "Side Delt Fly Station",
            targetMuscle = "Side Lateral Deltoids",
            category = ExerciseCategory.SHOULDERS,
            tip = "Isolates the side (lateral) deltoids with constant mechanical tension."
        ),

        // Upper Body – Back & Pulling
        GymMachineInfo(
            machineName = "Lat Pulldown Machine",
            everydayAlias = "Vertical Back Pull-Down Station",
            targetMuscle = "Lats, Upper Back & Biceps",
            category = ExerciseCategory.BACK,
            tip = "Works the latissimus dorsi (lats), upper back, and biceps via a vertical pulling motion."
        ),
        GymMachineInfo(
            machineName = "Seated Cable Row Machine",
            everydayAlias = "Low Row Cable Machine",
            targetMuscle = "Middle Back, Rhomboids, Traps & Biceps",
            category = ExerciseCategory.BACK,
            tip = "Targets the middle back, rhomboids, traps, and biceps via a horizontal pull."
        ),
        GymMachineInfo(
            machineName = "Assisted Pull-Up & Dip Machine",
            everydayAlias = "Gravitron Counterweight Station",
            targetMuscle = "Lats, Upper Back, Chest & Triceps",
            category = ExerciseCategory.BACK,
            tip = "Uses a counterweight knee pad or platform to assist with bodyweight pull-ups and dips."
        ),
        GymMachineInfo(
            machineName = "T-Bar Row Station",
            everydayAlias = "Landmine / Chest-Supported Row",
            targetMuscle = "Lats & Mid-Back Thickness",
            category = ExerciseCategory.BACK,
            tip = "Provides a semi-fixed leverage path for heavy back-building rows."
        ),
        GymMachineInfo(
            machineName = "Back Extension Machine",
            everydayAlias = "Hyperextension / Roman Chair",
            targetMuscle = "Lower Back, Erector Spinae & Glutes",
            category = ExerciseCategory.BACK,
            tip = "Strengthens the lower back, erector spinae, and glutes with controlled hip extension."
        ),

        // Lower Body – Legs
        GymMachineInfo(
            machineName = "Leg Press Machine",
            everydayAlias = "45-Degree Sled / Seated Leg Press",
            targetMuscle = "Quadriceps, Hamstrings & Glutes",
            category = ExerciseCategory.LEGS,
            tip = "A heavy compound machine targeting the quadriceps, hamstrings, and glutes."
        ),
        GymMachineInfo(
            machineName = "Leg Extension Machine",
            everydayAlias = "Quad Isolator Machine",
            targetMuscle = "Quadriceps",
            category = ExerciseCategory.LEGS,
            tip = "Isolates and builds the quadriceps at the knee joint with peak contraction."
        ),
        GymMachineInfo(
            machineName = "Seated Leg Curl Machine",
            everydayAlias = "Hamstring Curl Bench",
            targetMuscle = "Hamstrings",
            category = ExerciseCategory.LEGS,
            tip = "Isolates the hamstring muscles on the back of the thighs in a seated position."
        ),
        GymMachineInfo(
            machineName = "Lying Leg Curl Machine",
            everydayAlias = "Prone Hamstring Roller",
            targetMuscle = "Hamstrings",
            category = ExerciseCategory.LEGS,
            tip = "Isolates the hamstrings while lying face down for deep knee flexion."
        ),
        GymMachineInfo(
            machineName = "Hack Squat Machine",
            everydayAlias = "Angled Sled Squat",
            targetMuscle = "Quadriceps & Glutes",
            category = ExerciseCategory.LEGS,
            tip = "A sled-based machine that mimics a deep barbell squat while supporting the back and shoulders."
        ),
        GymMachineInfo(
            machineName = "Hip Abductor Machine",
            everydayAlias = "Outer Thigh Machine",
            targetMuscle = "Outer Thighs & Gluteus Medius",
            category = ExerciseCategory.LEGS,
            tip = "Targets the outer thighs and gluteus medius by opening the legs outwards against resistance."
        ),
        GymMachineInfo(
            machineName = "Hip Adductor Machine",
            everydayAlias = "Inner Thigh Machine",
            targetMuscle = "Inner Thigh Adductor Muscles",
            category = ExerciseCategory.LEGS,
            tip = "Targets the inner thigh adductor muscles by squeezing the legs inwards together."
        ),
        GymMachineInfo(
            machineName = "Standing Calf Raise Machine",
            everydayAlias = "Shoulder-Loaded Calf Block",
            targetMuscle = "Gastrocnemius & Soleus",
            category = ExerciseCategory.LEGS,
            tip = "Isolates the gastrocnemius of the lower leg under direct vertical load."
        ),
        GymMachineInfo(
            machineName = "Seated Calf Raise Machine",
            everydayAlias = "Knee-Pad Soleus Press",
            targetMuscle = "Soleus Calf Muscle",
            category = ExerciseCategory.LEGS,
            tip = "Isolates the soleus muscle of the lower leg with knees bent at 90 degrees."
        ),
        GymMachineInfo(
            machineName = "Glute-Ham Developer (GHD)",
            everydayAlias = "Hip Thrust / Glute Drive Machine",
            targetMuscle = "Glutes, Hamstrings & Posterior Chain",
            category = ExerciseCategory.LEGS,
            tip = "Specialized station for posterior chain, glute, and hamstring strength and hip extension."
        ),

        // Arms & Core
        GymMachineInfo(
            machineName = "Preacher Curl Machine",
            everydayAlias = "Bicep Curl Station / Scott Bench",
            targetMuscle = "Biceps",
            category = ExerciseCategory.ARMS,
            tip = "Isolates the biceps with complete elbow stabilization and constant tension."
        ),
        GymMachineInfo(
            machineName = "Triceps Extension Machine",
            everydayAlias = "Seated Triceps Pushdown Machine",
            targetMuscle = "Triceps",
            category = ExerciseCategory.ARMS,
            tip = "Isolates the triceps muscle group with guided elbow flexion and extension."
        ),
        GymMachineInfo(
            machineName = "Abdominal Crunch Machine",
            everydayAlias = "Core Roller / Seated Ab Machine",
            targetMuscle = "Rectus Abdominis",
            category = ExerciseCategory.CORE,
            tip = "Guided seated machine targeting the rectus abdominis through spinal flexion."
        ),
        GymMachineInfo(
            machineName = "Torso Rotation Machine",
            everydayAlias = "Rotary Torso / Oblique Machine",
            targetMuscle = "Obliques & Rotational Core",
            category = ExerciseCategory.CORE,
            tip = "Targets the obliques and rotational core strength with guided twisting motion."
        ),

        // Cable, Functional & Hybrid Stations
        GymMachineInfo(
            machineName = "Cable Crossover Machine",
            everydayAlias = "Functional Trainer / Dual Adjustable Pulley",
            targetMuscle = "Chest, Shoulders, Arms & Full Body",
            category = ExerciseCategory.CHEST,
            tip = "Dual adjustable pulley system that allows for a limitless variety of upper and lower body movements."
        ),
        GymMachineInfo(
            machineName = "Smith Machine",
            everydayAlias = "Barbell on Vertical Guided Rails",
            targetMuscle = "Chest, Quads, Shoulders & Back",
            category = ExerciseCategory.CHEST,
            tip = "A barbell fixed within steel guide rods for guided squats, bench presses, and shoulder presses with built-in safety hooks."
        ),
        GymMachineInfo(
            machineName = "Power Cage / Squat Rack",
            everydayAlias = "Half Rack with Spotter Bars",
            targetMuscle = "Full Body Compound Movements",
            category = ExerciseCategory.LEGS,
            tip = "Free-weight frame equipped with safety spotter arms and pull-up bars for squats and presses."
        ),
        GymMachineInfo(
            machineName = "Power Tower",
            everydayAlias = "Vertical Knee Raise (VKR) Station",
            targetMuscle = "Abdominals, Lats & Triceps",
            category = ExerciseCategory.CORE,
            tip = "Bodyweight station for hanging leg/knee raises, dips, and pull-ups."
        ),

        // Cardiovascular Equipment
        GymMachineInfo(
            machineName = "Treadmill",
            everydayAlias = "Motorized Running & Walking Belt",
            targetMuscle = "Cardiovascular System, Quads & Calves",
            category = ExerciseCategory.CARDIO,
            tip = "Adjustable speed and incline for steady-state running or HIIT sprints."
        ),
        GymMachineInfo(
            machineName = "Rowing Machine",
            everydayAlias = "Indoor Rowing Ergometer",
            targetMuscle = "Full Body Cardiovascular & Back",
            category = ExerciseCategory.CARDIO,
            tip = "Low-impact, full-body cardiovascular training with leg drive and back pull."
        ),
        GymMachineInfo(
            machineName = "Elliptical Trainer",
            everydayAlias = "Cross Trainer",
            targetMuscle = "Low-Impact Cardio & Full Body",
            category = ExerciseCategory.CARDIO,
            tip = "Zero-impact joint-friendly cardiovascular conditioning."
        ),
        GymMachineInfo(
            machineName = "Stationary Exercise Bike",
            everydayAlias = "Spin / Upright Cardio Bike",
            targetMuscle = "Legs & Cardiovascular Endurance",
            category = ExerciseCategory.CARDIO,
            tip = "Smooth flywheel resistance for aerobic capacity and quad stamina."
        ),
        GymMachineInfo(
            machineName = "Stair Climber",
            everydayAlias = "StepMill / Revolving Staircase",
            targetMuscle = "Glutes, Hamstrings, Calves & Heart Rate",
            category = ExerciseCategory.CARDIO,
            tip = "Continuous stair climbing for intense calorie burn and lower body conditioning."
        )
    )
}
