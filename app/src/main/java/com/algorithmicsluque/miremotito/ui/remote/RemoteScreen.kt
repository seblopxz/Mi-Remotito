package com.algorithmicsluque.miremotito.ui.remote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.ui.components.*
import com.algorithmicsluque.miremotito.ui.theme.FullRoundedShape
import com.algorithmicsluque.miremotito.ui.theme.MiRemotitoTheme
import com.algorithmicsluque.miremotito.ui.theme.RemoteSurfaceShape

@Composable
fun RemoteScreen(
    deviceId: String,
    viewModel: RemoteViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDetailsSheet by remember { mutableStateOf(false) }

    LaunchedEffect(deviceId) {
        viewModel.setDevice(deviceId)
    }

    Scaffold(
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
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item {
                RemoteBody(
                    onCommand = { viewModel.sendCommand(it) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                AppShortcutsSection(
                    onAppClick = { viewModel.sendCommand(RemoteCommand.OPEN_APP, it) }
                )
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
fun RemoteBody(
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Siri and Power
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RemoteButton(
                    icon = Icons.Rounded.Mic,
                    onClick = { onCommand(RemoteCommand.SIRI) },
                    size = 70.dp,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
                
                // Active indicator bar
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

            // DPad
            DPad(
                onUp = { onCommand(RemoteCommand.UP) },
                onDown = { onCommand(RemoteCommand.DOWN) },
                onLeft = { onCommand(RemoteCommand.LEFT) },
                onRight = { onCommand(RemoteCommand.RIGHT) },
                onOk = { onCommand(RemoteCommand.OK) }
            )

            // Basic Controls: Back and Home
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RemoteButton(
                    icon = Icons.Rounded.ArrowBackIosNew,
                    onClick = { onCommand(RemoteCommand.BACK) }
                )
                RemoteButton(
                    icon = Icons.Rounded.Home,
                    onClick = { onCommand(RemoteCommand.HOME) }
                )
            }

            // Media and Volume
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    RemoteButton(
                        icon = Icons.Rounded.PlayArrow,
                        onClick = { onCommand(RemoteCommand.PLAY_PAUSE) }
                    )
                    RemoteButton(
                        icon = Icons.AutoMirrored.Rounded.VolumeOff,
                        onClick = { onCommand(RemoteCommand.MUTE) }
                    )
                }
                
                RemotePillButton(
                    onPlusClick = { onCommand(RemoteCommand.VOL_UP) },
                    onMinusClick = { onCommand(RemoteCommand.VOL_DOWN) }
                )
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

@Preview(showBackground = true)
@Composable
fun RemoteScreenPreview() {
    MiRemotitoTheme {
        RemoteScreen(
            deviceId = "1",
            viewModel = RemoteViewModel(),
            onBackClick = {}
        )
    }
}
