package org.sea.rawg.utils

import platform.Foundation.*

/**
 * iOS implementation of DateUtils using NSDateFormatter and NSDate
 */
actual object DateUtils {
    /**
     * Get current date in YYYY-MM-DD format
     */
    actual fun getCurrentDate(): String {
        val date = NSDate()
        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(date)
    }

    /**
     * Get tomorrow's date in YYYY-MM-DD format
     */
    actual fun getTomorrowDate(): String {
        val calendar = NSCalendar.currentCalendar
        val date = NSDate()
        val tomorrow = calendar.dateByAddingUnit(
            NSCalendarUnitDay,
            1.toLong(),
            date,
            0u
        ) ?: date

        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(tomorrow)
    }

    /**
     * Get date for specified number of days ago in YYYY-MM-DD format
     */
    actual fun getDateDaysAgo(daysAgo: Int): String {
        val calendar = NSCalendar.currentCalendar
        val date = NSDate()
        val pastDate = calendar.dateByAddingUnit(
            NSCalendarUnitDay,
            (-daysAgo).toLong(),
            date,
            0u
        ) ?: date

        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(pastDate)
    }

    /**
     * Get date for a month ago in YYYY-MM-DD format
     */
    actual fun getLastMonthDate(): String {
        val calendar = NSCalendar.currentCalendar
        val date = NSDate()
        val lastMonth = calendar.dateByAddingUnit(
            NSCalendarUnitMonth,
            (-1).toLong(),
            date,
            0u
        ) ?: date

        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(lastMonth)
    }

    /**
     * Get future date for specified number of years ahead in YYYY-MM-DD format
     */
    actual fun getFutureDate(yearsAhead: Int): String {
        val calendar = NSCalendar.currentCalendar
        val date = NSDate()
        val futureDate = calendar.dateByAddingUnit(
            NSCalendarUnitYear,
            yearsAhead.toLong(),
            date,
            0u
        ) ?: date

        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(futureDate)
    }
}