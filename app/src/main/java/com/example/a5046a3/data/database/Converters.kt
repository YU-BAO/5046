package com.example.a5046a3.data.database

import androidx.room.TypeConverter
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import java.util.Date

/**
 * Room数据库类型转换器
 * 用于将复杂类型（如Date、Mood、ExerciseLevel）转换为基本类型以便Room存储
 */
class Converters {
    // Date转换器
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Mood转换器
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

    // ExerciseLevel转换器
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