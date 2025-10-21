package com.glassmorphism.calculator.util

/**
 * Simple offline currency rates helper. Rates are relative to USD.
 * This is a stub for demonstration — replace with network-backed rates for production.
 */
object CurrencyRates {
    // sample rates relative to USD
    private val rates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "INR" to 83.5,
        "JPY" to 154.3
    )

    fun availableCurrencies(): List<String> = rates.keys.sorted()

    fun convert(amount: Double, from: String, to: String): Double {
        val fromRate = rates[from] ?: 1.0
        val toRate = rates[to] ?: 1.0
        // convert from -> USD -> to
        val usd = amount / fromRate
        return usd * toRate
    }
}
