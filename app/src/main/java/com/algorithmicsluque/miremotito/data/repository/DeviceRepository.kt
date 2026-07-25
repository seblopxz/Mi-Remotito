package com.algorithmicsluque.miremotito.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Tv
import com.algorithmicsluque.miremotito.data.models.Device
import com.algorithmicsluque.miremotito.data.models.DeviceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DeviceRepository {
    private val _devices = MutableStateFlow<List<Device>>(
        listOf(
            Device("1", "Apple TV Remote", DeviceType.APPLE_TV, Icons.Rounded.Devices, "SALA DE ESTAR", "Apple TV 4K"),
            Device("2", "Samsung TV", DeviceType.TV, Icons.Rounded.Tv, "SALA DE ESTAR", "Samsung Crystal UHD"),
            Device("3", "Ventilador", DeviceType.FAN, Icons.Rounded.Air, "HABITACIÓN PRINCIPAL", "Generic Fan"),
            Device("4", "Godweather AC", DeviceType.AC, Icons.Rounded.AcUnit, "HABITACIÓN PRINCIPAL", "GW-12000")
        )
    )

    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    fun updateDevice(updatedDevice: Device) {
        val currentList = _devices.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == updatedDevice.id }
        if (index != -1) {
            currentList[index] = updatedDevice
            _devices.value = currentList
        }
    }

    fun addDevice(newDevice: Device) {
        val currentList = _devices.value.toMutableList()
        currentList.add(newDevice)
        _devices.value = currentList
    }

    fun deleteDevice(deviceId: String) {
        _devices.value = _devices.value.filterNot { it.id == deviceId }
    }

    private val _rooms = MutableStateFlow(listOf("SALA DE ESTAR", "HABITACIÓN PRINCIPAL", "COCINA", "OFICINA"))
    val availableRooms: StateFlow<List<String>> = _rooms.asStateFlow()

    fun addGroup(name: String) {
        if (name.isNotBlank() && !_rooms.value.contains(name.uppercase())) {
            _rooms.value = _rooms.value + name.uppercase()
        }
    }
    
    fun getAvailableRooms(): List<String> = _rooms.value
}
