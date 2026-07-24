package com.algorithmicsluque.miremotito.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.data.models.Device

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailsSheet(
    device: Device,
    availableRooms: List<String>,
    onDismiss: () -> Unit,
    onUpdate: (String, ImageVector, String) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(device.name) }
    var selectedIcon by remember { mutableStateOf(device.icon) }
    var selectedRoom by remember { mutableStateOf(device.roomName) }
    var showIconSelector by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .imePadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header: Icon and Name
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .clickable { showIconSelector = !showIconSelector },
                        shape = RoundedCornerShape(19.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = selectedIcon,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del dispositivo") },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true
                    )
                }
            }

            if (showIconSelector) {
                item {
                    IconSelector(
                        onIconSelected = {
                            selectedIcon = it
                            showIconSelector = false
                        }
                    )
                }
            }

            // Info Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "INFORMACIÓN DEL DISPOSITIVO",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    InfoCard(
                        icon = Icons.Rounded.Info,
                        text = "Dispositivo: ${device.model}",
                        isTop = true
                    )
                    Spacer(Modifier.height(3.dp))
                    InfoCard(
                        icon = Icons.Rounded.DateRange,
                        text = "Conectado el ${device.addedDate}",
                        isBottom = true
                    )
                }
            }

            // Room Selection
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "HABITACIÓN",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    availableRooms.forEach { room ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRoom = room }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = room == selectedRoom,
                                onClick = { selectedRoom = room }
                            )
                            Text(text = room, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            // Actions
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onUpdate(name, selectedIcon, selectedRoom)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar cambios")
                    }

                    TextButton(
                        onClick = onDelete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(Icons.Rounded.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar este dispositivo")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, text: String, isTop: Boolean = false, isBottom: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(
            topStart = if (isTop) 28.dp else 5.dp,
            topEnd = if (isTop) 28.dp else 5.dp,
            bottomStart = if (isBottom) 28.dp else 5.dp,
            bottomEnd = if (isBottom) 28.dp else 5.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(100.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun IconSelector(onIconSelected: (ImageVector) -> Unit) {
    val icons = listOf(
        Icons.Rounded.Tv, Icons.Rounded.Air, Icons.Rounded.AcUnit, Icons.Rounded.Devices,
        Icons.Rounded.Speaker, Icons.Rounded.Monitor, Icons.Rounded.SettingsRemote, Icons.Rounded.Kitchen
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp,
            bottomStart = 5.dp,
            bottomEnd = 5.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ICONOS PREDETERMINADOS",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            val rows = icons.chunked(4)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rows.forEach { rowIcons ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowIcons.forEach { icon ->
                            IconButton(
                                onClick = { onIconSelected(icon) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .weight(1f)
                            ) {
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}
