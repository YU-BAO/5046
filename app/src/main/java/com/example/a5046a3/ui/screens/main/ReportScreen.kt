package com.example.a5046a3.ui.screens.main

import android.app.Application
import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.a5046a3.auth.AuthManager
import com.example.a5046a3.data.models.Mood
import com.example.a5046a3.data.models.WellnessEntry
import com.example.a5046a3.data.models.WellnessEntryEntity
import com.example.a5046a3.ui.viewmodels.WellnessViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


/**
 * Report screen showing visualizations of wellness data
 *
 * @param navController Navigation controller for navigating between screens
 * @param viewModel WellnessViewModel to fetch data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: WellnessViewModel = viewModel()
) {
    // Available time periods for reports
    val timePeriods = listOf(
        "This Week",
        "Last Week",
        "Last 2 Weeks",
        "Last Month",
        "Custom Range"
    )

    // State for UI controls
    var selectedTimePeriod by remember { mutableStateOf(timePeriods[0]) }
    var showTimePeriodDropdown by remember { mutableStateOf(false) }
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var customStartDate by remember { mutableStateOf(Date()) }
    var customEndDate by remember { mutableStateOf(Date()) }

    val authManager = AuthManager()
//    val userId = "current_user"
    val userId = authManager.getCurrentUser()?.uid ?: "current_user"
    val entries by viewModel.getEntriesByUser(userId).collectAsState()
    // Collect entries from ViewModel with lifecycle awareness

    // Date formatter for display
    val dateFormatter = SimpleDateFormat("MMM d", Locale.getDefault())
    val dayFormatter = SimpleDateFormat("E", Locale.getDefault())

    // Stable calendar instance
    val calendar = remember { Calendar.getInstance() }

    // Calculate date range based on selected time period
    val (startDate, endDate) = remember(selectedTimePeriod, customStartDate, customEndDate) {
        calendar.time = Date()
        when (selectedTimePeriod) {
            "This Week" -> {
                val end = calendar.time // May 22, 2025
                calendar.add(Calendar.DAY_OF_MONTH, -6)
                val start = calendar.time // May 16, 2025
                Pair(start, end)
            }
            "Last Week" -> {
                calendar.add(Calendar.DAY_OF_MONTH, -7) // Move to May 15, 2025
                val end = calendar.time // May 15, 2025
                calendar.add(Calendar.DAY_OF_MONTH, -6)
                val start = calendar.time // May 9, 2025
                Pair(start, end)
            }
            "Last 2 Weeks" -> {
                val end = calendar.time // May 22, 2025
                calendar.add(Calendar.DAY_OF_MONTH, -13)
                val start = calendar.time // May 9, 2025
                Pair(start, end)
            }
            "Last Month" -> {
                val end = calendar.time // May 22, 2025
                calendar.add(Calendar.DAY_OF_MONTH, -30)
                val start = calendar.time // Apr 22, 2025
                Pair(start, end)
            }
            "Custom Range" -> {
                Pair(customStartDate, customEndDate)
            }
            else -> {
                val end = calendar.time // May 22, 2025
                calendar.add(Calendar.DAY_OF_MONTH, -6)
                val start = calendar.time // May 16, 2025
                Pair(start, end)
            }
        }
    }

    // Filter entries by date range
    val filteredEntries = remember(entries, startDate, endDate) {
        entries.filter { entry ->
            !entry.date.before(startDate) && !entry.date.after(endDate)
        }.sortedBy { it.date }
    }

    // Generate chart data for up to 7 days in the selected period
    val (displayDays, moodData, sleepData) = remember(filteredEntries, startDate, endDate) {
        val days = mutableListOf<String>()
        val moods = mutableListOf<Float>()
        val sleeps = mutableListOf<Float>()

        // Calculate number of days in the range (max 7 for display)
        val tempCalendar = Calendar.getInstance().apply { time = endDate }
        val daysInRange = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
        val displayCount = minOf(daysInRange, 7)

        // Start from the most recent date, going backward
        repeat(displayCount) { index ->
            tempCalendar.time = endDate
            tempCalendar.add(Calendar.DAY_OF_MONTH, -(displayCount - 1 - index))
            val currentDate = tempCalendar.time
            days.add(dayFormatter.format(currentDate))

            // Find entry for this date (assuming one entry per day)
            val entry = filteredEntries.find { entry ->
                val entryDate = Calendar.getInstance().apply { time = entry.date }
                val checkDate = Calendar.getInstance().apply { time = currentDate }
                entryDate.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                        entryDate.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
            }

            // Map mood to 0-1 scale based on Mood enum
            val moodValue = entry?.mood?.let {
                when (it) {
                    Mood.VERY_HAPPY -> 1.0f
                    Mood.HAPPY -> 0.75f
                    Mood.NEUTRAL -> 0.5f
                    Mood.SAD -> 0.25f
                    else -> 0.0f // Fallback for unknown Mood values
                }
            } ?: 0.0f
            moods.add(moodValue)
            sleeps.add(entry?.sleepHours ?: 0.0f)
        }

        Triple(days, moods, sleeps)
    }

    // Calculate mood distribution
    val moodDistribution = remember(filteredEntries) {
        val total = filteredEntries.size
        if (total == 0) {
            mapOf(
                "Very Happy" to 0,
                "Happy" to 0,
                "Neutral" to 0,
                "Sad" to 0,
                "Very Sad" to 0
            )
        } else {
            val veryHappyCount = filteredEntries.count { it.mood == Mood.VERY_HAPPY }
            val happyCount = filteredEntries.count { it.mood == Mood.HAPPY }
            val neutralCount = filteredEntries.count { it.mood == Mood.NEUTRAL }
            val sadCount = filteredEntries.count { it.mood == Mood.SAD }
            val verySadCount = filteredEntries.count { it.mood == Mood.VERY_SAD }
            mapOf(
                "Very Happy" to (veryHappyCount * 100 / total),
                "Happy" to (happyCount * 100 / total),
                "Neutral" to (neutralCount * 100 / total),
                "Sad" to (sadCount * 100 / total),
                "Very Sad" to (verySadCount * 100 / total)
            )
        }
    }

    // Format date range for display
    val formattedStartDate = dateFormatter.format(startDate)
    val formattedEndDate = dateFormatter.format(endDate)

    // Context for DatePickerDialog
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Wellness Report") },
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
                text = "Your Wellness Summary",
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
                    text = "$formattedStartDate - $formattedEndDate",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
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
                    if (displayDays.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            displayDays.forEachIndexed { index, day ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height((moodData[index] * 150).dp) // Scale height dynamically
                                            .background(
                                                color = when {
                                                    moodData[index] >= 1.0f -> Color(0xFF7B1FA2) // Very Happy
                                                    moodData[index] >= 0.75f -> Color(0xFFE91E63) // Happy
                                                    moodData[index] >= 0.5f -> Color(0xFFFFC107) // Neutral
                                                    moodData[index] >= 0.25f -> Color(0xFF009688) // Sad
                                                    else -> Color(0xFFFF5722) // Very Sad
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
                    } else {
                        Text(
                            text = "No mood data available for this period",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
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

                    // Happy row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "😊",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Happy",
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
                                        .width(150.dp * moodDistribution["Happy"]!! / 100)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFE91E63))
                                )
                            }

                            Text(
                                text = "${moodDistribution["Happy"]}%",
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
                                        .background(Color(0xFFFFC107))
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
                    Spacer(modifier = Modifier.height(8.dp))

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
                                text = "Very Sad",
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
                                        .width(150.dp * moodDistribution["Very Sad"]!! / 100)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFFF5722))
                                )
                            }

                            Text(
                                text = "${moodDistribution["Very Sad"]}%",
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

            // Sleep pattern chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                if (displayDays.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val maxSleep = sleepData.maxOfOrNull { it } ?: 8f
                            val minSleep = sleepData.minOfOrNull { it } ?: 0f
                            val range = if (maxSleep == minSleep) 8f else maxSleep - minSleep

                            // Scale points to fit the canvas
                            val points = sleepData.mapIndexed { index, hours ->
                                val x = width * index / (sleepData.size - 1).coerceAtLeast(1)
                                val normalizedY = if (range == 0f) 0.5f else (hours - minSleep) / range
                                val y = height - (normalizedY * height * 0.8f) - (height * 0.1f)
                                Offset(x, y)
                            }

                            // Draw connecting lines
                            for (i in 0 until points.size - 1) {
                                drawLine(
                                    color = Color(0xFF7B1FA2),
                                    start = points[i],
                                    end = points[i + 1],
                                    strokeWidth = 3f,
                                    cap = StrokeCap.Round
                                )
                            }

                            // Draw data points
                            points.forEach { point ->
                                drawCircle(
                                    color = Color(0xFF7B1FA2),
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
                } else {
                    Text(
                        text = "No sleep data available for this period",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Custom date range dialog
    if (showDateRangeDialog) {
        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDateRangeDialog = false },
            title = { Text("Select Date Range") },
            text = {
                Column {
                    Text("From:", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = dateFormatter.format(customStartDate),
                        onValueChange = { /* Read-only, handled by date picker */ },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select start date",
                                modifier = Modifier.clickable { showStartDatePicker = true }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("To:", style = MaterialTheme.typography.bodyLarge)
                    OutlinedTextField(
                        value = dateFormatter.format(customEndDate),
                        onValueChange = { /* Read-only, handled by date picker */ },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select end date",
                                modifier = Modifier.clickable { showEndDatePicker = true }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDateRangeDialog = false
                        if (customEndDate.before(customStartDate)) {
                            customEndDate = customStartDate
                        }
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

        // Start date picker dialog
        if (showStartDatePicker) {
            val calendar = Calendar.getInstance().apply { time = customStartDate }
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    customStartDate = calendar.time
                    showStartDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // End date picker dialog
        if (showEndDatePicker) {
            val calendar = Calendar.getInstance().apply { time = customEndDate }
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    customEndDate = calendar.time
                    showEndDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}