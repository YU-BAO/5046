package com.example.a5046a3.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a5046a3.data.database.entity.ExerciseEntry
import java.util.Date

/**
 * Data Access Object for exercise entries
 */
@Dao
interface ExerciseDao {
    
    /**
     * Insert a new exercise entry
     * 
     * @param entry The exercise entry to insert
     * @return The ID of the inserted entry
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: ExerciseEntry): Long
    
    /**
     * Update an existing exercise entry
     * 
     * @param entry The exercise entry to update
     */
    @Update
    fun update(entry: ExerciseEntry): Int
    
    /**
     * Delete an exercise entry
     * 
     * @param entry The exercise entry to delete
     */
    @Delete
    fun delete(entry: ExerciseEntry): Int
    
    /**
     * Get all exercise entries for a specific user
     * 
     * @param userId The user ID
     * @return LiveData list of exercise entries
     */
    @Query("SELECT * FROM exercise_entries WHERE userId = :userId ORDER BY date DESC")
    fun getAllExercisesForUser(userId: String): LiveData<List<ExerciseEntry>>
    
    /**
     * Get exercise entries for a specific date range
     * 
     * @param userId The user ID
     * @param startDate The start date
     * @param endDate The end date
     * @return List of exercise entries
     */
    @Query("SELECT * FROM exercise_entries WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExercisesForDateRange(userId: String, startDate: Date, endDate: Date): List<ExerciseEntry>
    
    /**
     * Get exercise entries that haven't been synced to the cloud
     * 
     * @return List of unsynced exercise entries
     */
    @Query("SELECT * FROM exercise_entries WHERE isSynced = 0")
    fun getUnsyncedExercises(): List<ExerciseEntry>
    
    /**
     * Update the sync status of an exercise entry
     * 
     * @param id The entry ID
     * @param isSynced The new sync status
     */
    @Query("UPDATE exercise_entries SET isSynced = :isSynced WHERE id = :id")
    fun updateSyncStatus(id: Long, isSynced: Boolean): Int
} 