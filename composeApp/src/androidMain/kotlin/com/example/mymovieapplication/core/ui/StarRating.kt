package com.example.mymovieapplication.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    voteAverage: Double,
    starSize: Dp = 16.dp,
    showRating: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Convert vote average from 0-10 scale to 0-5 scale for stars
        val starRating = (voteAverage / 2).toInt()

        repeat(5) { index ->
            Icon(
                imageVector = if (index < starRating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (index < starRating) Color(0xFFFFD700) else Color.Gray,
                modifier = Modifier.size(starSize)
            )
        }

        if (showRating) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f", voteAverage),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}