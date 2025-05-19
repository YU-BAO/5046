package com.example.a5046a3.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.a5046a3.MainActivity
import com.example.a5046a3.R
import com.example.a5046a3.auth.GoogleSignInManager
import kotlinx.coroutines.launch

/**
 * Activity that handles Google Sign In using Firebase Authentication
 */
class GoogleSignInActivity : ComponentActivity() {

    private val TAG = "GoogleSignInActivity"
    private lateinit var googleSignInManager: GoogleSignInManager
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Google Sign In Manager
        googleSignInManager = GoogleSignInManager(this)
        
        // Register for Google Sign In result
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            lifecycleScope.launch {
                val success = googleSignInManager.handleSignInResult(result.data)
                handleAuthResult(success)
            }
        }
        
        setContent {
            var isLoading by remember { mutableStateOf(true) }
            
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "使用Google账号登录中...",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // Use LaunchedEffect to start the Google Sign In flow
                LaunchedEffect(Unit) {
                    startGoogleSignIn()
                }
            }
        }
    }
    
    private fun startGoogleSignIn() {
        try {
            // Get the sign-in intent from the GoogleSignInManager
            val signInIntent = googleSignInManager.getSignInIntent()
            
            // Launch the Google Sign In activity
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Google Sign In", e)
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun handleAuthResult(success: Boolean) {
        if (success) {
            // Sign in success
            Log.d(TAG, "Google auth successful")
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            navigateToMainScreen()
        } else {
            // Sign in failed
            Log.w(TAG, "Google auth failed")
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun navigateToMainScreen() {
        // Create intent to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
} 