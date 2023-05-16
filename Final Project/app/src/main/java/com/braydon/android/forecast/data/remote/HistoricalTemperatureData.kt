package com.braydon.android.forecast.data.remote

import com.google.gson.annotations.SerializedName

data class HistoricalTemperatureData(
    @SerializedName("timestamp_local") val timestamp: String,
    @SerializedName("temperature") val temperature: Double
)