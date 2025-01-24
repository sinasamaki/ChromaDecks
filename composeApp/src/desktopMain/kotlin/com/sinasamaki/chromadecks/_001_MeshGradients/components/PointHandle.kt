package com.sinasamaki.chromadecks._001_MeshGradients.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.extensions.centerHorizontally
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.components.circler
import com.sinasamaki.chromadecks.ui.theme.Slate950

@Composable
fun BoxWithConstraintsScope.PointHandle(
    color: Color,
    offset: Offset,
    showColors: Boolean,
    showPoints: Boolean,
    shadow: Shadow? = null,
    contentColor: Color = LocalContentColor.current,
) {
    val delay = remember { ((offset.x * (offset.y + 1)) * 100).toInt() }
    Column(
        modifier = Modifier
            .offset {
                Offset(
                    maxWidth.toPx() * offset.x,
                    maxHeight.toPx() * offset.y,
                ).toIntOffset()
            }
            .offset((-16).dp, (-16).dp),
    ) {
        AnimatedVisibility(
            visible = showPoints,
            modifier = Modifier.circler(contentColor),
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500, delayMillis = delay
                )
            ),
            exit = scaleOut(
                animationSpec = tween(
                    durationMillis = 500, delayMillis = delay,
                )
            ),
        ) {
            val colorScale by animateFloatAsState(
                targetValue = if (showColors) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = delay,
                ),
            )
            val borderWidth by animateDpAsState(
                targetValue = if (showColors) 0.dp else 3.dp,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = delay,
                ),
            )
            val borderColor by animateColorAsState(
                targetValue = contentColor.copy(alpha = if (showColors) 0f else 1f),
                animationSpec = tween(
                    durationMillis = 700,
                    delayMillis = delay,
                ),
            )
            Box(
                Modifier
                    .size(32.dp)
                    .border(
                        width = borderWidth,
                        color = borderColor,
                        shape = CircleShape,
                    )
                    .scale(colorScale)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                    )
                    .background(
                        color = color, shape = CircleShape
                    )
            )
        }
        AnimatedVisibility(
            visible = showPoints,
            modifier = Modifier
                .offset(x = 16.dp)
                .centerHorizontally(),
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500, delayMillis = delay,
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 500, delayMillis = delay,
                )
            ),
        ) {
            Text(
                text = "(${offset.x.toString().take(4)}, ${offset.y.toString().take(4)})",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = contentColor,
                    shadow = shadow,
                ),
            )
        }
    }
}