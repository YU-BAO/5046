package com.example.a5046a3.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Data entry screen for logging wellness information
 * 
 * @param navController Navigation controller for navigating between screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEntryScreen(navController: NavController) {
    // State for form values
    var selectedDate by remember { mutableStateOf(SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault()).format(Date())) }
    var selectedMood by remember { mutableStateOf("Neutral") }
    var sleepHours by remember { mutableStateOf(7f) }
    var selectedExerciseLevel by remember { mutableStateOf("None") }
    var notes by remember { mutableStateOf("") }
    
    // State for dropdown menus
    var expandedMoodDropdown by remember { mutableStateOf(false) }
    var expandedExerciseDropdown by remember { mutableStateOf(false) }
    
    // State for success dialog
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Coroutine scope for delay
    val coroutineScope = rememberCoroutineScope()
    
    // Options for dropdowns
    val moodOptions = listOf("Very Happy", "Happy", "Neutral", "Sad", "Very Sad")
    val exerciseOptions = listOf("None", "Light", "Moderate", "Intense", "Very Intense")
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Wellness Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF5C6BC0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "How are you feeling today?",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Date selection field
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { /* Date picker would handle this */ },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mood selection field
                Text(
                    text = "Mood",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                ExposedDropdownMenuBox(
                    expanded = expandedMoodDropdown,
                    onExpandedChange = { expandedMoodDropdown = it }
                ) {
                    OutlinedTextField(
                        value = "😐 $selectedMood",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMoodDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expandedMoodDropdown,
                        onDismissRequest = { expandedMoodDropdown = false }
                    ) {
                        moodOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = when (option) {
                                            "Very Happy" -> "😄 Very Happy"
                                            "Happy" -> "🙂 Happy"
                                            "Neutral" -> "😐 Neutral"
                                            "Sad" -> "🙁 Sad"
                                            "Very Sad" -> "😢 Very Sad"
                                            else -> "😐 Neutral"
                                        }
                                    ) 
                                },
                                onClick = {
                                    selectedMood = option
                                    expandedMoodDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sleep duration
                Text(
                    text = "Sleep Duration: ${sleepHours.toInt()} hours ${((sleepHours - sleepHours.toInt()) * 60).toInt()} minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Sleep slider
                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 0f..12f,
                    steps = 23,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF3F51B5),
                        activeTrackColor = Color(0xFF3F51B5),
                        inactiveTrackColor = Color(0xFFD0D0D0)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Exercise level selection field
                Text(
                    text = "Exercise Level",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                ExposedDropdownMenuBox(
                    expanded = expandedExerciseDropdown,
                    onExpandedChange = { expandedExerciseDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedExerciseLevel,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedExerciseDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expandedExerciseDropdown,
                        onDismissRequest = { expandedExerciseDropdown = false }
                    ) {
                        exerciseOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedExerciseLevel = option
                                    expandedExerciseDropdown = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Notes field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Save button
                Button(
                    onClick = {
                        // Show success dialog
                        showSuccessDialog = true
                        
                        // Automatically navigate back after delay
                        coroutineScope.launch {
                            delay(2000) // Wait for 2 seconds
                            showSuccessDialog = false
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3F51B5)
                    )
                ) {
                    Text("Save Entry")
                }
            }
        }
        
        // Success Dialog
        if (showSuccessDialog) {
            Dialog(
                onDismissRequest = { 
                    showSuccessDialog = false
                    navController.popBackStack()
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Success icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF4CAF50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Success text
                        Text(
                            text = "Saved Successfully!",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Your wellness entry has been saved.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Dismiss button
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }
        }
    }
} 