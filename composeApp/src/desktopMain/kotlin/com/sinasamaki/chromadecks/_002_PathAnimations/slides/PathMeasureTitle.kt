package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Teal400
import com.sinasamaki.chromadecks.ui.theme.White

class PathMeasureTitleState()
class PathMeasureTitle : ListSlideAdvanced<PathMeasureTitleState>() {

    override val initialState: PathMeasureTitleState
        get() = PathMeasureTitleState()

    override val stateMutations: List<PathMeasureTitleState.() -> PathMeasureTitleState>
        get() = listOf()

    @Composable
    override fun content(state: PathMeasureTitleState) {
        val infinite = rememberInfiniteTransition()
        val measureValue by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 8000,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        var pingOffset by remember { mutableStateOf(Offset.Zero) }
        var lastTangent by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier
                .background(Neutral950)
                .padding(64.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .drawWithCache(
                    drawMeasureTitle(
                        progress = { measureValue },
                        onPing = { offset, tangent ->
                            if (lastTangent != tangent) {
                                pingOffset = offset
                                lastTangent = tangent
                            }
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            val pingAnimation = remember { Animatable(0f) }

            LaunchedEffect(pingOffset) {
                if (pingOffset.isSpecified) {
                    pingAnimation.snapTo(0f)
                    pingAnimation.animateTo(
                        1f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(-64.dp, -64.dp)
                    .offset { pingOffset.toIntOffset() }
                    .size(128.dp)
                    .scale(lerp(.1f, 3f, pingAnimation.value))
                    .alpha(lerp(1f, 0f, pingAnimation.value))
                    .border(
                        width = 1.dp,
                        color = White,
                        shape = CircleShape,
                    )
            )
            Text(
                text = "Path Measuring",
                style = MaterialTheme.typography.labelLarge.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Sky400,
                            Teal400,
                            Lime400,
                        )
                    )
                ),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 100.sp
            )
        }
    }
}

fun drawMeasureTitle(
    progress: () -> Float,
    onPing: (Offset, Int) -> Unit = { _, _ -> },
): CacheDrawScope.() -> DrawResult = {
    val (path, control) = createPath(
        points = listOf(
            Offset(x = 0.5f, y = 0.1f),
            Offset(x = 0.5f, y = 0.01f),
            Offset(x = 0.009f, y = 0.25f),
            Offset(x = 0.1f, y = 0.45f),
            Offset(x = 0.99f, y = 0.4f),
            Offset(x = 0.5f, y = 0.75f),
            Offset(x = 0.5f, y = 0.95f),

            Offset(x = 0.9f, y = 0.8f),
            Offset(x = 0.1f, y = 0.4f),
            Offset(x = 0.5f, y = 0.1f),
        ).map {
            it * size
        }
    )

    val measure = PathMeasure()
    measure.setPath(path, true)

    onDrawBehind {

        var currentTangentX = if (measure.getTangent(measure.length * progress()).x > 0f) 1 else -1

        onPing(
            measure.getPosition(measure.length * progress()),
            currentTangentX
        )


        val segment = Path()
        measure.getSegment(
            startDistance = (measure.length * progress()) - 800f,
            stopDistance = (measure.length * progress()),
            destination = segment
        )
        drawPath(
            path = segment,
//            color = Zinc950,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Sky400,
                    Teal400,
                    Lime400,
                )
            ),
            style = Stroke(
                width = 4f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect
                    .dashPathEffect(
                        intervals = floatArrayOf(
                            measure.length * progress(), measure.length
                        )
                    )
            )
        )

        drawCircle(
//            color = Zinc950,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Lime400,
                    Sky400,
                    Teal400,
                )
            ),
            center = measure.getPosition(measure.length * progress()),
            radius = 50f
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Sky400,
                    Teal400,
                    Lime400,
                )
            ),
            alpha = .1f,
        )
    }
}