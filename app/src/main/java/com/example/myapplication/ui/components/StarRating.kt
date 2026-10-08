package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Float,
    maxStars: Int = 5,
    starSize: Dp = 20.dp,
    starColor: Color = Color(0xFFFFB800),
    isInteractive: Boolean = false,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val isFilled = rating >= i
            val isHalf = rating >= i - 0.5f && rating < i

            val icon = when {
                isFilled -> Icons.Filled.Star
                isHalf -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Outlined.Star
            }

            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = if (isFilled || isHalf) starColor else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (isInteractive && onRatingChanged != null) {
                            Modifier
                                .clickable { onRatingChanged(i.toFloat()) }
                                .padding(2.dp)
                        } else {
                            Modifier.padding(1.dp)
                        }
                    )
            )
        }
    }
}
