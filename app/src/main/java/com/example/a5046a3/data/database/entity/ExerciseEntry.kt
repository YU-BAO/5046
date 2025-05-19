package com.example.a5046a3.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entity class representing an exercise entry in the database
 */
@Entity(tableName = "exercise_entries")
data class ExerciseEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // User ID for associating entries with specific users
    val userId: String,
    
    // Date of the exercise
    val date: Date,
    
    // Type of exercise (e.g., "Running", "Swimming", "Yoga")
    val type: String,
    
    // Duration in minutes
    val durationMinutes: Int,
    
    // Estimated calories burned
    val caloriesBurned: Int = 0,
    
    // Optional notes
    val notes: String = "",
    
    // Whether the entry has been synced to the cloud
    val isSynced: Boolean = false
) 