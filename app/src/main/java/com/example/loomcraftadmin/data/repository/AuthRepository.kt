package com.example.loomcraftadmin.data.repository

import com.example.loomcraftadmin.data.api.AuthApi
import com.example.loomcraftadmin.data.api.FcmTokenRequest
import com.example.loomcraftadmin.data.api.LoginRequest
import com.example.loomcraftadmin.data.local.TokenManager

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<Unit> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            tokenManager.saveSession(
                token = response.token,
                role = response.user.role,
                rememberMe = rememberMe,
                email = email
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenManager.clearAuth()
    }

    suspend fun registerFcmToken(token: String): Result<Unit> {
        return try {
            val response = authApi.registerFcmToken(FcmTokenRequest(token))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to register token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
