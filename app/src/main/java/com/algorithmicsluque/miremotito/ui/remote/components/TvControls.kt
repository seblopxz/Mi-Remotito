package com.algorithmicsluque.miremotito.ui.remote.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.data.models.RemoteCommand
import com.algorithmicsluque.miremotito.ui.components.DPad
import com.algorithmicsluque.miremotito.ui.components.RemoteButton
import com.algorithmicsluque.miremotito.ui.components.RemotePillButton

@Composable
fun TvControls(
    onCommand: (RemoteCommand) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
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
                icon = Icons.Rounded.ArrowBack,
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
                    icon = Icons.Rounded.VolumeOff,
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
