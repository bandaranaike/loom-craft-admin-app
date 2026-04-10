package com.example.loomcraftadmin.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    fun format(amount: Double?, currencyCode: String = "LKR"): String {
        if (amount == null) return "--"

        return try {
            val normalizedCurrencyCode = currencyCode.ifBlank { "LKR" }.uppercase()
            val currency = Currency.getInstance(normalizedCurrencyCode)
            val locale = when (normalizedCurrencyCode) {
                "LKR" -> Locale("en", "LK")
                "USD" -> Locale.US
                "EUR" -> Locale.FRANCE
                "GBP" -> Locale.UK
                else -> Locale("en", "LK")
            }

            NumberFormat.getCurrencyInstance(locale).apply {
                this.currency = currency
            }.format(amount)
        } catch (_: Exception) {
            "${currencyCode.ifBlank { "LKR" }.uppercase()} ${String.format(Locale.US, "%.2f", amount)}"
        }
    }
}
