<#
Build helper: runs the Gradle wrapper to assemble debug APK. Ensures we're in the project root.
Usage: .\scripts\build.ps1
#>

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

if (Test-Path .\gradlew.bat) {
    Write-Host "Running wrapper: .\gradlew.bat assembleDebug"
    .\gradlew.bat assembleDebug
} else {
    Write-Host "No gradlew.bat found. Trying system 'gradle assembleDebug'"
    gradle assembleDebug
}
