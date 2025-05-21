package com.example.a5046a3.data.models

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID

/**
 * Database entity class: Defines the storage structure of wellness records in the database
 */
@Entity(tableName = "wellness_entries")
data class WellnessEntryEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: Date,
    val mood: Mood,
    val sleepHours: Float,
    val exerciseLevel: ExerciseLevel,
    val notes: String
)

/**
 * Room type converters
 */
class Converters {
    // Date converter
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Mood converter
    @TypeConverter
    fun fromMoodString(value: String?): Mood? {
        return value?.let { 
            try {
                Mood.valueOf(it)
            } catch (e: IllegalArgumentException) {
                Mood.NEUTRAL
            }
        }
    }

    @TypeConverter
    fun moodToString(mood: Mood?): String? {
        return mood?.name
    }

    // ExerciseLevel converter
    @TypeConverter
    fun fromExerciseLevelString(value: String?): ExerciseLevel? {
        return value?.let {
            try {
                ExerciseLevel.valueOf(it)
            } catch (e: IllegalArgumentException) {
                ExerciseLevel.NONE
            }
        }
    }

    @TypeConverter
    fun exerciseLevelToString(exerciseLevel: ExerciseLevel?): String? {
        return exerciseLevel?.name
    }
}

/**
 * Data Access Object Interface: Defines database operations for wellness records
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

/**
 * Room Database class: Defines database configuration using singleton pattern
 */
@Database(entities = [WellnessEntryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WellnessDatabase : RoomDatabase() {

    abstract fun wellnessDao(): WellnessEntryDao

    companion object {
        @Volatile
        private var INSTANCE: WellnessDatabase? = null

        fun getDatabase(context: Context): WellnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WellnessDatabase::class.java,
                    "wellness_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Convert database entity to domain model
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
 * Convert domain model to database entity
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