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
}
