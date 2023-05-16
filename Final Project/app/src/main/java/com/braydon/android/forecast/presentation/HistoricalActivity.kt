package com.braydon.android.forecast.presentation

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.braydon.android.forecast.presentation.ui.theme.DarkBlue
import com.braydon.android.forecast.presentation.ui.theme.DeepBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale


private const val TAG = "BUTTON"

class HistoricalActivity : ComponentActivity() {
    private lateinit var selectedDate: Calendar
    private lateinit var pastDay: String
    private lateinit var pastMonth: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var expandedMonthMenu by remember { mutableStateOf(false) }
            var expandedDayMenu by remember { mutableStateOf(false) }
            var selectedMonthIndex by remember { mutableStateOf(0) }
            var selectedDayIndex by remember { mutableStateOf(0) }
            var averageTemp by remember { mutableStateOf("")}
            var highTemp by remember { mutableStateOf("")}
            var lowTemp by remember { mutableStateOf("")}
            var chosenDate by remember { mutableStateOf("")}

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBlue)
            ) {
                Column {
                    Text(
                        text = "Historical Weather",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 16.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    CalendarSelectionButton(onDateSelected = { month, day ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)

                        selectedDate = calendar
                        val formattedDate =
                            SimpleDateFormat("dd/MM", Locale.getDefault()).format(calendar.time)
                        chosenDate = formattedDate
                        val parts = chosenDate.split("/")
                        pastDay = parts[0]
                        pastMonth = parts[1]
                    })

                    TextField(
                        value = chosenDate,
                        onValueChange = { /* no-op */ },
                        label = { Text(text = "Selected Date") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.LightGray,
                            cursorColor = Color.Blue,
                            focusedIndicatorColor = Color.Green,
                            unfocusedIndicatorColor = Color.Gray),
                        enabled = false
                    )
                    Text(
                        text = "Statistics from the past 5 years",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 196.dp, bottom = 16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Button(
                        onClick = {
                            Log.d(TAG, "Button was clicked")
                            lifecycleScope.launch {
                                val temperatureData = getPastTemperatures(pastDay, pastMonth)
                                Log.d("Temps: ", temperatureData.toString())
                                averageTemp = calcAvg(temperatureData)
                                highTemp = calcHigh(temperatureData)
                                lowTemp = calcLow(temperatureData)
                                Log.d(TAG, "Avg: $averageTemp")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DeepBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text(text = "Calculate Statistics")
                    }
                    TextField(
                        value = averageTemp,
                        onValueChange = { averageTemp = it },
                        label = { Text("Average") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.LightGray,
                            cursorColor = Color.Blue,
                            focusedIndicatorColor = Color.Green,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp),
                        enabled = false
                    )
                    TextField(
                        value = highTemp,
                        onValueChange = { averageTemp = it },
                        label = { Text("High") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.LightGray,
                            cursorColor = Color.Blue,
                            focusedIndicatorColor = Color.Green,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp),
                        enabled = false
                    )
                    TextField(
                        value = lowTemp,
                        onValueChange = { averageTemp = it },
                        label = { Text("Low") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.LightGray,
                            cursorColor = Color.Blue,
                            focusedIndicatorColor = Color.Green,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp),
                        enabled = false
                    )
                }

            }
        }
    }
    private suspend fun getPastTemperatures(day: String, month: String): List<Double> {
        val currentYear = LocalDate.now().year
        val pastYears = listOf(currentYear - 1, currentYear - 2, currentYear - 3, currentYear - 4, currentYear - 5)
        val pastTemperatures = mutableListOf<Double>()

        withContext(Dispatchers.IO) { // Run network operations on IO thread
            for (year in pastYears) {
                val dateString = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
                Log.d("dates", dateString)
                val url = URL("https://archive-api.open-meteo.com/v1/archive?latitude=52.52&longitude=13.41&start_date=$dateString&end_date=$dateString&daily=apparent_temperature_max&timezone=America%2FChicago")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = conn.inputStream.bufferedReader().readText()
                    val json = JSONObject(response)
                    val daily = json.getJSONObject("daily")
                    val maxTemperatures = daily.getJSONArray("apparent_temperature_max")
                    val lastMaxTemperature = maxTemperatures.getDouble(maxTemperatures.length() - 1)
                    pastTemperatures.add(lastMaxTemperature)
                }

                conn.disconnect()
            }
        }

        return pastTemperatures
    }


    private fun calcAvg(temperatureData: List<Double>): String {
        val total = String.format("%.2f", temperatureData.sum()).toDouble()
        val avg = total / temperatureData.size
        var fAverage = String.format("%.2f", (avg * 1.8) + 32)

        return "$fAverage°F"
    }

    private fun calcHigh(temperatureData: List<Double>): String {
        var highVal = String.format("%.2f", temperatureData.maxOrNull()).toDouble()
        var fHigh = String.format("%.2f", (highVal * 1.8) + 32)

        return "$fHigh°F"
    }

    private fun calcLow(temperatureData: List<Double>): String {
        var lowVal = String.format("%.2f", temperatureData.minOrNull()).toDouble()
        var fLow = String.format("%.2f", (lowVal * 1.8) + 32)

        return "$fLow°F"
    }

}