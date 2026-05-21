package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.qrcode
import chromadecks.composeapp.generated.resources.voteBadge
import chromadecks.composeapp.generated.resources.voteDevice
import chromadecks.composeapp.generated.resources.voteTitle
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Orange50
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Pink50
import com.sinasamaki.chromadecks.ui.theme.Pink500
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

class VoteSlideState
class VoteSlide : ListSlideAdvanced<VoteSlideState>() {

    override val initialState: VoteSlideState
        get() = VoteSlideState()

    @Composable
    override fun content(state: VoteSlideState) {
        val transition = rememberInfiniteTransition()
        val yOffset by transition.animateFloat(
            initialValue = -180f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )

        val xOffset by transition.animateFloat(
            initialValue = -80f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Black,
                )
                .clip(RectangleShape)
        ) {
            Image(
                painter = painterResource(Res.drawable.voteTitle),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(Res.drawable.voteBadge),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .offset {
                        IntOffset(xOffset.roundToInt(), yOffset.roundToInt())
                    },
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(Res.drawable.voteDevice),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .offset {
                        IntOffset(-xOffset.roundToInt(), -(yOffset * .5f).roundToInt())
                    },
                contentScale = ContentScale.Crop,
            )

            Image(
                painter = painterResource(Res.drawable.qrcode),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = 110.dp)
                    .fillMaxHeight(0.45f)
                    .aspectRatio(1f)
                    .padding(32.dp)
                    .rotate(-5f)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val sx = size.width / 16f
                        val sy = size.height / 9f

                        // All coordinates in 16x9 space
                        val startX  = 9.5f;  val startY  = 6.8f
                        val cp1X    = 9.5f; val cp1Y    = 8.5f
                        val cp2X    = 11.5f; val cp2Y    = 9.5f
                        val endX    = 12.5f; val endY    = 8.2f

                        val arrowPath = Path().apply {
                            moveTo(startX * sx + xOffset, startY * sy + yOffset)
                            cubicTo(
                                cp1X * sx, cp1Y * sy,
                                cp2X * sx, cp2Y * sy,
                                endX  * sx - xOffset, endY  * sy - (yOffset * .5f),
                            )
                        }
                        drawPath(
                            path = arrowPath,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Pink500, Orange500,
                                ),
                                startX = center.x,
                                endX = size.width * .75f
                            ),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                            ),
                        )

                        val measure = PathMeasure()
                        measure.setPath(arrowPath, false)
                        val endPos = measure.getPosition(measure.length)
                        val tangent = measure.getTangent(measure.length)

                        val arrowHeadSize = 24f
                        val arrowAngle = (30f * Math.PI / 180f).toFloat()
                        val bx = -tangent.x
                        val by = -tangent.y
                        val wing1x = bx * cos(arrowAngle) - by * sin(arrowAngle)
                        val wing1y = bx * sin(arrowAngle) + by * cos(arrowAngle)
                        val wing2x = bx * cos(-arrowAngle) - by * sin(-arrowAngle)
                        val wing2y = bx * sin(-arrowAngle) + by * cos(-arrowAngle)

                        val arrowHead = Path().apply {
                            moveTo(endPos.x + wing1x * arrowHeadSize, endPos.y + wing1y * arrowHeadSize)
                            lineTo(endPos.x, endPos.y)
                            lineTo(endPos.x + wing2x * arrowHeadSize, endPos.y + wing2y * arrowHeadSize)
                        }
                        drawPath(
                            path = arrowHead,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Pink500, Orange500,
                                ),
                                startX = center.x,
                                endX = size.width * .75f
                            ),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                            ),
                        )
                    }
            )
        }
    }
}
