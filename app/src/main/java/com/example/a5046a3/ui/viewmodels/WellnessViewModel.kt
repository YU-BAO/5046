package com.example.a5046a3.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a5046a3.data.database.AppDatabase
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import com.example.a5046a3.data.models.WellnessEntry
import com.example.a5046a3.data.repository.WellnessRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

/**
 * ViewModel class: Handles business logic for wellness data
 */
class WellnessViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WellnessRepository
    
    // StateFlow for all entries
    val allEntries: StateFlow<List<WellnessEntry>>

    init {
        // Initialize database and repository
        val database = AppDatabase.getDatabase(application)
        repository = WellnessRepository(database.wellnessEntryDao())
        
        // Initialize StateFlow
        allEntries = repository.getAllEntries()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    /**
     * Add new entry
     */
    fun addEntry(
        userId: String,
        date: Date,
        mood: Mood,
        sleepHours: Float,
        exerciseLevel: ExerciseLevel,
        notes: String
    ) {
        val newEntry = WellnessEntry(
            id = UUID.randomUUID().toString(),
            userId = userId,
            date = date,
            mood = mood,
            sleepHours = sleepHours,
            exerciseLevel = exerciseLevel,
            notes = notes
        )
        
        viewModelScope.launch {
            repository.insertEntry(newEntry)
        }
    }

    /**
     * Update entry
     */
    fun updateEntry(entry: WellnessEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    /**
     * Delete entry
     */
    fun deleteEntry(entry: WellnessEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }

    /**
     * Get entries by user ID
     */
    fun getEntriesByUser(userId: String): StateFlow<List<WellnessEntry>> {
        return repository.getEntriesByUser(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
} 