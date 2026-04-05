package com.example.loomcraftadmin.data.repository

import com.example.loomcraftadmin.data.api.AuthApi
import com.example.loomcraftadmin.data.api.LoginRequest
import com.example.loomcraftadmin.data.local.TokenManager

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            tokenManager.saveToken(response.token)
            tokenManager.saveRole(response.user.role)
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
            val response = authApi.registerFcmToken(com.example.loomcraftadmin.data.api.FcmTokenRequest(token))
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
