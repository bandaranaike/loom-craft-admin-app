package com.example.loomcraftadmin.ui.features.vendor.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.ui.components.LoomCraftCard
import com.example.loomcraftadmin.ui.components.OrderAmountPanel
import com.example.loomcraftadmin.ui.components.OrderHeaderRow
import com.example.loomcraftadmin.ui.components.OrderLeadingBadge
import com.example.loomcraftadmin.ui.components.OrderMetaText
import com.example.loomcraftadmin.ui.components.OrderPageHeader
import com.example.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.example.loomcraftadmin.utils.CurrencyFormatter
import com.example.loomcraftadmin.utils.OrderUiFormatter
import com.example.loomcraftadmin.ui.components.orderStatusPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorOrderListScreen(
    onOrderClick: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orders by viewModel.vendorOrders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadVendorOrders()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = {}) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OrderPageHeader(
                        eyebrow = "Vendor",
                        title = "My Orders",
                        onLogoutClick = onLogout
                    )
                }
                items(orders) { order ->
                    OrderListItem(
                        order = order,
                        onClick = { onOrderClick(order.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderListItem(
    order: Order,
    onClick: () -> Unit
) {
    val palette = orderStatusPalette(order.status)
    LoomCraftCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OrderLeadingBadge(status = order.status)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                OrderHeaderRow(
                    orderLabel = "Order #${order.id}",
                    status = order.status
                )
                Text(
                    text = "Vendor order",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                OrderMetaText(
                    itemsText = "${order.itemsCount} ${if (order.itemsCount == 1) "item" else "items"}",
                    dateText = OrderUiFormatter.formatDatePart(order.createdAt),
                    timeText = OrderUiFormatter.formatTimePart(order.createdAt)
                )
            }
            OrderAmountPanel(
                label = "Amount",
                amount = CurrencyFormatter.format(order.vendorItemsTotal, order.currency),
                amountColor = palette.amountColor,
                modifier = Modifier.width(122.dp)
            )
        }
    }
}
