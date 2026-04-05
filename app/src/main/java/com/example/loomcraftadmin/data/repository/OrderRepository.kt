package com.example.loomcraftadmin.data.repository

import com.example.loomcraftadmin.data.api.OrderApi
import com.example.loomcraftadmin.data.api.UpdateStatusRequest
import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.data.model.OrderDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepository(private val orderApi: OrderApi) {

    fun getAdminOrders(): Flow<List<Order>> = flow {
        try {
            emit(orderApi.getAdminOrders())
        } catch (e: Exception) {
            // Log error
            emit(emptyList())
        }
    }

    fun getAdminOrderDetail(orderId: Int): Flow<OrderDetail?> = flow {
        try {
            emit(orderApi.getAdminOrderDetail(orderId))
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun getVendorOrders(): Flow<List<Order>> = flow {
        try {
            emit(orderApi.getVendorOrders())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getOrderDetail(orderId: Int): Flow<OrderDetail?> = flow {
        try {
            emit(orderApi.getVendorOrderDetail(orderId))
        } catch (e: Exception) {
            emit(null)
        }
    }

    suspend fun updateOrderStatus(orderId: Int, status: String): Result<Unit> {
        return try {
            orderApi.updateOrderStatus(orderId, UpdateStatusRequest(status))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
