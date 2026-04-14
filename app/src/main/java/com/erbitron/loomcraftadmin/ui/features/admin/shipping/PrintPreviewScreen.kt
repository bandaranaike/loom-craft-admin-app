package com.erbitron.loomcraftadmin.ui.features.admin.shipping

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erbitron.loomcraftadmin.data.model.OrderDetail
import com.erbitron.loomcraftadmin.ui.components.LoomCraftButton
import com.erbitron.loomcraftadmin.ui.features.orders.viewmodel.OrderViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintPreviewScreen(
    orderId: Int,
    onBackClick: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orderDetail by viewModel.orderDetail.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        viewModel.loadAdminOrderDetail(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Print Preview", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share PDF */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            orderDetail?.let { order ->
                Spacer(modifier = Modifier.height(32.dp))
                
                // Real UI component used for preview
                ShippingLabel(
                    order = order,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                LoomCraftButton(
                    text = "Save as PDF",
                    onClick = {
                        saveLabelAsPdf(context, order)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "Standard 4x6 inch label size",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// Simple PDF generation mock for demonstration
fun saveLabelAsPdf(context: Context, order: OrderDetail) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 450, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    paint.textSize = 12f
    canvas.drawText("LOOMCRAFT SHIPPING LABEL", 20f, 40f, paint)
    canvas.drawText("Order ID: #${order.id}", 20f, 60f, paint)
    canvas.drawText("Ship To: ${order.displayCustomerName() ?: "N/A"}", 20f, 100f, paint)
    val addressLine = order.displayCustomerAddress()?.take(40) ?: "No Address Provided"
    canvas.drawText("Address: $addressLine", 20f, 120f, paint)
    canvas.drawText("Contact: ${order.displayCustomerPhone() ?: "N/A"}", 20f, 140f, paint)

    pdfDocument.finishPage(page)

    val filePath = File(context.getExternalFilesDir(null), "ShippingLabel_${order.id}.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(filePath))
        Toast.makeText(context, "PDF Saved: ${filePath.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
    }
    pdfDocument.close()
}
