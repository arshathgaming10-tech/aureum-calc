Gradle wrapper helper

This project contains a `gradle/wrapper/gradle-wrapper.properties` and lightweight `gradlew`/`gradlew.bat` helper scripts.

Important notes:
- The `gradle-wrapper.jar` binary is not included. To fully use `./gradlew` you must either:
  1) Generate the wrapper JAR locally by running a system Gradle once:
     - Install Gradle (https://gradle.org/install/) or via Chocolatey: `choco install gradle`.
     - Run `gradle wrapper` from the project root. This will create `gradle/wrapper/gradle-wrapper.jar`.
  2) Open the project in Android Studio which will automatically generate the wrapper files.

- Alternatively, if you have Gradle installed on PATH, the helper `gradlew`/`gradlew.bat` will forward commands to it.

Example commands (PowerShell):

```powershell
cd "C:\Users\DELL\OneDrive\Desktop\PROGRAM lunguage\build apps"
# If you installed Gradle
gradle wrapper
# Then use the wrapper
.\gradlew.bat test
```

If you'd like, I can add a pre-built `gradle-wrapper.jar` into `gradle/wrapper/` for you, but note it is a binary (~ a few MBs). Say "add jar" and I'll include it in the repo.
