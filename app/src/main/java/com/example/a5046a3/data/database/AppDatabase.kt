package com.example.a5046a3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a5046a3.data.models.Converters
import com.example.a5046a3.data.models.WellnessEntryDao
import com.example.a5046a3.data.models.WellnessEntryEntity

/**
 * Room Database class: Defines database configuration using singleton pattern
 */
@Database(entities = [WellnessEntryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wellnessEntryDao(): WellnessEntryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
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