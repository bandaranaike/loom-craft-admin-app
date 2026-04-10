package com.example.loomcraftadmin.data.api

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String,
    @Json(name = "device_name") val deviceName: String = "android"
)

data class LoginResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val name: String,
    val role: String,
    @Json(name = "vendor_id") val vendorId: Int?
)

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("notifications/register")
    suspend fun registerFcmToken(@Body request: FcmTokenRequest): retrofit2.Response<Unit>
}

data class FcmTokenRequest(
    @Json(name = "fcm_token") val fcmToken: String,
    val platform: String = "android"
)
