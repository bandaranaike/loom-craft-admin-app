package com.example.loomcraftadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.loomcraftadmin.ui.theme.BrandAccent
import com.example.loomcraftadmin.ui.theme.BrandStrong

data class OrderStatusPalette(
    val chipBackground: Color,
    val chipText: Color,
    val accentBackground: Color,
    val accentForeground: Color,
    val amountColor: Color
)

fun orderStatusPalette(status: String): OrderStatusPalette {
    return when (status.lowercase()) {
        "paid" -> OrderStatusPalette(
            chipBackground = Color(0xFFFFE4D6),
            chipText = Color(0xFFF97316),
            accentBackground = Color(0xFFFFF1EA),
            accentForeground = Color(0xFFF97316),
            amountColor = Color(0xFFF97316)
        )
        "delivered", "shipped" -> OrderStatusPalette(
            chipBackground = Color(0xFFDCFCE7),
            chipText = Color(0xFF22C55E),
            accentBackground = Color(0xFFECFDF3),
            accentForeground = Color(0xFF22C55E),
            amountColor = Color(0xFF22C55E)
        )
        "pending" -> OrderStatusPalette(
            chipBackground = Color(0xFFFEF3C7),
            chipText = Color(0xFFF59E0B),
            accentBackground = Color(0xFFFFF7DA),
            accentForeground = Color(0xFFF59E0B),
            amountColor = Color(0xFFF59E0B)
        )
        else -> OrderStatusPalette(
            chipBackground = Color(0xFFEDE9FE),
            chipText = Color(0xFF7C3AED),
            accentBackground = Color(0xFFF5F3FF),
            accentForeground = Color(0xFF7C3AED),
            amountColor = BrandAccent
        )
    }
}

@Composable
fun OrderPageHeader(
    eyebrow: String,
    title: String,
    onSearchClick: (() -> Unit)? = null,
    onLogoutClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE0E7FF),
                        Color(0xFFFCE7F3),
                        Color(0xFFFDE68A)
                    ),
                    start = Offset.Zero,
                    end = Offset(1200f, 700f)
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFE9EDFF))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = eyebrow,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFF6674D8),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    onSearchClick?.let {
                        RoundedActionIcon(
                            icon = Icons.Outlined.Search,
                            contentDescription = "Search",
                            onClick = it
                        )
                    }
                    onLogoutClick?.let {
                        RoundedActionIcon(
                            icon = Icons.Outlined.Logout,
                            contentDescription = "Logout",
                            onClick = it
                        )
                    }
                }
            }

            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold
                )
            )
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
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.SemiBold
            )
        )
        ModernStatusTag(status = status)
    }
}

@Composable
fun ModernStatusTag(
    status: String,
    modifier: Modifier = Modifier
) {
    val palette = orderStatusPalette(status)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(palette.chipBackground)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = status.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = palette.chipText,
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            imageVector = when (status.lowercase()) {
                "pending" -> Icons.Outlined.AccessTime
                else -> Icons.Outlined.CheckCircle
            },
            contentDescription = null,
            tint = palette.chipText,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun OrderInfoChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun OrderAmountPanel(
    label: String,
    amount: String,
    amountColor: Color = BrandAccent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF6B7280)
            )
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.titleLarge.copy(
                color = amountColor,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun OrderSectionHeader(
    eyebrow: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = eyebrow,
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF6D71DA),
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color(0xFF0F172A),
                fontWeight = FontWeight.Bold
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetaPart(Icons.Outlined.Inventory2, itemsText)
        MetaDot()
        MetaPart(Icons.Outlined.Wallet, dateText)
        MetaDot()
        MetaPart(Icons.Outlined.AccessTime, timeText)
    }
}

@Composable
fun OrderLeadingBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val palette = orderStatusPalette(status)
    Box(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(palette.accentBackground),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when (status.lowercase()) {
                "delivered", "shipped" -> Icons.Outlined.CheckCircle
                "pending" -> Icons.Outlined.AccessTime
                "paid" -> Icons.Outlined.Wallet
                else -> Icons.Outlined.ArrowForward
            },
            contentDescription = null,
            tint = palette.accentForeground
        )
    }
}

@Composable
fun RoundedActionIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = BrandStrong
            )
        }
    }
}

@Composable
private fun MetaPart(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun MetaDot() {
    Text(
        text = "•",
        style = MaterialTheme.typography.bodySmall.copy(
            color = Color(0xFFCBD5E1)
        )
    )
}
