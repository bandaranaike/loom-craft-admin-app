package com.example.loomcraftadmin.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
        private val REMEMBERED_EMAIL_KEY = stringPreferencesKey("remembered_email")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userRole: Flow<String?> = context.dataStore.data.map { it[USER_ROLE_KEY] }
    val fcmToken: Flow<String?> = context.dataStore.data.map { it[FCM_TOKEN_KEY] }
    val rememberMe: Flow<Boolean> = context.dataStore.data.map { it[REMEMBER_ME_KEY] ?: true }
    val rememberedEmail: Flow<String> =
        context.dataStore.data.map { it[REMEMBERED_EMAIL_KEY].orEmpty() }

    suspend fun saveSession(
        token: String,
        role: String,
        rememberMe: Boolean,
        email: String
    ) {
        context.dataStore.edit {
            it[TOKEN_KEY] = token
            it[USER_ROLE_KEY] = role
            it[REMEMBER_ME_KEY] = rememberMe
            if (rememberMe) {
                it[REMEMBERED_EMAIL_KEY] = email
            } else {
                it.remove(REMEMBERED_EMAIL_KEY)
            }
        }
    }

    suspend fun saveRememberMe(enabled: Boolean) {
        context.dataStore.edit { it[REMEMBER_ME_KEY] = enabled }
    }

    suspend fun saveRememberedEmail(email: String) {
        context.dataStore.edit {
            if (email.isBlank()) {
                it.remove(REMEMBERED_EMAIL_KEY)
            } else {
                it[REMEMBERED_EMAIL_KEY] = email
            }
        }
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { it[FCM_TOKEN_KEY] = token }
    }

    suspend fun clearAuth() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ROLE_KEY)
        }
    }
}
