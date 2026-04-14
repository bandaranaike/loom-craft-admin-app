package com.erbitron.loomcraftadmin.data.repository

import com.erbitron.loomcraftadmin.data.api.AuthApi
import com.erbitron.loomcraftadmin.data.api.FcmTokenRequest
import com.erbitron.loomcraftadmin.data.api.LoginRequest
import com.erbitron.loomcraftadmin.data.local.TokenManager
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, password: String, rememberMe: Boolean): Result<Unit> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (!response.isSuccessful) {
                val message = response.errorBody()
                    ?.string()
                    ?.let(::extractErrorMessage)
                    ?: defaultLoginErrorMessage(response.code())
                return Result.failure(Exception(message))
            }

            val body = response.body()
                ?: return Result.failure(Exception("Login failed. Please try again."))

            tokenManager.saveSession(
                token = body.token,
                role = body.user.role,
                rememberMe = rememberMe,
                email = email
            )
            Result.success(Unit)
        } catch (_: HttpException) {
            Result.failure(Exception("Invalid email or password."))
        } catch (_: IOException) {
            Result.failure(Exception("Unable to reach the server. Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception(extractErrorMessage(e.message), e))
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

    private fun extractErrorMessage(raw: String?): String {
        if (raw.isNullOrBlank()) return "Login failed. Please try again."
        if (raw.contains("JsonReader.setLenient", ignoreCase = true)) {
            return "Invalid email or password."
        }

        return try {
            val json = JSONObject(raw)
            when {
                json.optString("message").isNotBlank() -> normalizeAuthMessage(json.optString("message"))
                json.optString("error").isNotBlank() -> normalizeAuthMessage(json.optString("error"))
                else -> "Login failed. Please try again."
            }
        } catch (_: Exception) {
            normalizeAuthMessage(raw)
        }
    }

    private fun normalizeAuthMessage(message: String): String {
        val trimmed = message.trim()
        if (trimmed.contains("JsonReader.setLenient", ignoreCase = true)) {
            return "Invalid email or password."
        }
        if (
            trimmed.contains("credential", ignoreCase = true) ||
            trimmed.contains("unauthor", ignoreCase = true) ||
            trimmed.contains("invalid", ignoreCase = true)
        ) {
            return "Invalid email or password."
        }
        return trimmed.ifBlank { "Login failed. Please try again." }
    }

    private fun defaultLoginErrorMessage(code: Int): String {
        return when (code) {
            401, 403, 422 -> "Invalid email or password."
            else -> "Login failed. Please try again."
        }
    }
}
