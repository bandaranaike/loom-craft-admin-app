package com.example.loomcraftadmin.data.model

import java.util.Date

data class Order(
    val id: Int,
    val status: String,
    val itemsCount: Int,
    val createdAt: String,
    val vendorItemsTotal: Double? = null, // Only for Vendor
    val customerName: String? = null,     // Only for Admin
    val total: Double? = null             // Only for Admin
)

data class OrderItem(
    val id: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val status: String
)

data class OrderDetail(
    val id: Int,
    val status: String,
    val items: List<OrderItem>,
    val customerName: String? = null,
    val customerAddress: String? = null,
    val customerPhone: String? = null,
    val total: Double? = null,
    val createdAt: String? = null
)
