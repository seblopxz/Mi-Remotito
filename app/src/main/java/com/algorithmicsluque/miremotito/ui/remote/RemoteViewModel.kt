package com.algorithmicsluque.miremotito.ui.remote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.data.models.Device
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.data.models.api.CommandRequest
import com.algorithmicsluque.miremotito.data.network.RemoteApiService
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RemoteUiState(
    val device: Device? = null,
    val isConnected: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RemoteViewModel(private val apiService: RemoteApiService) : ViewModel() {
    private val _deviceId = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _isServerAvailable = MutableStateFlow(true)
    
    val uiState: StateFlow<RemoteUiState> = combine(
        _deviceId,
        DeviceRepository.devices,
        _isLoading,
        _errorMessage,
        _isServerAvailable
    ) { id, devices, loading, error, available ->
        RemoteUiState(
            device = devices.find { it.id == id },
            isLoading = loading,
            errorMessage = error,
            isConnected = available
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RemoteUiState()
    )

    init {
        monitorServerHealth()
    }

    private fun monitorServerHealth() {
        viewModelScope.launch {
            while (true) {
                try {
                    apiService.getServerConfig()
                    _isServerAvailable.value = true
                } catch (e: Exception) {
                    _isServerAvailable.value = false
                }
                kotlinx.coroutines.delay(10000) // Check every 10s
            }
        }
    }

    fun setDevice(deviceId: String) {
        _deviceId.value = deviceId
    }

    fun sendCommand(command: RemoteCommand, extra: String? = null) {
        val deviceId = _deviceId.value ?: return
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val response = apiService.sendCommand(
                    CommandRequest(
                        device_id = deviceId,
                        command = command.name,
                        extra = extra
                    )
                )
                
                if (response.status != "success") {
                    _errorMessage.value = response.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo conectar con la Raspberry Pi"
                Log.e("RemoteViewModel", "Error sending command", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
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
