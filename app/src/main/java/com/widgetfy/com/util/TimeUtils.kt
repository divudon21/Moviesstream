package com.widgetfy.com.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun currentTime(): String {
        val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
        return sdf.format(Date())
    }
    fun currentTimeWithSeconds(): String {
        val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
    fun currentDate(): String {
        val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    fun currentDayMonth(): String {
        val sdf = SimpleDateFormat("EEE dd MMM", Locale.US)
        return sdf.format(Date()).uppercase()
    }
    fun currentMonthYear(): String {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    fun amPm(): String {
        val sdf = SimpleDateFormat("a", Locale.getDefault())
        return sdf.format(Date())
    }
    fun hour(): String {
        val sdf = SimpleDateFormat("hh", Locale.getDefault())
        return sdf.format(Date())
    }
    fun minute(): String {
        val sdf = SimpleDateFormat("mm", Locale.getDefault())
        return sdf.format(Date())
    }
    fun worldTimes(): Triple<String,String,String> {
        val delhi = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("Asia/Kolkata") }.format(Date())
        val london = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("Europe/London") }.format(Date())
        val ny = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("America/New_York") }.format(Date())
        return Triple(delhi, london, ny)
    }
    fun calendarDays(): String {
        val cal = Calendar.getInstance()
        val max = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = cal.get(Calendar.DAY_OF_MONTH)
        val sb = StringBuilder()
        for (d in 1..max) {
            val s = if (d == today) "•${d}•" else String.format("%2d", d)
            sb.append(s).append("  ")
            if (d % 7 == 0) sb.append("\n")
        }
        return sb.toString().trim()
    }
}
