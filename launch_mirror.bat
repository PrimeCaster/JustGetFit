@echo off
title JustGetFit Mobile Mirror
echo ==============================================
echo  Launching JustGetFit Live Screen Mirror
echo ==============================================
if exist "%USERPROFILE%\.android\scrcpy\scrcpy-win64-v5.0.1\scrcpy.exe" (
    "%USERPROFILE%\.android\scrcpy\scrcpy-win64-v5.0.1\scrcpy.exe" --window-title "JustGetFit Live Mobile"
) else (
    echo scrcpy not found in %%USERPROFILE%%\.android\scrcpy. Please install scrcpy.
)
if errorlevel 1 (
    echo.
    echo Make sure your phone is unlocked and USB debugging is enabled!
    pause
)
