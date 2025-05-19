package com.example.a5046a3.data.repository

import com.example.a5046a3.data.api.RetrofitInstance
import com.example.a5046a3.data.models.WeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Repository for fetching weather data from OpenWeatherMap API
 */
class WeatherRepository {
    private val apiService = RetrofitInstance.weatherApiService
    
    // OpenWeatherMap API Key - normally would be stored in gradle.properties or BuildConfig
    // For demo purposes, using a placeholder - you would need to replace with a real API key
    private val apiKey = "YOUR_API_KEY"
    
    /**
     * Fetch current weather for a given location
     * 
     * @param latitude Latitude of the location
     * @param longitude Longitude of the location
     * @return WeatherInfo object or null if request failed
     */
    suspend fun fetchCurrentWeather(latitude: Double, longitude: Double): Result<WeatherInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCurrentWeather(
                    lat = latitude,
                    lon = longitude,
                    units = "metric",
                    appid = apiKey
                )
                
                if (response.isSuccessful && response.body() != null) {
                    val weatherData = response.body()!!
                    val mainWeather = weatherData.weather.firstOrNull()?.main ?: "Unknown"
                    val description = weatherData.weather.firstOrNull()?.description ?: ""
                    val iconCode = weatherData.weather.firstOrNull()?.icon ?: "01d"
                    
                    Result.success(
                        WeatherInfo(
                            temperature = weatherData.main.temp.toFloat(),
                            condition = mainWeather,
                            humidity = weatherData.main.humidity,
                            windSpeed = weatherData.wind.speed.toFloat(),
                            icon = iconCode,
                            description = description
                        )
                    )
                } else {
                    Result.failure(IOException("API call failed with code: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Get mock weather data for demo purposes
     * Used as a fallback when API calls fail or during development
     */
    fun getMockWeather(): WeatherInfo {
        return WeatherInfo(
            temperature = 22.5f,
            condition = "Partly Cloudy",
            humidity = 65,
            windSpeed = 5.5f,
            icon = "03d",
            description = "scattered clouds"
        )
    }
} 