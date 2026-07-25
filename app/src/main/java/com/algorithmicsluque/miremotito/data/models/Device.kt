package com.algorithmicsluque.miremotito.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Device(
    val id: String,
    val name: String,
    val type: DeviceType,
    val icon: ImageVector,
    val roomName: String,
    val model: String = "Generic Device",
    val addedDate: String = "11 de junio"
)

enum class DeviceType {
    TV, AC, FAN, AUDIO, APPLE_TV, MONITOR, KITCHEN, SPEAKER
}

data class Room(
    val name: String,
    val devices: List<Device>
)
