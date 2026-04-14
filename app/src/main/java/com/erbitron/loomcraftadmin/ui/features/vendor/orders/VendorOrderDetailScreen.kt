package com.erbitron.loomcraftadmin.ui.features.vendor.orders

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
import com.erbitron.loomcraftadmin.data.model.OrderDetail
import com.erbitron.loomcraftadmin.data.model.OrderItem
import com.erbitron.loomcraftadmin.ui.components.LoomCraftButton
import com.erbitron.loomcraftadmin.ui.components.LoomCraftCard
import com.erbitron.loomcraftadmin.ui.components.OrderAmountPanel
import com.erbitron.loomcraftadmin.ui.components.OrderHeaderRow
import com.erbitron.loomcraftadmin.ui.components.OrderItemHeroImage
import com.erbitron.loomcraftadmin.ui.components.OrderPageHeader
import com.erbitron.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.erbitron.loomcraftadmin.utils.CurrencyFormatter
import com.erbitron.loomcraftadmin.utils.OrderUiFormatter

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
                        text = "Order Detail",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            orderDetail?.let { detail ->
                VendorOrderDetailContent(
                    orderDetail = detail,
                    padding = padding,
                    onUpdateStatus = { status -> viewModel.updateStatus(orderId, status) }
                )
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Order not found", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun VendorOrderDetailContent(
    orderDetail: OrderDetail,
    padding: PaddingValues,
    onUpdateStatus: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 12.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        OrderHeaderRow(
                            orderLabel = "Order #${orderDetail.id}",
                            status = orderDetail.status
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(0.85f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Your Items Portfolio",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = OrderUiFormatter.formatTimestamp(orderDetail.createdAt),
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            
                            OrderAmountPanel(
                                label = "Your Total Share",
                                amount = CurrencyFormatter.format(orderDetail.total, orderDetail.currency),
                                modifier = Modifier.weight(1.15f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Items for Fulfillment",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            items(orderDetail.items) { item ->
                VendorOrderItemCard(item)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Update Order Fulfillment Status",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LoomCraftButton(
                    text = "Accept",
                    onClick = { onUpdateStatus("accepted") },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
                LoomCraftButton(
                    text = "Reject",
                    onClick = { onUpdateStatus("rejected") },
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    contentColor = MaterialTheme.colorScheme.error
                )
            }

            LoomCraftButton(
                text = "Hand over to Admin",
                onClick = { onUpdateStatus("hand_over_to_admin") },
                modifier = Modifier.fillMaxWidth()
            )
        }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Qty ${item.quantity}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "â€¢",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = item.status.ifBlank { "Pending" },
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyFormatter.format(item.unitPrice, item.currency),
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Text(
                        text = CurrencyFormatter.format(item.quantity * item.unitPrice, item.currency),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
