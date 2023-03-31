package com.braydon.android.forecast.domain.repository

import com.braydon.android.forecast.domain.util.Resource
import com.braydon.android.forecast.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}