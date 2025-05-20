package com.example.a5046a3.data.repository

import com.example.a5046a3.data.models.WellnessEntry
import com.example.a5046a3.data.models.WellnessEntryDao
import com.example.a5046a3.data.models.toEntity
import com.example.a5046a3.data.models.toWellnessEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

/**
 * Wellness Data Repository: Manages wellness records using Room database
 */
class WellnessRepository(private val wellnessDao: WellnessEntryDao) {
    
    /**
     * Get all records
     */
    fun getAllEntries(): Flow<List<WellnessEntry>> {
        return wellnessDao.getAllEntries().map { entities ->
            entities.map { it.toWellnessEntry() }
        }
    }
    
    /**
     * Get records by user ID
     */
    fun getEntriesByUser(userId: String): Flow<List<WellnessEntry>> {
        return wellnessDao.getEntriesByUser(userId).map { entities ->
            entities.map { it.toWellnessEntry() }
        }
    }
    
    /**
     * Insert new record
     */
    suspend fun insertEntry(entry: WellnessEntry): Long {
        return withContext(Dispatchers.IO) {
            wellnessDao.insertEntry(entry.toEntity())
        }
    }
    
    /**
     * Update record
     */
    suspend fun updateEntry(entry: WellnessEntry): Int {
        return withContext(Dispatchers.IO) {
            wellnessDao.updateEntry(entry.toEntity())
        }
    }
    
    /**
     * Delete record
     */
    suspend fun deleteEntry(entry: WellnessEntry): Int {
        return withContext(Dispatchers.IO) {
            wellnessDao.deleteEntry(entry.toEntity())
        }
    }
    
    /**
     * Get record by ID
     */
    suspend fun getEntryById(id: String): WellnessEntry? {
        return withContext(Dispatchers.IO) {
            wellnessDao.getEntryById(id)?.toWellnessEntry()
        }
    }
    
    /**
     * Helper method to get date from one week ago
     */
    private fun getOneWeekAgoDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return calendar.time
    }
} 