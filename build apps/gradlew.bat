@echo off
REM Lightweight gradlew.bat - uses system gradle if available
where gradle >nul 2>nul
IF %ERRORLEVEL% EQU 0 (
  gradle %*
) ELSE (
  echo Gradle is not installed. Please install Gradle or open the project in Android Studio to generate the wrapper.
  exit /B 1
)
