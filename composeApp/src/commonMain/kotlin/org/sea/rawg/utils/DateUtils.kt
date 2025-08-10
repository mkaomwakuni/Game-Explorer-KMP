package org.sea.rawg.utils

expect object DateUtils {
    fun getCurrentDate(): String
    fun getTomorrowDate(): String
    fun getDateDaysAgo(daysAgo: Int): String
    fun getLastMonthDate(): String
    fun getFutureDate(yearsAhead: Int): String
}
