package org.sea.rawg.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual object DateUtils {
    actual fun getCurrentDate(): String {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.year}-${localDateTime.monthNumber}-${localDateTime.dayOfMonth}"
    }

    actual fun formatReleaseDate(releaseDate: String?): String {
        if (releaseDate.isNullOrEmpty()) return "TBA"

        try {
            val date = LocalDate.parse(releaseDate)
            return "${date.month.name.lowercase().capitalize()} ${date.dayOfMonth}, ${date.year}"
        } catch (e: Exception) {
            return releaseDate
        }
    }

    actual fun getRelativeDateString(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Unknown"

        try {
            val date = LocalDate.parse(dateString)
            val now = Clock.System.now()
            val currentDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

            val days = daysBetween(date, currentDate)

            return when {
                days == 0 -> "Today"
                days == 1 -> "Yesterday"
                days < 0 -> "${-days} days from now"
                days <= 30 -> "$days days ago"
                days <= 365 -> "${days / 30} months ago"
                else -> "${days / 365} years ago"
            }
        } catch (e: Exception) {
            return dateString
        }
    }

    actual fun getDaysSinceRelease(releaseDate: String?): Int {
        if (releaseDate.isNullOrEmpty()) return 0

        try {
            val date = LocalDate.parse(releaseDate)
            val now = Clock.System.now()
            val currentDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

            return daysBetween(date, currentDate)
        } catch (e: Exception) {
            return 0
        }
    }

    actual fun isUpcoming(releaseDate: String?): Boolean {
        if (releaseDate.isNullOrEmpty()) return false

        try {
            val date = LocalDate.parse(releaseDate)
            val now = Clock.System.now()
            val currentDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

            return date > currentDate
        } catch (e: Exception) {
            return false
        }
    }

    private fun daysBetween(startDate: LocalDate, endDate: LocalDate): Int {
        return daysSinceEpoch(endDate) - daysSinceEpoch(startDate)
    }

    private fun daysSinceEpoch(date: LocalDate): Int {
        val startDateInstant = Instant.parse("${date}T00:00:00Z")
        val epochInstant = Instant.parse("1970-01-01T00:00:00Z")
        return ((startDateInstant.toEpochMilliseconds() - epochInstant.toEpochMilliseconds()) / (24 * 60 * 60 * 1000)).toInt()
    }
}