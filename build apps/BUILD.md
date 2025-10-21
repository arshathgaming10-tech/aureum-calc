BUILD.md

This document explains how to prepare your Windows environment and build the Android app in this project.

Prerequisites
- JDK 17+ (OpenJDK 17 recommended)
- PowerShell
- Optional: Android Studio (recommended for beginners)

Recommended quick path (uses provided scripts)
1. Open PowerShell.
2. Run the SDK setup script (this downloads Android command-line tools to C:\Android\sdk for this session):

   .\scripts\setup-sdk.ps1

   If sdkmanager asks to accept licenses, run `sdkmanager --licenses` and accept them.

3. Build using the wrapper script:

   .\scripts\build.ps1

This will run `gradlew.bat assembleDebug` if the wrapper exists, otherwise it will try system Gradle.

Notes
- If you have Android Studio, open the project there and Android Studio will usually set up the SDK and Gradle wrapper automatically.
- If you prefer not to download SDK tools via the script, install them via Android Studio's SDK Manager instead.
- If you want me to add the Gradle wrapper JAR to the repo so you can build without installing Gradle, request "Add jar".

Troubleshooting
- If `sdkmanager` is not found after running `setup-sdk.ps1`, ensure your PowerShell session allowed downloads and extraction.
- If `gradlew.bat` exists but `gradle-wrapper.jar` is missing, run `gradle wrapper` if Gradle is installed, or open the project in Android Studio.

If anything fails, copy and paste the terminal output here and I will diagnose and fix it.
