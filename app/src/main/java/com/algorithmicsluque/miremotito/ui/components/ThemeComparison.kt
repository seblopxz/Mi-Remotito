package com.algorithmicsluque.miremotito.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.theme.MiRemotitoTheme

@Composable
fun ThemeComparisonContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Components with current theme",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DeviceCard(
                title = "Samsung TV",
                icon = Icons.Rounded.Tv,
                onClick = {}
            )
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RemoteButton(
                icon = Icons.Rounded.PowerSettingsNew,
                onClick = {},
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primaryContainer
            )
            RemoteButton(
                icon = Icons.Rounded.Add,
                onClick = {}
            )
        }
    }
}

@Preview(name = "Fallback Theme", showBackground = true)
@Composable
fun FallbackThemePreview() {
    MiRemotitoTheme(dynamicColor = false) {
        ThemeComparisonContent()
    }
}

@Preview(name = "Dynamic Theme", showBackground = true)
@Composable
fun DynamicThemePreview() {
    // Note: In Preview, this might use a generic dynamic palette 
    // depending on the IDE/system configuration.
    MiRemotitoTheme(dynamicColor = true) {
        ThemeComparisonContent()
    }
}
