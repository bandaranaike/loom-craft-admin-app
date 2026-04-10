package com.example.loomcraftadmin.ui.features.admin.shipping

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loomcraftadmin.data.model.OrderAddress
import com.example.loomcraftadmin.data.model.OrderDetail
import com.example.loomcraftadmin.data.model.OrderItem
import com.example.loomcraftadmin.ui.theme.LoomCraftAdminTheme

@Composable
fun ShippingLabel(
    order: OrderDetail,
    modifier: Modifier = Modifier
) {
    val inkColor = MaterialTheme.colorScheme.onSurface
    val mutedInkColor = MaterialTheme.colorScheme.onSurfaceVariant
    val panelColor = MaterialTheme.colorScheme.surface
    val dividerColor = MaterialTheme.colorScheme.outline
    val softDividerColor = MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = modifier
            .width(400.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = panelColor),
        border = BorderStroke(2.dp, dividerColor)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Header: Logo & Order ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LOOMCRAFT",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = inkColor
                    )
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "ORDER ID",
                        style = MaterialTheme.typography.labelSmall,
                        color = mutedInkColor
                    )
                    Text(
                        text = "#${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = inkColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = dividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Ship To Section
            Text(
                text = "SHIP TO:",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = inkColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = order.displayCustomerName() ?: "N/A",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = inkColor
            )
            Text(
                text = order.displayCustomerAddress() ?: "No Address Provided",
                style = MaterialTheme.typography.bodyLarge,
                color = inkColor,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Contact: ${order.displayCustomerPhone() ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = inkColor
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 1.dp, color = softDividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Items Summary
            Text(
                text = "CONTENTS:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = mutedInkColor
            )
            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}x ${item.productName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = inkColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer / Barcode Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(inkColor.copy(alpha = 0.05f))
                    .drawBehind {
                        // Simple barcode-like pattern
                        val barWidth = 4.dp.toPx()
                        val spacing = 2.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            val w = if ((x / (barWidth + spacing)).toInt() % 3 == 0) barWidth * 2 else barWidth
                            drawRect(
                                color = inkColor,
                                topLeft = Offset(x, 0f),
                                size = androidx.compose.ui.geometry.Size(w, size.height)
                            )
                            x += w + spacing
                        }
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShippingLabelPreview() {
    LoomCraftAdminTheme {
        Box(modifier = Modifier.padding(20.dp)) {
            ShippingLabel(
                order = OrderDetail(
                    id = 104,
                    status = "Accepted",
                    items = listOf(
                        OrderItem(
                            id = 1,
                            productName = "Silk Scarf",
                            quantity = 1,
                            unitPrice = 450.0,
                            status = "Accepted"
                        ),
                        OrderItem(
                            id = 2,
                            productName = "Handloom Saree",
                            quantity = 1,
                            unitPrice = 1200.0,
                            status = "Accepted"
                        )
                    ),
                    addresses = listOf(
                        OrderAddress(
                            id = 1,
                            type = "shipping",
                            fullName = "Priya Kapoor",
                            line1 = "123, Heritage Lane",
                            city = "Jaipur",
                            region = "Rajasthan",
                            postalCode = "302001",
                            countryCode = "IN",
                            phone = "+91 98765 43210"
                        )
                    ),
                    total = 1650.0,
                    createdAt = "2026-04-02"
                )
            )
        }
    }
}
