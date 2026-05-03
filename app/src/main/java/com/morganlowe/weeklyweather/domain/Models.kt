package com.morganlowe.weeklyweather.domain

/**
 * Domain models — clean data shapes the UI uses.
 * These are independent of any specific API.
 */

data class CurrentWeather(
    val city: String,
    val temp: Double,
    val apparentTemp: Double,        // "feels like" temperature
    val weatherCode: Int,
    val windSpeed: Double,
    val humidity: Int,               // % humidity
    val pressure: Double,            // hPa
    val tempMax: Double,             // today's high
    val tempMin: Double              // today's low
)

data class ForecastItem(
    val date: String,                  // e.g. "2026-05-02"
    val tempMax: Double,
    val tempMin: Double,
    val weatherCode: Int,
    val precipitationChance: Int,      // %
    val uvIndexMax: Double             // 0-11+ UV index
)
