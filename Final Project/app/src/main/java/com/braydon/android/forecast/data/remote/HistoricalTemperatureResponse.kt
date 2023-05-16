package com.braydon.android.forecast.data.remote

import com.google.gson.annotations.SerializedName

data class HistoricalTemperatureResponse (
    @SerializedName("data") val data: List<HistoricalTemperatureData>
)