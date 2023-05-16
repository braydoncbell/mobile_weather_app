package com.braydon.android.forecast.domain.weather

import android.icu.util.Calendar
import android.icu.util.TimeZone
import com.braydon.android.forecast.data.remote.HistoricalTemperatureData
import com.braydon.android.forecast.data.remote.OpenMeteoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HistoricalWeather {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openMeteoApiService = retrofit.create(OpenMeteoApiService::class.java)

    suspend fun getHistoricalTemperatureData(
        lat: Double,
        lon: Double,
        day: Int,
        month: Int
    ): List<HistoricalTemperatureData> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val response = openMeteoApiService.getHistoricalTemperatureData(
            lat = lat,
            lon = lon,
            day = day,
            month = month,
            yearFrom = currentYear - 3,
            yearTo = currentYear,
            lang = "en",
            unitSystem = "metric",
            timezone = TimeZone.getDefault().id
        )
        return response.data
    }
}