package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks.ui.theme.Emerald500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc900

/**
 * The thing the ribbon wraps: a habit row, the same shape the effect ships on in SubAtomic.
 * Deliberately plain, so every pixel of interest on the slide belongs to the ribbon.
 */
@Composable
fun HabitRow(
    modifier: Modifier = Modifier,
    label: String = "Morning run",
    done: Boolean = true,
    corner: androidx.compose.ui.unit.Dp = 28.dp,
) {
    Row(
        modifier = modifier
            .background(White, RoundedCornerShape(corner))
            .border(1.dp, Zinc200, RoundedCornerShape(corner))
            .padding(horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Zinc900,
        )

        Box(
            modifier = Modifier
                .padding(start = 32.dp)
                .size(44.dp)
                .background(if (done) Emerald500 else Zinc200, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (done) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}
