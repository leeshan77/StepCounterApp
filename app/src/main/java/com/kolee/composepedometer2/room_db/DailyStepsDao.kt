package com.kolee.composepedometer2.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStepsDao {

    @Query("SELECT * FROM DailyStepsEntity WHERE epochDay > 0")
    fun getAllStepsFlow(): Flow<List<DailyStepsEntity>>

    @Query("SELECT steps FROM DailyStepsEntity WHERE epochDay = :day")
    fun getStepsFlow(day: Long): Flow<Long?>

    @Query("SELECT steps FROM DailyStepsEntity WHERE epochDay = :day")
    fun getSteps(day: Long): Long?

    @Query("UPDATE DailyStepsEntity SET steps = steps + :steps, timeStamp = :timeStamp WHERE epochDay=(SELECT MAX(epochDay) FROM DailyStepsEntity)")
    fun addToLastEntry(steps: Long, timeStamp: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dailySteps: DailyStepsEntity)


}