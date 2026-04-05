package com.example.loomcraftadmin.ui.features.vendor.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VendorOrderDetailState {
    object Loading : VendorOrderDetailState()
    data class Success(val orderDetail: OrderDetail) : VendorOrderDetailState()
    data class Error(val message: String) : VendorOrderDetailState()
}

class VendorOrderDetailViewModel(
    private val orderId: Int,
    private val repository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<VendorOrderDetailState>(VendorOrderDetailState.Loading)
    val uiState: StateFlow<VendorOrderDetailState> = _uiState

    init {
        loadOrderDetail()
    }

    private fun loadOrderDetail() {
        viewModelScope.launch {
            repository.getOrderDetail(orderId).collect { detail ->
                _uiState.value = VendorOrderDetailState.Success(detail)
            }
        }
    }

    fun updateStatus(newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            loadOrderDetail() // Reload
        }
    }
}
