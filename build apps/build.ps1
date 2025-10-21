# PowerShell helper to check Gradle and run the wrapper
# Usage: .\build.ps1 [-Task assembleDebug]
param(
    [string]$Task = "assembleDebug"
)

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $root

Write-Host "Project root: $root"

# Check for gradle on PATH
$gradle = Get-Command gradle -ErrorAction SilentlyContinue
if ($null -ne $gradle) {
    Write-Host "Gradle found at: $($gradle.Path)"
} else {
    Write-Host "Gradle not found on PATH."
}

# Run wrapper if present
$wrapper = Join-Path $root 'gradlew.bat'
if (Test-Path $wrapper) {
    Write-Host "Running wrapper: $wrapper $Task"
    & $wrapper $Task
} else {
    Write-Host "No gradle wrapper found. To generate it, install Gradle and run 'gradle wrapper' from this directory, or open this project in Android Studio which will generate it for you."
}
