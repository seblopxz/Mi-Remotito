package com.algorithmicsluque.miremotito.ui.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UpdateState {
    object Idle : UpdateState()
    object Searching : UpdateState()
    object Found : UpdateState()
    object NotFound : UpdateState()
}

class UpdateViewModel : ViewModel() {
    private val _state = MutableStateFlow<UpdateState>(UpdateState.Searching)
    val state: StateFlow<UpdateState> = _state.asStateFlow()

    init {
        searchForUpdates()
    }

    fun searchForUpdates() {
        viewModelScope.launch {
            _state.value = UpdateState.Searching
            delay(3000) // Simulate search
            _state.value = UpdateState.Found
        }
    }
}
