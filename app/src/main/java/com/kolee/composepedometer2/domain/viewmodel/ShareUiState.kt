package com.kolee.composepedometer2.domain.viewmodel

import com.kolee.composepedometer2.room_db.DailyStepsEntity

data class ShareUiState(
    val stepsToday: Long = 0,
    val stepsAllDays: List<DailyStepsEntity> = emptyList()
)
