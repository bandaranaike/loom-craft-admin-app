package com.example.loomcraftadmin.utils

import java.time.OffsetDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object OrderUiFormatter {
    private val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a", Locale.ENGLISH)
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
    private val knownPatterns = listOf(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    )

    fun formatTimestamp(value: String?): String {
        if (value.isNullOrBlank()) return "Date unavailable"

        runCatching { OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
            .onSuccess { return it.format(outputFormatter) }

        knownPatterns.forEach { formatter ->
            runCatching { LocalDateTime.parse(value, formatter) }
                .onSuccess { return it.format(outputFormatter) }
        }

        runCatching {
            LocalDateTime.parse("${value.trim()} 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH))
        }.onSuccess {
            return it.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
        }

        return value
    }

    fun formatDatePart(value: String?): String {
        return parse(value)?.format(dateFormatter) ?: "Date unavailable"
    }

    fun formatTimePart(value: String?): String {
        return parse(value)?.format(timeFormatter) ?: "Time unavailable"
    }

    private fun parse(value: String?): LocalDateTime? {
        if (value.isNullOrBlank()) return null

        runCatching { OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime() }
            .onSuccess { return it }

        knownPatterns.forEach { formatter ->
            runCatching { LocalDateTime.parse(value, formatter) }
                .onSuccess { return it }
        }

        runCatching {
            LocalDateTime.parse("${value.trim()} 12:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH))
        }.onSuccess { return it }

        return null
    }
}
