package com.braydon.android.forecast.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {
    @GET("/history/temperature")
    suspend fun getHistoricalTemperatureData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("day") day: Int,
        @Query("month") month: Int,
        @Query("year_from") yearFrom: Int,
        @Query("year_to") yearTo: Int,
        @Query("lang") lang: String,
        @Query("unit_system") unitSystem: String,
        @Query("tz") timezone: String
    ): HistoricalTemperatureResponse
}