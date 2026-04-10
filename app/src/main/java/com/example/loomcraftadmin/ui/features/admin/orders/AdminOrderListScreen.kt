package com.example.loomcraftadmin.ui.features.admin.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loomcraftadmin.data.model.Order
import com.example.loomcraftadmin.ui.components.LoomCraftCard
import com.example.loomcraftadmin.ui.components.OrderAmountPanel
import com.example.loomcraftadmin.ui.components.OrderHeaderRow
import com.example.loomcraftadmin.ui.components.OrderMetaText
import com.example.loomcraftadmin.ui.components.OrderPageHeader
import com.example.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.example.loomcraftadmin.utils.CurrencyFormatter
import com.example.loomcraftadmin.utils.OrderUiFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderListScreen(
    onOrderClick: (Int) -> Unit,
    onLogout: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orders by viewModel.adminOrders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadAdminOrders()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { 
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            ) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            orders.isEmpty() && !isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No orders available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        OrderPageHeader(
                            eyebrow = "Marketplace",
                            title = "Orders",
                            onSearchClick = {},
                            onLogoutClick = onLogout
                        )
                    }
                    items(orders) { order ->
                        AdminOrderCard(order = order, onClick = { onOrderClick(order.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrderCard(order: Order, onClick: () -> Unit) {
    LoomCraftCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OrderHeaderRow(
                orderLabel = "Order #${order.id}",
                status = order.status
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1.2f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = order.customerName ?: "Customer unavailable",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    OrderMetaText(
                        itemsText = "${order.itemsCount} ${if (order.itemsCount == 1) "item" else "items"}",
                        dateText = OrderUiFormatter.formatDatePart(order.createdAt),
                        timeText = OrderUiFormatter.formatTimePart(order.createdAt)
                    )
                }
                
                OrderAmountPanel(
                    label = "Total Amount",
                    amount = CurrencyFormatter.format(order.total, order.currency),
                    modifier = Modifier.weight(0.8f)
                )
            }
        }
    }
}
