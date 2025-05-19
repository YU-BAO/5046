package com.example.a5046a3.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data class for the OpenWeatherMap API response
 */
data class WeatherResponse(
    val coord: Coordinates,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val name: String
)

/**
 * Coordinates data class
 */
data class Coordinates(
    val lat: Double,
    val lon: Double
)

/**
 * Weather condition data class
 */
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

/**
 * Main weather data class
 */
data class Main(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

/**
 * Wind data class
 */
data class Wind(
    val speed: Double,
    val deg: Int
)

/**
 * Clouds data class
 */
data class Clouds(
    val all: Int
) 