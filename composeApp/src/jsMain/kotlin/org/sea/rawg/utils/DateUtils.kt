package org.sea.rawg.utils

actual object DateUtils {
    actual fun getCurrentDate(): String {
        val date = js("new Date()")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    actual fun getTomorrowDate(): String {
        val date = js("new Date()")
        js("date.setDate(date.getDate() + 1)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    actual fun getDateDaysAgo(daysAgo: Int): String {
        val date = js("new Date()")
        js("date.setDate(date.getDate() - $daysAgo)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    actual fun getLastMonthDate(): String {
        val date = js("new Date()")
        js("date.setMonth(date.getMonth() - 1)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    actual fun getFutureDate(yearsAhead: Int): String {
        val date = js("new Date()")
        js("date.setFullYear(date.getFullYear() + $yearsAhead)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    actual fun formatReleaseDate(releaseDate: String?): String {
        if (releaseDate.isNullOrEmpty()) return "TBA"

        try {
            val date = js("new Date(releaseDate)")
            val isValid = js("!isNaN(date.getTime())")
            if (isValid as Boolean) {
                val monthNames =
                    js("['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']")
                val monthName = js("monthNames[date.getMonth()]")
                val day = js("date.getDate()")
                val year = js("date.getFullYear()")
                return "$monthName $day, $year"
            }
            return releaseDate
        } catch (e: Exception) {
            return releaseDate
        }
    }

    actual fun getRelativeDateString(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Unknown"

        try {
            val releaseDate = js("new Date(dateString)")
            val now = js("new Date()")
            val isValid = js("!isNaN(releaseDate.getTime())")

            if (isValid as Boolean) {
                val diffTime = js("now - releaseDate")
                val diffDays = js("Math.floor(diffTime / (1000 * 60 * 60 * 24))")
                val days = diffDays as Int

                return when {
                    days == 0 -> "Today"
                    days == 1 -> "Yesterday"
                    days < 0 -> "${-days} days from now"
                    days <= 30 -> "$days days ago"
                    days <= 365 -> "${days / 30} months ago"
                    else -> "${days / 365} years ago"
                }
            }
            return dateString
        } catch (e: Exception) {
            return dateString
        }
    }

    actual fun getDaysSinceRelease(releaseDate: String?): Int {
        if (releaseDate.isNullOrEmpty()) return 0

        try {
            val date = js("new Date(releaseDate)")
            val now = js("new Date()")
            val isValid = js("!isNaN(date.getTime())")

            if (isValid as Boolean) {
                val diffTime = js("now - date")
                val diffDays = js("Math.floor(diffTime / (1000 * 60 * 60 * 24))")
                return diffDays as Int
            }
            return 0
        } catch (e: Exception) {
            return 0
        }
    }

    actual fun isUpcoming(releaseDate: String?): Boolean {
        if (releaseDate.isNullOrEmpty()) return false

        try {
            val date = js("new Date(releaseDate)")
            val now = js("new Date()")
            val isValid = js("!isNaN(date.getTime())")

            if (isValid as Boolean) {
                return js("date > now") as Boolean
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }
}