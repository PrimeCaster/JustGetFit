package com.example.data

import com.example.model.MotivationQuote
import kotlin.random.Random

object MotivationQuoteEngine {

    val dailyInspirationalQuotes = listOf(
        MotivationQuote(
            "We don't rise to the level of our expectations; we fall to the level of our training.",
            "Archilochus",
            "Discipline"
        ),
        MotivationQuote(
            "Small daily disciplines repeated consistently over time lead to monumental fitness transformations.",
            "Athletic Creed",
            "Improvement"
        ),
        MotivationQuote(
            "Take care of your body. It's the only place you have to live.",
            "Jim Rohn",
            "Health"
        ),
        MotivationQuote(
            "The body achieves what the mind believes.",
            "Napoleon Hill",
            "Mindset"
        ),
        MotivationQuote(
            "Action is the foundational key to all success. Track every rep, fuel every recovery.",
            "Pablo Picasso",
            "Consistency"
        ),
        MotivationQuote(
            "Strength does not come from physical capacity. It comes from an indomitable will.",
            "Mahatma Gandhi",
            "Strength"
        ),
        MotivationQuote(
            "You don't have to be extreme, just consistent. Your future physique is built today.",
            "Coach Insight",
            "Habit"
        )
    )

    val exerciseEncouragingCues = mapOf(
        "Barbell Back Squat" to listOf(
            "Drive the floor away through your mid-foot. Keep your chest proud!",
            "Brace your core like taking a punch. Explode out of the hole!",
            "Great depth! Stay upright and lock out the hips at the top."
        ),
        "Barbell Bench Press" to listOf(
            "Retract your scapula, dig your feet into the platform, and squeeze the bar!",
            "Control the descent, touch your sternum smoothly, and drive upward!",
            "Solid arc and leg drive! Every rep reinforces your strength foundation."
        ),
        "Conventional Deadlift" to listOf(
            "Pull the slack out of the bar, wedge your hips in, and drag it up your shins!",
            "Engage your lats, push the earth away, and lock your glutes at the apex!",
            "Powerful hinge mechanics! Pure total-body power."
        ),
        "Overhead Press" to listOf(
            "Squeeze your glutes, lock your ribcage down, and press right past your nose!",
            "Punch the ceiling and lock your shoulders firmly overhead!",
            "Strict form over ego weight. Outstanding overhead stability!"
        ),
        "Pull-Ups" to listOf(
            "Lead with your sternum, pull your elbows into your back pockets!",
            "Full range of motion: dead hang to chin over bar. Unmatched lat recruitment!",
            "Bodyweight mastery in action!"
        ),
        "Treadmill" to listOf(
            "Find your aerobic cadence, breathe rhythmically, and relax your shoulders!",
            "Strong forward posture! Your cardiovascular engine is leveling up.",
            "Every kilometer logged builds stamina and cardiac efficiency."
        ),
        "Rowing Machine (Erg)" to listOf(
            "Legs, body, arms on the drive; arms, body, legs on the recovery!",
            "Keep the ratio 1:2 — explosive drive, patient recovery. Smooth power curve!",
            "Dominating the ergometer! Peak full-body endurance."
        ),
        "Elliptical" to listOf(
            "Engage your glutes on the downward push, keep your posture tall!",
            "Low-impact, high metabolic burn. Fantastic steady-state rhythm!"
        )
    )

    val mealLoggingAffirmations = listOf(
        "Nutritious choices today lay the brickwork for tomorrow's recovery!",
        "Macros locked in! You're feeding your muscles and optimizing vitality.",
        "Mindful eating builds metabolic health. Great job tracking your nutrition!",
        "Consistency in the kitchen multiplies the results in the gym.",
        "Clean fuel for peak performance. Your body thanks you!"
    )

    val workoutCompletionCelebrations = listOf(
        "Workout completed with authority! Rest, rehydrate, and recover like an athlete.",
        "Another deposit made in the bank of strength and longevity!",
        "Outstanding effort! Progressive overload conquered.",
        "You showed up, put in the work, and raised your ceiling today!"
    )

    fun getRandomDailyQuote(): MotivationQuote {
        return dailyInspirationalQuotes[Random.nextInt(dailyInspirationalQuotes.size)]
    }

    fun getExerciseCue(exerciseName: String): String {
        val cues = exerciseEncouragingCues[exerciseName]
        return if (!cues.isNullOrEmpty()) {
            cues[Random.nextInt(cues.size)]
        } else {
            "Maintain strict form, breathe through the rep, and own every inch of the motion!"
        }
    }

    fun getRandomMealAffirmation(): String {
        return mealLoggingAffirmations[Random.nextInt(mealLoggingAffirmations.size)]
    }

    fun getRandomWorkoutCelebration(): String {
        return workoutCompletionCelebrations[Random.nextInt(workoutCompletionCelebrations.size)]
    }
}
