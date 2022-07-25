package com.pranay.apodnasa.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * give two date string from given days different to current date
 */
fun getTwoDates(format: String = "yyyy-MM-dd", beforeDays: Long = 7): Pair<String, String>? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(format)
        val endDate = LocalDate.now().minusDays(1)
        val firstDate = formatter.format(endDate)
        val secondDate = formatter.format(endDate.minusDays(beforeDays))
        Pair(secondDate, firstDate)
    } catch (ex: Exception) {
        null
    }
}

/**
 * Convert String date to Date object
 */
fun String.toDate(): Date? {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(this) ?: null
}

/**
 * To convert date into formatted string date
 */
fun Date?.formatDate(): String? {
    if (this == null) return null
    return SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(this) ?: null
}