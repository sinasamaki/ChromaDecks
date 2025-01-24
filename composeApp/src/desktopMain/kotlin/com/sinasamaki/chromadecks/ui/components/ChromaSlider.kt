package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Slate300
import com.sinasamaki.chromadecks.ui.theme.White

val thumbSize = 84.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChromaSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    displayValue: (Float) -> String = { "" },
) {
    val animatedValue by animateFloatAsState(
        targetValue = value
    )
    Slider(
        value = animatedValue,
        onValueChange = onValueChange,
        modifier = modifier.padding(4.dp),
        valueRange = valueRange,
        thumb = {
            Box(
                Modifier
                    .size(thumbSize)
                    .padding(8.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                    )
                    .background(
                        White,
                        shape = CircleShape,
                    )
                    .padding(4.dp)
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                Color.Black.copy(alpha = .1f),
//                                Color.Transparent,
//                            ),
//                        ),
//                        shape = CircleShape,
//                    )
            ) {
                Text(
                    text = displayValue(animatedValue),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.labelLarge,
                    color = Black,
                )
            }
        },
        track = {
            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .height(thumbSize)
            ) {
                Box(
                    Modifier
                        .requiredWidth(maxWidth + thumbSize)
                        .height(thumbSize)
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    White.copy(alpha = .5f),
                                    White.copy(alpha = .1f),
                                )
                            ),
                            shape = CircleShape,
                        )
                        .padding(4.dp)
                        .background(
                            Slate300.copy(alpha = .05f),
                            CircleShape
                        )

                )
            }
        }
    )
}