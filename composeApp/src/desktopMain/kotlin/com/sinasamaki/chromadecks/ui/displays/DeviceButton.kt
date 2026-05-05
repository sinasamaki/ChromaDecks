package com.sinasamaki.chromadecks.ui.displays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc950

@Composable
fun DeviceButton(
    buttonSize: Dp = 12.dp
) {
    Box(
        modifier = Modifier
            .width(buttonSize)
            .height(60.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Zinc800,
                        Zinc950,
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = buttonSize,
                    bottomStart = buttonSize
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Zinc200.copy(alpha = .3f),
                        Transparent,
                    ),
                    endX = 2.dp.toPx
                ),
                shape = RoundedCornerShape(
                    topStart = buttonSize,
                    bottomStart = buttonSize
                )
            )
    )
}