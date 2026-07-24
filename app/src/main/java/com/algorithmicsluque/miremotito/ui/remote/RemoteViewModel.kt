package com.algorithmicsluque.miremotito.ui.remote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.data.models.Device
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class RemoteUiState(
    val device: Device? = null,
    val isConnected: Boolean = true
)

class RemoteViewModel : ViewModel() {
    private val _deviceId = MutableStateFlow<String?>(null)
    
    val uiState: StateFlow<RemoteUiState> = combine(
        _deviceId,
        DeviceRepository.devices
    ) { id, devices ->
        RemoteUiState(device = devices.find { it.id == id })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RemoteUiState()
    )

    fun setDevice(deviceId: String) {
        _deviceId.value = deviceId
    }

    fun sendCommand(command: RemoteCommand, extra: String? = null) {
        Log.d("RemoteViewModel", "Sending command: $command ${extra ?: ""}")
    }

    fun updateDevice(name: String, icon: androidx.compose.ui.graphics.vector.ImageVector, roomName: String) {
        uiState.value.device?.let { currentDevice ->
            DeviceRepository.updateDevice(
                currentDevice.copy(name = name, icon = icon, roomName = roomName)
            )
        }
    }

    fun deleteDevice() {
        _deviceId.value?.let { DeviceRepository.deleteDevice(it) }
    }

    fun getAvailableRooms() = DeviceRepository.getAvailableRooms()
}
