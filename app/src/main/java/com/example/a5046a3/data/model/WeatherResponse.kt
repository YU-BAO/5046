package com.example.a5046a3.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for weather response from OpenWeatherMap API
 */
data class WeatherResponse(
    val coord: Coordinates,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("sea_level")
    val seaLevel: Int?,
    @SerializedName("grnd_level")
    val groundLevel: Int?
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String,
    val sunrise: Long,
    val sunset: Long
) 