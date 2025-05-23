package com.example.a5046a3.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Authentication manager – handles Firebase Auth related functionality
 */
class AuthManager {
    private val TAG = "AuthManager"
    
    // Firebase Auth instance
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    
    // Get the currently logged-in user
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    // Check whether a user is logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    
    /**
     * Register a new user with email and password
     */
    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("User creation failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Login with email and password
     */
    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send password reset email: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Logout
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * Delete the current user account
     */
    suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete account: ${e.message}", e)
            Result.failure(e)
        }
    }
} 