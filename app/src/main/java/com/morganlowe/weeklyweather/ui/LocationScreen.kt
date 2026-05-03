package com.morganlowe.weeklyweather.ui

import androidx.compose.foundation.layout.*
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
import com.morganlowe.weeklyweather.ui.theme.White

/**
 * The location entry screen.
 *   - "WeeklyWeather" title at top
 *   - "Enter a city to see its forecast" subtitle
 *   - Search field with magnifying-glass icon
 *   - "Go!" button below
 *   - Optional error message
 *
 * White text on the teal background, search field has dark text on white background.
 */
@Composable
fun LocationScreen(
    onSearch: (String) -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "WeeklyWeather",
            color = White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter a city to see its forecast",
            color = White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // White-background text input — visible against the teal page
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
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedLeadingIconColor = Color.DarkGray,
                unfocusedLeadingIconColor = Color.DarkGray
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big "Go!" button — surface-color background to stand out from the teal
        Button(
            onClick = { onSearch(cityInput) },
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = androidx.compose.ui.graphics.Color(0xFF3EC9BE)  // Teal
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Go!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Optional error message
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = White.copy(alpha = 0.95f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
