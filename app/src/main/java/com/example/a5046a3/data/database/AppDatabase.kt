package com.example.a5046a3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a5046a3.data.database.converters.DateConverter
import com.example.a5046a3.data.database.dao.ExerciseDao
import com.example.a5046a3.data.database.dao.WellnessDao
import com.example.a5046a3.data.database.entity.ExerciseEntry
import com.example.a5046a3.data.database.entity.WellnessEntry

/**
 * Main database for the application, using Room
 */
@Database(
    entities = [
        WellnessEntry::class,
        ExerciseEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * DAO for accessing wellness entries
     */
    abstract fun wellnessDao(): WellnessDao
    
    /**
     * DAO for accessing exercise entries
     */
    abstract fun exerciseDao(): ExerciseDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Get database instance, creating it if necessary
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
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