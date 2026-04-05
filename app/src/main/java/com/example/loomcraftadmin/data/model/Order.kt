package com.example.loomcraftadmin.data.model

import java.util.Date

data class Order(
    val id: Int,
    val status: String,
    val itemsCount: Int,
    val createdAt: String,
    val vendorItemsTotal: Double? = null,
    val customerName: String? = null,
    val total: Double? = null,
    val currency: String = "INR"
)

data class OrderItem(
    val id: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val status: String,
    val currency: String = "INR"
)

data class OrderDetail(
    val id: Int,
    val status: String,
    val items: List<OrderItem>,
    val customerName: String? = null,
    val customerAddress: String? = null,
    val customerPhone: String? = null,
    val total: Double? = null,
    val createdAt: String? = null,
    val currency: String = "INR"
)
