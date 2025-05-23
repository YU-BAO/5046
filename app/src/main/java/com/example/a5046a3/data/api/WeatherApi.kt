package com.example.a5046a3.api

import com.example.a5046a3.data.api.RetrofitInstance
import com.example.a5046a3.data.api.WeatherApiService

/**
 * Object to expose the Weather API service to other parts of the app (e.g., WorkManager)
 */
object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        RetrofitInstance.weatherApiService
    }
}
