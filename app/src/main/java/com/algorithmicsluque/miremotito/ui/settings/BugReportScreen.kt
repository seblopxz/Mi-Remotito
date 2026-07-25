package com.algorithmicsluque.miremotito.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar
import com.algorithmicsluque.miremotito.ui.setup.AttachBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugReportScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val state = uiState.bugReportState

    LaunchedEffect(state.success) {
        if (state.success) {
            // Reset and go back or show success state
            viewModel.resetBugReport()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Reportar un bug", onBackClick = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 46.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp)
        ) {
            item {
                Text(
                    text = "¿Qué está fallando?",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.onBugReportFieldChanged("title", it) },
                        label = { Text("Título del problema") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true
                    )

                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = state.category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("UI", "Conectividad", "Control Remoto", "Otros").forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        viewModel.onBugReportFieldChanged("category", category)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.onBugReportFieldChanged("description", it) },
                        label = { Text("Descripción detallada") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }

            item {
                Text(
                    text = "ADJUNTAR CAPTURA",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                AttachBox(
                    title = "Foto o captura del error",
                    isAttached = state.hasPhoto,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onBugPhotoAttached() }
                )
            }

            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null)
                        Text(
                            text = "Se incluirá información técnica del dispositivo para ayudarnos a solucionar el problema.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.sendBugReport() },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(20.dp),
                    enabled = state.title.isNotBlank() && state.description.isNotBlank() && !state.isSending,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    if (state.isSending) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                    } else {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = null)
                        Spacer(Modifier.width(12.dp))
                        Text(text = "Enviar reporte", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}
