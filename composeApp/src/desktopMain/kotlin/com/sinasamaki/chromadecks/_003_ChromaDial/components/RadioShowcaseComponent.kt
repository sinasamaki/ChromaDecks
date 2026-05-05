package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Neutral500
import com.sinasamaki.chromadecks.ui.theme.Neutral600
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Neutral800
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Orange400

private val accentColor = Lime500

@Composable
fun RadioShowcase(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Neutral950,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .background(
                    color = Neutral500,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                    )
                )
        )

        var degree by remember { mutableStateOf(0f) }
        Dial(
            degree = degree,
            onDegreeChange = { degree = it },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxSize(.5f)
                .offset(24.dp, -24.dp),
            thumb = {
                Box(modifier = Modifier.fillMaxSize())
            },
            track = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            color = Neutral800,
                            shape = CircleShape,
                        )
                        .rotate(it.degree + it.overshootDegrees)
                        .drawBehind {
                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 360f,
                                interval = 10f,
                                radius = size.width * .52f
                            ) {
                                rotate(
                                    degrees = it.rotationAngle,
                                    pivot = it.position,
                                ) {
                                    translate(
                                        left = it.position.x,
                                        top = it.position.y,
                                    ) {
                                        drawLine(
                                            color = Neutral800,
                                            start = Offset(0f, 10f),
                                            end = Offset(0f, 40f),
                                            strokeWidth = 22f,
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }

                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 360f,
                                interval = 10f,
                                radius = size.width / 2f
                            ) {
                                rotate(
                                    degrees = it.rotationAngle,
                                    pivot = it.position,
                                ) {
                                    translate(
                                        left = it.position.x,
                                        top = it.position.y,
                                    ) {
                                        drawLine(
                                            color = accentColor,
                                            start = Offset(0f, 10f),
                                            end = Offset(0f, 40f),
                                            strokeWidth = 6f,
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }
                        }
                )
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .drawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Neutral800.copy(alpha = 0f),
                                    Neutral600.copy(alpha = .3f),
                                ),
                            ),
                        )
                    }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Neutral950)
            )
            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp,
                        )
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(.35f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    imageVector = Icons.Rounded.SkipPrevious,
                    shape = RoundedCornerShape(bottomStart = 24.dp),
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = Neutral950)
                )
                RadioButton(
                    imageVector = Icons.Rounded.PlayArrow,
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(color = Neutral950)
                )
                RadioButton(
                    imageVector = Icons.Rounded.SkipNext,
                    shape = RoundedCornerShape(bottomEnd = 24.dp),
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun RowScope.RadioButton(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    imageVector: ImageVector,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 5.dp else 0.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    val shadow by animateFloatAsState(
        targetValue = if (isPressed) .8f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 5f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = accentColor,
        modifier = modifier
            .clickable(
                interactionSource = interaction,
                indication = null,
            ) {}
            .graphicsLayer {
                transformOrigin = TransformOrigin(.5f, 0f)
                translationY = -pressOffset.toPx()
                rotationX = -rotation
            }
            .background(
                color = Neutral800,
                shape = shape,
            )
            .drawWithContent {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Neutral700.copy(alpha = .5f),
                            Neutral700.copy(alpha = 0f)
                        ),
                        endY = size.height * .8f
                    ),
                )
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Neutral950.copy(alpha = shadow),
                            Neutral700.copy(alpha = 0f)
                        ),
                        endY = size.height * .8f
                    ),
                )
            }
            .fillMaxHeight()
            .weight(1f)
            .padding(vertical = 24.dp)
    )
}
