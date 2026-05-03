package com.morganlowe.weeklyweather.network

import com.morganlowe.weeklyweather.domain.CurrentWeather
import com.morganlowe.weeklyweather.domain.ForecastItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects (DTOs) match the raw JSON returned by the APIs.
 * We convert them to clean domain models with `asDomainModel()`.
 *
 * Two APIs are used:
 *   1. Open-Meteo Geocoding — converts a city name to lat/lon
 *   2. Open-Meteo Forecast  — returns weather data for a lat/lon
 */


// ---------- GEOCODING API ----------

@JsonClass(generateAdapter = true)
data class GeocodingResponseDTO(
    val results: List<GeocodingResultDTO>?
)

@JsonClass(generateAdapter = true)
data class GeocodingResultDTO(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?
)


// ---------- FORECAST API ----------

@JsonClass(generateAdapter = true)
data class ForecastResponseDTO(
    val current: CurrentWeatherDTO,
    val daily: DailyDTO
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherDTO(
    @Json(name = "temperature_2m") val temperature: Double,
    @Json(name = "apparent_temperature") val apparentTemperature: Double,
    @Json(name = "relative_humidity_2m") val humidity: Int,
    @Json(name = "surface_pressure") val pressure: Double,
    @Json(name = "wind_speed_10m") val windSpeed: Double,
    @Json(name = "weather_code") val weatherCode: Int
)

@JsonClass(generateAdapter = true)
data class DailyDTO(
    val time: List<String>,
    @Json(name = "temperature_2m_max") val tempMax: List<Double>,
    @Json(name = "temperature_2m_min") val tempMin: List<Double>,
    @Json(name = "weather_code") val weatherCodes: List<Int>,
    @Json(name = "precipitation_probability_max") val precipitationProbability: List<Int?>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @Json(name = "uv_index_max") val uvIndexMax: List<Double?>
)


// ---------- DTO -> DOMAIN CONVERTERS ----------

/**
 * Convert the raw forecast response into a clean CurrentWeather object.
 * The city name has to be passed in because the forecast API doesn't return it.
 *
 * Today's high/low come from the FIRST item in `daily`, since `current` doesn't
 * include daily highs/lows directly.
 */
fun ForecastResponseDTO.asCurrentWeatherDomain(city: String): CurrentWeather {
    val todayMax = daily.tempMax.firstOrNull() ?: current.temperature
    val todayMin = daily.tempMin.firstOrNull() ?: current.temperature

    return CurrentWeather(
        city = city,
        temp = current.temperature,
        apparentTemp = current.apparentTemperature,
        weatherCode = current.weatherCode,
        windSpeed = current.windSpeed,
        humidity = current.humidity,
        pressure = current.pressure,
        tempMax = todayMax,
        tempMin = todayMin
    )
}

/**
 * Convert the parallel arrays in DailyDTO to a list of ForecastItem objects.
 * Open-Meteo returns daily data as separate arrays — we zip them by index.
 */
fun ForecastResponseDTO.asForecastListDomain(): List<ForecastItem> {
    return daily.time.mapIndexed { index, date ->
        ForecastItem(
            date = date,
            tempMax = daily.tempMax[index],
            tempMin = daily.tempMin[index],
            weatherCode = daily.weatherCodes[index],
            precipitationChance = daily.precipitationProbability.getOrNull(index) ?: 0,
            uvIndexMax = daily.uvIndexMax.getOrNull(index) ?: 0.0
        )
    }
}
