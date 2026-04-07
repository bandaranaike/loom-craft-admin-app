package com.example.loomcraftadmin.data.api

import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.data.model.OrderDetail
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface OrderApi {
    @GET("orders")
    suspend fun getAdminOrders(): List<Order>

    @GET("orders/{id}")
    suspend fun getAdminOrderDetail(@Path("id") id: Int): OrderDetail

    @GET("orders")
    suspend fun getVendorOrders(): List<Order>

    @GET("orders/{id}")
    suspend fun getVendorOrderDetail(@Path("id") id: Int): OrderDetail

    @PATCH("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: Int,
        @Body statusRequest: UpdateStatusRequest
    )
}

data class UpdateStatusRequest(
    val status: String
)
