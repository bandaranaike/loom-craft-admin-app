package com.example.loomcraftadmin.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userRole: Flow<String?> = context.dataStore.data.map { it[USER_ROLE_KEY] }
    val fcmToken: Flow<String?> = context.dataStore.data.map { it[FCM_TOKEN_KEY] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { it[USER_ROLE_KEY] = role }
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { it[FCM_TOKEN_KEY] = token }
    }

    suspend fun clearAuth() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ROLE_KEY)
            // We usually keep FCM token to unregister on server if needed, 
            // but for now let's keep it simple.
        }
    }
}
