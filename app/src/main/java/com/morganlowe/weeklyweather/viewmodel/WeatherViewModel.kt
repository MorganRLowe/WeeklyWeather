package com.morganlowe.weeklyweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morganlowe.weeklyweather.repository.WeatherData
import com.morganlowe.weeklyweather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represents every possible state the weather screen could be in.
 * Sealed interfaces are perfect for this — exhaustive when() checks
 * and clear separation of states.
 */
sealed interface WeatherUiState {
    object Idle : WeatherUiState                       // No search performed yet
    object Loading : WeatherUiState                    // API call in progress
    data class Success(val data: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    // _uiState is private (mutable) — only the ViewModel can change it.
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)

    // uiState is public (read-only) — the UI observes this.
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    /**
     * Called from the UI when the user taps "Go!" with a city name.
     * Triggers a coroutine to fetch weather and updates uiState as it progresses.
     */
    fun searchWeather(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("Please enter a city name")
            return
        }

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val data = repository.getWeatherForCity(cityName.trim())
                _uiState.value = WeatherUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    /**
     * Resets the UI back to the search screen.
     */
    fun resetState() {
        _uiState.value = WeatherUiState.Idle
    }
}
