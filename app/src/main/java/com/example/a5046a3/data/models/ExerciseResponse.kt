package com.example.a5046a3.data.models

import com.google.gson.annotations.SerializedName

/**
 * Data class for the API Ninjas Exercise API response
 */
data class ExerciseResponse(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
)

/**
 * Domain model class for Exercise
 */
data class Exercise(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
) {
    companion object {
        fun fromResponse(response: ExerciseResponse): Exercise {
            return Exercise(
                name = response.name,
                type = response.type,
                muscle = response.muscle,
                equipment = response.equipment,
                difficulty = response.difficulty,
                instructions = response.instructions
            )
        }
    }
} 