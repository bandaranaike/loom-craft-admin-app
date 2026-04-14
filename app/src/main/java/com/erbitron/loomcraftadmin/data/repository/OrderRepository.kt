package com.erbitron.loomcraftadmin.data.repository

import com.erbitron.loomcraftadmin.data.api.OrderApi
import com.erbitron.loomcraftadmin.data.api.UpdateStatusRequest
import com.erbitron.loomcraftadmin.data.model.Order
import com.erbitron.loomcraftadmin.data.model.OrderDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepository(private val orderApi: OrderApi) {

    fun getAdminOrders(): Flow<List<Order>> = flow {
        emit(orderApi.getAdminOrders())
    }

    fun getAdminOrderDetail(orderId: Int): Flow<OrderDetail?> = flow {
        emit(orderApi.getAdminOrderDetail(orderId))
    }

    fun getVendorOrders(): Flow<List<Order>> = flow {
        emit(orderApi.getVendorOrders())
    }

    fun getOrderDetail(orderId: Int): Flow<OrderDetail?> = flow {
        emit(orderApi.getVendorOrderDetail(orderId))
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
