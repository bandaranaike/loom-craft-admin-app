package com.example.loomcraftadmin.data.model

import com.squareup.moshi.Json

data class OrderAddress(
    val id: Int = 0,
    val type: String? = null,
    @Json(name = "full_name") val fullName: String? = null,
    val line1: String? = null,
    val line2: String? = null,
    val city: String? = null,
    val region: String? = null,
    @Json(name = "postal_code") val postalCode: String? = null,
    @Json(name = "country_code") val countryCode: String? = null,
    val phone: String? = null
) {
    fun formattedAddress(): String {
        return listOf(line1, line2, city, region, postalCode, countryCode)
            .mapNotNull { it?.takeIf(String::isNotBlank) }
            .joinToString(", ")
    }
}

data class Order(
    val id: Int,
    @Json(name = "public_id") val publicId: String? = null,
    val status: String,
    @Json(name = "items_count") val itemsCount: Int,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "vendor_items_total") val vendorItemsTotal: Double? = null,
    @Json(name = "customer_name") val customerName: String? = null,
    val total: Double? = null,
    val currency: String = "INR"
)

data class OrderItem(
    val id: Int,
    @Json(name = "product_name") val productName: String,
    val quantity: Int,
    @Json(name = "unit_price") val unitPrice: Double,
    val status: String = "",
    val currency: String = "INR"
)

data class OrderDetail(
    val id: Int,
    @Json(name = "public_id") val publicId: String? = null,
    val status: String,
    val items: List<OrderItem>,
    val addresses: List<OrderAddress> = emptyList(),
    @Json(name = "customer_name") val customerName: String? = null,
    @Json(name = "customer_address") val customerAddress: String? = null,
    @Json(name = "customer_phone") val customerPhone: String? = null,
    val total: Double? = null,
    @Json(name = "created_at") val createdAt: String? = null,
    val currency: String = "INR"
) {
    private fun preferredAddress(): OrderAddress? {
        return addresses.firstOrNull { it.type.equals("shipping", ignoreCase = true) }
            ?: addresses.firstOrNull { it.type.equals("billing", ignoreCase = true) }
            ?: addresses.firstOrNull()
    }

    fun displayCustomerName(): String? {
        return preferredAddress()?.fullName?.takeIf(String::isNotBlank)
            ?: customerName?.takeIf(String::isNotBlank)
    }

    fun displayCustomerPhone(): String? {
        return preferredAddress()?.phone?.takeIf(String::isNotBlank)
            ?: customerPhone?.takeIf(String::isNotBlank)
    }

    fun displayCustomerAddress(): String? {
        val nestedAddress = preferredAddress()?.formattedAddress()?.takeIf(String::isNotBlank)
        return nestedAddress ?: customerAddress?.takeIf(String::isNotBlank)
    }
}
