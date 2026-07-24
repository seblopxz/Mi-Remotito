package com.algorithmicsluque.miremotito.ui.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SetupFlow(
    onFinished: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: SetupViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentStep) {
        is SetupStep.AddDevice -> {
            AddDeviceScreen(
                onTypeSelected = { 
                    viewModel.onTypeSelected(it)
                    viewModel.onStepChanged(SetupStep.Brands) 
                },
                onBrandSelected = { 
                    viewModel.onBrandSelected(it)
                    viewModel.onStepChanged(SetupStep.Connecting)
                },
                onBack = onBack
            )
        }
        is SetupStep.Brands -> {
            BrandsScreen(
                onBrandSelected = {
                    viewModel.onBrandSelected(it)
                    viewModel.onStepChanged(SetupStep.Connecting)
                },
                onBack = { viewModel.onStepChanged(SetupStep.AddDevice) }
            )
        }
        is SetupStep.Connecting -> {
            ConnectingScreen(onBack = { viewModel.onStepChanged(SetupStep.AddDevice) })
            // Simulate connection success
            androidx.compose.runtime.LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                viewModel.onStepChanged(SetupStep.Testing)
            }
        }
        is SetupStep.Error -> {
            ErrorScreen(
                onRetry = { viewModel.onStepChanged(SetupStep.Connecting) },
                onCancel = onBack
            )
        }
        is SetupStep.Testing -> {
            TestingScreen(
                onPowerTest = { viewModel.onStepChanged(SetupStep.Confirmation) },
                onBack = { viewModel.onStepChanged(SetupStep.AddDevice) }
            )
        }
        is SetupStep.Confirmation -> {
            ConfirmationScreen(
                onYes = { viewModel.onStepChanged(SetupStep.Personalization) },
                onNo = { viewModel.onStepChanged(SetupStep.SuggestConfirmation) },
                onBack = { viewModel.onStepChanged(SetupStep.Testing) }
            )
        }
        is SetupStep.SuggestConfirmation -> {
            SuggestRemoteConfirmationScreen(
                onAccept = { viewModel.onStepChanged(SetupStep.SuggestForm) },
                onCancel = onBack
            )
        }
        is SetupStep.SuggestForm -> {
            SuggestRemoteFormScreen(
                state = uiState.suggestState,
                onCategoryChanged = { viewModel.onSuggestCategoryChanged(it) },
                onFieldChanged = { f, v -> viewModel.onSuggestFieldChanged(f, v) },
                onAttachPhoto = { viewModel.onPhotoAttached(it) },
                onSend = { 
                    viewModel.sendSuggestion()
                    onFinished() 
                },
                onBack = { viewModel.onStepChanged(SetupStep.SuggestConfirmation) }
            )
        }
        is SetupStep.Personalization -> {
            PersonalizationScreen(
                name = uiState.newDeviceName,
                onNameChange = { viewModel.onNameChanged(it) },
                selectedIcon = uiState.newDeviceIcon,
                onIconSelected = { viewModel.onIconSelected(it) },
                onAdd = { 
                    viewModel.completeSetup()
                    viewModel.onStepChanged(SetupStep.Success)
                },
                onBack = { viewModel.onStepChanged(SetupStep.Confirmation) }
            )
        }
        is SetupStep.Success -> {
            SuccessScreen(onGoHome = onFinished)
        }
        else -> Unit
    }
}
