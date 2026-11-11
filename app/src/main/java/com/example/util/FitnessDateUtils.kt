package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class DateSelectorOption(
    val dateString: String,      // Normalized date: "MMM d, yyyy", e.g. "Nov 8, 2026"
    val displayLabel: String,    // "Today", "Yesterday", "Nov 6 (Wed)", etc.
    val shortDayLabel: String,   // "Sun", "Sat", "Fri"
    val dayNumber: String,       // "8", "7", "6"
    val daysAgo: Int,
    val isToday: Boolean
)

data class DatePreset(
    val displayLabel: String,
    val dateString: String
)

object FitnessDateUtils {

    private val standardDateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    private val shortDayFormat = SimpleDateFormat("EEE", Locale.US)
    private val dayNumFormat = SimpleDateFormat("d", Locale.US)
    private val fullDayFormat = SimpleDateFormat("EEEE, MMM d", Locale.US)

    fun getTodayDateString(): String {
        return standardDateFormat.format(Date())
    }

    fun getNormalizedDate(rawDateStr: String): String {
        val trimmed = rawDateStr.trim()
        if (trimmed.equals("Today", ignoreCase = true)) {
            return getTodayDateString()
        }
        if (trimmed.equals("Yesterday", ignoreCase = true)) {
            return getDateDaysAgo(1)
        }
        return trimmed
    }

    fun isDateToday(dateStr: String): Boolean {
        if (dateStr.equals("Today", ignoreCase = true)) return true
        return getNormalizedDate(dateStr) == getTodayDateString()
    }

    fun isDateYesterday(dateStr: String): Boolean {
        if (dateStr.equals("Yesterday", ignoreCase = true)) return true
        return getNormalizedDate(dateStr) == getDateDaysAgo(1)
    }

    fun getDateDaysAgo(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return standardDateFormat.format(cal.time)
    }

    fun getRelativeLabel(dateStr: String): String {
        val normalized = getNormalizedDate(dateStr)
        val today = getTodayDateString()
        val yesterday = getDateDaysAgo(1)

        return when (normalized) {
            today -> "Today (${standardDateFormat.format(Date())})"
            yesterday -> "Yesterday ($yesterday)"
            getDateDaysAgo(2) -> "2 Days Ago ($normalized)"
            getDateDaysAgo(3) -> "3 Days Ago ($normalized)"
            getDateDaysAgo(7) -> "Last Week ($normalized)"
            else -> normalized
        }
    }

    /**
     * Shifts a date string forward or backward by the given number of days.
     */
    fun shiftDate(currentDateStr: String, deltaDays: Int): String {
        val normalized = getNormalizedDate(currentDateStr)
        val cal = Calendar.getInstance()
        try {
            val parsed = standardDateFormat.parse(normalized)
            if (parsed != null) {
                cal.time = parsed
                cal.add(Calendar.DAY_OF_YEAR, deltaDays)
                return standardDateFormat.format(cal.time)
            }
        } catch (_: Exception) {
        }
        // Fallback
        cal.add(Calendar.DAY_OF_YEAR, deltaDays)
        return standardDateFormat.format(cal.time)
    }

    /**
     * Generates a list of recent dates for quick horizontal pill selector.
     */
    fun getRecentDateOptions(count: Int = 14): List<DateSelectorOption> {
        val list = mutableListOf<DateSelectorOption>()
        val cal = Calendar.getInstance()

        for (daysAgo in 0 until count) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val d = c.time
            val dateStr = standardDateFormat.format(d)
            val isToday = daysAgo == 0
            val isYesterday = daysAgo == 1

            val label = when {
                isToday -> "Today"
                isYesterday -> "Yesterday"
                else -> "${shortDayFormat.format(d)}, ${dayNumFormat.format(d)}"
            }

            list.add(
                DateSelectorOption(
                    dateString = dateStr,
                    displayLabel = label,
                    shortDayLabel = shortDayFormat.format(d),
                    dayNumber = dayNumFormat.format(d),
                    daysAgo = daysAgo,
                    isToday = isToday
                )
            )
        }
        return list
    }

    /**
     * Provides quick jump presets: Today, Yesterday, 3 Days Ago, 1 Week Ago, 2 Weeks Ago, 1 Month Ago.
     */
    fun getQuickJumpPresets(): List<DatePreset> {
        return listOf(
            DatePreset("Today", getTodayDateString()),
            DatePreset("Yesterday", getDateDaysAgo(1)),
            DatePreset("3 Days Ago", getDateDaysAgo(3)),
            DatePreset("1 Week Ago", getDateDaysAgo(7)),
            DatePreset("2 Weeks Ago", getDateDaysAgo(14)),
            DatePreset("1 Month Ago", getDateDaysAgo(30))
        )
    }
}
