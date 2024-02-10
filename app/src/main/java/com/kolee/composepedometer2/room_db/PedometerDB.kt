package com.kolee.composepedometer2.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DailyStepsEntity::class], version = 1)
abstract class PedometerDB : RoomDatabase() {

    abstract fun dailyStepsDao(): DailyStepsDao

    companion object {
        @Volatile   // For Singleton instantiation
        private var instance: PedometerDB? = null

        fun getInstance(context: Context): PedometerDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PedometerDB {
            return Room.databaseBuilder(
                context,
                PedometerDB::class.java,
                "pedometer_db_1112"
            )
                .allowMainThreadQueries()
                .build()
        }
    }
}