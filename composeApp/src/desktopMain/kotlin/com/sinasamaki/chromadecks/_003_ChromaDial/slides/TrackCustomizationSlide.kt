package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Neutral400
import com.sinasamaki.chromadecks.ui.theme.Zinc950

internal data class TrackCustomizationState(
    val codeStyle: Int = 0,
    val showGradation: Boolean = false,
)

internal class TrackCustomizationSlide : ListSlideAdvanced<TrackCustomizationState>() {

    override val initialState get() = TrackCustomizationState()

    override val stateMutations
        get() = listOf<TrackCustomizationState.() -> TrackCustomizationState>(
            { copy(codeStyle = 1) },
            { copy(showGradation = true) },
        )

    @Composable
    override fun content(state: TrackCustomizationState) {
        var degree by remember { mutableFloatStateOf(135f) }

        val trackCode = when (state.codeStyle) {
            0 -> """
track = { dialState ->
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val radius = dialState.radius - 42.dp.toPx()
        val sweep = dialState.degreeRange.endInclusive - dialState.degreeRange.start
        drawArc(
            color = Neutral400,
            startAngle = dialState.startDegrees - 90f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(
                x = center.x - radius,
                y = center.y - radius,
            ),
            size = Size(
                width = radius * 2f,
                height = radius * 2f,
            ),
            style = Stroke(
                width = 8.dp.toPx(),
                cap = StrokeCap.Round,
            ),
        )
    }
}""".trimIndent()

            else -> """
track = { dialState ->
    Box(
        Modifier
            .fillMaxSize()
            .drawBehind {
                drawArc(
                    color = Neutral400,
                    startAngle = dialState.startDegrees,
                    sweepAngle = 270f,
                    radius = dialState.radius - 42.dp.toPx(),
                )
            }
    )
}""".trimIndent()
        }

        val gradationCode = """
drawEveryInterval(
    dialState = dialState,
    spacing = 9f,
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
                color = if (data.inActiveRange) Lime400
                        else Neutral400,
                start = Offset(0f, 0f),
                end = Offset(0f, 16.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}""".trimIndent()

        val tabs = buildList {
            add("CustomTrack.kt" to trackCode)
            add("Gradation.kt" to gradationCode)
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier.weight(1.4f),
                tabs = tabs,
                selectedTab = if (state.showGradation) 1 else 0,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Dial(
                    degree = degree,
                    onDegreeChange = { degree = it },
                    modifier = Modifier.size(400.dp),
                    startDegrees = 225f,
                    sweepDegrees = 270f,
                    thumb = { _ -> Box(Modifier.size(84.dp)) },
                    track = { dialState ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .drawBehind {
                                    drawArc(
                                        color = Zinc950,
                                        startAngle = dialState.startDegrees,
                                        sweepAngle = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                                        radius = dialState.radius - 42.dp.toPx(),
                                        strokeWidth = 32.dp,
                                    )
                                    drawArc(
                                        color = Lime500,
                                        startAngle = dialState.startDegrees,
                                        sweepAngle = dialState.degree + dialState.overshootDegrees,
                                        radius = dialState.radius - 42.dp.toPx(),
                                        strokeWidth = 24.dp,
                                    )
                                    if (state.showGradation) {
                                        drawEveryInterval(
                                            radius = dialState.radius - 34.dp.toPx(),
                                            startDegrees = dialState.startDegrees,
                                            sweepDegrees = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                                            spacing = 2f,
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
                                                        end = Offset(0f, 16.dp.toPx()),
                                                        strokeWidth = 1.dp.toPx(),
                                                        cap = StrokeCap.Round,
                                                    )
                                                }
                                            }
                                        }


                                        drawEveryInterval(
                                            radius = dialState.radius,
                                            startDegrees = dialState.startDegrees,
                                            sweepDegrees = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                                            spacing = 9f,
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
                                                        color = if (data.inActiveRange) Lime400 else Neutral400,
                                                        start = Offset(0f, 0f),
                                                        end = Offset(0f, 1.dp.toPx()),
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
                )
            }
        }
    }
}
