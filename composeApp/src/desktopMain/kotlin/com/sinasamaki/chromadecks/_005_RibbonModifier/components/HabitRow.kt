package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc900

@Composable
fun HabitRow(
    modifier: Modifier = Modifier,
    label: String = "Morning run",
) {
    Box(
        modifier = modifier
            .background(White, CircleShape)
            .border(1.dp, Zinc200, CircleShape)
            .padding(horizontal = 40.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Zinc900,
        )
    }
}
