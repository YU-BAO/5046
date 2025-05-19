package com.example.a5046a3.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing an exercise from the API Ninjas Exercise API
 */
@Serializable
data class Exercise(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
) 