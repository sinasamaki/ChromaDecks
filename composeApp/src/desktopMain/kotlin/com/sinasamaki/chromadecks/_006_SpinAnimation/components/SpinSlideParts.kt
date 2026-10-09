package com.sinasamaki.chromadecks._006_SpinAnimation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinRed
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinRedMuted

internal val DemoWidth: Dp = 400.dp
internal val DemoHeight: Dp = 116.dp
internal val DemoThickness: Dp = 24.dp

@Composable
internal fun HabitLabel(
    text: String = "Morning run",
    fontSize: TextUnit = 40.sp,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
        ),
    )
}

@Composable
internal fun DemoCaption(
    text: String,
    modifier: Modifier = Modifier,
    visible: Float = 1f,
) {
    Text(
        text = text,
        modifier = modifier.alpha(visible),
        style = MaterialTheme.typography.labelMedium,
        color = SpinRedMuted,
    )
}

@Composable
internal fun SlideHeading(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 60.sp,
        ),
        color = SpinRed,
    )
}
