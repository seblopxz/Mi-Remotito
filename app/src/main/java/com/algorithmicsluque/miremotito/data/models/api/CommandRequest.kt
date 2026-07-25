package com.algorithmicsluque.miremotito.data.models.api

import kotlinx.serialization.Serializable

@Serializable
data class CommandRequest(
    val device_id: String,
    val command: String,
    val extra: String? = null
)
