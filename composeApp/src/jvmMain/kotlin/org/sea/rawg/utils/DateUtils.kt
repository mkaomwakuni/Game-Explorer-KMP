package org.sea.rawg.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * JVM implementation of DateUtils using java.text.SimpleDateFormat
 */
actual object DateUtils {
    /**
     * Get current date in YYYY-MM-DD format
     */
    actual fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(Date())
    }

    /**
     * Get tomorrow's date in YYYY-MM-DD format
     */
    actual fun getTomorrowDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(calendar.time)
    }

    /**
     * Get date for specified number of days ago in YYYY-MM-DD format
     */
    actual fun getDateDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -daysAgo)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(calendar.time)
    }

    /**
     * Get date for a month ago in YYYY-MM-DD format
     */
    actual fun getLastMonthDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(calendar.time)
    }

    /**
     * Get future date for specified number of years ahead in YYYY-MM-DD format
     */
    actual fun getFutureDate(yearsAhead: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, yearsAhead)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(calendar.time)
    }
}