package com.algorithmicsluque.miremotito.data.network

import com.algorithmicsluque.miremotito.data.models.api.ApiResponse
import com.algorithmicsluque.miremotito.data.models.api.CommandRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteApiService {

    @POST("send_command")
    suspend fun sendCommand(@Body request: CommandRequest): ApiResponse

    @GET("brands")
    suspend fun getBrands(): List<String>
    
    // Configuración específica del servidor para el control dinámico
    @GET("config")
    suspend fun getServerConfig(): ApiResponse
}
