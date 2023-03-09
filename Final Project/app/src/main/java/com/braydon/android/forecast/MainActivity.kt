package com.braydon.android.forecast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button



class MainActivity : AppCompatActivity() {

    private lateinit var dailyForecastButton: Button
    private lateinit var hourlyForecastButton: Button
    private lateinit var historicalWeatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dailyForecastButton = findViewById<Button>(R.id.daily_forecast_button)
        hourlyForecastButton = findViewById<Button>(R.id.hourly_forecast_txt)
        historicalWeatherButton = findViewById<Button>(R.id.average_forecast_txt)

        dailyForecastButton.setOnClickListener{view: View ->
            val intent = Intent(this, DailyForecast::class.java)
            startActivity(intent)
        }

        hourlyForecastButton.setOnClickListener{view: View ->
            val intent = Intent(this, HourlyForecast::class.java)
            startActivity(intent)
        }

        historicalWeatherButton.setOnClickListener{view: View ->
            val intent = Intent(this, HistoricalWeather::class.java)
            startActivity(intent)
        }
    }


}