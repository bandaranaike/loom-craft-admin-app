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

    private val mockAdminOrders = listOf(
        Order(101, "pending", 1, "2026-04-05", customerName = "Aditi Sharma", total = 520.00),
        Order(102, "processing", 2, "2026-04-04", customerName = "Vikram Singh", total = 980.00),
        Order(103, "accepted", 1, "2026-04-03", customerName = "Rahul Mehra", total = 1400.00),
        Order(104, "hand over to admin", 3, "2026-04-02", customerName = "Priya Kapoor", total = 3500.00),
        Order(105, "shipped", 1, "2026-04-01", customerName = "Anjali Gupta", total = 2200.00),
        Order(106, "delivered", 2, "2026-03-31", customerName = "Sandeep Varma", total = 1800.00)
    )

    fun getAdminOrders(): Flow<List<Order>> = flow {
        delay(1000)
        emit(mockAdminOrders)
    }

    fun getAdminOrderDetail(orderId: Int): Flow<OrderDetail> = flow {
        delay(800)
        emit(
            OrderDetail(
                id = orderId,
                status = "hand over to admin",
                items = listOf(
                    OrderItem(1, "Handwoven Silk Scarf", 1, 450.0, "processing"),
                    OrderItem(2, "Cotton Handloom Saree", 1, 1200.0, "pending")
                ),
                customerName = "Priya Kapoor",
                customerAddress = "123, Heritage Lane, Jaipur, Rajasthan - 302001",
                customerPhone = "+91 98765 43210",
                total = 3500.0,
                createdAt = "2026-04-02 10:30 AM"
            )
        )
    }

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
