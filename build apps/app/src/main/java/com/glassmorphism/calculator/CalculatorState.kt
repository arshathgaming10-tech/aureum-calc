package com.glassmorphism.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.*

class CalculatorState : ViewModel() {
    var display by mutableStateOf("0")
        private set
    
    var operation by mutableStateOf<Operation?>(null)
        private set
    
    var previousNumber by mutableStateOf<Double?>(null)
        private set
    
    var waitingForNewNumber by mutableStateOf(false)
        private set

    val history = mutableStateListOf<String>()

    fun onNumberClick(number: String) {
        if (waitingForNewNumber) {
            display = number
            waitingForNewNumber = false
        } else {
            display = if (display == "0") number else display + number
        }
    }

    fun onDecimalClick() {
        if (waitingForNewNumber) {
            display = "0."
            waitingForNewNumber = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    fun onOperationClick(operation: Operation) {
        val currentNumber = display.toDoubleOrNull() ?: return
        
        if (this.operation != null && !waitingForNewNumber) {
            calculate()
        }
        
        this.operation = operation
        previousNumber = currentNumber
        waitingForNewNumber = true
    }

    fun onEqualsClick() {
        if (operation != null && previousNumber != null && !waitingForNewNumber) {
            calculate()
        }
    }

    fun onClearClick() {
        display = "0"
        operation = null
        previousNumber = null
        waitingForNewNumber = false
    }

    fun clearHistory() {
        history.clear()
    }

    /**
     * Return a snapshot copy of the current history list. Useful for undo operations.
     */
    fun getHistorySnapshot(): List<String> = history.toList()

    /**
     * Replace the current history contents with the provided list.
     */
    fun replaceHistory(newHistory: List<String>) {
        history.clear()
        history.addAll(newHistory)
    }

    /**
     * Convert current history list into pair list of expression/result for Room insertion
     */
    fun toEntryPairs(): List<Pair<String, String>> {
        return history.map { entry ->
            val parts = entry.split(" = ")
            val expr = parts.getOrNull(0) ?: entry
            val res = parts.getOrNull(1) ?: ""
            expr to res
        }
    }

    /**
     * Export history as CSV. Each entry in history is expected to be in the form
     * "expression = result" (or special forms like "√ 9 = 3"). This method
     * will produce a simple CSV with two columns: Expression,Result
     */
    fun exportHistoryCsv(): String {
        if (history.isEmpty()) return ""
        val sb = StringBuilder()
        sb.append("Expression,Result\n")
        history.forEach { entry ->
            val parts = entry.split(" = ")
            val expr = parts.getOrNull(0)?.replace("\"", "\"\"") ?: ""
            val res = parts.getOrNull(1)?.replace("\"", "\"\"") ?: ""
            sb.append('"').append(expr).append('",')
            sb.append('"').append(res).append('"').append('\n')
        }
        return sb.toString()
    }

    fun onDeleteClick() {
        if (display.length > 1) {
            display = display.dropLast(1)
        } else {
            display = "0"
        }
    }

    private fun calculate() {
        val currentNumber = display.toDoubleOrNull() ?: return
        val prevNumber = previousNumber ?: return
        val op = operation ?: return

        val result = when (op) {
            Operation.ADD -> prevNumber + currentNumber
            Operation.SUBTRACT -> prevNumber - currentNumber
            Operation.MULTIPLY -> prevNumber * currentNumber
            Operation.DIVIDE -> if (currentNumber != 0.0) prevNumber / currentNumber else Double.NaN
            Operation.POWER -> prevNumber.pow(currentNumber)
            Operation.SQRT -> sqrt(prevNumber)
            Operation.PERCENTAGE -> prevNumber * (currentNumber / 100)
        }

        val resultString = if (result.isNaN() || result.isInfinite()) {
            "Error"
        } else {
            formatResult(result)
        }

        // Append to history if calculation is valid and meaningful
        val opSymbol = when (op) {
            Operation.ADD -> "+"
            Operation.SUBTRACT -> "−"
            Operation.MULTIPLY -> "×"
            Operation.DIVIDE -> "÷"
            Operation.POWER -> "^"
            Operation.SQRT -> "√"
            Operation.PERCENTAGE -> "%"
        }
        val expression = when (op) {
            Operation.SQRT -> "√ ${formatResult(prevNumber)} = $resultString"
            else -> "${formatResult(prevNumber)} $opSymbol ${formatResult(currentNumber)} = $resultString"
        }
        history.add(0, expression)

        display = resultString
        operation = null
        previousNumber = null
        waitingForNewNumber = true
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.8f", result).trimEnd('0').trimEnd('.')
        }
    }
}

enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER, SQRT, PERCENTAGE
}
