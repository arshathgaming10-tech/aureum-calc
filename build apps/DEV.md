Developer Guide — Glassmorphism Calculator

Project structure (recommended)

- app/src/main/java/com/glassmorphism/calculator
  - ui/components (GlassmorphicButton.kt, DisplayPanel.kt, HistoryPanel.kt)
  - ui/theme (Color.kt, Theme.kt, Type.kt)
  - viewmodel (CalculatorState.kt or CalculatorViewModel.kt)
  - MainActivity.kt

Core code snippets

1) ViewModel / CalculatorState (already present: `app/src/main/java/com/glassmorphism/calculator/CalculatorState.kt`)

Key behaviors:
- Basic operations: + - * /
- Decimal handling and single decimal prevention
- equals (onEqualsClick) and clear (onClearClick)
- history recording and export-ready strings

2) Recommended alternative ViewModel (compact example)

```kotlin
// Place in viewmodel/CalculatorViewModel.kt
package com.glassmorphism.calculator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var displayText by mutableStateOf("0")
        private set

    private var leftOperand: Double? = null
    private var pendingOp: Char? = null
    private var enteringNumber = false
    private var currentInput = "0"

    fun inputDigit(d: Char) { /* ... */ }
    fun inputDecimal() { /* ... */ }
    fun performOperation(op: Char) { /* ... */ }
    fun evaluate() { /* ... */ }
    fun clearAll() { /* ... */ }
}
```

3) Glass-styled button (Jetpack Compose)

`app/src/main/java/com/glassmorphism/calculator/ui/components/GlassmorphicButton.kt` already implements a glass button.

Unit tests

- I added `app/src/test/java/com/glassmorphism/calculator/CalculatorStateTest.kt` with basic arithmetic tests.
- To run tests locally you need the Gradle wrapper JAR or a system Gradle installation.

Build & test (local)

```powershell
cd "C:\Users\DELL\OneDrive\Desktop\PROGRAM lunguage\build apps"
# If you have Gradle installed
gradle wrapper
# Then run tests
.\gradlew.bat test
```

If you prefer I can add the `gradle-wrapper.jar` into `gradle/wrapper/` in the repo — say "Add jar" and I'll include it.

Extra notes

- Use Locale-aware number formatting only for display; keep internal calculations locale-agnostic.
- For backdrop blur on Compose, prefer RenderEffect on API 31+. Provide a fallback solid translucent surface for older devices.
