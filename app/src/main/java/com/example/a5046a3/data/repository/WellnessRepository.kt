package com.example.a5046a3.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.a5046a3.data.database.dao.WellnessEntryDao
import com.example.a5046a3.data.database.entity.WellnessEntryEntity
import com.example.a5046a3.data.models.WellnessEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

/**
 * Repository for managing wellness entry data using Room database
 */
class WellnessRepository(private val wellnessEntryDao: WellnessEntryDao) {
    
    /**
     * Get all wellness entries as LiveData
     */
    val allEntries: LiveData<List<WellnessEntry>> = wellnessEntryDao.getAllEntries().map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    /**
     * Get entries from the past week as LiveData
     */
    val pastWeekEntries: LiveData<List<WellnessEntry>> = wellnessEntryDao.getEntriesInDateRange(
        getOneWeekAgoDate(), 
        Date()
    ).map { entities ->
        entities.map { it.toDomainModel() }
    }
    
    /**
     * Insert a new wellness entry
     */
    suspend fun insert(entry: WellnessEntry) {
        withContext(Dispatchers.IO) {
            wellnessEntryDao.insert(WellnessEntryEntity.fromDomainModel(entry))
        }
    }
    
    /**
     * Update an existing wellness entry
     */
    suspend fun update(entry: WellnessEntry) {
        withContext(Dispatchers.IO) {
            wellnessEntryDao.update(WellnessEntryEntity.fromDomainModel(entry))
        }
    }
    
    /**
     * Delete a wellness entry
     */
    suspend fun delete(entry: WellnessEntry) {
        withContext(Dispatchers.IO) {
            wellnessEntryDao.delete(WellnessEntryEntity.fromDomainModel(entry))
        }
    }
    
    /**
     * Get a wellness entry by ID
     */
    suspend fun getEntryById(id: String): WellnessEntry? {
        return withContext(Dispatchers.IO) {
            wellnessEntryDao.getEntryById(id)?.toDomainModel()
        }
    }
    
    /**
     * Get the latest wellness entry
     */
    suspend fun getLatestEntry(): WellnessEntry? {
        return withContext(Dispatchers.IO) {
            wellnessEntryDao.getLatestEntry()?.toDomainModel()
        }
    }
    
    /**
     * Delete all wellness entries
     */
    suspend fun deleteAllEntries() {
        withContext(Dispatchers.IO) {
            wellnessEntryDao.deleteAllEntries()
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