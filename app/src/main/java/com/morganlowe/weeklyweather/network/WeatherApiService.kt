package com.morganlowe.weeklyweather.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo APIs. Both are FREE and require NO API key.
 * - Geocoding: https://geocoding-api.open-meteo.com/v1/search?name=Knoxville&count=1
 * - Forecast:  https://api.open-meteo.com/v1/forecast?latitude=...&longitude=...
 */

private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/v1/"
private const val FORECAST_BASE_URL = "https://api.open-meteo.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val geocodingRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(GEOCODING_BASE_URL)
    .build()

private val forecastRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(FORECAST_BASE_URL)
    .build()

object WeatherApi {
    val geocodingService: GeocodingApiService by lazy {
        geocodingRetrofit.create(GeocodingApiService::class.java)
    }

    val forecastService: ForecastApiService by lazy {
        forecastRetrofit.create(ForecastApiService::class.java)
    }
}

/**
 * Converts a city name like "Knoxville" into latitude/longitude coordinates.
 */
interface GeocodingApiService {
    @GET("search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 1
    ): GeocodingResponseDTO
}

/**
 * Fetches current weather + multi-day forecast for a given lat/lon.
 *
 * Asks Open-Meteo for everything we display:
 *   - `current`: temp, apparent temp, humidity, pressure, wind, weather code
 *   - `daily`:  high/low, weather code, precipitation probability, sunrise, sunset, UV
 */
interface ForecastApiService {
    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String =
            "temperature_2m,apparent_temperature,relative_humidity_2m," +
            "surface_pressure,wind_speed_10m,weather_code",
        @Query("daily") daily: String =
            "temperature_2m_max,temperature_2m_min,weather_code," +
            "precipitation_probability_max,sunrise,sunset,uv_index_max",
        @Query("forecast_days") forecastDays: Int = 14,
        @Query("timezone") timezone: String = "auto",
        @Query("temperature_unit") temperatureUnit: String = "fahrenheit",
        @Query("wind_speed_unit") windspeedUnit: String = "mph"
    ): ForecastResponseDTO
}
