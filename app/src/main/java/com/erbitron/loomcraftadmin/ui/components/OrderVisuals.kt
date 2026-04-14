package com.erbitron.loomcraftadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.erbitron.loomcraftadmin.ui.theme.BrandDanger
import com.erbitron.loomcraftadmin.ui.theme.BrandSuccess
import com.erbitron.loomcraftadmin.ui.theme.BrandWarning

@Composable
fun OrderPageHeader(
    eyebrow: String,
    title: String,
    onSearchClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 28.dp, bottom = 16.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = eyebrow.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                onSearchClick?.let {
                    RoundedActionIcon(
                        icon = Icons.Outlined.Search,
                        onClick = it
                    )
                }
                onLogoutClick?.let {
                    RoundedActionIcon(
                        icon = Icons.Outlined.Logout,
                        onClick = it
                    )
                }
            }
        }
    }
}

@Composable
fun OrderHeaderRow(
    orderLabel: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = orderLabel,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        )
        StatusTag(status = status)
    }
}

@Composable
fun OrderAmountPanel(
    label: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

@Composable
fun OrderMetaText(
    itemsText: String,
    dateText: String,
    timeText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MetaItem(Icons.Outlined.Inventory2, itemsText)
        MetaItem(Icons.Outlined.AccessTime, dateText)
        MetaItem(Icons.Outlined.AccessTime, timeText)
    }
}

@Composable
fun RoundedActionIcon(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MetaItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// Deprecated or Replaced functions - keeping skeleton if needed elsewhere
@Composable
fun ModernStatusTag(status: String, modifier: Modifier = Modifier) = StatusTag(status, modifier)

@Composable
fun orderStatusPalette(status: String): OrderStatusPalette {
    val color = when (status.lowercase()) {
        "pending" -> BrandWarning
        "paid", "delivered", "shipped" -> BrandSuccess
        "rejected", "cancelled" -> BrandDanger
        else -> MaterialTheme.colorScheme.primary
    }
    return OrderStatusPalette(color.copy(alpha = 0.1f), color, color.copy(alpha = 0.1f), color, color)
}

data class OrderStatusPalette(
    val chipBackground: Color,
    val chipText: Color,
    val accentBackground: Color,
    val accentForeground: Color,
    val amountColor: Color
)
