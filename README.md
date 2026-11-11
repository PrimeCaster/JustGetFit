# JustGetFit 🏋️‍♂️🥗⚡

[![Android CI & Build](https://github.com/PrimeCaster/JustGetFit/actions/workflows/android.yml/badge.svg)](https://github.com/PrimeCaster/JustGetFit/actions/workflows/android.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Design-Material%203-blueviolet.svg)](https://m3.material.io)
[![API](https://img.shields.io/badge/API-26%2B-orange.svg)](https://android.com)

**JustGetFit** is an all-in-one modern Android fitness, strength training, cardio, and precision AI nutrition tracking application built natively with Jetpack Compose, Kotlin Coroutines, and Material 3.

---

## ✨ Key Features

### 1. 🏋️ Strength Training & Gym Equipment Logging
- **Standardized Machine & Movement Naming**:
  - Automatically formatted as: `<Technical Name> (<Everyday Name>) (<Target Muscle Group>)` (e.g. *Chest Press Machine (Seated Chest Press) (Pectoralis Major, Deltoids & Triceps)*).
  - Includes **Free Dumbbells (Dumbbells / Free Weights) (Full Body & All Muscle Groups)** for barbell and free-weight sessions.
- **Dual Selection Validation**:
  - Enforces selection of both the gym equipment/machine and the exercise movement before logging, preventing incomplete workout entries.
- **Dropdown & Manual Fallback**:
  - Search from curated equipment and exercise presets, or switch to manual input with custom entries.
- **Fully Editable Logged Sets**:
  - Edit weights (kg), reps, RPE, gym machine, and exercise names at any point during or after a session.
- **Interactive Rest Timer**:
  - Between-set countdown timer (60s, 90s, 120s) with active progress visualization.

### 2. 🥗 AI Precision Nutrition & Macro Calculator
- **Natural Language Meal Analysis**:
  - Type or dictate everyday meals (e.g. *"2 scrambled eggs with 1 slice whole wheat toast and half an avocado"*).
- **Dual LLM Provider Support**:
  - **Google Gemini**: Powered by `gemini-2.5-flash` (or custom model override) using strict structured JSON schema output (`responseMimeType: application/json`).
  - **OpenAI ChatGPT**: Powered by `gpt-4o-mini` (or custom model override) using JSON completion mode.
- **Instant Macro Breakdown**:
  - Automatically calculates Calories, Protein (g), Carbohydrates (g), Fats (g), and Dietary Fiber (g).
- **100% Editable**:
  - Review and customize all macro values and serving sizes before saving directly to your daily meal diary (Breakfast, Lunch, Dinner, Snacks).
- **Custom Food Catalog**:
  - Save custom recipes and foods for one-tap future logging.

### 3. ⚙️ Unified Settings & Athletic Themes
- **Top Burger Menu (`☰`)**:
  - Convenient, persistent access across all screens (Today, Workouts, Nutrition, Progress).
- **Theme & Display Mode**:
  - Seamless toggle between **🌙 Dark Mode** and **☀️ Light Mode**.
  - **6 High-Performance Athletic Color Palettes**:
    - ⚡ **Emerald Vitality**: Electric Mint & Active Coral
    - 🌌 **Cyber Neon**: Electric Cyan & Cyber Violet
    - 🔥 **Solar Flare**: Crimson Blaze & Molten Amber
    - 🌊 **Ocean Titan**: Deep Cobalt & Glacier Teal
    - 🔮 **Midnight Amethyst**: Imperial Purple & Rose Gold
    - 🌲 **Nordic Forest**: Alpine Evergreen & Champagne Amber
- **LLM API Key Management**:
  - Masked input with password visibility toggle.
  - In-app **"Test Connection"** verification with live status diagnostics.
  - Zero hardcoded keys — keys remain securely in local app memory.
- **Configurable Daily Targets**:
  - Interactive stepper controls for Daily Calorie Target (kcal) and Daily Water Target (ml).

### 4. 🏃 Endurance, Cardio & Body Transformation
- **Cardio Equipment Tracking**:
  - Track distance (km), duration, pace, and speed across Treadmill, Stationary Bike, Rowing Machine, Stairmaster, and Elliptical.
- **Body Transformation Visualizer**:
  - Interactive Before & After comparison slider with front and side progress photo tracking.
- **Circumference & Weight Trend**:
  - Log body weight, waist, chest, arms, and hips with goal trajectory analytics.
- **Fitness Report Exporter**:
  - One-tap export of workout and nutrition records to PDF / CSV.

---

## 🛠️ Architecture & Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin 2.0 |
| **UI Framework** | Jetpack Compose (Declarative UI) with Material 3 |
| **Architecture** | Unidirectional Data Flow (UDF) / Clean Architecture / Repository Pattern |
| **Asynchronous** | Kotlin Coroutines & `StateFlow` |
| **Networking** | OkHttp 4 & Kotlin IO |
| **AI Integration** | Google Gemini REST API & OpenAI Chat Completions API |
| **Unit Testing** | JUnit 4 & AndroidX Test |
| **CI/CD** | GitHub Actions (Automated Test & Debug APK Build) |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35 (compileSdk: 35, minSdk: 26)

### Clone & Build
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

## 🔑 AI Key Setup (Optional)

To enable the **AI Nutrition Macro Calculator**:
1. Open the app on your phone or emulator.
2. Tap the **`☰` burger menu** in the top-right corner.
3. Scroll to **2. AI Nutrition Coach & LLM**.
4. Choose your preferred provider:
   - **Google Gemini**: Get a free key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey).
   - **OpenAI ChatGPT**: Get a key at [platform.openai.com/api-keys](https://platform.openai.com/api-keys).
5. Paste your key and tap **"Test Connection"** to verify.

> **Security Note:** API keys are never packaged inside the APK or committed to source control. They are entered securely at runtime and stored only on your device.

---

## 🛡️ License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
