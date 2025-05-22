package com.example.a5046a3.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a5046a3.BuildConfig
import com.example.a5046a3.data.api.RetrofitInstance
import com.example.a5046a3.data.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class HomeViewModel : ViewModel() {

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    fun fetchWeather(lat: Double = -37.8136, lon: Double = 144.9631) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.weatherApiService.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    appid = BuildConfig.OPENWEATHER_API_KEY
                )
                if (response.isSuccessful) {
                    Log.d("WeatherDebug", "Success: ${response.body()}")
                    _weather.value = response.body()
                } else {
                    Log.e("WeatherDebug", "Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("WeatherDebug", "Exception: ${e.message}")
            }
        }
    }
}
