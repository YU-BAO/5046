package com.example.a5046a3.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object interface: defines database operations for wellness records
 */
@Dao
interface WellnessEntryDao {
    // Insert new record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: WellnessEntryEntity): Long
    
    // Update record
    @Update
    fun updateEntry(entry: WellnessEntryEntity): Int
    
    // Delete record
    @Delete
    fun deleteEntry(entry: WellnessEntryEntity): Int
    
    // Get all records by user ID
    @Query("SELECT * FROM wellness_entries WHERE userId = :userId ORDER BY date DESC")
    fun getEntriesByUser(userId: String): Flow<List<WellnessEntryEntity>>
    
    // Get single record by ID
    @Query("SELECT * FROM wellness_entries WHERE id = :id LIMIT 1")
    fun getEntryById(id: String): WellnessEntryEntity?
    
    // Get all records
    @Query("SELECT * FROM wellness_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<WellnessEntryEntity>>
} 