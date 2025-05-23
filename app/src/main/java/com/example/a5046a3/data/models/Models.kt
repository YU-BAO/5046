package com.example.a5046a3.data.models

import java.util.Date

/**
 * Enumeration of user mood values
 */
enum class Mood(val label: String, val emoji: String) {
    VERY_SAD("Very Sad", "😢"),
    SAD("Sad", "🙁"),
    NEUTRAL("Neutral", "😐"),
    HAPPY("Happy", "🙂"),
    VERY_HAPPY("Very Happy", "😄")
}

/**
 * Enumeration of user exercise levels
 */
enum class ExerciseLevel(val label: String) {
    NONE("No Exercise"),
    LIGHT("Light Exercise"),
    MODERATE("Moderate Exercise"),
    INTENSE("Intense Exercise")
}

/**
 * Data model for a wellness entry
 */
data class WellnessEntry(
    val id: String,
    val userId: String = "",
    val date: Date,
    val mood: Mood,
    val sleepHours: Float,
    val exerciseLevel: ExerciseLevel,
    val notes: String = ""
)

/**
 * User data model
 */
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String? = null
) 