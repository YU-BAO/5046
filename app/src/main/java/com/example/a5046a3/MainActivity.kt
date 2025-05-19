package com.example.a5046a3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.a5046a3.navigation.AppNavigation
import com.example.a5046a3.ui.theme._5046A3Theme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            Log.d(TAG, "MainActivity onCreate()")
            
            setContent {
                _5046A3Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
            
            Log.d(TAG, "MainActivity UI setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "MainActivity initialization failed", e)
        }
    }
}