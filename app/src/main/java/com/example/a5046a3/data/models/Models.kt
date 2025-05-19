package com.example.a5046a3.data.models

import java.util.Date

/**
 * 用户情绪枚举
 */
enum class Mood(val label: String, val emoji: String) {
    VERY_SAD("Very Sad", "😢"),
    SAD("Sad", "🙁"),
    NEUTRAL("Neutral", "😐"),
    HAPPY("Happy", "🙂"),
    VERY_HAPPY("Very Happy", "😄")
}

/**
 * 用户运动等级枚举
 */
enum class ExerciseLevel(val label: String) {
    NONE("No Exercise"),
    LIGHT("Light Exercise"),
    MODERATE("Moderate Exercise"),
    INTENSE("Intense Exercise")
}

/**
 * 用户健康记录数据模型
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
 * 用户数据模型
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