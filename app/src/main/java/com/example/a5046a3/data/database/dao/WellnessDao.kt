package com.example.a5046a3.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a5046a3.data.database.entity.WellnessEntry
import java.util.Date

/**
 * Data Access Object for wellness entries
 */
@Dao
interface WellnessDao {
    
    /**
     * Insert a new wellness entry
     * 
     * @param entry The wellness entry to insert
     * @return The ID of the inserted entry
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: WellnessEntry): Long
    
    /**
     * Update an existing wellness entry
     * 
     * @param entry The wellness entry to update
     */
    @Update
    fun update(entry: WellnessEntry): Int
    
    /**
     * Delete a wellness entry
     * 
     * @param entry The wellness entry to delete
     */
    @Delete
    fun delete(entry: WellnessEntry): Int
    
    /**
     * Get all wellness entries for a specific user
     * 
     * @param userId The user ID
     * @return LiveData list of wellness entries
     */
    @Query("SELECT * FROM wellness_entries WHERE userId = :userId ORDER BY date DESC")
    fun getAllEntriesForUser(userId: String): LiveData<List<WellnessEntry>>
    
    /**
     * Get wellness entries for a specific date range
     * 
     * @param userId The user ID
     * @param startDate The start date
     * @param endDate The end date
     * @return List of wellness entries
     */
    @Query("SELECT * FROM wellness_entries WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getEntriesForDateRange(userId: String, startDate: Date, endDate: Date): List<WellnessEntry>
    
    /**
     * Get wellness entries that haven't been synced to the cloud
     * 
     * @return List of unsynced wellness entries
     */
    @Query("SELECT * FROM wellness_entries WHERE isSynced = 0")
    fun getUnsyncedEntries(): List<WellnessEntry>
    
    /**
     * Update the sync status of a wellness entry
     * 
     * @param id The entry ID
     * @param isSynced The new sync status
     */
    @Query("UPDATE wellness_entries SET isSynced = :isSynced WHERE id = :id")
    fun updateSyncStatus(id: Long, isSynced: Boolean): Int
} 