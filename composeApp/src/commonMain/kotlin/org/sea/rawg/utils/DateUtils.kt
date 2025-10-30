package org.sea.rawg.utils

expect object DateUtils {
    fun getCurrentDate(): String
    fun getTomorrowDate(): String
    fun getDateDaysAgo(daysAgo: Int): String
    fun getLastMonthDate(): String
    fun getFutureDate(yearsAhead: Int): String

    // Add the missing functions that are implemented in platform-specific code
    fun formatReleaseDate(releaseDate: String?): String
    fun getRelativeDateString(dateString: String?): String
    fun getDaysSinceRelease(releaseDate: String?): Int
    fun isUpcoming(releaseDate: String?): Boolean
}
