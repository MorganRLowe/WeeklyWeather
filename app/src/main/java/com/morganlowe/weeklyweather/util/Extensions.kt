package com.morganlowe.weeklyweather.util

import androidx.annotation.DrawableRes
import com.morganlowe.weeklyweather.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 *  Round a temperature double and add °F.
 */
fun Double.asTempString(): String {
    return "${this.toInt()}°F"
}

/**
 * Convert an Open-Meteo WMO weather code to a human-readable description.
 * Reference: https://open-meteo.com/en/docs (search "WMO Weather interpretation codes")
 */
fun Int.toWeatherDescription(): String {
    return when (this) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "Partly cloudy"
        3 -> "Overcast"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Freezing drizzle"
        61, 63, 65 -> "Rain"
        66, 67 -> "Freezing rain"
        71, 73, 75 -> "Snow"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers"
        85, 86 -> "Snow showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with hail"
        else -> "Unknown"
    }
}

/**
 * Convert an Open-Meteo WMO weather code to a drawable icon resource.
 * The icons themselves are PNGs ported over from the original WeatherChecker
 * project (originally sourced from Flaticon — see README credits).
 *
 * Mapping is approximate because Open-Meteo and OpenWeatherMap use different
 * code systems; we pick the closest visual match for each WMO code.
 */
@DrawableRes
fun Int.toWeatherIconRes(): Int {
    return when (this) {
        0 -> R.drawable.icon_01d                    // Clear → sun
        1 -> R.drawable.icon_02d                    // Mainly clear → sun + small cloud
        2 -> R.drawable.icon_03                     // Partly cloudy → cloud
        3 -> R.drawable.icon_04                     // Overcast → heavier clouds
        45, 48 -> R.drawable.icon_50                // Fog → mist
        51, 53, 55, 56, 57 -> R.drawable.icon_09    // Drizzle
        61, 63, 65, 66, 67, 80, 81, 82 -> R.drawable.icon_10d  // Rain
        71, 73, 75, 77, 85, 86 -> R.drawable.icon_13           // Snow
        95, 96, 99 -> R.drawable.icon_11            // Thunderstorm
        else -> R.drawable.icon                     // Fallback (default question-mark style)
    }
}

/**
 * Open-Meteo returns dates as "2026-05-02".
 * Format them as "Mon, May 2" for display (or just "Mon" / "02 May" for compact rows).
 */
fun String.asPrettyDate(): String {
    return try {
        val date = LocalDate.parse(this)
        date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
    } catch (e: Exception) {
        this  // fall back to the raw string if parsing fails
    }
}

/**
 * Compact day-of-week ("Mon", "Tue", etc.) — used in forecast rows.
 */
fun String.asDayOfWeek(): String {
    return try {
        LocalDate.parse(this).format(DateTimeFormatter.ofPattern("EEE"))
    } catch (e: Exception) {
        this
    }
}

/**
 * Compact date ("02 May") — used in forecast rows.
 */
fun String.asShortDate(): String {
    return try {
        LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd MMM"))
    } catch (e: Exception) {
        this
    }
}
