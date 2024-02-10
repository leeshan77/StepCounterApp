package com.kolee.composepedometer2.domain.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class StatefulViewModel<T>(
    initState: T
) : ViewModel() {

    private val _uiState = MutableStateFlow(initState)
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    protected fun updateState(newState: T.() -> T) {
        _uiState.update(newState)
    }
}