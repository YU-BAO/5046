package com.example.a5046a3.ui.screens.auth

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a5046a3.R
import com.example.a5046a3.auth.AuthManager
import com.example.a5046a3.auth.GoogleSignInManager
import com.example.a5046a3.navigation.Screen
import kotlinx.coroutines.launch
import com.example.a5046a3.ui.screens.auth.GoogleSignInActivity
import com.example.a5046a3.StudentWellnessApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var rememberPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val authManager = remember { AuthManager() }
    val googleSignInManager = remember { GoogleSignInManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val userManager = StudentWellnessApp.userManager
    
    // Check if user is already logged in, if so, navigate directly to home page
    LaunchedEffect(Unit) {
        if (authManager.isUserLoggedIn() || userManager.isLoggedIn()) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo - Student cap icon
        Image(
            painter = painterResource(id = R.drawable.ic_graduation_cap),
            contentDescription = "Graduation Cap",
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // App title
        Text(
            text = "Student Wellness Tracker",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Subtitle
        Text(
            text = "Track and improve your daily wellness habits",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        
        // Remember password checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberPassword,
                onCheckedChange = { rememberPassword = it }
            )
            
            Text(
                text = "Remember password",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Add Forgot Password button
            TextButton(onClick = {
                if (email.isNotEmpty()) {
                    coroutineScope.launch {
                        try {
                            authManager.sendPasswordResetEmail(email)
                            Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Failed to send reset email: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    errorMessage = "Please enter your email"
                }
            }) {
                Text("Forgot Password?")
            }
        }
        
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Login button
        Button(
            onClick = {
                // Reset error message
                errorMessage = null
                
                // Basic validation
                when {
                    email.isEmpty() -> errorMessage = "Please enter your email"
                    password.isEmpty() -> errorMessage = "Please enter your password"
                    else -> {
                        isLoading = true
                        coroutineScope.launch {
                            val result = authManager.loginWithEmail(email, password)
                            isLoading = false
                            result.fold(
                                onSuccess = { firebaseUser ->
                                    userManager.saveUserId(firebaseUser.uid)
                                    Log.d("LoginScreen", "User ID saved: ${firebaseUser.uid}")
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onFailure = { exception ->
                                    errorMessage = exception.message ?: "Login failed. Please check your credentials."
                                }
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }
        
        // OR divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = " OR ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Divider(modifier = Modifier.weight(1f))
        }
        
        // Google sign in button
        OutlinedButton(
            onClick = {
                val intent = Intent(context, GoogleSignInActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Continue with Google"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Register link
        TextButton(
            onClick = { navController.navigate(Screen.Register.route) }
        ) {
            Text("Don't have an account? Sign up")
        }
    }
} 