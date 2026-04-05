package com.example.loomcraftadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loomcraftadmin.ui.theme.AccentSoft
import com.example.loomcraftadmin.ui.theme.BrandStrong
import com.example.loomcraftadmin.ui.theme.LoomCraftAdminTheme

@Composable
fun LoomCraftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape, // Pill shaped
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun LoomCraftCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun StatusTag(
    status: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status.lowercase()) {
        "pending" -> AccentSoft
        "processing", "accepted" -> MaterialTheme.colorScheme.secondaryContainer
        "rejected" -> MaterialTheme.colorScheme.errorContainer
        "hand over to admin" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
        "shipped" -> Color(0xFFE8F5E9)
        "delivered" -> Color(0xFFC8E6C9)
        "cancelled" -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = when (status.lowercase()) {
        "pending" -> BrandStrong
        "processing", "accepted" -> MaterialTheme.colorScheme.secondary
        "rejected" -> MaterialTheme.colorScheme.error
        "hand over to admin" -> MaterialTheme.colorScheme.tertiary
        "shipped" -> Color(0xFF2E7D32)
        "delivered" -> Color(0xFF1B5E20)
        "cancelled" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun ComponentsPreview() {
    LoomCraftAdminTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(16.dp)) {
                LoomCraftButton(text = "Primary Button", onClick = {})
                Spacer(modifier = Modifier.height(16.dp))
                LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("Order #12345", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusTag(status = "Pending")
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusTag(status = "Processing")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                StatusTag(status = "Hand over to admin")
            }
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun ComponentsPreviewDark() {
    LoomCraftAdminTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(16.dp)) {
                LoomCraftButton(text = "Primary Button", onClick = {})
                Spacer(modifier = Modifier.height(16.dp))
                LoomCraftCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("Order #12345", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusTag(status = "Pending")
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusTag(status = "Processing")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                StatusTag(status = "Hand over to admin")
            }
        }
    }
}
