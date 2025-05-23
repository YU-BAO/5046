package com.example.a5046a3.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import java.util.Date

/**
 * Database entity class that defines the storage structure of wellness records
 */
@Entity(tableName = "wellness_entries")
@TypeConverters(Converters::class)
data class WellnessEntryEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: Date,
    val mood: Mood,
    val sleepHours: Float,
    val exerciseLevel: ExerciseLevel,
    val notes: String
) 