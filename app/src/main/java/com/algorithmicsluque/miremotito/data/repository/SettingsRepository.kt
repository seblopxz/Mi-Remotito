package com.algorithmicsluque.miremotito.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        private val SERVER_URL = stringPreferencesKey("server_url")
        private val VIBRATE_ON_PRESS = booleanPreferencesKey("vibrate_on_press")
        private val USE_ONLINE_INFO = booleanPreferencesKey("use_online_info")
        
        const val DEFAULT_URL = "http://192.168.1.100:5000/"
    }

    val serverUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SERVER_URL] ?: DEFAULT_URL
    }

    val vibrateOnPress: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VIBRATE_ON_PRESS] ?: true
    }

    val useOnlineInfo: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USE_ONLINE_INFO] ?: false
    }

    suspend fun updateServerUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[SERVER_URL] = url
        }
    }

    suspend fun updateVibration(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRATE_ON_PRESS] = enabled
        }
    }

    suspend fun updateOnlineInfo(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_ONLINE_INFO] = enabled
        }
    }
}
