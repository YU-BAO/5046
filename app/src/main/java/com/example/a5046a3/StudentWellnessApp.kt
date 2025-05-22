package com.example.a5046a3

import android.app.Application
import android.util.Log
import com.example.a5046a3.data.database.AppDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseOptions
import com.example.a5046a3.data.manager.UserManager

/**
 * Application Main Class - Initialize necessary components at app startup
 */
class StudentWellnessApp : Application() {
    companion object {
        private const val TAG = "StudentWellnessApp"
        lateinit var userManager: UserManager
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        
        try {
            // Initialize Firebase
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d(TAG, "Initializing Firebase")
                FirebaseApp.initializeApp(this)
            } else {
                Log.d(TAG, "Firebase already initialized")
            }
            
            // Check if Firebase initialization was successful
            val firebaseApp = FirebaseApp.getInstance()
            Log.d(TAG, "Firebase instance name: ${firebaseApp.name}")
            
            // Check authentication instance
            val auth = FirebaseAuth.getInstance()
            Log.d(TAG, "FirebaseAuth instance acquired")
            
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
        }
        
        // 初始化数据库
        AppDatabase.getDatabase(this)

        // init UserManager
        userManager = UserManager.getInstance(this)
    }
} 