package com.example.a5046a3.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a5046a3.data.database.entity.WellnessEntryEntity
import java.util.Date

/**
 * Data Access Object interface for wellness entries
 */
@Dao
interface WellnessEntryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WellnessEntryEntity)
    
    @Update
    suspend fun update(entry: WellnessEntryEntity)
    
    @Delete
    suspend fun delete(entry: WellnessEntryEntity)
    
    @Query("SELECT * FROM wellness_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<WellnessEntryEntity>>
    
    @Query("SELECT * FROM wellness_entries WHERE id = :id")
    suspend fun getEntryById(id: String): WellnessEntryEntity?
    
    @Query("SELECT * FROM wellness_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getEntriesInDateRange(startDate: Date, endDate: Date): LiveData<List<WellnessEntryEntity>>
    
    @Query("SELECT * FROM wellness_entries ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntry(): WellnessEntryEntity?
    
    @Query("DELETE FROM wellness_entries")
    suspend fun deleteAllEntries()
} 