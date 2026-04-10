package com.example.loomcraftadmin.ui.features.admin.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.model.OrderItem
import com.example.loomcraftadmin.ui.components.LoomCraftButton
import com.example.loomcraftadmin.ui.components.LoomCraftCard
import com.example.loomcraftadmin.ui.components.OrderAmountPanel
import com.example.loomcraftadmin.ui.components.OrderHeaderRow
import com.example.loomcraftadmin.ui.components.OrderItemHeroImage
import com.example.loomcraftadmin.ui.components.OrderInfoChip
import com.example.loomcraftadmin.ui.components.OrderPageHeader
import com.example.loomcraftadmin.ui.components.OrderSectionHeader
import com.example.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.example.loomcraftadmin.utils.CurrencyFormatter
import com.example.loomcraftadmin.utils.OrderUiFormatter
import com.example.loomcraftadmin.ui.components.orderStatusPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderDetailScreen(
    orderId: Int,
    onBackClick: () -> Unit,
    onPrintLabelClick: (Int) -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orderDetail by viewModel.orderDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showStatusMenu by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.loadAdminOrderDetail(orderId)
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
                title = {
                    Text(
                        text = "Order View",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showStatusMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showStatusMenu,
                            onDismissRequest = { showStatusMenu = false }
                        ) {
                            val statuses = listOf(
                                "Pending",
                                "Processing",
                                "Accepted",
                                "Shipped",
                                "Delivered",
                                "Cancelled"
                            )
                            statuses.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        viewModel.updateStatus(orderId, status.lowercase())
                                        showStatusMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            orderDetail?.let { order ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        OrderPageHeader(
                            eyebrow = "Marketplace",
                            title = "Order View"
                        )
                    }
                    item { AdminOrderDetailHeader(order) }

                    if (order.displayCustomerAddress() != null || order.displayCustomerPhone() != null) {
                        item { ShippingAddressSection(order) }
                    }

                    item {
                        OrderSectionHeader(
                            eyebrow = "Fulfilment",
                            title = "Order Items"
                        )
                    }

                    items(order.items) { item ->
                        AdminOrderItemRow(item)
                    }

                    item { SummarySection(order) }

                    item {
                        LoomCraftButton(
                            text = "Generate Shipping Label",
                            onClick = { onPrintLabelClick(order.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Order details unavailable",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AdminOrderDetailHeader(order: OrderDetail) {
    val palette = orderStatusPalette(order.status)
    LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            OrderHeaderRow(
                orderLabel = "Order #${order.id}",
                status = order.status
            )

            order.displayCustomerName()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            OrderAmountPanel(
                label = "Grand total",
                amount = CurrencyFormatter.format(order.total, order.currency),
                amountColor = palette.amountColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderInfoChip(
                    text = "${order.items.size} ${if (order.items.size == 1) "item" else "items"}",
                    modifier = Modifier.weight(1f)
                )
                OrderInfoChip(
                    text = OrderUiFormatter.formatTimestamp(order.createdAt),
                    modifier = Modifier.weight(1.5f)
                )
            }
        }
    }
}

@Composable
fun AdminOrderItemRow(item: OrderItem) {
    LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OrderItemHeroImage(
                imageUrl = item.displayImageUrl(),
                contentDescription = item.productName
            )

            Text(
                text = item.productName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = CurrencyFormatter.format(item.unitPrice, item.currency),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = CurrencyFormatter.format(item.quantity * item.unitPrice, item.currency),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OrderInfoChip(text = "Qty ${item.quantity}")
                OrderInfoChip(text = item.status.ifBlank { "Pending" })
            }
        }
    }
}

@Composable
fun ShippingAddressSection(order: OrderDetail) {
    LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OrderSectionHeader(
                eyebrow = "Dispatch",
                title = "Shipping Details"
            )
            order.displayCustomerName()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            order.displayCustomerAddress()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            order.displayCustomerPhone()?.let {
                OrderInfoChip(text = "Contact $it")
            }
        }
    }
}

@Composable
fun SummarySection(order: OrderDetail) {
    val palette = orderStatusPalette(order.status)
    LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            OrderSectionHeader(
                eyebrow = "Finance",
                title = "Order Summary"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                Text(
                    CurrencyFormatter.format(order.total, order.currency),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            HorizontalDivider()
            OrderAmountPanel(
                label = "Grand total",
                amount = CurrencyFormatter.format(order.total, order.currency),
                amountColor = palette.amountColor
            )
        }
    }
}
