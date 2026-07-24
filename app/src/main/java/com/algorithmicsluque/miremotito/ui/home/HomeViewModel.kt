package com.algorithmicsluque.miremotito.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.data.models.Room
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val rooms: List<Room> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel : ViewModel() {
    val uiState: StateFlow<HomeUiState> = DeviceRepository.devices
        .map { devices ->
            val rooms = devices.groupBy { it.roomName }
                .map { (roomName, roomDevices) ->
                    Room(roomName, roomDevices)
                }
            HomeUiState(rooms = rooms)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
}
