package com.algorithmicsluque.miremotito.ui.remote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.data.models.DeviceType
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.ui.components.*
import com.algorithmicsluque.miremotito.ui.remote.components.AcControls
import com.algorithmicsluque.miremotito.ui.remote.components.TvControls
import com.algorithmicsluque.miremotito.ui.theme.FullRoundedShape
import com.algorithmicsluque.miremotito.ui.theme.RemoteSurfaceShape

@Composable
fun RemoteScreen(
    deviceId: String,
    viewModel: RemoteViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDetailsSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deviceId) {
        viewModel.setDevice(deviceId)
    }

    // Mostrar errores en Snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Reintentar",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                // El usuario decidió reintentar manualmente (los comandos no tienen auto-retry)
                // Por simplicidad en este MVP, borramos el error para que intente de nuevo el siguiente toque
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RemotitoAppBar(
                title = uiState.device?.name ?: "",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { showDetailsSheet = true }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "More")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .alpha(if (uiState.isLoading || !uiState.isConnected) 0.6f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            if (!uiState.isConnected) {
                item {
                    Surface(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Raspberry Pi fuera de línea. Intentando reconectar...",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            item {
                RemoteContainer(
                    deviceType = uiState.device?.type ?: DeviceType.TV,
                    onCommand = { if (uiState.isConnected) viewModel.sendCommand(it) }
                )
            }
            
            if (uiState.device?.type == DeviceType.TV || uiState.device?.type == DeviceType.APPLE_TV) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    AppShortcutsSection(
                        onAppClick = { viewModel.sendCommand(RemoteCommand.OPEN_APP, it) }
                    )
                }
            }
        }
    }

    if (showDetailsSheet) {
        uiState.device?.let { device ->
            DeviceDetailsSheet(
                device = device,
                availableRooms = viewModel.getAvailableRooms(),
                onDismiss = { showDetailsSheet = false },
                onUpdate = { name, icon, room ->
                    viewModel.updateDevice(name, icon, room)
                },
                onDelete = {
                    viewModel.deleteDevice()
                    showDetailsSheet = false
                    onBackClick()
                }
            )
        }
    }
}

@Composable
fun RemoteContainer(
    deviceType: DeviceType,
    onCommand: (RemoteCommand) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 45.dp)
            .width(322.dp)
            .height(720.dp),
        shape = RemoteSurfaceShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Siri/Assistant and Power (Común a todos)
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RemoteButton(
                    icon = if (deviceType == DeviceType.AC) Icons.Rounded.AcUnit else Icons.Rounded.Mic,
                    onClick = { onCommand(RemoteCommand.SIRI) },
                    size = 70.dp,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
                
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(5.dp)
                        .background(MaterialTheme.colorScheme.secondary, FullRoundedShape)
                )

                RemoteButton(
                    icon = Icons.Rounded.PowerSettingsNew,
                    onClick = { onCommand(RemoteCommand.POWER) },
                    size = 70.dp,
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            // Cuerpo Adaptativo
            Box(modifier = Modifier.weight(1f)) {
                when (deviceType) {
                    DeviceType.TV, DeviceType.APPLE_TV, DeviceType.MONITOR -> TvControls(onCommand)
                    DeviceType.AC, DeviceType.FAN -> AcControls(onCommand)
                    else -> TvControls(onCommand) // Fallback
                }
            }
        }
    }
}

@Composable
fun AppShortcutsSection(
    onAppClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 45.dp)
            .width(322.dp)
            .height(253.dp),
        shape = RemoteSurfaceShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "MIS APLICACIONES",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 10.dp, bottom = 20.dp)
            )
            
            val apps = listOf("Netflix", "Apple TV", "Photos", "HBO Max", "Prime Video", "Paramount+")
            
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    AppIcon(name = apps[0], onClick = { onAppClick(apps[0]) })
                    AppIcon(name = apps[1], onClick = { onAppClick(apps[1]) })
                    AppIcon(name = apps[2], onClick = { onAppClick(apps[2]) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                    AppIcon(name = apps[3], onClick = { onAppClick(apps[3]) })
                    AppIcon(name = apps[4], onClick = { onAppClick(apps[4]) })
                    AppIcon(name = apps[5], onClick = { onAppClick(apps[5]) })
                }
            }
        }
    }
}

@Composable
fun AppIcon(name: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.size(84.dp),
        shape = RoundedCornerShape(40.dp),
        color = Color.Black,
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Apps, contentDescription = name, tint = Color.White)
        }
    }
}
