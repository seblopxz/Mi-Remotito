package com.algorithmicsluque.miremotito.ui.updates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalUriHandler
import com.algorithmicsluque.miremotito.ui.components.ExpressiveListItem
import com.algorithmicsluque.miremotito.ui.components.ListItemPosition
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateFlow(
    viewModel: UpdateViewModel,
    onNavigateToBeta: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is UpdateState.Searching -> SearchingUpdatesScreen(onBack = onBack)
        is UpdateState.Found -> UpdateFoundScreen(
            onInstall = onBack,
            onRemindLater = onBack,
            onNavigateToBeta = onNavigateToBeta,
            onBack = onBack
        )
        is UpdateState.NotFound -> NoUpdatesScreen(
            onRetry = { viewModel.searchForUpdates() },
            onNavigateToBeta = onNavigateToBeta,
            onBack = onBack
        )
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchingUpdatesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = { RemotitoAppBar(title = "Actualización", onBackClick = onBack) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContainedLoadingIndicator(
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerShape = RoundedCornerShape(24.dp)

                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Buscando actualizaciones...",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .width(200.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(Icons.Rounded.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Comprobar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateFoundScreen(
    onInstall: () -> Unit,
    onRemindLater: () -> Unit,
    onNavigateToBeta: () -> Unit,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = { RemotitoAppBar(title = "Actualización", onBackClick = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 46.dp, end = 46.dp, top = 24.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Surface(modifier = Modifier.size(80.dp), shape = RoundedCornerShape(20.dp), color = Color(0xFF4CAF50)) {
                             Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.SettingsRemote, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                             }
                        }
                        Column {
                            Text("Mi Remotito", style = MaterialTheme.typography.titleLargeEmphasized, fontWeight = FontWeight.Bold)
                            Text("Versión 1.1 (Beta) - 25.1 MB", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            "Esta actualización proporciona varios arreglos de bugs y funciones nuevas.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Para más información, visitá este sitio web:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "https://github.com/algorithmicsluque/MiRemotito/releases/tag/1.1-beta",
                            style = MaterialTheme.typography.bodyMediumEmphasized,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://github.com/algorithmicsluque/MiRemotito/releases/tag/1.1-beta")
                            }
                        )
                    }
                }
            }

            item {
                ExpressiveListItem(
                    position = ListItemPosition.SINGLE,
                    onClick = onNavigateToBeta,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Icon(Icons.Rounded.Science, contentDescription = null)
                    Text("Actualizaciones beta", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onInstall,
                        modifier = Modifier.width(230.dp).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Instalar actualización")
                    }
                    OutlinedButton(
                        onClick = onRemindLater,
                        modifier = Modifier.width(230.dp).height(56.dp)
                    ) {
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Recordarme luego")
                    }
                }
            }
        }
    }
}

@Composable
fun NoUpdatesScreen(
    onRetry: () -> Unit,
    onNavigateToBeta: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { RemotitoAppBar(title = "Actualización", onBackClick = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 46.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                "No se encontró ninguna actualización",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))
            
            ExpressiveListItem(
                position = ListItemPosition.SINGLE,
                onClick = onNavigateToBeta,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(Icons.Rounded.Science, contentDescription = null)
                Text("Actualizaciones beta", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onRetry,
                modifier = Modifier.padding(bottom = 48.dp).fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Text("Comprobar", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
