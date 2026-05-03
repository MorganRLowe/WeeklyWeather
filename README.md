# WeeklyWeather

A Jetpack Compose reimplementation of an Android Views-based weather app. Built as a learning project demonstrating the migration of an XML/Fragment-based UI to modern Compose.

## About

WeeklyWeather is inspired by [WeatherChecker](https://github.com/ersiver/WeatherChecker) by ersiver. The original app was built with traditional Android Views (XML layouts, Fragments, Data Binding, RecyclerView, LiveData). This version rebuilds the same general functionality using:

- **Jetpack Compose** for all UI
- **Material 3** theming
- **StateFlow** instead of LiveData
- **Sealed interface UI state** instead of multiple LiveData fields
- **Single-Activity, screen-state-driven** navigation (no Fragments)

<img src="Docs/WeatherChecker.gif" alt="WeeklyWeather demo" width="300"/>

## Differences from the original

| Aspect | Original WeatherChecker | WeeklyWeather |
|---|---|---|
| UI toolkit | XML + Fragments + Data Binding | Jetpack Compose |
| Lists | `RecyclerView` + custom `Adapter` | `LazyColumn` (built-in) |
| State holder | Multiple `LiveData` fields | Single `StateFlow<WeatherUiState>` |
| API | OpenWeatherMap (sample key) | [Open-Meteo](https://open-meteo.com/) (no key required) |
| Screens | One screen | Two screens (location entry → forecast) |
| Forecast length | 14 days | 14 days |
| Visual style | Teal background, white icons | Same — kept faithful to the original |

## Project structure

```
com.morganlowe.weeklyweather/
├── MainActivity.kt          App entry point
├── domain/                  Data models
├── network/                 API + JSON parsing
├── repository/              Bridges network and ViewModel
├── viewmodel/               UI state management
├── ui/                      Compose screens and theme
└── util/                    Shared helpers
```

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- Lifecycle ViewModel + StateFlow
- Coroutines
- Retrofit + Moshi
- Open-Meteo (no API key required)

## Building

1. Open in Android Studio (Panda or newer)
2. Let Gradle sync
3. Run on an emulator with API 24+

## Future work

The original WeatherChecker included a Widget. For keeping inline with the original goal of converting from 
Android Views to Jetpack Compose it is omitted in this repo.
Reintroducing widget support would be possible in the future.

Other potential extensions:
- GPS-based current location detection from permissions
- Multiple saved cities / favorites
- Hourly forecast view
- Pressure / humidity / UV index display

## Credits

- Original Views-based project: [ersiver/WeatherChecker](https://github.com/ersiver/WeatherChecker)
- Weather data: [Open-Meteo](https://open-meteo.com/) (CC BY 4.0)
- Weather icons: ported from the original WeatherChecker project, sourced from [Flaticon](https://flaticon.com) (Freepik, Swifticons, Good Ware, Vitaliy Gorbachev, Those Icon, Hirschwolf Lineal, Iconixar, Eucalyp, Pixel Perfect)
