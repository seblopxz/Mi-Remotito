package com.algorithmicsluque.miremotito.ui.setup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.R
import com.algorithmicsluque.miremotito.data.models.Device
import com.algorithmicsluque.miremotito.data.models.DeviceType
import com.algorithmicsluque.miremotito.data.repository.DeviceRepository
import com.algorithmicsluque.miremotito.data.network.RemoteApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

data class BrandInfo(
    val name: String, 
    val icon: ImageVector? = null,
    val imageRes: Int? = null
)

data class DeviceTypeInfo(val label: String, val icon: ImageVector, val type: DeviceType)

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
    val searchQuery: String = "",
    val selectedType: DeviceType? = null,
    val selectedBrand: String? = null,
    val newDeviceName: String = "",
    val newDeviceIcon: ImageVector = Icons.Rounded.Tv,
    val selectedRoom: String = "SALA DE ESTAR",
    val suggestState: SuggestUiState = SuggestUiState(),
    val filteredBrands: List<BrandInfo> = emptyList(),
    val filteredDeviceTypes: List<DeviceTypeInfo> = emptyList(),
    val isLoadingBrands: Boolean = false,
    val newGroupName: String = ""
)

class SetupViewModel(private val apiService: RemoteApiService? = null) : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    private var allBrands = listOf(
        BrandInfo("SAMSUNG", imageRes = com.algorithmicsluque.miremotito.R.drawable.logo_samsung),
        BrandInfo("Apple", imageRes = com.algorithmicsluque.miremotito.R.drawable.logo_apple),
        BrandInfo("LG", imageRes = com.algorithmicsluque.miremotito.R.drawable.logo_lg),
        BrandInfo("Philips", imageRes = com.algorithmicsluque.miremotito.R.drawable.logo_philips)
    )

    private val allDeviceTypes = listOf(
        DeviceTypeInfo("TV", Icons.Rounded.Tv, DeviceType.TV),
        DeviceTypeInfo("Ventilador", Icons.Rounded.Air, DeviceType.FAN),
        DeviceTypeInfo("Barra de Sonido", Icons.Rounded.Speaker, DeviceType.AUDIO),
        DeviceTypeInfo("Aire Acondicionado", Icons.Rounded.AcUnit, DeviceType.AC)
    )

    val defaultGroups = listOf("Habitación Principal", "Sala de Estar", "Garage", "Cocina")

    init {
        updateFilteredLists("")
        fetchBrandsFromServer()
    }

    fun onGroupNameChanged(name: String) {
        _uiState.update { it.copy(newGroupName = name) }
    }

    fun createGroup() {
        val name = _uiState.value.newGroupName
        if (name.isNotBlank()) {
            DeviceRepository.addGroup(name)
        }
    }

    private fun fetchBrandsFromServer() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoadingBrands = true) }
                val serverBrands = apiService?.getBrands()
                if (!serverBrands.isNullOrEmpty()) {
                    val newBrands = serverBrands.map { BrandInfo(it) }
                    allBrands = (allBrands + newBrands).distinctBy { it.name.lowercase() }
                    updateFilteredLists(_uiState.value.searchQuery)
                }
            } catch (e: Exception) {
                android.util.Log.e("SetupViewModel", "Error fetching brands", e)
            } finally {
                _uiState.update { it.copy(isLoadingBrands = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        updateFilteredLists(query)
    }

    private fun updateFilteredLists(query: String) {
        val filteredBrands = if (query.isEmpty()) {
            allBrands
        } else {
            allBrands.filter { it.name.contains(query, ignoreCase = true) }
        }

        val filteredTypes = if (query.isEmpty()) {
            allDeviceTypes
        } else {
            allDeviceTypes.filter { it.label.contains(query, ignoreCase = true) }
        }

        _uiState.update { it.copy(
            filteredBrands = filteredBrands,
            filteredDeviceTypes = filteredTypes
        ) }
    }

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
        android.util.Log.d("SetupViewModel", "Sending suggestion: ${_uiState.value.suggestState}")
    }

    fun completeSetup() {
        val state = _uiState.value
        val newDevice = Device(
            id = java.util.UUID.randomUUID().toString(),
            name = state.newDeviceName.ifBlank { "${state.selectedBrand ?: ""} ${state.selectedType ?: "Dispositivo"}" },
            type = state.selectedType ?: DeviceType.TV,
            icon = state.newDeviceIcon,
            roomName = state.selectedRoom,
            model = state.selectedBrand ?: "Generic"
        )
        DeviceRepository.addDevice(newDevice)
    }
}
