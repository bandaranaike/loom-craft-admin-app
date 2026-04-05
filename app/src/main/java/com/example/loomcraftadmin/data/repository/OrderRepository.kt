package com.example.loomcraftadmin.data.repository

import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.model.OrderItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepository {
    // Simulated data
    private val mockVendorOrders = listOf(
        Order(101, "pending", 1, "2026-04-05", 450.00),
        Order(102, "processing", 2, "2026-04-04", 890.00),
        Order(103, "accepted", 1, "2026-04-03", 1200.00),
        Order(104, "hand over to admin", 3, "2026-04-02", 3200.00)
    )

    fun getVendorOrders(): Flow<List<Order>> = flow {
        delay(1000) // Simulate network delay
        emit(mockVendorOrders)
    }

    fun getOrderDetail(orderId: Int): Flow<OrderDetail> = flow {
        delay(800)
        emit(
            OrderDetail(
                id = orderId,
                status = "processing",
                items = listOf(
                    OrderItem(1, "Handwoven Silk Scarf", 1, 450.0, "processing"),
                    OrderItem(2, "Cotton Handloom Saree", 1, 1200.0, "pending")
                )
            )
        )
    }

    suspend fun updateOrderStatus(orderId: Int, status: String) {
        delay(500)
        // Mock update
    }
}
