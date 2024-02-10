package com.kolee.composepedometer2.room_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyStepsEntity(
    @PrimaryKey
    val epochDay: Long,
    val steps: Long = Long.MIN_VALUE,
    val timeStamp: String = ""
)
