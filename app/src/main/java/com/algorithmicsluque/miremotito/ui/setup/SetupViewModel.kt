package com.algorithmicsluque.miremotito.ui.setup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.algorithmicsluque.miremotito.data.models.Device
import com.algorithmicsluque.miremotito.data.models.DeviceType
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class SetupStep {
    object AddChoice : SetupStep()
    object AddDevice : SetupStep()
    object Brands : SetupStep()
    object Connecting : SetupStep()
    object Error : SetupStep()
    object Testing : SetupStep()
    object Confirmation : SetupStep()
    object SuggestConfirmation : SetupStep()
    object SuggestForm : SetupStep()
    object Personalization : SetupStep()
    object Success : SetupStep()
}

data class SuggestUiState(
    val selectedCategory: String = "TV", // TV, AC, Otros
    val brand: String = "",
    val model: String = "",
    val remoteModel: String = "",
    val specialButtons: String = "",
    val hasBackPhoto: Boolean = false,
    val hasRemotePhoto: Boolean = false,
    val hasFrontPhoto: Boolean = false
)

data class SetupUiState(
    val currentStep: SetupStep = SetupStep.AddDevice,
    val selectedType: DeviceType? = null,
    val selectedBrand: String? = null,
    val newDeviceName: String = "",
    val newDeviceIcon: ImageVector = Icons.Rounded.Tv,
    val selectedRoom: String = "SALA DE ESTAR",
    val suggestState: SuggestUiState = SuggestUiState()
)

class SetupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    fun onStepChanged(step: SetupStep) {
        _uiState.value = _uiState.value.copy(currentStep = step)
    }

    fun onTypeSelected(type: DeviceType) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun onBrandSelected(brand: String) {
        _uiState.value = _uiState.value.copy(selectedBrand = brand)
    }

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(newDeviceName = name)
    }

    fun onIconSelected(icon: ImageVector) {
        _uiState.value = _uiState.value.copy(newDeviceIcon = icon)
    }

    fun onRoomSelected(room: String) {
        _uiState.value = _uiState.value.copy(selectedRoom = room)
    }

    fun onSuggestCategoryChanged(category: String) {
        _uiState.value = _uiState.value.copy(
            suggestState = _uiState.value.suggestState.copy(selectedCategory = category)
        )
    }

    fun onSuggestFieldChanged(field: String, value: String) {
        val current = _uiState.value.suggestState
        val next = when(field) {
            "brand" -> current.copy(brand = value)
            "model" -> current.copy(model = value)
            "remoteModel" -> current.copy(remoteModel = value)
            "specialButtons" -> current.copy(specialButtons = value)
            else -> current
        }
        _uiState.value = _uiState.value.copy(suggestState = next)
    }

    fun onPhotoAttached(photoType: String) {
        val current = _uiState.value.suggestState
        val next = when(photoType) {
            "back" -> current.copy(hasBackPhoto = true)
            "remote" -> current.copy(hasRemotePhoto = true)
            "front" -> current.copy(hasFrontPhoto = true)
            else -> current
        }
        _uiState.value = _uiState.value.copy(suggestState = next)
    }

    fun sendSuggestion() {
        // Mock send action
        android.util.Log.d("SetupViewModel", "Sending suggestion to sebastianald1234@gmail.com: ${_uiState.value.suggestState}")
    }

    fun completeSetup() {
        val state = _uiState.value
        val newDevice = Device(
            id = java.util.UUID.randomUUID().toString(),
            name = state.newDeviceName.ifBlank { "${state.selectedBrand} ${state.selectedType}" },
            type = state.selectedType ?: DeviceType.TV,
            icon = state.newDeviceIcon,
            roomName = state.selectedRoom,
            model = state.selectedBrand ?: "Generic"
        )
        DeviceRepository.addDevice(newDevice)
    }
}
