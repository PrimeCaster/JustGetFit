# JustGetFit 🏋️‍♂️🥗⚡

[![Latest Release](https://img.shields.io/badge/Release-v0.5%20Beta-blue.svg?logo=github&style=flat-square)](https://github.com/PrimeCaster/JustGetFit/releases/tag/v0.5)
[![Android CI & Build](https://github.com/PrimeCaster/JustGetFit/actions/workflows/android.yml/badge.svg)](https://github.com/PrimeCaster/JustGetFit/actions/workflows/android.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Design-Material%203-blueviolet.svg)](https://m3.material.io)
[![API](https://img.shields.io/badge/API-24%2B-orange.svg)](https://android.com)

**JustGetFit** is an all-in-one modern Android fitness, strength training, cardio equipment telemetry, recovery, and precision AI nutrition tracking application built natively with Jetpack Compose, Kotlin Coroutines, and Material 3.

---

## 📲 Download the v0.5 Beta APK

Download and install the latest Beta APK directly to your Android device:

[![Download JustGetFit APK](https://img.shields.io/badge/Download-JustGetFit--v0.5--beta.apk-success?style=for-the-badge&logo=android)](https://github.com/PrimeCaster/JustGetFit/releases/download/v0.5/JustGetFit-v0.5-beta.apk)

> **Installation Note:** Since this is a direct build release APK, enable *"Install unknown apps"* for your browser or file manager in Android Settings when prompted.

---

## 🌟 What's New in v0.5 Beta

### 1. 🎯 Dedicated Targets & Goals Hub
* **Interactive Target Categories**: Segmented sub-navigation bar (`All`, `📏 Body & Measurements`, `🥗 Diet & Hydration`, `💊 Supplements Routine`) for immediate, clutter-free access.
* **Weight Goal Trajectory**: Real-time interactive weight target slider and starting/current/target trajectory progress bar.
* **Circumference Evolution (3-in-1 Graph)**: Multi-curve tracking for Waist, Chest, and Arms simultaneously in a unified visualization.
* **Endurance & Cardio Telemetry**: Equipment output tracking across Treadmill, Stationary Bike, Rowing Erg, and Elliptical.
* **Macronutrient & Caloric Deficit Planner**: Real-time Protein, Carbs, and Fats allocation adjusters with Daily vs. Weekly switch.
* **Hydration Goal Stepper**: Quick-increment water targets with daily progress telemetry.
* **Daily Supplement Regimen**: Checklists for Morning and Evening regimens with dynamic add/edit/delete capabilities.

### 2. 🗓️ Independent Date Navigation
* Workouts and Nutrition now feature completely decoupled date navigation (`[ ◀ ] Today [ ▶ ]`).
* Review and log past strength workouts (up to 14 days of contiguous historical data) without resetting or altering your daily nutrition diary, and vice versa.

### 3. 🎨 Executive "Pro UI" & Athletic Palettes
* **`💼 Pro UI`** *(Default)*: Engineered around clean, glare-free executive aesthetics (Linear Pro / GitHub Dark / Raycast) with soft precision azure (`#60a5fa`), sea-glass mint, and deep obsidian graphite.
* **`🏁 Forza Motorsport`**: Inspired by authentic racing telemetry with apex amber/gold highlights and smoked carbon glass.
* **`🏎️ Forza Telemetry`**: Electric racing cyan border styling and high-contrast telemetry indicators.
* **`⚙️ Stealth Titanium`**: Minimalist monochrome automotive cockpit (brushed platinum and cool gunmetal).
* **`🌿 Nordic Sage`**: Natural, calming wellness aesthetic with matte eucalyptus and forest slate.
* **`🏔️ Alpine Cobalt`**: Precision sports cockpit telemetry in crisp glacier blue and smoked navy.
* **`🏎️ Tuscan Copper`**: Luxury chronometer bronze and brushed terracotta.

### 4. 📊 14-Day Pre-Populated Testing Datasets
* Out-of-the-box multi-record datasets across all screens:
  * 14 days of logged strength exercises (chest press, squats, deadlifts, pull-ups, rows, shoulder press).
  * 14 days of breakfast, lunch, dinner, and snack meal diaries with full macro calculations.
  * 14 days of cardio equipment sessions (distance, duration, pace, calories).
  * 14 days of water intake telemetry and transformation slider photos.

---

## ✨ Key Feature Modules

### 🏋️‍♂️ Strength Workout Logging
* **Standardized Equipment Nomenclature**: `<Technical Name> (<Common Name>) (<Target Muscle Group>)` (e.g., *Chest Press Machine (Seated Chest Press) (Pectoralis Major, Deltoids & Triceps)*).
* **Free Weights & Dumbbells**: Fully integrated free-weight library with muscle-group tags.
* **Dual Selection Guard**: Prevents incomplete logs by requiring both machine and exercise movement selection.
* **Full In-Place Editing**: Edit sets, weights (kg), reps, RPE, and notes on the fly.
* **Interactive Rest Timer**: Built-in 60s / 90s / 120s rest interval timer with visual progress indicator.

### 🥗 AI Precision Nutrition & Macro Calculator
* **Natural Language Logging**: Type everyday meal descriptions (e.g., *"3 scrambled eggs with 1 slice sourdough toast and half an avocado"*).
* **Dual Provider Engine**:
  * **Google Gemini API**: Structured JSON schema output via `gemini-2.5-flash`.
  * **OpenAI ChatGPT API**: Structured JSON completion via `gpt-4o-mini`.
* **Complete Macro Diagnostics**: Instant computation of Calories, Protein (g), Net Carbohydrates (g), Fats (g), and Fiber (g).
* **Custom Meal Catalog**: Save favorite meals and recipes for single-tap logging.

### 🏃 Cardio Equipment Telemetry
* Multi-machine support: Treadmill, Stationary Bike, Rowing Ergometer, Elliptical, and Stairmaster.
* Logs distance (km), duration (min), pace (min/km), speed (km/h or mph), and active caloric expenditure.

---

## 🛠️ Architecture & Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin 2.0 |
| **UI Framework** | Jetpack Compose (Declarative UI) with Material 3 |
| **Architecture** | Unidirectional Data Flow (UDF) / Clean Architecture / Repository Pattern |
| **Concurrency** | Kotlin Coroutines & `StateFlow` |
| **Networking** | OkHttp 4 / Retrofit / Moshi |
| **AI Integration** | Google Gemini REST API & OpenAI Chat Completions API |
| **Testing** | JUnit 4, Robolectric, AndroidX Test |
| **CI/CD** | GitHub Actions (Automated Unit Tests & APK Compilation) |

---

## 🚀 Building from Source

### Prerequisites
* Android Studio Hedgehog (2023.1.1) or newer
* JDK 17
* Android SDK 36 (minSdk: 24, targetSdk: 36)

### Clone & Compile
```bash
git clone https://github.com/PrimeCaster/JustGetFit.git
cd JustGetFit
```

#### Run Unit Tests:
```bash
./gradlew test
```

#### Build Debug APK:
```bash
./gradlew assembleDebug
```
The compiled APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🔒 Security & Privacy

* **Zero Hardcoded Secrets**: No API keys, credentials, or personal tokens are stored in the codebase or packaged into the APK.
* **Client-Side Key Management**: AI API keys are optionally entered by the user at runtime in local storage and never transmitted to external telemetry servers.
* **Strict `.gitignore`**: Keystores, `.env` files, and local SDK configurations are strictly ignored from version control.

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
