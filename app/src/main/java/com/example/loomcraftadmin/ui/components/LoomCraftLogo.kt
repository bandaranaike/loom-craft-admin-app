package com.example.loomcraftadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loomcraftadmin.ui.theme.BrandGold

@Composable
fun LoomCraftLogo(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val size = if (compact) 44.dp else 84.dp
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "L",
                color = BrandGold,
                fontSize = if (compact) 28.sp else 54.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "C",
                color = BrandGold,
                fontSize = if (compact) 26.sp else 48.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}
