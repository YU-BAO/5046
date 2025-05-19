package com.example.a5046a3.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a5046a3.data.models.ExerciseLevel
import com.example.a5046a3.data.models.Mood
import com.example.a5046a3.data.models.WellnessEntry
import java.util.Date
import java.util.UUID

/**
 * Entity class for wellness entries to be stored in Room database
 */
@Entity(tableName = "wellness_entries")
data class WellnessEntryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val date: Date = Date(),
    val mood: String = Mood.NEUTRAL.name,
    val sleepHours: Float = 0f,
    val exerciseLevel: String = ExerciseLevel.NONE.name,
    val notes: String = ""
) {
    /**
     * Convert entity to domain model
     */
    fun toDomainModel(): WellnessEntry {
        return WellnessEntry(
            id = id,
            userId = userId,
            date = date,
            mood = try { Mood.valueOf(mood) } catch (e: Exception) { Mood.NEUTRAL },
            sleepHours = sleepHours,
            exerciseLevel = try { ExerciseLevel.valueOf(exerciseLevel) } catch (e: Exception) { ExerciseLevel.NONE },
            notes = notes
        )
    }
    
    companion object {
        /**
         * Convert domain model to entity
         */
        fun fromDomainModel(entry: WellnessEntry): WellnessEntryEntity {
            return WellnessEntryEntity(
                id = entry.id.ifEmpty { UUID.randomUUID().toString() },
                userId = entry.userId,
                date = entry.date,
                mood = entry.mood.name,
                sleepHours = entry.sleepHours,
                exerciseLevel = entry.exerciseLevel.name,
                notes = entry.notes
            )
        }
    }
} 