package com.algorithmicsluque.miremotito.ui.remote.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.ui.components.RemoteButton
import com.algorithmicsluque.miremotito.ui.components.RemotePillButton

@Composable
fun AcControls(
    onCommand: (RemoteCommand) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        // Temperature Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "24°",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 82.sp),
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "TEMPERATURA",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Temperature Adjustment
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RemotePillButton(
                onPlusClick = { onCommand(RemoteCommand.VOL_UP) }, // Reutilizamos VOL_UP/DOWN como TEMP_UP/DOWN
                onMinusClick = { onCommand(RemoteCommand.VOL_DOWN) }
            )
        }

        // Mode and Fan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RemoteButton(
                icon = Icons.Rounded.AcUnit,
                onClick = { onCommand(RemoteCommand.PLAY_PAUSE) }, // Reutilizado para modo
                modifier = Modifier.weight(1f),
                size = 70.dp
            )
            RemoteButton(
                icon = Icons.Rounded.Air,
                onClick = { onCommand(RemoteCommand.MUTE) }, // Reutilizado para fan
                modifier = Modifier.weight(1f),
                size = 70.dp
            )
        }
    }
}
