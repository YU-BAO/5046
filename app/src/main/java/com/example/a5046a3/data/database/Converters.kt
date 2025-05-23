package com.example.a5046a3.data.database

import androidx.room.TypeConverter
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import java.util.Date

/**
 * Room database type converters
 * Converts complex types (such as Date, Mood, ExerciseLevel) into primitives for Room persistence
 */
class Converters {
    // Date converter
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Mood converter
    @TypeConverter
    fun fromMoodString(value: String?): Mood? {
        return value?.let { 
            try {
                Mood.valueOf(it)
            } catch (e: IllegalArgumentException) {
                Mood.NEUTRAL
            }
        }
    }

    @TypeConverter
    fun moodToString(mood: Mood?): String? {
        return mood?.name
    }

    // Exercise Level converter
    @TypeConverter
    fun fromExerciseLevelString(value: String?): ExerciseLevel? {
        return value?.let {
            try {
                ExerciseLevel.valueOf(it)
            } catch (e: IllegalArgumentException) {
                ExerciseLevel.NONE
            }
        }
    }

    @TypeConverter
    fun exerciseLevelToString(exerciseLevel: ExerciseLevel?): String? {
        return exerciseLevel?.name
    }
} 