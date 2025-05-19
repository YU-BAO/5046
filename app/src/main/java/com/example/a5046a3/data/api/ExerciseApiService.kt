package com.example.a5046a3.data.api

import com.example.a5046a3.data.model.Exercise
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface for the Exercise API from API Ninjas
 */
interface ExerciseApiService {
    
    /**
     * Get exercises from API Ninjas
     * @param name Filter by exercise name (optional)
     * @param type Filter by exercise type (cardio, strength, stretching, etc.) (optional)
     * @param muscle Filter by muscle group (optional)
     * @param difficulty Filter by difficulty (beginner, intermediate, expert) (optional)
     * @return List of exercises
     */
    @Headers("X-Api-Key: YOUR_API_NINJAS_KEY")
    @GET("v1/exercises")
    suspend fun getExercises(
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("muscle") muscle: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): List<Exercise>
} 