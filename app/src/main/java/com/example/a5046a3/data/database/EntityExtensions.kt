package com.example.a5046a3.data.database

import com.example.a5046a3.data.models.WellnessEntry
import java.util.UUID

/**
 * Data transformation extension functions: convert between Entity and domain model
 */

/**
 * Converts a database entity to a domain model
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
 * Converts a domain model to a database entity
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