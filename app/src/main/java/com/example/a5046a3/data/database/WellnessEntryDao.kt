package com.example.a5046a3.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 数据访问对象接口：定义对wellness记录的数据库操作
 */
@Dao
interface WellnessEntryDao {
    // 插入新记录
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: WellnessEntryEntity): Long
    
    // 更新记录
    @Update
    fun updateEntry(entry: WellnessEntryEntity): Int
    
    // 删除记录
    @Delete
    fun deleteEntry(entry: WellnessEntryEntity): Int
    
    // 根据用户ID获取所有记录
    @Query("SELECT * FROM wellness_entries WHERE userId = :userId ORDER BY date DESC")
    fun getEntriesByUser(userId: String): Flow<List<WellnessEntryEntity>>
    
    // 根据ID获取单条记录
    @Query("SELECT * FROM wellness_entries WHERE id = :id LIMIT 1")
    fun getEntryById(id: String): WellnessEntryEntity?
    
    // 获取所有记录
    @Query("SELECT * FROM wellness_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<WellnessEntryEntity>>
} 