package com.example.a5046a3.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.a5046a3.data.database.AppDatabase
import com.example.a5046a3.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Worker to sync local database with Firebase Firestore.
 * This worker runs in the background on a schedule to ensure data is synchronized
 * even when the app is not actively being used.
 */
class DatabaseSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val TAG = "DatabaseSyncWorker"
    private val firebaseRepository = FirebaseRepository()
    private val database = AppDatabase.getInstance(context)
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Starting database sync work at ${getCurrentDateTime()}")
            
            // Check if user is authenticated
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Log.d(TAG, "User not authenticated, skipping sync")
                return@withContext Result.success()
            }
            
            // Sync wellness entries
            syncWellnessEntries(currentUser.uid)
            
            // Sync exercise data
            syncExerciseData(currentUser.uid)
            
            // Sync other necessary data
            // ...
            
            Log.d(TAG, "Database sync completed successfully at ${getCurrentDateTime()}")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error during database sync: ${e.message}")
            Result.retry()  // Retry on failure
        }
    }
    
    /**
     * Sync wellness entries with Firestore
     */
    private suspend fun syncWellnessEntries(userId: String) {
        try {
            // Get local wellness entries that need syncing
            val unsyncedEntries = database.wellnessDao().getUnsyncedEntries()
            
            Log.d(TAG, "Found ${unsyncedEntries.size} unsynced wellness entries")
            
            for (entry in unsyncedEntries) {
                // Convert entry to Map for Firestore
                val entryData = mapOf(
                    "userId" to userId,
                    "date" to entry.date,
                    "mood" to entry.mood,
                    "sleepHours" to entry.sleepHours,
                    "stressLevel" to entry.stressLevel,
                    "notes" to entry.notes,
                    "timestamp" to System.currentTimeMillis()
                )
                
                // Save to Firestore
                val result = firebaseRepository.saveData(
                    collection = "wellness_entries",
                    document = entry.id.toString(),
                    data = entryData
                )
                
                if (result.isSuccess) {
                    // Update local entry to mark as synced
                    database.wellnessDao().updateSyncStatus(entry.id, true)
                    Log.d(TAG, "Successfully synced wellness entry ${entry.id}")
                } else {
                    Log.e(TAG, "Failed to sync wellness entry ${entry.id}: ${result.exceptionOrNull()?.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing wellness entries: ${e.message}")
            throw e
        }
    }
    
    /**
     * Sync exercise data with Firestore
     */
    private suspend fun syncExerciseData(userId: String) {
        try {
            // Get local exercise data that needs syncing
            val unsyncedExercises = database.exerciseDao().getUnsyncedExercises()
            
            Log.d(TAG, "Found ${unsyncedExercises.size} unsynced exercise entries")
            
            for (exercise in unsyncedExercises) {
                // Convert exercise to Map for Firestore
                val exerciseData = mapOf(
                    "userId" to userId,
                    "date" to exercise.date,
                    "type" to exercise.type,
                    "durationMinutes" to exercise.durationMinutes,
                    "caloriesBurned" to exercise.caloriesBurned,
                    "notes" to exercise.notes,
                    "timestamp" to System.currentTimeMillis()
                )
                
                // Save to Firestore
                val result = firebaseRepository.saveData(
                    collection = "exercise_entries",
                    document = exercise.id.toString(),
                    data = exerciseData
                )
                
                if (result.isSuccess) {
                    // Update local exercise to mark as synced
                    database.exerciseDao().updateSyncStatus(exercise.id, true)
                    Log.d(TAG, "Successfully synced exercise entry ${exercise.id}")
                } else {
                    Log.e(TAG, "Failed to sync exercise entry ${exercise.id}: ${result.exceptionOrNull()?.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing exercise data: ${e.message}")
            throw e
        }
    }
    
    /**
     * Get current date and time as formatted string
     */
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    companion object {
        // Work name for scheduling
        const val WORK_NAME = "DATABASE_SYNC_WORK"
    }
} 