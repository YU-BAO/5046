package com.example.a5046a3.data.api

import com.example.a5046a3.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for interacting with the OpenWeatherMap API
 */
interface WeatherApiService {
    
    /**
     * Get current weather data for a specific location
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param units Units of measurement (standard, metric, imperial)
     * @param appid API key
     * @return Response containing current weather data
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String
    ): Response<WeatherResponse>
}
