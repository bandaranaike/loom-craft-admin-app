package com.example.loomcraftadmin.ui.features.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private enum class OrderDetailScope {
        ADMIN,
        VENDOR
    }

    private val _adminOrders = MutableStateFlow<List<Order>>(emptyList())
    val adminOrders = _adminOrders.asStateFlow()

    private val _vendorOrders = MutableStateFlow<List<Order>>(emptyList())
    val vendorOrders = _vendorOrders.asStateFlow()

    private val _orderDetail = MutableStateFlow<OrderDetail?>(null)
    val orderDetail = _orderDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var currentDetailScope: OrderDetailScope? = null

    fun clearError() {
        _errorMessage.value = null
    }

    fun loadAdminOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                orderRepository.getAdminOrders().collect {
                    _adminOrders.value = it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load admin orders"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadVendorOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                orderRepository.getVendorOrders().collect {
                    _vendorOrders.value = it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load vendor orders"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAdminOrderDetail(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            currentDetailScope = OrderDetailScope.ADMIN
            try {
                orderRepository.getAdminOrderDetail(orderId).collect {
                    _orderDetail.value = it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load order details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadVendorOrderDetail(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            currentDetailScope = OrderDetailScope.VENDOR
            try {
                orderRepository.getOrderDetail(orderId).collect {
                    _orderDetail.value = it
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load order details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = orderRepository.updateOrderStatus(orderId, status)
            if (result.isSuccess) {
                when (currentDetailScope) {
                    OrderDetailScope.ADMIN -> loadAdminOrderDetail(orderId)
                    OrderDetailScope.VENDOR -> loadVendorOrderDetail(orderId)
                    null -> Unit
                }
            } else {
                _errorMessage.value =
                    result.exceptionOrNull()?.message ?: "Failed to update status"
            }
            _isLoading.value = false
        }
    }
}
