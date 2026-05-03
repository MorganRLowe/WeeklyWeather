package com.morganlowe.weeklyweather.repository

import com.morganlowe.weeklyweather.domain.CurrentWeather
import com.morganlowe.weeklyweather.domain.ForecastItem
import com.morganlowe.weeklyweather.network.WeatherApi
import com.morganlowe.weeklyweather.network.asCurrentWeatherDomain
import com.morganlowe.weeklyweather.network.asForecastListDomain

/**
 * The repository is the single point of contact between the ViewModel
 * and the data sources. The ViewModel doesn't know there are TWO API
 * calls happening (geocoding + forecast) — it just asks for weather
 * by city name and gets clean data back.
 *
 * This is a simple data class wrapper for what the repository returns.
 */
data class WeatherData(
    val current: CurrentWeather,
    val forecast: List<ForecastItem>
)

class WeatherRepository {

    /**
     * Looks up a city's coordinates, then fetches the forecast for those coordinates.
     * Throws if the city isn't found or if either API call fails.
     */
    suspend fun getWeatherForCity(cityName: String): WeatherData {
        // Step 1: Geocode the city name to lat/lon
        val geoResponse = WeatherApi.geocodingService.searchCity(cityName)
        val firstResult = geoResponse.results?.firstOrNull()
            ?: throw Exception("City \"$cityName\" not found")

        // Step 2: Fetch the forecast for those coordinates
        val forecastResponse = WeatherApi.forecastService.getForecast(
            latitude = firstResult.latitude,
            longitude = firstResult.longitude
        )

        // Step 3: Convert raw API responses to clean domain models
        return WeatherData(
            current = forecastResponse.asCurrentWeatherDomain(firstResult.name),
            forecast = forecastResponse.asForecastListDomain()
        )
    }
}
