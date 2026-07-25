package com.algorithmicsluque.miremotito.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.components.ExpressiveListItem
import com.algorithmicsluque.miremotito.ui.components.ListItemPosition
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateToAbout: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Configuración", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                                .padding(horizontal = 46.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ExpressiveListItem(
                    position = ListItemPosition.FIRST,
                    content = {
                        SettingsToggleContent(
                            title = "Vibrar al presionar un botón",
                            checked = uiState.vibrateOnPress,
                            onCheckedChange = { viewModel.toggleVibration(it) }
                            
                        )
                    }
                )
                ExpressiveListItem(
                    position = ListItemPosition.MIDDLE,
                    content = {
                        SettingsToggleContent(
                            title = "Usar información en línea para nuevos controles",
                            checked = uiState.useOnlineInfo,
                            onCheckedChange = { viewModel.toggleOnlineInfo(it) }
                        )
                    }
                )
                ExpressiveListItem(
                    position = ListItemPosition.MIDDLE,
                    content = {
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Text(
                                text = "Dirección IP de la Raspberry Pi",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = uiState.serverUrl,
                                onValueChange = { viewModel.updateServerUrl(it) },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                placeholder = { Text("http://192.168.1.100:5000/") },
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium
                            )
                        }
                    }
                )
                ExpressiveListItem(
                    position = ListItemPosition.LAST,
                    onClick = onNavigateToAbout,
                    content = {
                        Text(
                            text = "Sobre Mi Remotito",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsToggleContent(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
