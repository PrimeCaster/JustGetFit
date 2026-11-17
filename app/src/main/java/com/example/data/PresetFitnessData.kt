package com.example.data

import com.example.model.*
import com.example.util.FitnessDateUtils

object PresetFitnessData {

    val exerciseCatalog = listOf(
        // CHEST
        StrengthExercise(
            name = "Barbell Bench Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Plant feet firmly, touch mid-chest, drive upward with full body tightness.",
            encouragingCue = "Lock the lats, drive your feet through the floor!"
        ),
        StrengthExercise(
            name = "Incline Barbell Bench Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Incline bench at 30 degrees, touch upper chest, press with authority.",
            encouragingCue = "Build shelf-like upper chest thickness!"
        ),
        StrengthExercise(
            name = "Decline Barbell Bench Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Hook legs securely, lower bar to lower sternum, press locked.",
            encouragingCue = "Massive lower pec recruitment with reduced shoulder shear!"
        ),
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
            techniqueTip = "Incline bench, elbows tucked at 45 degrees, squeeze upper clavicular pecs.",
            encouragingCue = "Solid stretch at bottom, squeeze upper chest at top!"
        ),
        StrengthExercise(
            name = "Decline Dumbbell Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Decline position, controlled eccentric, squeeze lower chest fibers.",
            encouragingCue = "Pure lower pectoral contraction without momentum!"
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
            name = "Flat Machine Chest Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Press handles evenly forward, full chest squeeze, slow eccentric return.",
            encouragingCue = "Locked-in path of motion for pure chest hypertrophy!"
        ),
        StrengthExercise(
            name = "Decline Machine Chest Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Focus on pressing downwards along the machine trajectory.",
            encouragingCue = "Isolate the lower pectoral heads safely!"
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
            equipmentType = ExerciseCategory.CHEST.let { EquipmentType.CABLE_MACHINE },
            defaultRestSeconds = 60,
            techniqueTip = "Set pulleys at shoulder or high position, press down and across sternum.",
            encouragingCue = "Feel that deep outer chest stretch, lock down the contraction!"
        ),
        StrengthExercise(
            name = "Low-to-High Cable Flyes",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Pulleys at lowest setting, scoop handles upward and together at upper chest level.",
            encouragingCue = "Sculpt the clavicular chest line with constant tension!"
        ),
        StrengthExercise(
            name = "High-to-Low Cable Flyes",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Pulleys overhead, sweep handles downward towards pelvis.",
            encouragingCue = "Deep lower chest burn and razor-sharp separation!"
        ),
        StrengthExercise(
            name = "Chest Dips",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 90,
            techniqueTip = "Lean torso forward 30 degrees, flare elbows slightly, descend to 90 degrees.",
            encouragingCue = "The king of bodyweight upper body mass builders!"
        ),
        StrengthExercise(
            name = "Push-Ups",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Straight rigid plank, chest touches floor, press up to full lockout.",
            encouragingCue = "Total upper body stability and functional pushing power!"
        ),
        StrengthExercise(
            name = "Landmine Chest Press",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Angled barbell path, press upward and inward with one or both hands.",
            encouragingCue = "Shoulder-friendly pressing angle with supreme inner chest contraction!"
        ),
        StrengthExercise(
            name = "Dumbbell Pullover",
            category = ExerciseCategory.CHEST,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Cross bench, lower dumbbell overhead in arc, pull over chest.",
            encouragingCue = "Expand the ribcage and stretch the serratus and pecs!"
        ),

        // BACK
        StrengthExercise(
            name = "Conventional Deadlift",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 150,
            techniqueTip = "Pack lats, hinge hips back, squeeze glutes at the lockout.",
            encouragingCue = "Push the earth away! Unstoppable posterior chain power!"
        ),
        StrengthExercise(
            name = "Sumo Deadlift",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 150,
            techniqueTip = "Wide stance, toes flared, open hips and drive vertically through heels.",
            encouragingCue = "Upright torso drive with colossal hip and back leverage!"
        ),
        StrengthExercise(
            name = "Barbell Bent-Over Rows",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Hinge forward 45 degrees, pull bar to lower belly, retract scapulae.",
            encouragingCue = "Thick, powerful back development from traps to lats!"
        ),
        StrengthExercise(
            name = "Pendlay Rows",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Torso parallel to floor, dead-stop every rep from the floor.",
            encouragingCue = "Explosive pulling power with zero hip momentum!"
        ),
        StrengthExercise(
            name = "Single-Arm Dumbbell Rows",
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
            name = "T-Bar Rows",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Straddle bar, close neutral grip, pull handles to chest.",
            encouragingCue = "Massive mid-back density and lat wing thickness!"
        ),
        StrengthExercise(
            name = "Lat Pulldowns",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Wide or neutral grip, slight lean back, pull bar to upper chest leading with elbows.",
            encouragingCue = "Pull your elbows into your back pockets! Flare those lats!"
        ),
        StrengthExercise(
            name = "Close-Grip Lat Pulldowns",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 75,
            techniqueTip = "V-bar attachment, pull to chest, feel deep lower lat stretch at top.",
            encouragingCue = "Emphasize lower lats and full stretch amplitude!"
        ),
        StrengthExercise(
            name = "Reverse-Grip Lat Pulldowns",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 75,
            techniqueTip = "Underhand supinated grip, pull down to upper chest.",
            encouragingCue = "Heavy lat overload with strong bicep leverage!"
        ),
        StrengthExercise(
            name = "Pull-Ups",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 90,
            techniqueTip = "Overhand grip, pull chest to bar, controlled descent to dead hang.",
            encouragingCue = "The ultimate benchmark of relative upper body pulling strength!"
        ),
        StrengthExercise(
            name = "Chin-Ups",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 90,
            techniqueTip = "Underhand grip, drive elbows down, chin clears bar.",
            encouragingCue = "Huge bicep and lat synergy!"
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
            name = "Rack Pulls",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Bar positioned at knee height in power rack, lock out with glutes and traps.",
            encouragingCue = "Heavy trap, erector, and upper back overload!"
        ),
        StrengthExercise(
            name = "Back Extensions",
            category = ExerciseCategory.BACK,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Hips on pad, hinge forward, raise back until parallel with legs.",
            encouragingCue = "Ironclad lower back, erector spinae, and glutes!"
        ),

        // SHOULDERS & TRAPS
        StrengthExercise(
            name = "Overhead Barbell Press",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Stand tall, squeeze glutes and abs, press bar in straight vertical line overhead.",
            encouragingCue = "Raw overhead pressing power and total shoulder armor!"
        ),
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
            name = "Arnold Press",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Start with palms facing chest, rotate outwards while pressing overhead.",
            encouragingCue = "Engage all three deltoid heads in one continuous movement!"
        ),
        StrengthExercise(
            name = "Smith Machine Shoulder Press",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Guided bar path, press smoothly from clavicles to full lockout.",
            encouragingCue = "Lock into the groove and overload the front delts safely!"
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
            name = "Machine Lateral Raises",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Elbow pads at arm height, drive elbows outward and up.",
            encouragingCue = "Perfect isolation with strict mechanical resistance!"
        ),
        StrengthExercise(
            name = "Dumbbell Front Raises",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Raise dumbbells in front of body to eye level, keep torso strictly still.",
            encouragingCue = "Sculpt the anterior deltoids with zero swinging!"
        ),
        StrengthExercise(
            name = "Cable Front Raises",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Cable between legs or from low pulley, raise straight forward.",
            encouragingCue = "Constant tension from start to finish on the front delt!"
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
            name = "Bent-Over Dumbbell Rear Delt Flyes",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Hips hinged forward, sweep dumbbells outward with slight elbow bend.",
            encouragingCue = "Posterior deltoid roundness and balanced shoulders!"
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
            name = "Barbell Upright Rows",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Shoulder-width grip, pull bar up towards sternum leading with high elbows.",
            encouragingCue = "Side delts and upper traps in unison!"
        ),
        StrengthExercise(
            name = "Dumbbell Shrugs",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Elevate shoulders straight towards ears, pause for 1 second, lower smoothly.",
            encouragingCue = "Heavy trap contraction! Do not roll your shoulders."
        ),
        StrengthExercise(
            name = "Barbell Shrugs",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Hold bar in front of thighs, shrug straight up, peak 2-second hold.",
            encouragingCue = "Stack heavy iron for mountain-like trapezius thickness!"
        ),
        StrengthExercise(
            name = "Machine Shrugs",
            category = ExerciseCategory.SHOULDERS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Grip handles on shrug or dip machine, shrug shoulders to ears.",
            encouragingCue = "Guided heavy trap overload with neutral wrist grip!"
        ),

        // LEGS (QUADS, HAMSTRINGS, GLUTES, CALVES)
        StrengthExercise(
            name = "Barbell Back Squat",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Descend until hip crease is below knee caps. Drive mid-foot into ground.",
            encouragingCue = "Drive the floor away! Proud chest, strong lockout!"
        ),
        StrengthExercise(
            name = "Barbell Front Squat",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 120,
            techniqueTip = "Clean grip or cross-arm, upright torso, deep knee flexion.",
            encouragingCue = "Maximum quadriceps isolation and anterior core rigidity!"
        ),
        StrengthExercise(
            name = "Leg Press",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 120,
            techniqueTip = "Feet mid-platform, lower weight sled until deep knee bend, press through heels.",
            encouragingCue = "Massive quad and glute drive! Don't lock knees out aggressively."
        ),
        StrengthExercise(
            name = "Incline Sled Press",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 120,
            techniqueTip = "Angled platform, controlled descent, push evenly through mid-foot and heel.",
            encouragingCue = "Heavy lower body mass builder with total lower back security!"
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
            name = "Pendulum Squat Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 120,
            techniqueTip = "Curved counterbalance arm, deep knee travel, drive smoothly out of hole.",
            encouragingCue = "Deepest quad stretch and knee-friendly mechanics on the planet!"
        ),
        StrengthExercise(
            name = "Belt Squat Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Weight attached to waist belt, squat deep with zero spinal compression.",
            encouragingCue = "Pure leg overload with completely unloaded spine!"
        ),
        StrengthExercise(
            name = "Sissy Squat Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Calves locked in pad, lean back, hinge deeply at the knees.",
            encouragingCue = "Direct rectus femoris and teardrop quad fire!"
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
            equipmentType = ExerciseCategory.LEGS.let { EquipmentType.GYM_MACHINE },
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
            name = "Standing Single-Leg Curl Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "One leg curled upward against roller, isolate hamstring unilaterally.",
            encouragingCue = "Fix muscular imbalances and carve out clean hamstring definition!"
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
            name = "Barbell Romanian Deadlift",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Barbell stays against shins, hinge hips back until deep hamstring stretch.",
            encouragingCue = "Heavy posterior chain loaded stretch!"
        ),
        StrengthExercise(
            name = "Bulgarian Split Squats",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Rear foot on bench, sink front thigh parallel to floor, drive through front heel.",
            encouragingCue = "The ultimate single-leg quad and glute test!"
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
            name = "Barbell Hip Thrust",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Upper back against bench, barbell on hips, drive hips up to full horizontal lockout.",
            encouragingCue = "Maximum glute contraction and athletic sprint power!"
        ),
        StrengthExercise(
            name = "Machine Hip Thrust",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 90,
            techniqueTip = "Belt secured, drive hip pad up to full glute squeeze at top.",
            encouragingCue = "Locked-in comfort for massive glute overload!"
        ),
        StrengthExercise(
            name = "Cable Glute Kickbacks",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Ankle strap on low cable, kick leg backward in an arc, squeeze glute.",
            encouragingCue = "Target the upper glutes and glute medius!"
        ),
        StrengthExercise(
            name = "Hip Abductor Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Open knees outward against pads, hold peak contraction for 1 second.",
            encouragingCue = "Build outer glute width and hip stability!"
        ),
        StrengthExercise(
            name = "Hip Adductor Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Squeeze legs inward together against pads, slow controlled release.",
            encouragingCue = "Develop dense inner thighs and groin resilience!"
        ),
        StrengthExercise(
            name = "Standing Calf Raise Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Balls of feet on block, deep ankle stretch at bottom, press high onto big toes.",
            encouragingCue = "Full ankle flexion and extension! Build diamond calves."
        ),
        StrengthExercise(
            name = "Seated Calf Raise Machine",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Knees bent 90 degrees, slow deep stretch, explode up through toes.",
            encouragingCue = "Direct soleus isolation for thick lower calf width!"
        ),
        StrengthExercise(
            name = "Leg Press Calf Raises",
            category = ExerciseCategory.LEGS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Balls of feet on bottom edge of sled, extend ankles forward.",
            encouragingCue = "Heavy gastrocnemius loading with straight legs!"
        ),

        // ARMS (BICEPS, TRICEPS, FOREARMS)
        StrengthExercise(
            name = "Barbell Bicep Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 75,
            techniqueTip = "Stand tall, pin elbows to sides, curl barbell in arc to upper chest.",
            encouragingCue = "Old-school bicep mass builder! Strictly control the negative."
        ),
        StrengthExercise(
            name = "EZ-Bar Bicep Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Angled grip relieves wrist strain, strict bicep curl without swinging.",
            encouragingCue = "Ergonomic wrist comfort for heavy arm loading!"
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
            name = "Incline Dumbbell Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Lean back on incline bench, let arms hang straight down, curl to top.",
            encouragingCue = "Maximum stretch on the long head of the bicep for peak height!"
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
            name = "Preacher Curls (EZ-Bar)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Arms locked onto slanted pad, curl EZ-bar with pure bicep tension.",
            encouragingCue = "Isolate the lower bicep and peak contraction!"
        ),
        StrengthExercise(
            name = "Concentration Curls",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Elbow against inner thigh, curl dumbbell with focused contraction.",
            encouragingCue = "Arnold's favorite peak bicep builder!"
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
            name = "Close-Grip Barbell Bench Press",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BARBELL,
            defaultRestSeconds = 90,
            techniqueTip = "Hands shoulder-width apart, elbows tucked close to torso, press up.",
            encouragingCue = "Heavy mechanical overload for massive triceps thickness!"
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
            name = "Cable Triceps Pushdowns",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Elbows pinned to sides, push straight or V-bar down and lock triceps out.",
            encouragingCue = "Lock out and feel that tricep horseshoe burn!"
        ),
        StrengthExercise(
            name = "Cable Triceps Rope Pushdowns",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Spread rope ends apart at bottom of motion for peak contraction.",
            encouragingCue = "Spread the rope and fire the lateral tricep head!"
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
            name = "Overhead Dumbbell Triceps Extension",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 60,
            techniqueTip = "Hold single dumbbell with both hands overhead, lower behind head, press up.",
            encouragingCue = "Deep stretch on the long head of the triceps!"
        ),
        StrengthExercise(
            name = "Seated Machine Triceps Extension",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Back against pad, elbows aligned with machine pivot, extend handles down.",
            encouragingCue = "Safe, locked-in tricep burn with continuous tension!"
        ),
        StrengthExercise(
            name = "Triceps Dips",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 75,
            techniqueTip = "Keep torso upright, elbows tucked close, push to full extension.",
            encouragingCue = "Raw bodyweight triceps power!"
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
            name = "Wrist Curls (Palms Up)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 45,
            techniqueTip = "Forearms resting on thighs or bench, curl wrists upward to full flexion.",
            encouragingCue = "Popeye forearm endurance and grip strength!"
        ),
        StrengthExercise(
            name = "Reverse Wrist Curls (Palms Down)",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.DUMBBELL,
            defaultRestSeconds = 45,
            techniqueTip = "Forearms on bench, curl wrists upward with knuckles toward ceiling.",
            encouragingCue = "Build the top of the forearms and wrist stability!"
        ),
        StrengthExercise(
            name = "Forearm Wrist Roller",
            category = ExerciseCategory.ARMS,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rotate roller handle forward and backward to wind weight plate up and down.",
            encouragingCue = "Relentless forearm burn and crushing grip endurance!"
        ),

        // CORE
        StrengthExercise(
            name = "Hanging Leg Raise",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Tilt pelvis up towards chin to fully recruit lower abdominals.",
            encouragingCue = "Total core control! Resisting momentum builds iron abs."
        ),
        StrengthExercise(
            name = "Captain's Chair Knee Raise",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Back against pad, forearms supported, drive knees cleanly to chest.",
            encouragingCue = "Strict lower abdominal compression!"
        ),
        StrengthExercise(
            name = "Cable Kneeling Crunch",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rope held at forehead, kneel on floor, crunch elbows to thighs.",
            encouragingCue = "Progressive overload for deep six-pack abdominal ridges!"
        ),
        StrengthExercise(
            name = "Cable Woodchoppers",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.CABLE_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rotate torso diagonally downward or upward across body with arms extended.",
            encouragingCue = "Rotational power and oblique definition!"
        ),
        StrengthExercise(
            name = "Abdominal Crunch Machine",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Sit back, hold handles, crunch forward curling the spine smoothly.",
            encouragingCue = "Targeted rectus abdominis overload with pinpoint resistance!"
        ),
        StrengthExercise(
            name = "Rotary Torso Machine",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.GYM_MACHINE,
            defaultRestSeconds = 60,
            techniqueTip = "Rotate torso smoothly through guided range of motion against pad.",
            encouragingCue = "Build rotational abdominal torque and oblique strength!"
        ),
        StrengthExercise(
            name = "Decline Bench Sit-Ups",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Feet locked in decline bench, curl torso upward focusing on abdominal squeeze.",
            encouragingCue = "Classic core flexion against gravity!"
        ),
        StrengthExercise(
            name = "Planks",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Elbows under shoulders, lock glutes and quads, pull belly button to spine.",
            encouragingCue = "Solid pillar of core stability!"
        ),
        StrengthExercise(
            name = "Russian Twists",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Seated with heels elevated, rotate torso side to side touching hands to floor.",
            encouragingCue = "Dynamic core stability and rotational endurance!"
        ),
        StrengthExercise(
            name = "Ab Wheel Rollouts",
            category = ExerciseCategory.CORE,
            equipmentType = EquipmentType.BODYWEIGHT,
            defaultRestSeconds = 60,
            techniqueTip = "Kneel, roll wheel forward extending torso, pull back with abdominals.",
            encouragingCue = "Unrivaled eccentric core tension and anti-extension strength!"
        ),

        // CARDIO
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
        val bench = exerciseCatalog.firstOrNull { it.name.contains("Bench", ignoreCase = true) } ?: exerciseCatalog[0]
        val inclineDb = exerciseCatalog.firstOrNull { it.name.contains("Incline Dumbbell", ignoreCase = true) } ?: exerciseCatalog[4]
        val pecFlyes = exerciseCatalog.firstOrNull { it.name.contains("Pec Deck", ignoreCase = true) } ?: exerciseCatalog[9]
        val shoulderPress = exerciseCatalog.firstOrNull { it.name.contains("Shoulder Press", ignoreCase = true) } ?: exerciseCatalog[0]
        val lateralRaises = exerciseCatalog.firstOrNull { it.name.contains("Lateral", ignoreCase = true) } ?: exerciseCatalog[14]
        val pushdowns = exerciseCatalog.firstOrNull { it.name.contains("Pushdown", ignoreCase = true) } ?: exerciseCatalog[0]

        val chestMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Chest Press", ignoreCase = true) } ?: gymMachinesCatalog[0]
        val flyMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Pec Deck", ignoreCase = true) } ?: gymMachinesCatalog[3]
        val shoulderMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Shoulder Press", ignoreCase = true) } ?: gymMachinesCatalog[0]
        val tricepsMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Triceps Extension", ignoreCase = true) } ?: gymMachinesCatalog[0]

        return WorkoutSession(
            title = "Push (Chest, Shoulders, Triceps)",
            splitCategory = "Push",
            dateString = "Today",
            durationMinutes = 60,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = bench,
                    gymMachine = chestMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 32.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 34.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 36.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = inclineDb,
                    gymMachine = chestMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 26.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 28.0, reps = 8, rpe = 9, isCompleted = true),
                        WorkoutSet(3, weightKg = 30.0, reps = 7, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = pecFlyes,
                    gymMachine = flyMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 60.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 65.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 70.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = shoulderPress,
                    gymMachine = shoulderMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 22.0, reps = 10, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 24.0, reps = 8, rpe = 9, isCompleted = false),
                        WorkoutSet(3, weightKg = 24.0, reps = 8, rpe = 9, isCompleted = false)
                    )
                ),
                ExerciseLog(
                    exercise = lateralRaises,
                    gymMachine = shoulderMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 12.0, reps = 15, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 14.0, reps = 12, rpe = 9, isCompleted = false),
                        WorkoutSet(3, weightKg = 14.0, reps = 12, rpe = 9, isCompleted = false)
                    )
                ),
                ExerciseLog(
                    exercise = pushdowns,
                    gymMachine = tricepsMachine,
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 25.0, reps = 12, rpe = 8, isCompleted = false),
                        WorkoutSet(2, weightKg = 30.0, reps = 10, rpe = 9, isCompleted = false),
                        WorkoutSet(3, weightKg = 30.0, reps = 10, rpe = 9, isCompleted = false)
                    )
                )
            ),
            isCompleted = false,
            totalVolumeKg = 3450.0
        )
    }

    fun createInitialWorkoutsHistory(): Map<String, WorkoutSession> {
        val todayStr = FitnessDateUtils.getTodayDateString()
        val day1Str = FitnessDateUtils.getDateDaysAgo(1)
        val day2Str = FitnessDateUtils.getDateDaysAgo(2)
        val day3Str = FitnessDateUtils.getDateDaysAgo(3)
        val day4Str = FitnessDateUtils.getDateDaysAgo(4)
        val day5Str = FitnessDateUtils.getDateDaysAgo(5)
        val day6Str = FitnessDateUtils.getDateDaysAgo(6)
        val day7Str = FitnessDateUtils.getDateDaysAgo(7)
        val day8Str = FitnessDateUtils.getDateDaysAgo(8)
        val day9Str = FitnessDateUtils.getDateDaysAgo(9)
        val day10Str = FitnessDateUtils.getDateDaysAgo(10)
        val day11Str = FitnessDateUtils.getDateDaysAgo(11)
        val day12Str = FitnessDateUtils.getDateDaysAgo(12)
        val day13Str = FitnessDateUtils.getDateDaysAgo(13)
        val day14Str = FitnessDateUtils.getDateDaysAgo(14)

        val todayWorkout = createInitialWorkoutSession()

        val day1Workout = WorkoutSession(
            title = "Legs (Quads, Hamstrings & Calves)",
            splitCategory = "Legs",
            dateString = day1Str,
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
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Curl", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Curl", ignoreCase = true) } ?: gymMachinesCatalog[13],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 55.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 60.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 65.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Lunges", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 22.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 24.0, reps = 10, rpe = 9, isCompleted = true),
                        WorkoutSet(3, weightKg = 26.0, reps = 10, rpe = 9, isCompleted = true)
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
            totalVolumeKg = 8950.0
        )

        val day2Workout = WorkoutSession(
            title = "Pull (Back, Lats & Biceps)",
            splitCategory = "Pull",
            dateString = day2Str,
            durationMinutes = 58,
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
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Face Pull", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Cable", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 25.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 30.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 30.0, reps = 12, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Incline Dumbbell Curl", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 14.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 16.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 16.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Hammer Curl", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 14.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 16.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 5420.0
        )

        val day3Workout = WorkoutSession(
            title = "Shoulders & Core Power",
            splitCategory = "Shoulders",
            dateString = day3Str,
            durationMinutes = 52,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Dumbbell Shoulder Press", ignoreCase = true) } ?: exerciseCatalog[13],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Shoulder Press", ignoreCase = true) } ?: gymMachinesCatalog[4],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 24.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 26.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 28.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Cable Lateral", ignoreCase = true) } ?: exerciseCatalog[14],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Lateral", ignoreCase = true) } ?: gymMachinesCatalog[5],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 10.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 12.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 12.0, reps = 12, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Rear Delt", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Pec Deck", ignoreCase = true) } ?: gymMachinesCatalog[3],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 45.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 50.0, reps = 12, rpe = 9, isCompleted = true),
                        WorkoutSet(3, weightKg = 55.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Shrugs", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 32.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 36.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 40.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 4850.0
        )

        val day4Workout = WorkoutSession(
            title = "Chest & Triceps Hypertrophy",
            splitCategory = "Push",
            dateString = day4Str,
            durationMinutes = 60,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Incline Barbell", ignoreCase = true) } ?: exerciseCatalog[1],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Incline Chest", ignoreCase = true) } ?: gymMachinesCatalog[1],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 70.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 75.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 80.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Flat Dumbbell", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 32.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 34.0, reps = 7, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Cable Chest", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Cable", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 15.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 17.5, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 20.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Dips", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Dip", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 0.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 10.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 15.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Tricep", ignoreCase = true) } ?: exerciseCatalog[29],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Triceps", ignoreCase = true) } ?: gymMachinesCatalog[20],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 35.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 40.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 45.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 5980.0
        )

        val day5Workout = WorkoutSession(
            title = "Legs & Posterior Power",
            splitCategory = "Legs",
            dateString = day5Str,
            durationMinutes = 62,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Hack Squat", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Hack Squat", ignoreCase = true) } ?: gymMachinesCatalog[14],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 100.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 120.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 140.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Romanian", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 80.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 90.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 100.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Curl", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Curl", ignoreCase = true) } ?: gymMachinesCatalog[13],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 50.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 55.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 60.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Calf Raise", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Calf", ignoreCase = true) } ?: gymMachinesCatalog[17],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 70.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 75.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 80.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 7420.0
        )

        val day6Workout = WorkoutSession(
            title = "Back & Lats Density",
            splitCategory = "Pull",
            dateString = day6Str,
            durationMinutes = 55,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bent-Over Rows", ignoreCase = true) } ?: exerciseCatalog[7],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 65.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 70.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 75.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Lat Pulldown", ignoreCase = true) } ?: exerciseCatalog[6],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Lat Pulldown", ignoreCase = true) } ?: gymMachinesCatalog[6],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 55.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 60.0, reps = 10, rpe = 9, isCompleted = true),
                        WorkoutSet(3, weightKg = 65.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Single-Arm", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 28.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 32.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 34.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bicep Curls", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 35.0, reps = 8, rpe = 9, isCompleted = true),
                        WorkoutSet(3, weightKg = 35.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 5680.0
        )

        val day7Workout = WorkoutSession(
            title = "Full Body Dynamic Power",
            splitCategory = "Full Body",
            dateString = day7Str,
            durationMinutes = 65,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Back Squat", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 90.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 100.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 110.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bench Press", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Chest Press", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 75.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 80.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 85.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Pull-Ups", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 0.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 0.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 0.0, reps = 9, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Overhead Barbell", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 45.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 50.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 6120.0
        )

        val day8Workout = WorkoutSession(
            title = "Push Volume & Delts Focus",
            splitCategory = "Push",
            dateString = day8Str,
            durationMinutes = 55,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Flat Dumbbell", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 32.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 34.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Dumbbell Lateral", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 12.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 14.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 14.0, reps = 12, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Pushdown", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Triceps", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 25.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 35.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 4320.0
        )

        val day9Workout = WorkoutSession(
            title = "Heavy Deadlift & Pull Power",
            splitCategory = "Pull",
            dateString = day9Str,
            durationMinutes = 65,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Deadlift", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 120.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 140.0, reps = 6, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 150.0, reps = 5, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Lat Pulldown", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Lat Pulldown", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 60.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 65.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 70.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Preacher", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Preacher", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 25.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 30.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 5180.0
        )

        val day10Workout = WorkoutSession(
            title = "Quad-Dominant Leg Blast",
            splitCategory = "Legs",
            dateString = day10Str,
            durationMinutes = 60,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Front Squat", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 70.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 80.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 85.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Press", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Press", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 180.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 200.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Leg Extension", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Extension", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 55.0, reps = 15, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 60.0, reps = 12, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 6840.0
        )

        val day11Workout = WorkoutSession(
            title = "Arms & Delts Conditioning",
            splitCategory = "Arms",
            dateString = day11Str,
            durationMinutes = 50,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Arnold Press", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 20.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 22.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 24.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Barbell Bicep", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 30.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 35.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Close-Grip", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Chest Press", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 50.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 55.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 3980.0
        )

        val day12Workout = WorkoutSession(
            title = "Chest & Back Antagonist Density",
            splitCategory = "Upper Body",
            dateString = day12Str,
            durationMinutes = 58,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Incline Dumbbell", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 26.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 28.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 30.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Seated Cable", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Cable Row", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 55.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 60.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Pec Deck", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Pec Deck", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 60.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 65.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 4650.0
        )

        val day13Workout = WorkoutSession(
            title = "Hamstrings & Glute Power",
            splitCategory = "Legs",
            dateString = day13Str,
            durationMinutes = 55,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Hip Thrust", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Hip Thrust", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 100.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 120.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 130.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Romanian Deadlift", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 85.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 95.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Seated Leg Curl", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Leg Curl", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 50.0, reps = 12, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 55.0, reps = 10, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 6120.0
        )

        val day14Workout = WorkoutSession(
            title = "Strength Peak Full Body",
            splitCategory = "Full Body",
            dateString = day14Str,
            durationMinutes = 65,
            exercises = mutableListOf(
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Back Squat", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 100.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 110.0, reps = 6, rpe = 8, isCompleted = true),
                        WorkoutSet(3, weightKg = 120.0, reps = 4, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bench Press", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Chest Press", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 80.0, reps = 8, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 85.0, reps = 6, rpe = 9, isCompleted = true)
                    )
                ),
                ExerciseLog(
                    exercise = exerciseCatalog.firstOrNull { it.name.contains("Bent-Over Rows", ignoreCase = true) } ?: exerciseCatalog[0],
                    gymMachine = gymMachinesCatalog.firstOrNull { it.machineName.contains("Free Weights", ignoreCase = true) } ?: gymMachinesCatalog[0],
                    sets = mutableListOf(
                        WorkoutSet(1, weightKg = 70.0, reps = 10, rpe = 8, isCompleted = true),
                        WorkoutSet(2, weightKg = 75.0, reps = 8, rpe = 9, isCompleted = true)
                    )
                )
            ),
            isCompleted = true,
            totalVolumeKg = 6890.0
        )

        return mapOf(
            todayStr to todayWorkout,
            "Today" to todayWorkout,
            day1Str to day1Workout,
            "Yesterday" to day1Workout,
            "1 Days Ago" to day1Workout,
            day2Str to day2Workout,
            "2 Days Ago" to day2Workout,
            day3Str to day3Workout,
            "3 Days Ago" to day3Workout,
            day4Str to day4Workout,
            "4 Days Ago" to day4Workout,
            day5Str to day5Workout,
            "5 Days Ago" to day5Workout,
            day6Str to day6Workout,
            "6 Days Ago" to day6Workout,
            day7Str to day7Workout,
            "7 Days Ago" to day7Workout,
            "1 Week Ago" to day7Workout,
            day8Str to day8Workout,
            "8 Days Ago" to day8Workout,
            day9Str to day9Workout,
            "9 Days Ago" to day9Workout,
            day10Str to day10Workout,
            "10 Days Ago" to day10Workout,
            day11Str to day11Workout,
            "11 Days Ago" to day11Workout,
            day12Str to day12Workout,
            "12 Days Ago" to day12Workout,
            day13Str to day13Workout,
            "13 Days Ago" to day13Workout,
            day14Str to day14Workout,
            "14 Days Ago" to day14Workout,
            "2 Weeks Ago" to day14Workout
        )
    }

    val upcomingScheduledWorkouts = listOf(
        ScheduledWorkout(
            dayOfWeek = "Mon",
            date = "Today",
            title = "Push (Chest, Shoulders, Triceps)",
            focusArea = "Flat DB Bench, Seated Machine Press, Cable Pushdowns",
            estimatedMinutes = 55,
            isToday = true,
            isCompleted = true
        ),
        ScheduledWorkout(
            dayOfWeek = "Tue",
            date = "Tomorrow",
            title = "Pull (Back, Biceps & Lats)",
            focusArea = "Lat Pulldowns, Seated Cable Rows, Preacher Curls",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Wed",
            date = "Wed",
            title = "Legs (Quad & Calves Hypertrophy)",
            focusArea = "Leg Press, Hack Squats, Seated Calf Raises",
            estimatedMinutes = 60,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Thu",
            date = "Thu",
            title = "Active Recovery & Cardio",
            focusArea = "Incline Treadmill Walk, Mobility & Core Flow",
            estimatedMinutes = 40,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Fri",
            date = "Fri",
            title = "Push Hypertrophy & Delts",
            focusArea = "Incline DB Press, Lateral Raises, Overhead Extensions",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Sat",
            date = "Sat",
            title = "Pull Density & Posterior Chain",
            focusArea = "Dumbbell Rows, Face Pulls, Romanian Deadlifts",
            estimatedMinutes = 55,
            isToday = false,
            isCompleted = false
        ),
        ScheduledWorkout(
            dayOfWeek = "Sun",
            date = "Sun",
            title = "Rest, Meal Prep & Recovery",
            focusArea = "Full rest day, grocery prep & sauna recovery",
            estimatedMinutes = 30,
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
            name = "Steamed Broccoli Florets",
            brandOrCategory = "Vegetables & Micronutrients",
            servingUnit = "150g steamed",
            caloriesPerServing = 45,
            proteinGrams = 3.5,
            carbsGrams = 8.0,
            fatGrams = 0.5,
            fiberGrams = 3.8
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
            name = "Raw Whole Almonds",
            brandOrCategory = "Healthy Fats & Micronutrients",
            servingUnit = "30g (approx 24 nuts)",
            caloriesPerServing = 164,
            proteinGrams = 6.0,
            carbsGrams = 6.0,
            fatGrams = 14.0,
            fiberGrams = 3.5
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
            name = "Fresh Banana",
            brandOrCategory = "Potassium Rich Fruit",
            servingUnit = "1 medium banana (118g)",
            caloriesPerServing = 105,
            proteinGrams = 1.3,
            carbsGrams = 27.0,
            fatGrams = 0.3,
            fiberGrams = 3.1
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
            name = "Extra Virgin Olive Oil",
            brandOrCategory = "Healthy Monounsaturated Fats",
            servingUnit = "1 tbsp (14g)",
            caloriesPerServing = 119,
            proteinGrams = 0.0,
            carbsGrams = 0.0,
            fatGrams = 13.5
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
            name = "Canned Albacore Tuna",
            brandOrCategory = "Ultra Lean Protein",
            servingUnit = "1 can in water (120g drained)",
            caloriesPerServing = 130,
            proteinGrams = 28.0,
            carbsGrams = 0.0,
            fatGrams = 1.0
        ),
        FoodItem(
            name = "Sirloin Steak (Lean Cut)",
            brandOrCategory = "Iron & Protein Rich Beef",
            servingUnit = "180g grilled",
            caloriesPerServing = 340,
            proteinGrams = 48.0,
            carbsGrams = 0.0,
            fatGrams = 15.0
        ),
        FoodItem(
            name = "Organic Whole Milk",
            brandOrCategory = "Dairy & Healthy Fats",
            servingUnit = "250ml cup",
            caloriesPerServing = 149,
            proteinGrams = 8.0,
            carbsGrams = 12.0,
            fatGrams = 8.0
        ),
        FoodItem(
            name = "Low Fat Cottage Cheese",
            brandOrCategory = "Slow-Digesting Casein Protein",
            servingUnit = "200g tub",
            caloriesPerServing = 163,
            proteinGrams = 24.0,
            carbsGrams = 8.0,
            fatGrams = 4.0
        ),
        FoodItem(
            name = "Cooked Brown Rice",
            brandOrCategory = "Whole Grain Complex Carbs",
            servingUnit = "150g cooked",
            caloriesPerServing = 216,
            proteinGrams = 5.0,
            carbsGrams = 45.0,
            fatGrams = 1.8,
            fiberGrams = 3.5
        ),
        FoodItem(
            name = "Artisan Sourdough Toast",
            brandOrCategory = "Complex Carbohydrates",
            servingUnit = "1 slice (40g)",
            caloriesPerServing = 105,
            proteinGrams = 4.2,
            carbsGrams = 20.5,
            fatGrams = 0.6,
            fiberGrams = 1.2
        ),
        FoodItem(
            name = "Whey-Casein Blend Protein",
            brandOrCategory = "Sustained Amino Release",
            servingUnit = "1 level scoop (34g)",
            caloriesPerServing = 130,
            proteinGrams = 26.0,
            carbsGrams = 2.0,
            fatGrams = 1.5
        ),
        FoodItem(
            name = "Crisp Honeycrisp Apple",
            brandOrCategory = "Pre-Workout Fiber & Carbs",
            servingUnit = "1 medium apple (180g)",
            caloriesPerServing = 95,
            proteinGrams = 0.5,
            carbsGrams = 25.0,
            fatGrams = 0.3,
            fiberGrams = 4.4
        ),
        FoodItem(
            name = "Extra Virgin Olive Oil",
            brandOrCategory = "Monounsaturated Healthy Fats",
            servingUnit = "1 tablespoon (14ml)",
            caloriesPerServing = 119,
            proteinGrams = 0.0,
            carbsGrams = 0.0,
            fatGrams = 14.0
        ),
        FoodItem(
            name = "Baby Spinach Greens",
            brandOrCategory = "Iron & Micronutrient Superfood",
            servingUnit = "100g fresh leaves",
            caloriesPerServing = 23,
            proteinGrams = 2.9,
            carbsGrams = 3.6,
            fatGrams = 0.4,
            fiberGrams = 2.2
        ),
        FoodItem(
            name = "All-Natural Almond Butter",
            brandOrCategory = "Dense Healthy Fats & Vitamin E",
            servingUnit = "2 tbsp (32g)",
            caloriesPerServing = 196,
            proteinGrams = 7.0,
            carbsGrams = 6.0,
            fatGrams = 18.0,
            fiberGrams = 3.3
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
        ),
        Recipe(
            title = "Flank Steak Fajita Protein Skillet",
            description = "Tender marinated flank steak seared with tri-color bell peppers, sweet red onions, and lime crema.",
            prepTimeMinutes = 18,
            calories = 520,
            proteinG = 48.0,
            carbsG = 28.0,
            fatG = 18.0,
            ingredients = listOf(
                "200g Lean Flank Steak sliced thin",
                "1 Bell Pepper sliced",
                "1/2 Red Onion sliced",
                "1 tbsp Extra Virgin Olive Oil",
                "1 tbsp Fajita spice seasoning",
                "Lime wedges & fresh cilantro"
            ),
            instructions = listOf(
                "Season flank steak strips with fajita blend.",
                "Sear over smoking cast iron skillet for 4 minutes until medium-rare.",
                "Sauté peppers and onions until caramelized and toss together with fresh lime juice."
            ),
            tags = listOf("High Iron", "Dinner", "Keto-Friendly")
        ),
        Recipe(
            title = "Greek Lemon Garlic Turkey Meatballs & Quinoa",
            description = "Lean baked turkey meatballs seasoned with oregano and garlic served over fluffy herb quinoa.",
            prepTimeMinutes = 22,
            calories = 490,
            proteinG = 45.0,
            carbsG = 48.0,
            fatG = 11.0,
            ingredients = listOf(
                "180g Lean Ground Turkey 93/7",
                "120g Cooked Tri-Color Quinoa",
                "1 tbsp Minced Garlic & Greek Oregano",
                "40g Reduced-fat Feta crumbles",
                "Cucumbers and cherry tomatoes"
            ),
            instructions = listOf(
                "Roll ground turkey with herbs into 5 golf-ball sized meatballs.",
                "Bake at 200°C (400°F) for 15 minutes until golden.",
                "Serve over fluffy warm quinoa with feta and crisp cucumber spears."
            ),
            tags = listOf("Lean Muscle", "Lunch", "Meal Prep")
        ),
        Recipe(
            title = "Post-Workout Banana Whey Power Pancakes",
            description = "Fluffy three-ingredient protein pancakes served with crushed walnuts and warm maple syrup drizzle.",
            prepTimeMinutes = 12,
            calories = 440,
            proteinG = 36.0,
            carbsG = 52.0,
            fatG = 8.0,
            ingredients = listOf(
                "1 Scoop Whey Protein Isolate",
                "1 Ripe Medium Banana",
                "2 Whole Eggs or 4 Egg Whites",
                "1/2 tsp Cinnamon",
                "10g Crushed Walnuts"
            ),
            instructions = listOf(
                "Mash banana thoroughly with a fork and whisk together with eggs and protein powder.",
                "Pour 3 small circles onto non-stick skillet over medium-low heat.",
                "Flip after 2 minutes when bubbles form and top with walnuts and cinnamon."
            ),
            tags = listOf("High Protein", "Breakfast", "Post-Workout")
        ),
        Recipe(
            title = "Avocado & Egg White Power Wrap",
            description = "Spinach tortilla loaded with scrambled egg whites, grilled chicken breast slices, and avocado mash.",
            prepTimeMinutes = 10,
            calories = 380,
            proteinG = 34.0,
            carbsG = 32.0,
            fatG = 10.0,
            ingredients = listOf(
                "1 Whole Grain or Spinach Tortilla Wrap",
                "150ml Liquid Egg Whites scrambled",
                "75g Grilled Chicken Strips",
                "1/4 Ripe Avocado mashed",
                "Handful Fresh Baby Spinach"
            ),
            instructions = listOf(
                "Scramble egg whites in skillet with a pinch of sea salt and pepper.",
                "Spread mashed avocado over warm tortilla.",
                "Layer baby spinach, scrambled egg whites, and warm chicken, wrap tightly and slice."
            ),
            tags = listOf("Quick Prep", "Lunch", "Clean Fuel")
        ),
        Recipe(
            title = "Teriyaki Glazed Air-Fryer Salmon Bowl",
            description = "Crispy skin salmon tossed in a low-sodium teriyaki glaze over warm jasmine rice with sesame seeds.",
            prepTimeMinutes = 15,
            calories = 520,
            proteinG = 42.0,
            carbsG = 48.0,
            fatG = 16.0,
            ingredients = listOf(
                "160g Fresh Atlantic Salmon fillet",
                "150g Cooked Jasmine Rice",
                "1.5 tbsp Low-Sodium Teriyaki Glaze",
                "100g Steamed Snap Peas",
                "1/2 tsp Toasted Sesame Seeds"
            ),
            instructions = listOf(
                "Brush salmon with teriyaki glaze.",
                "Air-fry at 200°C (400°F) for 10 minutes until flaky and crisp.",
                "Serve atop jasmine rice, garnish with steamed snap peas and sesame seeds."
            ),
            tags = listOf("Omega-3", "Dinner", "Air Fryer")
        ),
        Recipe(
            title = "Overnight Protein Chia Power Pudding",
            description = "Creamy chilled chia pudding infused with vanilla whey isolate, unsweetened almond milk, and crushed raspberries.",
            prepTimeMinutes = 5,
            calories = 340,
            proteinG = 32.0,
            carbsG = 26.0,
            fatG = 11.0,
            ingredients = listOf(
                "3 tbsp Black Chia Seeds",
                "1 Scoop Vanilla Whey Protein Isolate",
                "200ml Unsweetened Vanilla Almond Milk",
                "50g Fresh Raspberries",
                "1/4 tsp Ground Cinnamon"
            ),
            instructions = listOf(
                "Whisk chia seeds, protein powder, and almond milk vigorously in a mason jar.",
                "Refrigerate overnight (or at least 4 hours) until thick and set.",
                "Top with fresh raspberries and cinnamon before enjoying."
            ),
            tags = listOf("No Cook", "Breakfast", "Meal Prep")
        ),
        Recipe(
            title = "Lean Beef & Crispy Sweet Potato Power Hash",
            description = "Grass-fed lean beef skillet sautéed with diced sweet potatoes, caramelized onions, and pasture-raised egg.",
            prepTimeMinutes = 18,
            calories = 560,
            proteinG = 48.0,
            carbsG = 45.0,
            fatG = 18.0,
            ingredients = listOf(
                "180g Extra Lean Ground Beef 96/4",
                "180g Diced Sweet Potato",
                "1/2 Red Onion diced",
                "1 Whole Pasture-Raised Sunny Egg",
                "1 tsp Smoked Paprika & Sea Salt"
            ),
            instructions = listOf(
                "Brown diced sweet potato in skillet until tender and crispy.",
                "Add ground beef and onion, seasoned with smoked paprika.",
                "Top with a sunny-side egg and fresh chives."
            ),
            tags = listOf("High Protein", "Post-Workout", "Dinner")
        )
    )

    fun createInitialMealEntries(): List<LoggedMealItem> {
        val day0Str = FitnessDateUtils.getTodayDateString()
        val day1Str = FitnessDateUtils.getDateDaysAgo(1)
        val day2Str = FitnessDateUtils.getDateDaysAgo(2)
        val day3Str = FitnessDateUtils.getDateDaysAgo(3)
        val day4Str = FitnessDateUtils.getDateDaysAgo(4)
        val day5Str = FitnessDateUtils.getDateDaysAgo(5)
        val day6Str = FitnessDateUtils.getDateDaysAgo(6)
        val day7Str = FitnessDateUtils.getDateDaysAgo(7)
        val day8Str = FitnessDateUtils.getDateDaysAgo(8)
        val day9Str = FitnessDateUtils.getDateDaysAgo(9)
        val day10Str = FitnessDateUtils.getDateDaysAgo(10)
        val day11Str = FitnessDateUtils.getDateDaysAgo(11)
        val day12Str = FitnessDateUtils.getDateDaysAgo(12)
        val day13Str = FitnessDateUtils.getDateDaysAgo(13)
        val day14Str = FitnessDateUtils.getDateDaysAgo(14)

        return listOf(
            // --- Day 0 (Today) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[12], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[6], mealCategory = MealCategory.LUNCH, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day0Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[9], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day0Str),

            // --- Day 1 (Yesterday) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[16], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day1Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[9], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day1Str),

            // --- Day 2 (2 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[11], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[10], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[19], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day2Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[18], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day2Str),

            // --- Day 3 (3 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.2, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[15], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[14], mealCategory = MealCategory.LUNCH, numberOfServings = 0.5, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.5, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day3Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.5, dateString = day3Str),

            // --- Day 4 (4 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[17], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day4Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[11], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day4Str),

            // --- Day 5 (5 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[16], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[19], mealCategory = MealCategory.LUNCH, numberOfServings = 1.0, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day5Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day5Str),

            // --- Day 6 (6 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[11], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.LUNCH, numberOfServings = 1.0, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.0, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.5, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day6Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[18], mealCategory = MealCategory.SNACKS, numberOfServings = 1.2, dateString = day6Str),

            // --- Day 7 (1 Week Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.2, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[16], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day7Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day7Str),

            // --- Day 8 (8 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[13], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.3, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[19], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[6], mealCategory = MealCategory.LUNCH, numberOfServings = 1.0, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.DINNER, numberOfServings = 1.1, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day8Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[9], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day8Str),

            // --- Day 9 (9 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[12], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[15], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.3, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.4, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day9Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[18], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day9Str),

            // --- Day 10 (10 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[11], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[16], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day10Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.2, dateString = day10Str),

            // --- Day 11 (11 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.2, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[19], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.3, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day11Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[9], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day11Str),

            // --- Day 12 (12 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[14], mealCategory = MealCategory.BREAKFAST, numberOfServings = 0.5, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[15], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day12Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[18], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day12Str),

            // --- Day 13 (13 Days Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[1], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.5, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[2], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[10], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[16], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[19], mealCategory = MealCategory.LUNCH, numberOfServings = 1.2, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day13Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[8], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day13Str),

            // --- Day 14 (2 Weeks Ago) ---
            LoggedMealItem(foodItem = commonFoodsCatalog[3], mealCategory = MealCategory.BREAKFAST, numberOfServings = 2.0, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[11], mealCategory = MealCategory.BREAKFAST, numberOfServings = 1.0, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[0], mealCategory = MealCategory.LUNCH, numberOfServings = 1.5, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[5], mealCategory = MealCategory.LUNCH, numberOfServings = 1.3, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[4], mealCategory = MealCategory.DINNER, numberOfServings = 1.2, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[7], mealCategory = MealCategory.DINNER, numberOfServings = 1.0, dateString = day14Str),
            LoggedMealItem(foodItem = commonFoodsCatalog[18], mealCategory = MealCategory.SNACKS, numberOfServings = 1.0, dateString = day14Str)
        )
    }

    fun createInitialWaterIntake(): Map<String, Int> {
        val day0Str = FitnessDateUtils.getTodayDateString()
        val day1Str = FitnessDateUtils.getDateDaysAgo(1)
        val day2Str = FitnessDateUtils.getDateDaysAgo(2)
        val day3Str = FitnessDateUtils.getDateDaysAgo(3)
        val day4Str = FitnessDateUtils.getDateDaysAgo(4)
        val day5Str = FitnessDateUtils.getDateDaysAgo(5)
        val day6Str = FitnessDateUtils.getDateDaysAgo(6)
        val day7Str = FitnessDateUtils.getDateDaysAgo(7)
        val day8Str = FitnessDateUtils.getDateDaysAgo(8)
        val day9Str = FitnessDateUtils.getDateDaysAgo(9)
        val day10Str = FitnessDateUtils.getDateDaysAgo(10)
        val day11Str = FitnessDateUtils.getDateDaysAgo(11)
        val day12Str = FitnessDateUtils.getDateDaysAgo(12)
        val day13Str = FitnessDateUtils.getDateDaysAgo(13)
        val day14Str = FitnessDateUtils.getDateDaysAgo(14)

        return mapOf(
            day0Str to 2800,
            "Today" to 2800,
            day1Str to 3400,
            "Yesterday" to 3400,
            "1 Days Ago" to 3400,
            day2Str to 3100,
            "2 Days Ago" to 3100,
            day3Str to 3500,
            "3 Days Ago" to 3500,
            day4Str to 3250,
            "4 Days Ago" to 3250,
            day5Str to 3600,
            "5 Days Ago" to 3600,
            day6Str to 3300,
            "6 Days Ago" to 3300,
            day7Str to 3450,
            "7 Days Ago" to 3450,
            "1 Week Ago" to 3450,
            day8Str to 3200,
            "8 Days Ago" to 3200,
            day9Str to 3500,
            "9 Days Ago" to 3500,
            day10Str to 3300,
            "10 Days Ago" to 3300,
            day11Str to 3650,
            "11 Days Ago" to 3650,
            day12Str to 3150,
            "12 Days Ago" to 3150,
            day13Str to 3400,
            "13 Days Ago" to 3400,
            day14Str to 3300,
            "14 Days Ago" to 3300,
            "2 Weeks Ago" to 3300
        )
    }

    val historicalWeightData = listOf(
        WeightRecord(dateString = "Jul 15", weightKg = 89.5, bodyFatPercentage = 24.8),
        WeightRecord(dateString = "Aug 1", weightKg = 88.8, bodyFatPercentage = 24.5),
        WeightRecord(dateString = "Aug 8", weightKg = 88.2, bodyFatPercentage = 24.3),
        WeightRecord(dateString = "Aug 15", weightKg = 87.6, bodyFatPercentage = 24.0),
        WeightRecord(dateString = "Aug 22", weightKg = 87.0, bodyFatPercentage = 23.7),
        WeightRecord(dateString = "Aug 29", weightKg = 86.4, bodyFatPercentage = 23.4),
        WeightRecord(dateString = "Sep 5", weightKg = 85.8, bodyFatPercentage = 23.0),
        WeightRecord(dateString = "Sep 12", weightKg = 85.2, bodyFatPercentage = 22.7),
        WeightRecord(dateString = "Sep 19", weightKg = 84.6, bodyFatPercentage = 22.3),
        WeightRecord(dateString = "Sep 26", weightKg = 84.0, bodyFatPercentage = 21.9),
        WeightRecord(dateString = "Oct 3", weightKg = 83.3, bodyFatPercentage = 21.5),
        WeightRecord(dateString = "Oct 10", weightKg = 82.6, bodyFatPercentage = 21.0),
        WeightRecord(dateString = "Oct 17", weightKg = 81.9, bodyFatPercentage = 20.6),
        WeightRecord(dateString = "Oct 24", weightKg = 81.2, bodyFatPercentage = 20.2),
        WeightRecord(dateString = "Oct 31", weightKg = 80.5, bodyFatPercentage = 19.8),
        WeightRecord(dateString = "Nov 4", weightKg = 79.9, bodyFatPercentage = 19.4),
        WeightRecord(dateString = "Nov 8", weightKg = 79.2, bodyFatPercentage = 19.1),
        WeightRecord(dateString = "Nov 11", weightKg = 78.8, bodyFatPercentage = 18.8),
        WeightRecord(dateString = "Nov 14", weightKg = 78.5, bodyFatPercentage = 18.6),
        WeightRecord(dateString = "Nov 16", weightKg = 78.3, bodyFatPercentage = 18.5),
        WeightRecord(dateString = "Today", weightKg = 78.2, bodyFatPercentage = 18.4)
    )

    val initialProgressPhotos = listOf(
        ProgressPhotoItem(
            dateString = "Aug 1, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 88.8,
            title = "Starting Baseline",
            notes = "Day 1 of body recomposition. Prioritizing progressive overload and clean nutrition.",
            accentHueSeed = 0
        ),
        ProgressPhotoItem(
            dateString = "Aug 22, 2026",
            pose = PhotoPoseType.SIDE,
            weightKg = 87.0,
            title = "Week 3 Consistency",
            notes = "Posture improving, midsection tightening, energy levels up.",
            accentHueSeed = 1
        ),
        ProgressPhotoItem(
            dateString = "Sep 15, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 85.2,
            title = "Month 1 Milestone",
            notes = "Waist down 4cm. Visible shoulder and quad definition emerging.",
            accentHueSeed = 2
        ),
        ProgressPhotoItem(
            dateString = "Oct 1, 2026",
            pose = PhotoPoseType.BACK,
            weightKg = 83.3,
            title = "Mid-Cut Conditioning",
            notes = "Lat spread widening, upper back traps and rhomboids popping.",
            accentHueSeed = 3
        ),
        ProgressPhotoItem(
            dateString = "Oct 17, 2026",
            pose = PhotoPoseType.SIDE,
            weightKg = 81.9,
            title = "Waist Taper Milestone",
            notes = "Obliques showing, abdominal wall noticeably leaner.",
            accentHueSeed = 4
        ),
        ProgressPhotoItem(
            dateString = "Oct 31, 2026",
            pose = PhotoPoseType.SIDE,
            weightKg = 80.5,
            title = "Lower Body Definition",
            notes = "Vastus medialis teardrop vascularity and hamstring separation.",
            accentHueSeed = 5
        ),
        ProgressPhotoItem(
            dateString = "Nov 8, 2026",
            pose = PhotoPoseType.FRONT,
            weightKg = 79.2,
            title = "Week 14 Abs Milestone",
            notes = "Full six-pack clearly visible under gym lighting. Strength PRs maintained.",
            accentHueSeed = 6
        ),
        ProgressPhotoItem(
            dateString = "Nov 17, 2026",
            pose = PhotoPoseType.BACK,
            weightKg = 78.2,
            title = "Current Peak V-Taper",
            notes = "Lat spread width peaked, rear delts and lower back Christmas tree definition.",
            accentHueSeed = 7
        )
    )

    val initialCircumferences = listOf(
        BodyCircumference(dateString = "Aug 1", waistCm = 97.5, chestCm = 103.0, armsCm = 36.0, thighsCm = 60.0),
        BodyCircumference(dateString = "Aug 15", waistCm = 96.0, chestCm = 103.5, armsCm = 36.5, thighsCm = 60.5),
        BodyCircumference(dateString = "Aug 29", waistCm = 94.8, chestCm = 104.0, armsCm = 36.8, thighsCm = 60.8),
        BodyCircumference(dateString = "Sep 5", waistCm = 94.0, chestCm = 104.2, armsCm = 37.0, thighsCm = 61.0),
        BodyCircumference(dateString = "Sep 15", waistCm = 92.5, chestCm = 104.5, armsCm = 37.2, thighsCm = 61.2),
        BodyCircumference(dateString = "Sep 26", waistCm = 91.5, chestCm = 104.8, armsCm = 37.4, thighsCm = 61.4),
        BodyCircumference(dateString = "Oct 3", waistCm = 90.5, chestCm = 105.0, armsCm = 37.6, thighsCm = 61.5),
        BodyCircumference(dateString = "Oct 10", waistCm = 89.5, chestCm = 105.2, armsCm = 37.7, thighsCm = 61.6),
        BodyCircumference(dateString = "Oct 17", waistCm = 88.5, chestCm = 105.5, armsCm = 37.9, thighsCm = 61.8),
        BodyCircumference(dateString = "Oct 24", waistCm = 87.8, chestCm = 105.8, armsCm = 38.0, thighsCm = 61.9),
        BodyCircumference(dateString = "Oct 31", waistCm = 87.0, chestCm = 106.0, armsCm = 38.2, thighsCm = 62.0),
        BodyCircumference(dateString = "Nov 4", waistCm = 86.0, chestCm = 106.3, armsCm = 38.4, thighsCm = 62.1),
        BodyCircumference(dateString = "Nov 8", waistCm = 85.0, chestCm = 106.5, armsCm = 38.6, thighsCm = 62.2),
        BodyCircumference(dateString = "Nov 12", waistCm = 84.5, chestCm = 106.8, armsCm = 38.8, thighsCm = 62.4),
        BodyCircumference(dateString = "Nov 15", waistCm = 84.2, chestCm = 107.2, armsCm = 38.9, thighsCm = 62.5),
        BodyCircumference(dateString = "Today", waistCm = 84.0, chestCm = 107.5, armsCm = 39.0, thighsCm = 62.5)
    )

    val defaultReminders = listOf(
        CustomReminder(
            title = "Morning Hydration (500ml)",
            category = ReminderCategory.HYDRATION,
            timeLabel = "07:15 AM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Morning Vitamins & Omega-3",
            category = ReminderCategory.RECOVERY,
            timeLabel = "07:45 AM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Mid-Day Hydration Refill (750ml)",
            category = ReminderCategory.HYDRATION,
            timeLabel = "11:30 AM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Pre-Workout Fuel & Warmup",
            category = ReminderCategory.WORKOUT,
            timeLabel = "04:45 PM",
            daysLabel = "Mon, Tue, Wed, Fri, Sat",
            isEnabled = true
        ),
        CustomReminder(
            title = "Scheduled Workout Session",
            category = ReminderCategory.WORKOUT,
            timeLabel = "05:15 PM",
            daysLabel = "Mon, Tue, Wed, Fri, Sat",
            isEnabled = true
        ),
        CustomReminder(
            title = "Post-Workout Protein & Creatine Shake",
            category = ReminderCategory.WORKOUT,
            timeLabel = "06:45 PM",
            daysLabel = "Mon, Tue, Wed, Fri, Sat",
            isEnabled = true
        ),
        CustomReminder(
            title = "Daily 10k Steps Check-In",
            category = ReminderCategory.WORKOUT,
            timeLabel = "07:45 PM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Dinner & Daily Macro Review",
            category = ReminderCategory.MEAL_LOG,
            timeLabel = "08:30 PM",
            daysLabel = "Everyday",
            isEnabled = true
        ),
        CustomReminder(
            title = "Magnesium & Sleep Wind-Down",
            category = ReminderCategory.RECOVERY,
            timeLabel = "10:15 PM",
            daysLabel = "Everyday",
            isEnabled = true
        )
    )

    val initialCardioSessions = listOf(
        CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = FitnessDateUtils.getTodayDateString(),
            durationMinutes = 35,
            distanceKm = 4.2,
            caloriesBurned = 330,
            avgHeartRateBpm = 134,
            treadmillSpeedKmh = 5.4,
            treadmillInclinePercent = 10.0
        ),
        CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = FitnessDateUtils.getDateDaysAgo(1),
            durationMinutes = 45,
            distanceKm = 6.5,
            caloriesBurned = 520,
            avgHeartRateBpm = 138,
            treadmillSpeedKmh = 5.2,
            treadmillInclinePercent = 12.0
        ),
        CardioSession(
            machineType = CardioMachineType.STATIONARY_BIKE,
            dateString = FitnessDateUtils.getDateDaysAgo(2),
            durationMinutes = 35,
            distanceKm = 13.8,
            caloriesBurned = 360,
            avgHeartRateBpm = 142,
            bikeCadenceRpm = 88,
            bikeResistanceLevel = 9
        ),
        CardioSession(
            machineType = CardioMachineType.ROWING_MACHINE,
            dateString = FitnessDateUtils.getDateDaysAgo(3),
            durationMinutes = 25,
            distanceKm = 5.5,
            caloriesBurned = 325,
            avgHeartRateBpm = 156,
            rowingSplitSeconds500m = 120,
            rowingStrokesPerMin = 29,
            rowingDamperSetting = 6,
            rowingWatts = 230
        ),
        CardioSession(
            machineType = CardioMachineType.STAIR_CLIMBER,
            dateString = FitnessDateUtils.getDateDaysAgo(4),
            durationMinutes = 25,
            distanceKm = 2.4,
            caloriesBurned = 280,
            avgHeartRateBpm = 148,
            stairFloorsClimbed = 105
        ),
        CardioSession(
            machineType = CardioMachineType.ELLIPTICAL,
            dateString = FitnessDateUtils.getDateDaysAgo(5),
            durationMinutes = 40,
            distanceKm = 6.2,
            caloriesBurned = 340,
            avgHeartRateBpm = 130,
            ellipticalResistanceLevel = 8,
            ellipticalStridesPerMin = 132
        ),
        CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = FitnessDateUtils.getDateDaysAgo(6),
            durationMinutes = 30,
            distanceKm = 5.2,
            caloriesBurned = 390,
            avgHeartRateBpm = 164,
            treadmillSpeedKmh = 10.4,
            treadmillInclinePercent = 2.0
        ),
        CardioSession(
            machineType = CardioMachineType.OUTDOOR_RUN,
            dateString = FitnessDateUtils.getDateDaysAgo(7),
            durationMinutes = 52,
            distanceKm = 8.4,
            caloriesBurned = 670,
            avgHeartRateBpm = 152
        ),
        CardioSession(
            machineType = CardioMachineType.STAIR_CLIMBER,
            dateString = FitnessDateUtils.getDateDaysAgo(8),
            durationMinutes = 20,
            distanceKm = 2.0,
            caloriesBurned = 250,
            avgHeartRateBpm = 146,
            stairFloorsClimbed = 90
        ),
        CardioSession(
            machineType = CardioMachineType.STATIONARY_BIKE,
            dateString = FitnessDateUtils.getDateDaysAgo(9),
            durationMinutes = 30,
            distanceKm = 12.0,
            caloriesBurned = 330,
            avgHeartRateBpm = 148,
            bikeCadenceRpm = 90,
            bikeResistanceLevel = 10
        ),
        CardioSession(
            machineType = CardioMachineType.OUTDOOR_RUN,
            dateString = FitnessDateUtils.getDateDaysAgo(10),
            durationMinutes = 28,
            distanceKm = 5.1,
            caloriesBurned = 420,
            avgHeartRateBpm = 158
        ),
        CardioSession(
            machineType = CardioMachineType.ROWING_MACHINE,
            dateString = FitnessDateUtils.getDateDaysAgo(11),
            durationMinutes = 20,
            distanceKm = 4.8,
            caloriesBurned = 280,
            avgHeartRateBpm = 158,
            rowingSplitSeconds500m = 118,
            rowingStrokesPerMin = 31,
            rowingDamperSetting = 7,
            rowingWatts = 240
        ),
        CardioSession(
            machineType = CardioMachineType.ELLIPTICAL,
            dateString = FitnessDateUtils.getDateDaysAgo(12),
            durationMinutes = 35,
            distanceKm = 5.5,
            caloriesBurned = 290,
            avgHeartRateBpm = 124,
            ellipticalResistanceLevel = 7,
            ellipticalStridesPerMin = 128
        ),
        CardioSession(
            machineType = CardioMachineType.TREADMILL,
            dateString = FitnessDateUtils.getDateDaysAgo(13),
            durationMinutes = 45,
            distanceKm = 4.8,
            caloriesBurned = 440,
            avgHeartRateBpm = 132,
            treadmillSpeedKmh = 5.0,
            treadmillInclinePercent = 12.0
        ),
        CardioSession(
            machineType = CardioMachineType.OUTDOOR_RUN,
            dateString = FitnessDateUtils.getDateDaysAgo(14),
            durationMinutes = 60,
            distanceKm = 10.2,
            caloriesBurned = 820,
            avgHeartRateBpm = 150
        )
    )

    val gymMachinesCatalog = listOf(
        // Free Weights Option (Dumbbells, Barbells, Kettlebells)
        GymMachineInfo(
            machineName = "Free Weights",
            everydayAlias = "Dumbbells / Barbells / Kettlebells",
            targetMuscle = "Full Body & Targeted Muscle Groups",
            category = ExerciseCategory.ARMS,
            tip = "Versatile free-weight dumbbells, barbells, and kettlebells for progressive overload, unilateral stability, and full range of motion."
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
            machineName = "Reverse Pec Deck Machine",
            everydayAlias = "Rear Delt Fly Station",
            targetMuscle = "Rear Deltoids & Upper Back",
            category = ExerciseCategory.SHOULDERS,
            tip = "Isolates the posterior deltoids and rhomboids through horizontal abduction."
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
        GymMachineInfo(
            machineName = "Front Raise Machine",
            everydayAlias = "Deltoid Raise Station",
            targetMuscle = "Anterior Deltoids",
            category = ExerciseCategory.SHOULDERS,
            tip = "Isolates the anterior deltoids through guided forward elevation."
        ),
        GymMachineInfo(
            machineName = "Shrug Machine",
            everydayAlias = "Trap Shrug Station",
            targetMuscle = "Upper Trapezius",
            category = ExerciseCategory.SHOULDERS,
            tip = "Overloads the upper trapezius muscles with vertical shoulder elevation."
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
            machineName = "High Row Machine",
            everydayAlias = "Iso-Lateral High Row Station",
            targetMuscle = "Upper Lats & Mid-Back",
            category = ExerciseCategory.BACK,
            tip = "Provides a high-to-low pulling trajectory for comprehensive upper lat and rhomboid recruitment."
        ),
        GymMachineInfo(
            machineName = "Chest-Supported T-Bar Row Machine",
            everydayAlias = "T-Bar Row Station",
            targetMuscle = "Lats & Mid-Back Thickness",
            category = ExerciseCategory.BACK,
            tip = "Provides a semi-fixed leverage path for heavy back-building rows without lumbar strain."
        ),
        GymMachineInfo(
            machineName = "Assisted Pull-Up & Dip Machine",
            everydayAlias = "Gravitron Counterweight Station",
            targetMuscle = "Lats, Upper Back, Chest & Triceps",
            category = ExerciseCategory.BACK,
            tip = "Uses a counterweight knee pad or platform to assist with bodyweight pull-ups and dips."
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
            everydayAlias = "Incline Sled Press",
            targetMuscle = "Quadriceps, Hamstrings & Glutes",
            category = ExerciseCategory.LEGS,
            tip = "A heavy compound machine targeting the quadriceps, hamstrings, and glutes."
        ),
        GymMachineInfo(
            machineName = "Vertical Leg Press Machine",
            everydayAlias = "Vertical Sled Press",
            targetMuscle = "Glutes & Quadriceps",
            category = ExerciseCategory.LEGS,
            tip = "Positions the lifter flat on their back pressing directly upward against gravity."
        ),
        GymMachineInfo(
            machineName = "Hack Squat Machine",
            everydayAlias = "Angled Sled Squat",
            targetMuscle = "Quadriceps & Glutes",
            category = ExerciseCategory.LEGS,
            tip = "A sled-based machine that mimics a deep barbell squat while supporting the back and shoulders."
        ),
        GymMachineInfo(
            machineName = "Pendulum Squat Machine",
            everydayAlias = "Arc Squat Sled",
            targetMuscle = "Quadriceps & VMO",
            category = ExerciseCategory.LEGS,
            tip = "Curving arc resistance curve that minimizes lower back shear while maximizing quad stretch."
        ),
        GymMachineInfo(
            machineName = "Belt Squat Machine",
            everydayAlias = "Pit Shark Squat",
            targetMuscle = "Quads & Glutes without Spinal Load",
            category = ExerciseCategory.LEGS,
            tip = "Loads the lower body directly through a hip belt with zero axial spinal compression."
        ),
        GymMachineInfo(
            machineName = "Sissy Squat Machine",
            everydayAlias = "Sissy Squat Bench",
            targetMuscle = "Quadriceps Isolation",
            category = ExerciseCategory.LEGS,
            tip = "Locks feet and calves for strict deep knee flexion and extreme rectus femoris loading."
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
            machineName = "Standing Single Leg Curl Machine",
            everydayAlias = "Standing Hamstring Roller",
            targetMuscle = "Hamstrings",
            category = ExerciseCategory.LEGS,
            tip = "Isolates the hamstring of each leg independently with an upright stance."
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
            machineName = "Donkey Calf Raise Machine",
            everydayAlias = "Hinged Calf Station",
            targetMuscle = "Calves",
            category = ExerciseCategory.LEGS,
            tip = "Hinged hip position that puts gastrocnemius under maximum stretch under load."
        ),
        GymMachineInfo(
            machineName = "Glute-Ham Developer (GHD)",
            everydayAlias = "Posterior Chain Station",
            targetMuscle = "Glutes, Hamstrings & Lower Back",
            category = ExerciseCategory.LEGS,
            tip = "Specialized station for posterior chain, glute, and hamstring strength and hip extension."
        ),
        GymMachineInfo(
            machineName = "Hip Thrust Machine",
            everydayAlias = "Glute Drive / Booty Builder",
            targetMuscle = "Gluteus Maximus",
            category = ExerciseCategory.LEGS,
            tip = "Dedicated station with padded lap belt for heavy, comfortable glute hip thrusts."
        ),
        GymMachineInfo(
            machineName = "Glute Kickback Machine",
            everydayAlias = "Standing Butt Blaster",
            targetMuscle = "Gluteus Maximus",
            category = ExerciseCategory.LEGS,
            tip = "Standing machine with a rear push pad to isolate each glute independently."
        ),

        // Arms & Core
        GymMachineInfo(
            machineName = "Preacher Curl Machine",
            everydayAlias = "Bicep Curl Station",
            targetMuscle = "Biceps",
            category = ExerciseCategory.ARMS,
            tip = "Isolates the biceps with complete elbow stabilization and constant tension."
        ),
        GymMachineInfo(
            machineName = "Bicep Curl Cable Machine",
            everydayAlias = "Dual Arm Curl Station",
            targetMuscle = "Biceps & Brachialis",
            category = ExerciseCategory.ARMS,
            tip = "Dual-cable curl station delivering constant resistance through the entire curling arc."
        ),
        GymMachineInfo(
            machineName = "Triceps Extension Machine",
            everydayAlias = "Seated Triceps Pushdown Machine",
            targetMuscle = "Triceps",
            category = ExerciseCategory.ARMS,
            tip = "Isolates the triceps muscle group with guided elbow flexion and extension."
        ),
        GymMachineInfo(
            machineName = "Triceps Dip Machine",
            everydayAlias = "Seated Dip Station",
            targetMuscle = "Triceps & Chest",
            category = ExerciseCategory.ARMS,
            tip = "Seated plate-loaded or pin-selected dip machine for heavy triceps overload."
        ),
        GymMachineInfo(
            machineName = "Overhead Triceps Extension Machine",
            everydayAlias = "French Press Station",
            targetMuscle = "Triceps Long Head",
            category = ExerciseCategory.ARMS,
            tip = "Isolates the long head of the triceps with overhead guided handles."
        ),
        GymMachineInfo(
            machineName = "Forearm Wrist Roller Station",
            everydayAlias = "Grip & Wrist Roller",
            targetMuscle = "Forearms & Grip Strength",
            category = ExerciseCategory.ARMS,
            tip = "Spindle-mounted wrist roller for intense forearm flexor and extensor burn."
        ),
        GymMachineInfo(
            machineName = "Abdominal Crunch Machine",
            everydayAlias = "Core Roller / Seated Ab Machine",
            targetMuscle = "Rectus Abdominis",
            category = ExerciseCategory.CORE,
            tip = "Guided seated machine targeting the rectus abdominis through spinal flexion."
        ),
        GymMachineInfo(
            machineName = "Rotary Torso Machine",
            everydayAlias = "Oblique Twister Machine",
            targetMuscle = "Obliques & Rotational Core",
            category = ExerciseCategory.CORE,
            tip = "Targets the obliques and rotational core strength with guided twisting motion."
        ),
        GymMachineInfo(
            machineName = "Decline Abdominal Sit-Up Bench",
            everydayAlias = "Slanted Crunch Board",
            targetMuscle = "Upper & Lower Abdominals",
            category = ExerciseCategory.CORE,
            tip = "Adjustable decline bench for weighted abdominal sit-ups and trunk flexion."
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
            everydayAlias = "Vertical Knee Raise Station",
            targetMuscle = "Abdominals, Lats & Triceps",
            category = ExerciseCategory.CORE,
            tip = "Bodyweight station for hanging leg/knee raises, dips, and pull-ups."
        ),
        GymMachineInfo(
            machineName = "Olympic Flat Bench Press Station",
            everydayAlias = "Standard Barbell Bench",
            targetMuscle = "Pectorals & Triceps",
            category = ExerciseCategory.CHEST,
            tip = "Standard Olympic bench with safety uprights for heavy flat barbell bench press."
        ),
        GymMachineInfo(
            machineName = "Olympic Incline Bench Press Station",
            everydayAlias = "Incline Barbell Bench",
            targetMuscle = "Upper Pectorals & Shoulders",
            category = ExerciseCategory.CHEST,
            tip = "Fixed incline Olympic bench for heavy upper chest barbell pressing."
        ),
        GymMachineInfo(
            machineName = "Olympic Decline Bench Press Station",
            everydayAlias = "Decline Barbell Bench",
            targetMuscle = "Lower Pectorals",
            category = ExerciseCategory.CHEST,
            tip = "Fixed decline Olympic bench with ankle supports for lower chest barbell pressing."
        ),
        GymMachineInfo(
            machineName = "Preacher Curl Bench",
            everydayAlias = "Free Weight Scott Bench",
            targetMuscle = "Biceps",
            category = ExerciseCategory.ARMS,
            tip = "Angled arm rest bench for strict dumbbell and EZ-bar bicep curls."
        )
    )

    val defaultSupplements = listOf(
        SupplementItem(
            name = "Multivitamin Complex",
            dosage = "1 tablet with breakfast",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "With food and full glass of water"
        ),
        SupplementItem(
            name = "Omega-3 Fish Oil",
            dosage = "1000 mg EPA/DHA",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "Joint health and cognitive support"
        ),
        SupplementItem(
            name = "Vitamin D3 + K2",
            dosage = "5000 IU",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "Fat-soluble, take with healthy fats"
        ),
        SupplementItem(
            name = "Creatine Monohydrate",
            dosage = "5 g micronized powder",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "Mix with water or post-workout shake"
        ),
        SupplementItem(
            name = "L-Citrulline Malate 2:1",
            dosage = "6 g powder",
            timing = SupplementTiming.MORNING,
            isTaken = false,
            notes = "30-45 mins pre-workout for nitric oxide pump & muscular endurance"
        ),
        SupplementItem(
            name = "Electrolyte Hydration Salts",
            dosage = "1 stick pack in 750ml",
            timing = SupplementTiming.MORNING,
            isTaken = false,
            notes = "Sodium, Potassium, Magnesium for intra-workout hydration"
        ),
        SupplementItem(
            name = "Magnesium Glycinate",
            dosage = "400 mg",
            timing = SupplementTiming.EVENING,
            isTaken = false,
            notes = "30-45 minutes before bedtime for muscle recovery & sleep quality"
        ),
        SupplementItem(
            name = "Zinc Picolinate",
            dosage = "25 mg",
            timing = SupplementTiming.EVENING,
            isTaken = false,
            notes = "Immune support, take with evening meal"
        ),
        SupplementItem(
            name = "Ashwagandha KSM-66",
            dosage = "600 mg",
            timing = SupplementTiming.EVENING,
            isTaken = false,
            notes = "Cortisol modulation and relaxation"
        ),
        SupplementItem(
            name = "CoQ10 Ubiquinol",
            dosage = "100 mg",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "Mitochondrial energy production and cardiovascular health"
        ),
        SupplementItem(
            name = "Daily Probiotics Complex",
            dosage = "50 Billion CFU",
            timing = SupplementTiming.MORNING,
            isTaken = true,
            notes = "Gut microbiome, digestion & nutrient absorption"
        ),
        SupplementItem(
            name = "L-Theanine",
            dosage = "200 mg",
            timing = SupplementTiming.EVENING,
            isTaken = false,
            notes = "Synergistic with magnesium for restorative deep sleep"
        )
    )
}