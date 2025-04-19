package org.sea.rawg.utils

/**
 * Interface for platform-specific date utilities
 */
expect object DateUtils {
    fun getCurrentDate(): String
    fun getTomorrowDate(): String
    fun getDateDaysAgo(daysAgo: Int): String
    fun getLastMonthDate(): String
    fun getFutureDate(yearsAhead: Int): String
}
