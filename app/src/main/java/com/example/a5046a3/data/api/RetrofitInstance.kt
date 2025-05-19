package com.example.a5046a3.data.api

import com.example.a5046a3.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton class for creating and managing Retrofit instances
 */
object RetrofitInstance {
    
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private const val API_KEY = "your_api_key_here" // Default API key (replace with BuildConfig.API_KEY in production)
    
    /**
     * HTTP client with logging and timeouts
     */
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Retrofit instance for weather API
     */
    private val weatherRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Weather API service
     */
    val weatherApiService: WeatherApiService by lazy {
        weatherRetrofit.create(WeatherApiService::class.java)
    }
    
    /**
     * Create a custom Retrofit instance for a different API
     * 
     * @param baseUrl Base URL for the API
     * @return Retrofit instance
     */
    fun createCustomRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
} 