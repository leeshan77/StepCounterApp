package com.kolee.composepedometer2.domain.viewmodel

import androidx.lifecycle.viewModelScope
import com.kolee.composepedometer2.room_db.StepsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    stepsRepository: StepsRepository
) : StatefulViewModel<ShareUiState>(ShareUiState()) {

    init {
        viewModelScope.launch {
            stepsRepository.getAllStepsFlow().collect { stepsAllDays ->
                updateState { copy(stepsAllDays = stepsAllDays) }
            }
        }

        viewModelScope.launch {
            stepsRepository.getStepsTodayFlow().collect { stepsToday ->
                updateState { copy(stepsToday = stepsToday) }
            }
        }
    }
}