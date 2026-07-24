package com.algorithmicsluque.miremotito.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.components.DeviceCard
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar
import com.algorithmicsluque.miremotito.ui.theme.Dimens
import com.algorithmicsluque.miremotito.ui.theme.MiRemotitoTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onDeviceClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            RemotitoAppBar(
                title = "Mi Remotito",
                navigationIcon = {
                     IconButton(onClick = onAddClick) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(imageVector = Icons.Rounded.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.VerticalGap),
            contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
        ) {
            items(uiState.rooms, key = { it.name }) { room ->
                RoomSection(
                    name = room.name,
                    devices = room.devices,
                    onDeviceClick = onDeviceClick
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoomSection(
    name: String,
    devices: List<com.algorithmicsluque.miremotito.data.models.Device>,
    onDeviceClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.ElementGap)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Horizontal flow of devices as per Figma
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.ElementGap),
            verticalArrangement = Arrangement.spacedBy(Dimens.ElementGap),
            maxItemsInEachRow = 2
        ) {
            devices.forEach { device ->
                DeviceCard(
                    title = device.name,
                    icon = device.icon,
                    onClick = { onDeviceClick(device.id) },
                    modifier = Modifier.weight(1f)
                )
            }
            // If odd number, add spacer to avoid stretching the last item
            if (devices.size % 2 != 0) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MiRemotitoTheme {
        HomeScreen(
            viewModel = HomeViewModel(),
            onDeviceClick = {},
            onProfileClick = {},
            onAddClick = {}
        )
    }
}
