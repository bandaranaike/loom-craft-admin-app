package com.example.loomcraftadmin.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    fun format(amount: Double?, currencyCode: String = "INR"): String {
        if (amount == null) return "—"
        
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")) // Default to India for INR
            val currency = Currency.getInstance(currencyCode)
            
            // Adjust locale based on currency for better symbol placement
            val locale = when (currencyCode.uppercase()) {
                "USD" -> Locale.US
                "EUR" -> Locale.FRANCE
                "GBP" -> Locale.UK
                else -> Locale("en", "IN")
            }
            
            val localizedFormat = NumberFormat.getCurrencyInstance(locale)
            localizedFormat.currency = currency
            localizedFormat.format(amount)
        } catch (e: Exception) {
            "$currencyCode ${String.format("%.2f", amount)}"
        }
    }
}
