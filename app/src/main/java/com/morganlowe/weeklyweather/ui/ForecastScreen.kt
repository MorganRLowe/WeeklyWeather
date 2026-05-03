package com.morganlowe.weeklyweather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.morganlowe.weeklyweather.R
import com.morganlowe.weeklyweather.domain.CurrentWeather
import com.morganlowe.weeklyweather.domain.ForecastItem
import com.morganlowe.weeklyweather.repository.WeatherData
import com.morganlowe.weeklyweather.ui.theme.White
import com.morganlowe.weeklyweather.util.asDayOfWeek
import com.morganlowe.weeklyweather.util.asShortDate
import com.morganlowe.weeklyweather.util.asTempString
import com.morganlowe.weeklyweather.util.toWeatherDescription
import com.morganlowe.weeklyweather.util.toWeatherIconRes

/**
 * The forecast screen, mirroring the original WeatherChecker layout:
 *   - Pin icon + city name
 *   - Big current temperature
 *   - Big weather icon + description
 *   - Stats row (min, max, humidity, pressure, wind) — matches original
 *   - Multi-day forecast list (cards) with high/low/precip chance
 */
@Composable
fun ForecastScreen(
    data: WeatherData,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // "Search again" link
        item {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Search again",
                    color = White,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Current weather block (pin + city, big temp, big icon + description)
        item {
            CurrentWeatherSection(current = data.current)
        }

        // Divider
        item {
            HorizontalDivider(
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Stats row (matches the original's 5-column layout)
        item {
            StatsRow(current = data.current)
        }

        // Divider
        item {
            HorizontalDivider(
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Section header
        item {
            Text(
                text = "14-day forecast",
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Forecast cards — LazyColumn replaces the original RecyclerView + Adapter
        items(data.forecast) { item ->
            ForecastCard(item = item)
        }

        // Bottom spacing so the last card isn't flush with the bottom edge
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Top section: pin + city name, big temperature, big weather icon + description.
 */
@Composable
private fun CurrentWeatherSection(current: CurrentWeather) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Pin icon + city name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = "Location",
                tint = White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = current.city,
                color = White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Big temperature, centered
        Text(
            text = current.temp.asTempString(),
            color = White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        // Apparent (feels-like) temperature beneath
        Text(
            text = "Feels like ${current.apparentTemp.asTempString()}",
            color = White.copy(alpha = 0.85f),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big icon + condition description, centered
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(current.weatherCode.toWeatherIconRes()),
                contentDescription = current.weatherCode.toWeatherDescription(),
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = current.weatherCode.toWeatherDescription(),
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Stats row with 5 columns matching the original's layout:
 * Min temp ↓ | Max temp ↑ | Humidity 💧 | Pressure | Wind
 */
@Composable
private fun StatsRow(current: CurrentWeather) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatColumn(
            iconRes = R.drawable.min,
            value = current.tempMin.asTempString()
        )
        StatColumn(
            iconRes = R.drawable.max,
            value = current.tempMax.asTempString()
        )
        StatColumn(
            iconRes = R.drawable.humidity,
            value = "${current.humidity}%"
        )
        StatColumn(
            iconRes = R.drawable.pressure,
            value = "${current.pressure.toInt()} hPa"
        )
        StatColumn(
            iconRes = R.drawable.wind,
            value = "${current.windSpeed.toInt()} mph"
        )
    }
}

@Composable
private fun StatColumn(iconRes: Int, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
/**
 * One day in the multi-day forecast, rendered as a card.
 * Layout: [Day | Date]   [Icon + Description]   [💧 chance%]   [High / Low]
 *
 * Replaces list_item_forecast.xml + ForecastAdapter from the original.
 * Using a Card here gives each day a subtle visual border without breaking
 * the all-white-on-teal aesthetic.
 */
@Composable
private fun ForecastCard(item: ForecastItem) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = White.copy(alpha = 0.12f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column 1: Day of week (e.g. "Mon")
            Text(
                text = item.date.asDayOfWeek(),
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            // Column 2: Date (e.g. "02 Mar")
            Text(
                text = item.date.asShortDate(),
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            // Column 3: Weather icon — centered in its own column
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(item.weatherCode.toWeatherIconRes()),
                    contentDescription = item.weatherCode.toWeatherDescription(),
                    modifier = Modifier.size(40.dp)
                )
            }

            // Column 4: High temperature (bold)
            Text(
                text = item.tempMax.asTempString(),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Column 5: Low temperature
            Text(
                text = item.tempMin.asTempString(),
                color = White.copy(alpha = 0.75f),
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Centered loading spinner.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = White)
    }
}
