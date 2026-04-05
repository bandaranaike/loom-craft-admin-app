package com.example.loomcraftadmin.ui.features.admin.shipping

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.repository.OrderRepository
import com.example.loomcraftadmin.ui.components.LoomCraftButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PrintPreviewViewModel(
    private val orderId: Int,
    private val repository: OrderRepository = OrderRepository()
) : ViewModel() {
    private val _orderDetail = MutableStateFlow<OrderDetail?>(null)
    val orderDetail: StateFlow<OrderDetail?> = _orderDetail.asStateFlow()

    init {
        loadOrderDetail()
    }

    private fun loadOrderDetail() {
        viewModelScope.launch {
            repository.getAdminOrderDetail(orderId).collect {
                _orderDetail.value = it
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintPreviewScreen(
    viewModel: PrintPreviewViewModel,
    onBackClick: () -> Unit
) {
    val orderDetail by viewModel.orderDetail.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Print Preview", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share PDF */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
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
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "Standard 4x6 inch label size",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
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
    canvas.drawText("Ship To: ${order.customerName}", 20f, 100f, paint)
    canvas.drawText("Address: ${order.customerAddress?.take(30)}...", 20f, 120f, paint)
    canvas.drawText("Contact: ${order.customerPhone}", 20f, 140f, paint)

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
