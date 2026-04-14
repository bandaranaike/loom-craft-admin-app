package com.erbitron.loomcraftadmin.ui.features.vendor.orders

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erbitron.loomcraftadmin.data.model.Order
import com.erbitron.loomcraftadmin.ui.components.LoomCraftCard
import com.erbitron.loomcraftadmin.ui.components.OrderAmountPanel
import com.erbitron.loomcraftadmin.ui.components.OrderHeaderRow
import com.erbitron.loomcraftadmin.ui.components.OrderMetaText
import com.erbitron.loomcraftadmin.ui.components.OrderPageHeader
import com.erbitron.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.erbitron.loomcraftadmin.utils.CurrencyFormatter
import com.erbitron.loomcraftadmin.utils.OrderUiFormatter

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
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
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
                        text = "Vendor Order Item",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
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
                    label = "Your Share",
                    amount = CurrencyFormatter.format(order.vendorItemsTotal, order.currency),
                    modifier = Modifier.weight(0.8f)
                )
            }
        }
    }
}
