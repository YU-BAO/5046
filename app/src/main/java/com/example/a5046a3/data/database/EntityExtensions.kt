package com.example.a5046a3.data.database

import com.example.a5046a3.data.models.WellnessEntry
import java.util.UUID

/**
 * 数据转换扩展函数：用于Entity与Model之间的转换
 */

/**
 * 将数据库实体转换为域模型
 */
fun WellnessEntryEntity.toWellnessEntry(): WellnessEntry {
    return WellnessEntry(
        id = this.id,
        userId = this.userId,
        date = this.date,
        mood = this.mood,
        sleepHours = this.sleepHours,
        exerciseLevel = this.exerciseLevel,
        notes = this.notes
    )
}

/**
 * 将域模型转换为数据库实体
 */
fun WellnessEntry.toEntity(): WellnessEntryEntity {
    return WellnessEntryEntity(
        id = this.id.ifEmpty { UUID.randomUUID().toString() },
        userId = this.userId,
        date = this.date,
        mood = this.mood,
        sleepHours = this.sleepHours,
        exerciseLevel = this.exerciseLevel,
        notes = this.notes
    )
} 