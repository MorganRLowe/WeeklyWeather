package com.morganlowe.weeklyweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.morganlowe.weeklyweather.ui.ForecastScreen
import com.morganlowe.weeklyweather.ui.LoadingScreen
import com.morganlowe.weeklyweather.ui.LocationScreen
import com.morganlowe.weeklyweather.ui.theme.WeeklyWeatherTheme
import com.morganlowe.weeklyweather.viewmodel.WeatherUiState
import com.morganlowe.weeklyweather.viewmodel.WeatherViewModel

/**
 * Single-Activity app — Compose handles all the screen switching internally.
 * No Fragments. No Navigation Component (yet — could add it later for more screens).
 */
class MainActivity : ComponentActivity() {

    // `by viewModels()` is the modern way to get a ViewModel that survives
    // configuration changes (rotation, etc.). Same instance for the activity's lifetime.
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeeklyWeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeeklyWeatherApp(viewModel)
                }
            }
        }
    }
}

/**
 * Top-level composable. Watches the ViewModel's uiState and switches
 * between the LocationScreen and ForecastScreen based on what state we're in.
 */
@Composable
fun WeeklyWeatherApp(viewModel: WeatherViewModel) {
    // Observing StateFlow in Compose: collectAsState() turns it into a State<T>
    // that automatically triggers recomposition when it changes.
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is WeatherUiState.Idle -> {
            LocationScreen(
                onSearch = { city -> viewModel.searchWeather(city) }
            )
        }
        is WeatherUiState.Loading -> {
            LoadingScreen()
        }
        is WeatherUiState.Success -> {
            ForecastScreen(
                data = state.data,
                onSearch = { city -> viewModel.searchWeather(city) }
            )
        }
        is WeatherUiState.Error -> {
            // Show LocationScreen with error message displayed below the input
            LocationScreen(
                onSearch = { city -> viewModel.searchWeather(city) },
                errorMessage = state.message
            )
        }
    }
}
