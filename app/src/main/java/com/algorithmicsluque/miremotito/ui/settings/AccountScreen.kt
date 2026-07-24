package com.algorithmicsluque.miremotito.ui.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.components.ExpressiveListItem
import com.algorithmicsluque.miremotito.ui.components.ListItemPosition
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar

@Composable
fun AccountScreen(
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Mi cuenta", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 46.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Card (Always Single/Full rounded)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "Invitado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Iniciá sesión para tener más opciones",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Segmented Menu
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ExpressiveListItem(
                    position = ListItemPosition.FIRST,
                    onClick = {
                        val intent = Intent(Settings.ACTION_SYNC_SETTINGS)
                        context.startActivity(intent)
                    }
                ) {
                    Icon(Icons.Rounded.ManageAccounts, contentDescription = null)
                    Text(
                        text = "Administrar cuentas en este dispositivo",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                ExpressiveListItem(
                    position = ListItemPosition.LAST,
                    onClick = onNavigateToSettings
                ) {
                    Icon(Icons.Rounded.Settings, contentDescription = null)
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
