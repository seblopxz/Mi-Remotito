package com.algorithmicsluque.miremotito.data.network

import com.algorithmicsluque.miremotito.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }
    
    // Interceptor para cambiar la URL base dinámicamente
    class DynamicUrlInterceptor(private val repository: SettingsRepository) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val newUrl = runBlocking { repository.serverUrl.first() }
            
            val updatedUrl = request.url.newBuilder()
                .scheme(if (newUrl.startsWith("https")) "https" else "http")
                .host(newUrl.replace("http://", "").replace("https://", "").split(":")[0].split("/")[0])
                .port(try { newUrl.split(":").last().replace("/", "").toInt() } catch (e: Exception) { 80 })
                .build()
            
            request = request.newBuilder()
                .url(updatedUrl)
                .build()
                
            return chain.proceed(request)
        }
    }

    // Interceptor para reintentos (Solo en GET)
    class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(request)
            
            if (request.method == "GET") {
                var tryCount = 0
                while (!response.isSuccessful && tryCount < maxRetries) {
                    tryCount++
                    response.close()
                    response = chain.proceed(request)
                }
            }
            return response
        }
    }

    fun provideRetrofit(repository: SettingsRepository): RemoteApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(DynamicUrlInterceptor(repository))
            .addInterceptor(RetryInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(SettingsRepository.DEFAULT_URL) // URL inicial obligatoria para Retrofit
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RemoteApiService::class.java)
    }
}
