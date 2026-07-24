package com.algorithmicsluque.miremotito.ui.updates

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.components.ExpressiveListItem
import com.algorithmicsluque.miremotito.ui.components.ListItemPosition
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar

@Composable
fun BetaUpdatesScreen(onBack: () -> Unit) {
    var selectedChannel by remember { mutableStateOf("Beta") }

    Scaffold(
        topBar = { RemotitoAppBar(title = "Programa Beta", onBackClick = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 46.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ExpressiveListItem(
                    position = ListItemPosition.FIRST,
                    onClick = { selectedChannel = "Beta" }
                ) {
                    Text(
                        text = "Mi Remotito Beta",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedChannel == "Beta") {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
                ExpressiveListItem(
                    position = ListItemPosition.LAST,
                    onClick = { selectedChannel = "Canary" }
                ) {
                    Text(
                        text = "Mi Remotito Canary",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedChannel == "Canary") {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Las versiones Beta y Canary pueden ser bastante inestables. Podés encontrar bugs, crashes, etc.",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
