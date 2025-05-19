package com.example.a5046a3.data.models

/**
 * Domain model for weather information
 * Simplified model that contains only the essential weather data needed by the UI
 */
data class WeatherInfo(
    val temperature: Float,  // Temperature in Celsius
    val condition: String,   // Weather condition (e.g., "Clear", "Cloudy", "Rain")
    val humidity: Int,       // Humidity percentage
    val windSpeed: Float,    // Wind speed in meters per second
    val icon: String,        // Icon code for weather conditions
    val description: String = "",  // Detailed weather description
    val iconUrl: String = "https://openweathermap.org/img/wn/${icon}@2x.png"  // Full URL to weather icon
) 