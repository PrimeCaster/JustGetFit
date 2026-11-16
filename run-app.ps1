# ==============================================================================
# JustGetFit Fast Launch Automation Script
# Usage: .\run-app.ps1
# ==============================================================================

$ErrorActionPreference = "Stop"
$AVD_NAME = "medium_phone"
$APP_PACKAGE = "com.aistudio.apexfit.vkmxpq"
$MAIN_ACTIVITY = "com.example.MainActivity"

$EMULATOR_EXE = if (Get-Command emulator -ErrorAction SilentlyContinue) { "emulator" } else { "D:\Android\emulator\emulator.exe" }
$ADB_EXE = if (Get-Command adb -ErrorAction SilentlyContinue) { "adb" } else { "D:\Android\platform-tools\adb.exe" }

Write-Host "🔍 [1/4] Checking connected devices..." -ForegroundColor Cyan
$devices = & $ADB_EXE devices
$emulator = $devices | Select-String "emulator-"

if (-not $emulator) {
    Write-Host "🚀 Launching emulator ($AVD_NAME)..." -ForegroundColor Yellow
    Start-Process $EMULATOR_EXE -ArgumentList "@$AVD_NAME"
    Write-Host "⏳ Waiting for emulator to boot..." -ForegroundColor Yellow
    & $ADB_EXE wait-for-device
    while ((& $ADB_EXE shell getprop sys.boot_completed).Trim() -ne "1") {
        Start-Sleep -Seconds 2
    }
    Write-Host "✅ Emulator is online and ready!" -ForegroundColor Green
} else {
    Write-Host "✅ Emulator is already running!" -ForegroundColor Green
}

Write-Host "🔨 [2/4] Building and installing debug APK..." -ForegroundColor Cyan
.\gradlew.bat installDebug

Write-Host "📱 [3/4] Launching JustGetFit on emulator..." -ForegroundColor Cyan
& adb shell am start -n "$APP_PACKAGE/$MAIN_ACTIVITY"

Write-Host "🎉 [4/4] JustGetFit is running on your screen!" -ForegroundColor Green

