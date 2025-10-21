package com.glassmorphism.calculator

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorStateTest {
    private lateinit var state: CalculatorState

    @Before
    fun setup() {
        state = CalculatorState()
    }

    @Test
    fun testAddition() {
        state.onNumberClick("1")
        state.onNumberClick("2") // 12
        state.onOperationClick(Operation.ADD)
        state.onNumberClick("3")
        state.onEqualsClick()
        assertEquals("15", state.display)
    }

    @Test
    fun testDecimalMultiply() {
        state.onNumberClick("2")
        state.onDecimalClick()
        state.onNumberClick("5") // 2.5
        state.onOperationClick(Operation.MULTIPLY)
        state.onNumberClick("4")
        state.onEqualsClick()
        assertEquals("10", state.display)
    }

    @Test
    fun testDivideByZero() {
        state.onNumberClick("7")
        state.onOperationClick(Operation.DIVIDE)
        state.onNumberClick("0")
        state.onEqualsClick()
        assertEquals("Error", state.display)
    }
}
