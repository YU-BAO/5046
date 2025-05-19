package com.example.a5046a3.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entity class representing a wellness entry in the database
 */
@Entity(tableName = "wellness_entries")
data class WellnessEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // User ID for associating entries with specific users
    val userId: String,
    
    // Date of the entry
    val date: Date,
    
    // Mood rating (1-5)
    val mood: Int,
    
    // Hours of sleep
    val sleepHours: Float,
    
    // Stress level (1-5)
    val stressLevel: Int,
    
    // Optional notes
    val notes: String = "",
    
    // Whether the entry has been synced to the cloud
    val isSynced: Boolean = false
) 