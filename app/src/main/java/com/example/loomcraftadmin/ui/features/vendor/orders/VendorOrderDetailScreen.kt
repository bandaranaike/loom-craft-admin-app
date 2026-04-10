package com.example.loomcraftadmin.ui.features.vendor.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.loomcraftadmin.ui.components.OrderItemThumbnail
import com.example.loomcraftadmin.ui.components.StatusTag
import com.example.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import com.example.loomcraftadmin.utils.CurrencyFormatter

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
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                orderDetail?.let { detail ->
                    OrderDetailContent(
                        detail,
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
fun OrderDetailContent(
    orderDetail: OrderDetail,
    onUpdateStatus: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Order #${orderDetail.id}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.weight(1f))
            StatusTag(status = orderDetail.status)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Items",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(orderDetail.items) { item ->
                OrderItemCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
fun OrderItemCard(item: OrderItem) {
    LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OrderItemThumbnail(
                imageUrl = item.displayImageUrl(),
                contentDescription = item.productName
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.productName,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Qty: ${item.quantity} x ${CurrencyFormatter.format(item.unitPrice, item.currency)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                CurrencyFormatter.format(item.quantity * item.unitPrice, item.currency),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
