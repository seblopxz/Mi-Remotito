package com.algorithmicsluque.miremotito.data.models.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val status: String,
    val message: String? = null
)
