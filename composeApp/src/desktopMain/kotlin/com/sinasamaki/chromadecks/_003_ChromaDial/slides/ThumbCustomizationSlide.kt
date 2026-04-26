package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Neutral400
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Zinc900

internal class ThumbCustomizationSlide : ListSlideAdvanced<Unit>() {

    override val initialState get() = Unit

    @Composable
    override fun content(state: Unit) {
        var degree by remember { mutableFloatStateOf(0f) }

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier.weight(1f),
                tabs = listOf(
                    "CustomThumb.kt" to """
thumb = { _ ->
    Canvas(
        modifier = Modifier.size(48.dp)
    ) {
        drawCircle(
            color = Zinc900,
        )
        drawCircle(
            brush = Brush.sweepGradient(
                listOf(
                    Lime400,
                    Sky400,
                    Rose400,
                    Violet400,
                    Lime400,
                )
            ),
            style = Stroke(
                width = 3.dp.toPx()
            ),
        )
    }
}
                    """.trimIndent(),
                ),
                selectedTab = 0,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                val thumbSize = 48.dp

                Dial(
                    degree = degree,
                    onDegreeChange = { degree = it },
                    modifier = Modifier.size(280.dp),
                    startDegrees = 135f,
                    sweepDegrees = 270f,
                    thumb = { _ ->
                        Canvas(modifier = Modifier.size(thumbSize)) {
                            drawCircle(color = Zinc900)
                            drawCircle(
                                brush = Brush.sweepGradient(
                                    listOf(Lime400, Sky400, Rose400, Violet400, Lime400)
                                ),
                                style = Stroke(width = 3.dp.toPx()),
                            )
                        }
                    },
                    track = { dialState ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .drawBehind {
                                    drawArc(
                                        color = Neutral400,
                                        radius = dialState.radius - (thumbSize.toPx() / 2),
                                        startAngle = dialState.startDegrees,
                                        sweepAngle = dialState.degreeRange.endInclusive - dialState.degreeRange.start,
                                    )
                                    drawArc(
                                        color = Lime500,
                                        radius = dialState.radius - (thumbSize.toPx() / 2),
                                        startAngle = dialState.startDegrees,
                                        sweepAngle = dialState.degree + dialState.overshootDegrees,
                                    )
                                }
                        )
                    },
                )
            }
        }
    }
}




