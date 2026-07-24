package com.algorithmicsluque.miremotito.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Collaborator(
    val name: String,
    val role: String,
    val githubUrl: String,
    val avatarUrl: String? = null
)

data class SettingsUiState(
    val vibrateOnPress: Boolean = true,
    val useOnlineInfo: Boolean = false,
    val collaborators: List<Collaborator> = listOf(
        Collaborator("Sebastián López", "Diseño UI", "https://github.com/"),
        Collaborator("Cesar Alejandro", "Programación", "https://github.com/"),
        Collaborator("Berna Perez", "Programación", "https://github.com/"),
        Collaborator("Elena", "Programación", "https://github.com/"),
        Collaborator("Matias", "Programación", "https://github.com/"),
        Collaborator("Facundo", "Programación", "https://github.com/")
    )
)

class SettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun toggleVibration(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(vibrateOnPress = enabled)
    }

    fun toggleOnlineInfo(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(useOnlineInfo = enabled)
    }
}
