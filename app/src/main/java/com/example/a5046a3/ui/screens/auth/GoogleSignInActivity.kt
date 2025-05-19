package com.example.a5046a3.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.a5046a3.MainActivity
import com.example.a5046a3.R
import com.example.a5046a3.auth.GoogleSignInManager
import kotlinx.coroutines.launch

/**
 * Activity that handles Google Sign In
 * Note: This is a simplified implementation. In a production app, you would handle the Google Sign In process properly.
 */
class GoogleSignInActivity : ComponentActivity() {

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
                // Note: In this placeholder implementation, success will always be true
                // In a real app, the result would be processed by GoogleSignIn APIs
                val success = googleSignInManager.handleSignInResult(result.data)
                if (success) {
                    // Sign in success, go to main activity
                    Toast.makeText(this@GoogleSignInActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    // Navigate to main screen
                    navigateToMainScreen()
                } else {
                    // Sign in failed
                    Toast.makeText(this@GoogleSignInActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    // Finish activity and go back to login
                    finish()
                }
            }
        }
        
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Use LaunchedEffect to start Google Sign In when the screen is shown
                LaunchedEffect(Unit) {
                    startGoogleSignIn()
                }
            }
        }
    }
    
    private fun startGoogleSignIn() {
        val signInIntent = googleSignInManager.getSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }
    
    private fun navigateToMainScreen() {
        // Create intent to MainActivity in the correct package
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
} 