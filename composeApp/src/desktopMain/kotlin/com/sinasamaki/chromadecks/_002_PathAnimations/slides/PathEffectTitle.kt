package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.util.ChainedPathEffect

class PathEffectTitleState()
class PathEffectTitle : ListSlideAdvanced<PathEffectTitleState>() {

    override val initialState: PathEffectTitleState
        get() = PathEffectTitleState()

    override val stateMutations: List<PathEffectTitleState.() -> PathEffectTitleState>
        get() = listOf()

    @Composable
    override fun content(state: PathEffectTitleState) {

        val infinite = rememberInfiniteTransition()
        val effectsValue by infinite.animateFloat(
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

        Box(
            modifier = Modifier
                .background(Neutral950)
                .padding(64.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .drawWithCache(
                    drawPathEffectTitle(
                        progress = { effectsValue },
                        tracks = 10,
                        cornerRadius = 700f,
                        advance = 10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Path Effects",
                style = MaterialTheme.typography.labelLarge.copy(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Lime400,
                            Yellow300,
                            Orange500,
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

fun drawPathEffectTitle(
    progress: () -> Float,
    tracks: Int = 3,
    cornerRadius: Float = 50f,
    advance: Float = 2f
): CacheDrawScope.() -> DrawResult = {
    val (path, _) = createPath(
        points = listOf(
            Offset(x = 0.9f, y = -0.2f),
            Offset(x = 0.05f, y = 0.4f),
            Offset(x = 1.01f, y = 0.4f),
            Offset(x = 0.8f, y = 0.6f),
            Offset(x = 0.2f, y = 0.6f),
            Offset(x = 0.5f, y = 0.8f),
            Offset(x = 0.0f, y = 0.9f),
        ).map {
            it * size
        },
        curvature = 0f
    )

    val measure = PathMeasure()
    measure.setPath(path, false)

    val circles = Path()

    for (i in 0..tracks) {
        circles.addOval(
            oval = Rect(
                center = Offset(0f, 20f * i),
                radius = 2.5f,
            )
        )
    }

    onDrawBehind {

        drawPath(
            path = path,
//            color = Zinc950,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Lime400,
                    Yellow300,
                    Orange500,
                )
            ),
            style = Stroke(
                width = 10f,
                cap = StrokeCap.Round,
                pathEffect = ChainedPathEffect()
                    .cornerPathEffect(cornerRadius)
                    .dashPathEffect(
                        intervals = floatArrayOf(
                            500f, 100f
                        ),
                        phase = -measure.length * progress()
                    )
                    .stampedPathEffect(
                        shape = circles,
                        advance = advance,
                        phase = 0f,
                        style = StampedPathEffectStyle.Translate
                    )
                    .effect
            ),
            alpha = .7f,
        )
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Orange500,
                    Yellow300,
                    Lime400,
                )
            ),
            alpha = .05f
        )
    }
}