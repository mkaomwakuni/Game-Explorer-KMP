package org.sea.rawg.utils

import platform.Foundation.*

actual object DateUtils {
    actual fun getCurrentDate(): String {
        val date = NSDate()
        val formatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(date)
    }
    
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

    actual fun formatReleaseDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "TBA"

        val inputFormatter = NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale = NSLocale.currentLocale
        }

        val date = inputFormatter.dateFromString(dateString)

        return if (date != null) {
            val outputFormatter = NSDateFormatter().apply {
                dateFormat = "MMM d, yyyy"
                locale = NSLocale.currentLocale
            }
            outputFormatter.stringFromDate(date)
        } else {
            dateString
        }
    }
}
