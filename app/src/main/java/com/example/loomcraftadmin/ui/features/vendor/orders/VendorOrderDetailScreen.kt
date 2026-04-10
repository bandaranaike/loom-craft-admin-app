package com.example.loomcraftadmin.ui.features.vendor.orders

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
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
import com.example.loomcraftadmin.ui.components.OrderInfoChip
import com.example.loomcraftadmin.ui.components.OrderItemHeroImage
import com.example.loomcraftadmin.ui.components.OrderPageHeader
import com.example.loomcraftadmin.ui.components.OrderSectionHeader
import com.example.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.example.loomcraftadmin.utils.CurrencyFormatter
import com.example.loomcraftadmin.utils.OrderUiFormatter
import com.example.loomcraftadmin.ui.components.orderStatusPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorOrderDetailScreen(
    orderId: Int,
    onBackClick: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orderDetail by viewModel.orderDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(orderId) {
        viewModel.loadVendorOrderDetail(orderId)
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
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                orderDetail?.let { detail ->
                    VendorOrderDetailContent(
                        orderDetail = detail,
                        onUpdateStatus = { status -> viewModel.updateStatus(orderId, status) }
                    )
                } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Order not found")
                }
            }
        }
    }
}

@Composable
fun VendorOrderDetailContent(
    orderDetail: OrderDetail,
    onUpdateStatus: (String) -> Unit
) {
    val palette = orderStatusPalette(orderDetail.status)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OrderPageHeader(
                    eyebrow = "Vendor",
                    title = "Order View"
                )
            }
            item {
                LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OrderHeaderRow(
                            orderLabel = "Order #${orderDetail.id}",
                            status = orderDetail.status
                        )
                        OrderAmountPanel(
                            label = "Your total",
                            amount = CurrencyFormatter.format(orderDetail.total, orderDetail.currency),
                            amountColor = palette.amountColor
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OrderInfoChip(
                                text = "${orderDetail.items.size} ${if (orderDetail.items.size == 1) "item" else "items"}",
                                modifier = Modifier.weight(1f)
                            )
                            OrderInfoChip(
                                text = OrderUiFormatter.formatTimestamp(orderDetail.createdAt),
                                modifier = Modifier.weight(1.5f)
                            )
                        }
                    }
                }
            }

            item {
                OrderSectionHeader(
                    eyebrow = "Fulfilment",
                    title = "Order Items"
                )
            }

            items(orderDetail.items) { item ->
                VendorOrderItemCard(item)
            }
        }

        Text(
            "Update Status",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LoomCraftButton(
                text = "Accept",
                onClick = { onUpdateStatus("accepted") },
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            LoomCraftButton(
                text = "Reject",
                onClick = { onUpdateStatus("rejected") },
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LoomCraftButton(
            text = "Hand over to Admin",
            onClick = { onUpdateStatus("hand_over_to_admin") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun VendorOrderItemCard(item: OrderItem) {
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
