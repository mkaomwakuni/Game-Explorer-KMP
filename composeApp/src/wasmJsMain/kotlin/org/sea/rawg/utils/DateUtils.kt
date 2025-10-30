package org.sea.rawg.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.plus
import kotlinx.datetime.DatePeriod

actual object DateUtils {
    actual fun getCurrentDate(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return formatDate(today)
    }
    
    actual fun getTomorrowDate(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val tomorrow = today.plus(DatePeriod(days = 1))
        return formatDate(tomorrow)
    }
    
    actual fun getDateDaysAgo(daysAgo: Int): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val pastDate = today.plus(DatePeriod(days = -daysAgo))
        return formatDate(pastDate)
    }
    
    actual fun getLastMonthDate(): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val lastMonth = today.plus(DatePeriod(months = -1))
        return formatDate(lastMonth)
    }
    
    actual fun getFutureDate(yearsAhead: Int): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val futureDate = today.plus(DatePeriod(years = yearsAhead))
        return formatDate(futureDate)
    }

    private fun formatDate(date: LocalDate): String {
        return "${date.year}-${
            date.monthNumber.toString().padStart(2, '0')
        }-${date.dayOfMonth.toString().padStart(2, '0')}"
    }

    actual fun formatReleaseDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "TBA"

        return try {
            val date = LocalDate.parse(dateString)
            val month = when (date.monthNumber) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                12 -> "Dec"
                else -> date.monthNumber.toString()
            }
            "$month ${date.dayOfMonth}, ${date.year}"
        } catch (e: Exception) {
            dateString
        }
    }
}
