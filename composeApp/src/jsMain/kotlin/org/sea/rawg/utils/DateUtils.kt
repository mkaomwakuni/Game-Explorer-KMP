package org.sea.rawg.utils

/**
 * JavaScript implementation of DateUtils
 */
actual object DateUtils {
    /**
     * Get current date in YYYY-MM-DD format
     */
    actual fun getCurrentDate(): String {
        // For demo, using JS Date object and formatting it to YYYY-MM-DD
        val date = js("new Date()")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    /**
     * Get tomorrow's date in YYYY-MM-DD format
     */
    actual fun getTomorrowDate(): String {
        // Add one day to current date
        val date = js("new Date()")
        js("date.setDate(date.getDate() + 1)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    /**
     * Get date for specified number of days ago in YYYY-MM-DD format
     */
    actual fun getDateDaysAgo(daysAgo: Int): String {
        // Subtract days from current date
        val date = js("new Date()")
        js("date.setDate(date.getDate() - $daysAgo)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    /**
     * Get date for a month ago in YYYY-MM-DD format
     */
    actual fun getLastMonthDate(): String {
        // Subtract one month from current date
        val date = js("new Date()")
        js("date.setMonth(date.getMonth() - 1)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }

    /**
     * Get future date for specified number of years ahead in YYYY-MM-DD format
     */
    actual fun getFutureDate(yearsAhead: Int): String {
        // Add years to current date
        val date = js("new Date()")
        js("date.setFullYear(date.getFullYear() + $yearsAhead)")
        val year = js("date.getFullYear()")
        val month = js("(date.getMonth() + 1).toString().padStart(2, '0')")
        val day = js("date.getDate().toString().padStart(2, '0')")
        return "$year-$month-$day"
    }
}