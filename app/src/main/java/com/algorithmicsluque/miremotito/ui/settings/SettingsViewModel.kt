package com.algorithmicsluque.miremotito.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorithmicsluque.miremotito.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Collaborator(
    val name: String,
    val role: String,
    val githubUrl: String,
    val avatarUrl: String? = null
)

data class BugReportUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "UI", // UI, Conectividad, Control Remoto, Otros
    val hasPhoto: Boolean = false,
    val isSending: Boolean = false,
    val success: Boolean = false
)

data class SettingsUiState(
    val vibrateOnPress: Boolean = true,
    val useOnlineInfo: Boolean = false,
    val serverUrl: String = SettingsRepository.DEFAULT_URL,
    val bugReportState: BugReportUiState = BugReportUiState(),
    val collaborators: List<Collaborator> = listOf(
        Collaborator("Sebastián López", "Diseño UI", "https://github.com/"),
        Collaborator("Cesar Alejandro", "Programación", "https://github.com/"),
        Collaborator("Berna Perez", "Programación", "https://github.com/"),
        Collaborator("Elena", "Programación", "https://github.com/"),
        Collaborator("Matias", "Programación", "https://github.com/"),
        Collaborator("Facundo", "Programación", "https://github.com/")
    )
)

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        // Observar cambios en el repositorio
        repository.vibrateOnPress.onEach { value ->
            _uiState.update { it.copy(vibrateOnPress = value) }
        }.launchIn(viewModelScope)

        repository.useOnlineInfo.onEach { value ->
            _uiState.update { it.copy(useOnlineInfo = value) }
        }.launchIn(viewModelScope)

        repository.serverUrl.onEach { value ->
            _uiState.update { it.copy(serverUrl = value) }
        }.launchIn(viewModelScope)
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateVibration(enabled)
        }
    }

    fun toggleOnlineInfo(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateOnlineInfo(enabled)
        }
    }

    fun updateServerUrl(url: String) {
        viewModelScope.launch {
            repository.updateServerUrl(url)
        }
    }

    fun onBugReportFieldChanged(field: String, value: String) {
        val current = _uiState.value.bugReportState
        val next = when(field) {
            "title" -> current.copy(title = value)
            "description" -> current.copy(description = value)
            "category" -> current.copy(category = value)
            else -> current
        }
        _uiState.update { it.copy(bugReportState = next) }
    }

    fun onBugPhotoAttached() {
        _uiState.update { 
            it.copy(bugReportState = it.bugReportState.copy(hasPhoto = true))
        }
    }

    fun sendBugReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(bugReportState = it.bugReportState.copy(isSending = true)) }
            // Mock send action
            kotlinx.coroutines.delay(2000)
            _uiState.update { 
                it.copy(bugReportState = it.bugReportState.copy(isSending = false, success = true))
            }
        }
    }

    fun resetBugReport() {
        _uiState.update { it.copy(bugReportState = BugReportUiState()) }
    }
}
