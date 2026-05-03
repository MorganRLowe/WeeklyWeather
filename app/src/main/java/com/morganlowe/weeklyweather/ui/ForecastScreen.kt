package com.morganlowe.weeklyweather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * Forecast screen. Mirrors the original WeatherChecker layout:
 * search bar pinned at the top, current weather + 14-day forecast below.
 * Searching a new city replaces the content without leaving the screen.
 */
@Composable
fun ForecastScreen(
    data: WeatherData,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var cityInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Search bar pinned at the top — same as original layout
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = cityInput,
                    onValueChange = { cityInput = it },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search"
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        disabledContainerColor = White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLeadingIconColor = Color.DarkGray,
                        unfocusedLeadingIconColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                TextButton(onClick = {
                    onSearch(cityInput)
                    cityInput = ""
                }) {
                    Text(
                        text = "Go!",
                        color = White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Current weather block
        item {
            CurrentWeatherSection(current = data.current)
        }

        item {
            HorizontalDivider(
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        // Stats row
        item {
            StatsRow(current = data.current)
        }

        item {
            HorizontalDivider(
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

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

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CurrentWeatherSection(current: CurrentWeather) {
    Column(modifier = Modifier.fillMaxWidth()) {
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

        Text(
            text = current.temp.asTempString(),
            color = White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Text(
            text = "Feels like ${current.apparentTemp.asTempString()}",
            color = White.copy(alpha = 0.85f),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
private fun StatsRow(current: CurrentWeather) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatColumn(R.drawable.min, current.tempMin.asTempString())
        StatColumn(R.drawable.max, current.tempMax.asTempString())
        StatColumn(R.drawable.humidity, "${current.humidity}%")
        StatColumn(R.drawable.pressure, "${current.pressure.toInt()} hPa")
        StatColumn(R.drawable.wind, "${current.windSpeed.toInt()} mph")
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
            Text(
                text = item.date.asDayOfWeek(),
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = item.date.asShortDate(),
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

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

            Text(
                text = item.tempMax.asTempString(),
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

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

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = White)
    }
}