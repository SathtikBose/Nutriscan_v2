package com.buildstack.nutriscan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AllergenWarning(
    warnings: List<String>,
    severity: String, // safe, caution, dangerous
    modifier: Modifier = Modifier
) {
    if (warnings.isEmpty() && severity == "safe") return

    val (bgColor, contentColor) = when (severity.lowercase()) {
        "dangerous" -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B)) // Light Red, Dark Red
        "caution" -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E)) // Light Amber, Dark Amber
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF475569)) // Gray
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.WarningAmber,
            contentDescription = "Warning",
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = if (severity.lowercase() == "dangerous") "Allergen Alert!" else "Caution",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = contentColor
            )
            Text(
                text = "Contains: ${warnings.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor
            )
        }
    }
}
