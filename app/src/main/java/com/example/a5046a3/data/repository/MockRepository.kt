package com.example.a5046a3.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import com.example.a5046a3.data.models.User
import com.example.a5046a3.data.models.WeatherInfo
import com.example.a5046a3.data.models.WellnessEntry
import java.util.Calendar
import java.util.Date
import java.util.UUID

/**
 * Mock repository for wellness entries
 * This is used for UI-only implementation without database
 */
object MockRepository {
    private val entries = mutableListOf<WellnessEntry>()
    private val entriesLiveData = MutableLiveData<List<WellnessEntry>>()
    
    init {
        // Add some mock data
        generateMockData()
        refreshLiveData()
    }
    
    /**
     * Get all entries as LiveData
     */
    fun getAllEntries(): LiveData<List<WellnessEntry>> {
        return entriesLiveData
    }
    
    /**
     * Add a new entry
     */
    fun addEntry(entry: WellnessEntry) {
        entries.add(entry)
        refreshLiveData()
    }
    
    /**
     * Update an existing entry
     */
    fun updateEntry(entry: WellnessEntry) {
        val index = entries.indexOfFirst { it.id == entry.id }
        if (index != -1) {
            entries[index] = entry
            refreshLiveData()
        }
    }
    
    /**
     * Delete an entry
     */
    fun deleteEntry(entry: WellnessEntry) {
        entries.removeIf { it.id == entry.id }
        refreshLiveData()
    }
    
    /**
     * Clear all entries
     */
    fun clearEntries() {
        entries.clear()
        refreshLiveData()
    }
    
    /**
     * Refresh the LiveData with the current list of entries
     */
    private fun refreshLiveData() {
        entriesLiveData.postValue(entries.toList())
    }
    
    /**
     * Generate mock data for testing
     */
    private fun generateMockData() {
        // Create a calendar for date manipulation
        val calendar = Calendar.getInstance()
        
        // Add some mock entries for the past week
        for (i in 6 downTo 0) {
            calendar.time = Date() // Reset to today
            calendar.add(Calendar.DAY_OF_YEAR, -i) // Subtract days
            
            val entry = WellnessEntry(
                id = UUID.randomUUID().toString(),
                date = calendar.time,
                mood = when ((0..4).random()) {
                    0 -> Mood.VERY_SAD
                    1 -> Mood.SAD
                    2 -> Mood.NEUTRAL
                    3 -> Mood.HAPPY
                    else -> Mood.VERY_HAPPY
                },
                sleepHours = (5.0 + Math.random() * 4.0).toFloat(),
                exerciseLevel = when ((0..3).random()) {
                    0 -> ExerciseLevel.NONE
                    1 -> ExerciseLevel.LIGHT
                    2 -> ExerciseLevel.MODERATE
                    else -> ExerciseLevel.INTENSE
                },
                notes = if (Math.random() > 0.5) "Mock note for day ${i + 1}" else ""
            )
            
            entries.add(entry)
        }
    }
}

/**
 * Extension function to get a random element from a range of array indices
 */
private fun <T> Array<T>.random(): T = this[(indices).random()]

/**
 * Extension function to get a random floating point number from a range
 */
private fun ClosedFloatingPointRange<Double>.random(): Double = 
    start + (Math.random() * (endInclusive - start)) 