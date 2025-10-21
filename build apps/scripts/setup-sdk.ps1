<#
PowerShell helper: download Android command-line tools, install required SDK components, and set session environment variables.
Run this script from a PowerShell window (normal user). It does NOT permanently modify PATH; it sets env vars for the session.

Usage:
    .\scripts\setup-sdk.ps1

If you prefer Android Studio, open SDK Manager there instead.
#>

$ErrorActionPreference = 'Stop'

$SdkRoot = 'C:\Android\sdk'
Write-Host "Using SDK path: $SdkRoot"

# Create directory
New-Item -ItemType Directory -Force -Path $SdkRoot | Out-Null

# Download command-line tools (Windows)
$zip = "$env:TEMP\cmdline-tools.zip"
$downloadUrl = 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip'
Write-Host "Downloading command-line tools from $downloadUrl..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $zip -UseBasicParsing

# Extract
$extractDir = "$env:TEMP\cmdline-tools-extract"
Remove-Item -Recurse -Force -ErrorAction SilentlyContinue $extractDir
Expand-Archive -Path $zip -DestinationPath $extractDir -Force

# Place files under sdk\cmdline-tools\latest
$dest = Join-Path $SdkRoot 'cmdline-tools\latest'
New-Item -ItemType Directory -Force -Path $dest | Out-Null

if (Test-Path "$extractDir\cmdline-tools") {
    Move-Item -Force "$extractDir\cmdline-tools\*" "$dest\"
} else {
    Move-Item -Force "$extractDir\*" "$dest\"
}

# Set session environment variables
$env:ANDROID_SDK_ROOT = $SdkRoot
$env:PATH = "$SdkRoot\cmdline-tools\latest\bin;$SdkRoot\platform-tools;$env:PATH"

Write-Host "Verifying sdkmanager availability..."
Get-Command sdkmanager -ErrorAction SilentlyContinue | ForEach-Object { Write-Host $_.Source }

Write-Host "sdkmanager version:"
sdkmanager --version

# Install required components
Write-Host "Installing platform-tools, platforms;android-34, build-tools;34.0.0..."
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

Write-Host "If sdkmanager requested licenses, run: sdkmanager --licenses"
Write-Host "SDK setup complete for this session. You can now run the build script: .\scripts\build.ps1"
