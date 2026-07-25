package com.algorithmicsluque.miremotito.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.data.models.Room
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import kotlinx.coroutines.flow.*

data class HomeUiState(
    val rooms: List<Room> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel : ViewModel() {
    val uiState: StateFlow<HomeUiState> = combine(
        DeviceRepository.devices,
        DeviceRepository.availableRooms
    ) { devices, availableRooms ->
        val roomsWithDevices = devices.groupBy { it.roomName }
        val allRooms = availableRooms.map { roomName ->
            Room(roomName, roomsWithDevices[roomName] ?: emptyList())
        }.filter { it.devices.isNotEmpty() }
        HomeUiState(rooms = allRooms)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
}
