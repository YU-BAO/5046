package com.example.a5046a3.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import com.example.a5046a3.R

/**
 * Manager class for Google Sign In authentication with Firebase
 */
class GoogleSignInManager(private val context: Context) {
    
    private val TAG = "GoogleSignInManager"
    
    // Firebase Auth instance
    private val auth: FirebaseAuth = Firebase.auth
    
    // Google Sign In client
    private val googleSignInClient: GoogleSignInClient
    
    init {
        // Configure Google Sign In with Firebase integration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    /**
     * Get Google Sign In intent for starting the authentication flow
     */
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    /**
     * Handle the sign-in result from Google Sign In
     */
    suspend fun handleSignInResult(data: Intent?): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.await()
                Log.d(TAG, "Google sign in succeeded")
                
                // Get ID token from Google Sign In account
                val idToken = account.idToken
                if (idToken != null) {
                    // Authenticate with Firebase using Google credentials
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Log.w(TAG, "No ID token received from Google")
                    false
                }
            } catch (e: Exception) {
                Log.w(TAG, "Google sign in failed", e)
                false
            }
        }
    }
    
    /**
     * Authenticate with Firebase using Google credentials
     */
    private suspend fun firebaseAuthWithGoogle(idToken: String): Boolean {
        return suspendCoroutine { continuation ->
            try {
                // Get credentials from Google ID token
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                
                // Sign in to Firebase with Google credentials
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            Log.d(TAG, "User: ${user?.displayName}, ${user?.email}")
                            continuation.resume(true)
                        } else {
                            // Sign in failed
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            continuation.resume(false)
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Firebase authentication failed", e)
                continuation.resume(false)
            }
        }
    }
    
    /**
     * Sign out from Firebase and Google
     */
    fun signOut() {
        // Sign out from Firebase
        auth.signOut()
        
        // Sign out from Google
        googleSignInClient.signOut()
    }
    
    /**
     * Check if user is signed in
     */
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }
    
    /**
     * Get current user
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
} 