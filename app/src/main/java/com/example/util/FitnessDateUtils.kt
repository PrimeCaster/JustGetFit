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

    private fun getStandardFormat() = SimpleDateFormat("MMM d, yyyy", Locale.US)
    private fun getShortDayFormat() = SimpleDateFormat("EEE", Locale.US)
    private fun getDayNumFormat() = SimpleDateFormat("d", Locale.US)
    private fun getFullDayFormat() = SimpleDateFormat("EEEE, MMM d", Locale.US)
    private fun getIsoFormat() = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getTodayDateString(): String {
        return getStandardFormat().format(Date())
    }

    fun getNormalizedDate(rawDateStr: String): String {
        val trimmed = rawDateStr.trim()
        if (trimmed.isEmpty() || trimmed.equals("Today", ignoreCase = true)) {
            return getTodayDateString()
        }
        if (trimmed.equals("Yesterday", ignoreCase = true)) {
            return getDateDaysAgo(1)
        }
        if (trimmed.equals("Tomorrow", ignoreCase = true)) {
            return getDateDaysAgo(-1)
        }

        // Check for "<N> Days Ago" pattern
        val daysAgoMatch = Regex("""(\d+)\s+days?\s+ago""", RegexOption.IGNORE_CASE).find(trimmed)
        if (daysAgoMatch != null) {
            val days = daysAgoMatch.groupValues[1].toIntOrNull()
            if (days != null) return getDateDaysAgo(days)
        }

        // Check if already in standard "MMM d, yyyy" format
        try {
            val parsedStandard = getStandardFormat().parse(trimmed)
            if (parsedStandard != null) {
                return getStandardFormat().format(parsedStandard)
            }
        } catch (_: Exception) {}

        // Check if in ISO "yyyy-MM-dd" format
        try {
            val parsedIso = getIsoFormat().parse(trimmed)
            if (parsedIso != null) {
                return getStandardFormat().format(parsedIso)
            }
        } catch (_: Exception) {}

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
        return getStandardFormat().format(cal.time)
    }

    fun getRelativeLabel(dateStr: String): String {
        val normalized = getNormalizedDate(dateStr)
        val today = getTodayDateString()
        val yesterday = getDateDaysAgo(1)

        return when (normalized) {
            today -> "Today (${getStandardFormat().format(Date())})"
            yesterday -> "Yesterday ($yesterday)"
            getDateDaysAgo(2) -> "2 Days Ago ($normalized)"
            getDateDaysAgo(3) -> "3 Days Ago ($normalized)"
            getDateDaysAgo(7) -> "Last Week ($normalized)"
            else -> normalized
        }
    }

    /**
     * MyFitnessPal-style Primary Header Title:
     * "Today", "Yesterday", "Tomorrow", "2 Days Ago", or day name "Sunday, Nov 15"
     */
    fun getMainDateHeading(dateStr: String): String {
        val normalized = getNormalizedDate(dateStr)
        val today = getTodayDateString()
        val yesterday = getDateDaysAgo(1)
        val tomorrow = shiftDate(today, 1)

        return when (normalized) {
            today -> "Today"
            yesterday -> "Yesterday"
            tomorrow -> "Tomorrow"
            getDateDaysAgo(2) -> "2 Days Ago"
            getDateDaysAgo(3) -> "3 Days Ago"
            else -> {
                val days = getDaysAgo(normalized)
                if (days in 2..30) {
                    "$days Days Ago"
                } else {
                    try {
                        val parsed = getStandardFormat().parse(normalized)
                        if (parsed != null) getFullDayFormat().format(parsed) else normalized
                    } catch (_: Exception) {
                        normalized
                    }
                }
            }
        }
    }

    /**
     * MyFitnessPal-style Secondary Header Subtitle:
     * Full readable date, e.g. "Sunday, Nov 17, 2026"
     */
    fun getSubDateHeading(dateStr: String): String {
        val normalized = getNormalizedDate(dateStr)
        return try {
            val parsed = getStandardFormat().parse(normalized)
            if (parsed != null) {
                SimpleDateFormat("EEEE, MMM d, yyyy", Locale.US).format(parsed)
            } else {
                normalized
            }
        } catch (_: Exception) {
            normalized
        }
    }

    /**
     * Shifts a date string forward or backward by the given number of days.
     */
    fun shiftDate(currentDateStr: String, deltaDays: Int): String {
        val normalized = getNormalizedDate(currentDateStr)
        val cal = Calendar.getInstance()

        // Try standard format
        try {
            val parsed = getStandardFormat().parse(normalized)
            if (parsed != null) {
                cal.time = parsed
                cal.add(Calendar.DAY_OF_YEAR, deltaDays)
                return getStandardFormat().format(cal.time)
            }
        } catch (_: Exception) {}

        // Try ISO format
        try {
            val parsed = getIsoFormat().parse(normalized)
            if (parsed != null) {
                cal.time = parsed
                cal.add(Calendar.DAY_OF_YEAR, deltaDays)
                return getStandardFormat().format(cal.time)
            }
        } catch (_: Exception) {}

        // Fallback: calculate based on getDaysAgo
        val currentDaysAgo = getDaysAgo(currentDateStr)
        val targetDaysAgo = (currentDaysAgo - deltaDays).coerceAtLeast(0)
        return getDateDaysAgo(targetDaysAgo)
    }

    /**
     * Dynamic date selector options based on the currently selected date:
     * - By default when on Today: ONLY "Today" shows up!
     * - When navigating to previous days (e.g. Yesterday, 2 Days Ago), past days up to Today are shown,
     *   allowing the user to see the historical days and tap back to Today at any time.
     */
    fun getDynamicDateOptions(selectedDateStr: String): List<DateSelectorOption> {
        val daysAgo = getDaysAgo(selectedDateStr).coerceAtLeast(0)
        val list = mutableListOf<DateSelectorOption>()
        val cal = Calendar.getInstance()

        // From past day(s) up to Today (offset 0)
        for (offset in -daysAgo..0) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, offset)
            val d = c.time
            val dateStr = getStandardFormat().format(d)
            val isToday = offset == 0
            val isYesterday = offset == -1

            val label = when {
                isToday -> "★ Today"
                isYesterday -> "Yesterday"
                else -> "${getShortDayFormat().format(d)} ${getDayNumFormat().format(d)}"
            }

            list.add(
                DateSelectorOption(
                    dateString = dateStr,
                    displayLabel = label,
                    shortDayLabel = getShortDayFormat().format(d),
                    dayNumber = getDayNumFormat().format(d),
                    daysAgo = -offset,
                    isToday = isToday
                )
            )
        }
        return list
    }

    /**
     * Calculates how many days ago a normalized date string is relative to Today.
     */
    fun getDaysAgo(dateStr: String): Int {
        val normalized = getNormalizedDate(dateStr)
        val today = getTodayDateString()
        if (normalized == today) return 0
        val yesterday = getDateDaysAgo(1)
        if (normalized == yesterday) return 1
        return try {
            val d1 = getStandardFormat().parse(today)
            val d2 = getStandardFormat().parse(normalized)
            if (d1 != null && d2 != null) {
                val diffMillis = d1.time - d2.time
                val diffDays = Math.round(diffMillis.toDouble() / (1000.0 * 60.0 * 60.0 * 24.0)).toInt()
                diffDays.coerceAtLeast(0)
            } else 0
        } catch (_: Exception) {
            0
        }
    }

    /**
     * Generates a list of recent dates for quick horizontal pill selector.
     */
    fun getRecentDateOptions(count: Int = 14): List<DateSelectorOption> {
        return getCenteredDateOptions(pastDaysCount = 3, futureDaysCount = 3)
    }

    /**
     * Generates dates in chronological order with 'Today' centered in the middle, past days on the left,
     * and tomorrow / future days on the right (just like MyFitnessPal):
     * e.g., 3 Days Ago, 2 Days Ago, Yesterday, TODAY (in middle), Tomorrow, Day +2, Day +3
     */
    fun getCenteredDateOptions(pastDaysCount: Int = 3, futureDaysCount: Int = 3): List<DateSelectorOption> {
        val list = mutableListOf<DateSelectorOption>()
        val cal = Calendar.getInstance()

        for (offset in -pastDaysCount..futureDaysCount) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, offset)
            val d = c.time
            val dateStr = getStandardFormat().format(d)
            val isToday = offset == 0
            val isYesterday = offset == -1
            val isTomorrow = offset == 1

            val label = when {
                isToday -> "Today"
                isYesterday -> "Yesterday"
                isTomorrow -> "Tomorrow"
                else -> "${getShortDayFormat().format(d)} ${getDayNumFormat().format(d)}"
            }

            list.add(
                DateSelectorOption(
                    dateString = dateStr,
                    displayLabel = label,
                    shortDayLabel = getShortDayFormat().format(d),
                    dayNumber = getDayNumFormat().format(d),
                    daysAgo = -offset,
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
