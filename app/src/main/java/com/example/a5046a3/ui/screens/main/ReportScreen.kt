package com.example.a5046a3.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a5046a3.data.models.Mood
import com.example.a5046a3.data.models.WellnessEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * Report screen showing visualizations of wellness data
 * 
 * @param navController Navigation controller for navigating between screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(navController: NavController) {
    // Available time periods for reports
    val timePeriods = listOf(
        "This Week",
        "Last Week",
        "Last 2 Weeks",
        "Last Month",
        "Custom Range"
    )
    
    // State for selected time period
    var selectedTimePeriod by remember { mutableStateOf(timePeriods[0]) }
    var showTimePeriodDropdown by remember { mutableStateOf(false) }
    
    // Mock data for charts
    val weekDays = listOf("Thu", "Fri", "Sat", "Sun", "Mon", "Tue")
    val moodData = listOf(0.3f, 0.2f, 0.9f, 0.9f, 0.2f, 0.2f) // Values 0-1 for mood chart heights
    val sleepData = listOf(6.2f, 5.5f, 8.0f, 5.8f, 7.2f, 5.0f) // Hours of sleep
    
    val moodDistribution = mapOf(
        "Very Happy" to 33,
        "Neutral" to 50,
        "Sad" to 16
    )
    
    // Date range for the report
    val dateFormatter = SimpleDateFormat("MMM d", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val endDate = dateFormatter.format(calendar.time)
    calendar.add(Calendar.DAY_OF_MONTH, -7)
    val startDate = dateFormatter.format(calendar.time)
    
    // Custom date range dialog state
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var customStartDate by remember { mutableStateOf(startDate) }
    var customEndDate by remember { mutableStateOf(endDate) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weekly Report") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Header section
            Text(
                text = "Your Weekly Wellness Summary",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
            
            // Time period selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$startDate - $endDate",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                
                Box {
                    OutlinedButton(
                        onClick = { showTimePeriodDropdown = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF5C6BC0)
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(selectedTimePeriod)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Time Period"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showTimePeriodDropdown,
                        onDismissRequest = { showTimePeriodDropdown = false }
                    ) {
                        timePeriods.forEach { period ->
                            DropdownMenuItem(
                                text = { Text(period) },
                                onClick = {
                                    selectedTimePeriod = period
                                    showTimePeriodDropdown = false
                                    
                                    // Show custom date picker if "Custom Range" is selected
                                    if (period == "Custom Range") {
                                        showDateRangeDialog = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Mood Trends section
            Text(
                text = "Mood Trends",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mood chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Mood bar chart
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weekDays.forEachIndexed { index, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Mood bar with different colors based on value
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height((moodData[index] * 120).dp)
                                        .background(
                                            color = when {
                                                moodData[index] > 0.8f -> Color(0xFF7B1FA2) // Purple for very happy
                                                moodData[index] > 0.6f -> Color(0xFF7B1FA2) // Purple for happy
                                                moodData[index] > 0.4f -> Color(0xFFE0E0E0) // Gray for neutral
                                                moodData[index] > 0.2f -> Color(0xFFE0E0E0) // Gray for neutral
                                                else -> Color(0xFF009688) // Teal for sad
                                            }
                                        )
                                )
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = day,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    // Mood distribution section
                    Text(
                        text = "Mood Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Very Happy row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "😁",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Very Happy",
                                fontSize = 14.sp
                            )
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp * moodDistribution["Very Happy"]!! / 100)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF7B1FA2))
                                )
                            }
                            
                            Text(
                                text = "${moodDistribution["Very Happy"]}%",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Neutral row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "😐",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Neutral",
                                fontSize = 14.sp
                            )
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp * moodDistribution["Neutral"]!! / 100)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF7B1FA2))
                                )
                            }
                            
                            Text(
                                text = "${moodDistribution["Neutral"]}%",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Sad row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "😔",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Sad",
                                fontSize = 14.sp
                            )
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp * moodDistribution["Sad"]!! / 100)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF009688))
                                )
                            }
                            
                            Text(
                                text = "${moodDistribution["Sad"]}%",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sleep Patterns section
            Text(
                text = "Sleep Patterns",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sleep pattern line chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                ) {
                    // Draw sleep line chart
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val maxSleep = sleepData.maxOf { it }
                        val minSleep = sleepData.minOf { it }
                        val range = maxSleep - minSleep
                        
                        // Scale points to fit the canvas
                        val points = sleepData.mapIndexed { index, hours ->
                            val x = width * index / (sleepData.size - 1)
                            val normalizedY = (hours - minSleep) / range
                            val y = height - (normalizedY * height * 0.8f) - (height * 0.1f)
                            Offset(x, y)
                        }
                        
                        // Draw connecting lines
                        for (i in 0 until points.size - 1) {
                            drawLine(
                                color = Color(0xFF7B1FA2),  // Purple line color
                                start = points[i],
                                end = points[i + 1],
                                strokeWidth = 3f,
                                cap = StrokeCap.Round
                            )
                        }
                        
                        // Draw data points
                        points.forEach { point ->
                            drawCircle(
                                color = Color(0xFF7B1FA2),  // Purple point color
                                radius = 6f,
                                center = point
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3f,
                                center = point
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp)) // Extra space at bottom for bottom navigation
        }
    }
    
    // Custom date range dialog
    if (showDateRangeDialog) {
        AlertDialog(
            onDismissRequest = { showDateRangeDialog = false },
            title = { Text("Select Date Range") },
            text = {
                Column {
                    // This is a simplified date picker - in a real app, you would
                    // use a proper DatePicker component from a library
                    Text("From:", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = customStartDate,
                        onValueChange = { customStartDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Start Date"
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("To:", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = customEndDate,
                        onValueChange = { customEndDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "End Date"
                            )
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDateRangeDialog = false
                        // Update the displayed date range
                    }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
} 