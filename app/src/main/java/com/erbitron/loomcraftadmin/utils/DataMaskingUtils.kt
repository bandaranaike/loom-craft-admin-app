package com.erbitron.loomcraftadmin.utils

object DataMaskingUtils {
    /**
     * Masks a name by keeping the first letter and replacing the rest with asterisks,
     * or keeping first and last names' first letters.
     * Example: "John Doe" -> "J*** D**"
     */
    fun maskName(name: String?): String {
        if (name.isNullOrBlank()) return "N/A"
        return name.split(" ").joinToString(" ") { part ->
            if (part.length <= 1) part
            else part.first() + "*".repeat(part.length - 1)
        }
    }

    /**
     * Masks a phone number by keeping the last 4 digits.
     * Example: "+1234567890" -> "******7890"
     */
    fun maskPhone(phone: String?): String {
        if (phone.isNullOrBlank()) return "N/A"
        return if (phone.length > 4) {
            "*".repeat(phone.length - 4) + phone.takeLast(4)
        } else {
            phone
        }
    }

    /**
     * Masks an address by showing only the first few characters.
     * Example: "123 Main St, Springfield" -> "123 Ma*********"
     */
    fun maskAddress(address: String?): String {
        if (address.isNullOrBlank()) return "N/A"
        return if (address.length > 6) {
            address.take(6) + "*".repeat(address.length - 6)
        } else {
            address
        }
    }
}
