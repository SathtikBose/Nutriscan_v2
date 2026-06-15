package com.buildstack.nutriscan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.buildstack.nutriscan.domain.model.NutritionSummary

@Composable
fun NutritionTable(
    nutrition: NutritionSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Text(
            text = "Nutrition Facts (per ${nutrition.servingSize})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        NutritionRow(label = "Calories", value = nutrition.calories, isBold = true)
        NutritionRow(label = "Total Fat", value = nutrition.fat)
        NutritionRow(label = "Sodium", value = nutrition.sodium)
        NutritionRow(label = "Total Carbohydrate", value = nutrition.carbs)
        NutritionRow(label = "Dietary Fiber", value = nutrition.fiber, isIndented = true)
        NutritionRow(label = "Total Sugars", value = nutrition.sugar, isIndented = true)
        NutritionRow(label = "Protein", value = nutrition.protein, isBold = true, showDivider = false)
    }
}

@Composable
private fun NutritionRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    isIndented: Boolean = false,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .padding(start = if (isIndented) 16.dp else 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (showDivider) {
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}
