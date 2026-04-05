package com.example.loomcraftadmin.ui.features.vendor.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VendorOrdersState {
    object Loading : VendorOrdersState()
    data class Success(val orders: List<Order>) : VendorOrdersState()
    data class Error(val message: String) : VendorOrdersState()
}

class VendorOrderViewModel(
    private val repository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<VendorOrdersState>(VendorOrdersState.Loading)
    val uiState: StateFlow<VendorOrdersState> = _uiState

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            repository.getVendorOrders().collect { orders ->
                _uiState.value = VendorOrdersState.Success(orders)
            }
        }
    }
}
