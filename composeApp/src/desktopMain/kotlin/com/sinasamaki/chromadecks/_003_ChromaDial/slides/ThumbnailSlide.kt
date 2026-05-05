package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.components.HeartLogo
import com.sinasamaki.chromadecks.ui.components.HeartShape
import com.sinasamaki.chromadecks.ui.components.createHeartPath
import com.sinasamaki.chromadecks.ui.components.heartRatio
import com.sinasamaki.chromadecks.ui.displays.CompactDevice
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime600
import com.sinasamaki.chromadecks.ui.theme.Neutral400
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc950

internal data class ThumbnailSlideState(val revealed: Boolean = false)

internal class ThumbnailSlide : ListSlideAdvanced<ThumbnailSlideState>() {

    override val initialState: ThumbnailSlideState
        get() = ThumbnailSlideState()

    override val stateMutations: List<ThumbnailSlideState.() -> ThumbnailSlideState>
        get() = listOf()

    @Composable
    override fun content(state: ThumbnailSlideState) {

        Box(
            Modifier
                .fillMaxSize()
                .aspectRatio(16 / 9f)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Lime600,
                            Lime500,
                            Lime600,
                        )
                    )
                ),
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Black,
                        shape = RoundedCornerShape(
                            bottomEnd = 48.dp
                        )
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(
                            bottomEnd = 48.dp
                        )
                    ) {
                        color = Lime400
                        radius = 50f
                        offset = Offset(-20f, -20f)
                        alpha = .25f
                    }
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .heartRatio()
                        .fillMaxWidth()
                        .background(
                            color = Zinc200,
                            shape = HeartShape,
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .graphicsLayer {
                        val scale = 1.3f
                        scaleX = scale
                        scaleY = scale

                        rotationZ = -4f
                    },
                horizontalArrangement = Arrangement.spacedBy(
                    64.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {


                CodeIDE(
                    modifier = Modifier
                        .width(590.dp),
                    tabs = listOf(
                        "ChromaDial.kt" to """
Dial(
    degree = degree,
    onDegreeChange = { degree = it },
    startDegrees = 225f,
    sweepDegrees = 270f,
)
                    """.trimIndent(),
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    selectedTab = 0,
                    onTabSelect = {},
                )

                CompactDevice(
                    modifier = Modifier
                        .fillMaxHeight()
                        .offset(y = 100.dp)
//                    .weight(1f)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                color = Black,
                            )
                    ) {
                        var degree by remember { mutableStateOf(0f) }
                        ComplexDial(
//                        degree = degree,
//                        onDegreeChange = { degree = it },
//                        startDegrees = 225f,
//                        sweepDegrees = 270f,
//                        interval = 15f,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .aspectRatio(1f)
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun ComplexDial(modifier: Modifier = Modifier) {
    var degree by remember { mutableStateOf(0f) }

    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        startDegrees = 225f,
        sweepDegrees = 270f,
        interval = 15f,
        modifier = modifier
            .padding(12.dp),
        track = { dialState ->
            val measure = rememberTextMeasurer()
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawArc(
                            color = Zinc950,
                            startAngle = dialState.startDegrees,
                            sweepAngle = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                            radius = dialState.radius - 36.dp.toPx(),
                            strokeWidth = 32.dp,
                        )
                        drawArc(
                            color = Lime500,
                            startAngle = dialState.startDegrees,
                            sweepAngle = dialState.degree + dialState.overshootDegrees,
                            radius = dialState.radius - 36.dp.toPx(),
                            strokeWidth = 24.dp,
                        )

                        drawEveryInterval(
                            radius = dialState.radius - 28.dp.toPx(),
                            startDegrees = dialState.startDegrees,
                            sweepDegrees = dialState.degree + dialState.overshootDegrees,
                            interval = 3f,
                        ) { data ->
                            rotate(
                                degrees = data.rotationAngle,
                                pivot = data.position,
                            ) {
                                translate(
                                    left = data.position.x,
                                    top = data.position.y,
                                ) {
                                    drawLine(
                                        color = Zinc950,
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, 8.dp.toPx()),
                                        strokeWidth = 4.dp.toPx(),
                                        cap = StrokeCap.Round,
                                    )
                                }
                            }
                        }


                        drawEveryInterval(
                            interval = 9f,
                            radius = dialState.radius,
                            center = this.center,
                            startDegrees = dialState.startDegrees,
                            sweepDegrees = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                        ) { data ->
                            rotate(
                                degrees = data.rotationAngle + dialState.overshootDegrees,
                                pivot = data.position,
                            ) {
                                translate(
                                    left = data.position.x,
                                    top = data.position.y,
                                ) {
                                    if (data.index % 5 == 0) {
                                        drawText(
                                            textMeasurer = measure,
                                            text = "${data.intervalDegree.toInt()}",
                                            style = TextStyle(
                                                color = Lime400,
                                                textAlign = TextAlign.Center,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 18.sp
                                            ),
                                            size = Size(18.sp.toPx() * 3, 100f),
                                            topLeft = Offset(
                                                -50f,
                                                -18.sp.toPx() / 2,
                                            )
                                        )
                                    } else {
                                        drawLine(
                                            color = if (data.inActiveRange) Lime400 else Neutral400,
                                            start = Offset(0f, 0f),
                                            end = Offset(0f, 2.dp.toPx()),
                                            strokeWidth = 2.dp.toPx(),
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }
                        }

                    }
            )
        },
        thumb = {
            Box(Modifier.size(84.dp))
        }
    )
}