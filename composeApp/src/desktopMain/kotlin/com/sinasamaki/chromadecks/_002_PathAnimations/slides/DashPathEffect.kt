package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.ChromaSlider
import com.sinasamaki.chromadecks.ui.components.circler
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow300

internal data class DashPathEffectState(
    val useLength: Boolean = false,
)

internal class DashPathEffect : ListSlideAdvanced<DashPathEffectState>() {

    override val initialState: DashPathEffectState
        get() = DashPathEffectState()

    override val stateMutations: List<DashPathEffectState.() -> DashPathEffectState>
        get() = listOf(
            { copy(useLength = true) }
        )

    @Composable
    override fun content(state: DashPathEffectState) {

        var dash by remember { mutableStateOf(100f) }
        var space by remember { mutableStateOf(100f) }
        var phase by remember { mutableStateOf(0f) }
        var pathLength by remember { mutableStateOf(1f) }

        CodeBlockCarousel(
            codeBlocks = listOf(
                CodeBlockData(
                    code = """
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                ${dash.toInt()}f, ${space.toInt()}f,
                            ),
                            phase = ${phase.toInt()}f
                        )
                    """.trimIndent(),
                    implementation = {
                        val points by remember {
                            derivedStateOf {
                                buildList {
//                                    add(Offset(-100f, 10f))
                                    add(Offset(10f, 0f))
                                    add(Offset(10f, 10f))
                                    add(Offset(10f, 90f))
                                    add(Offset(30f, 90f))
                                    add(Offset(30f, 20f))
                                    add(Offset(60f, 20f))
                                    add(Offset(60f, 80f))
                                    add(Offset(80f, 80f))
                                    add(Offset(80f, 40f))
                                    add(Offset(100f, 40f))
//                                    add(Offset(200f, 40f))
//                                    for (i in 0..100) {
//                                        add(
//                                            Offset(
//                                                x = (Random.nextFloat() * (i / 10f)) * 100,
//                                                y = (Random.nextFloat() * (i / 10f)) * 100,
//                                            )
//                                        )
//                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RectangleShape)
                                    .drawCartesianGrid(
                                        color = Slate50
                                    )
                                    .drawBehind {
                                        val path = scalePath(
                                            path = Path().apply {
                                                points.forEachIndexed { index, it ->
                                                    if (index == 0)
                                                        moveTo(it)
                                                    else
                                                        lineTo(it)
                                                }
                                            },
                                        )

                                        val measure = PathMeasure()
                                        measure.setPath(path, false)
                                        pathLength = measure.length

                                        drawPath(
                                            path = path,
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Green500, Yellow300, Orange600
                                                )
                                            ),
                                            style = Stroke(
                                                width = 50f,
                                                join = StrokeJoin.Round,
                                                cap = StrokeCap.Round,
                                                pathEffect = if (dash != 0f && space != 0f) {
                                                    PathEffect.dashPathEffect(
                                                        intervals = floatArrayOf(
                                                            dash, space,
                                                        ),
                                                        phase = phase
                                                    )
                                                } else null

                                            )
                                        )
                                    }
                            )
                            Spacer(Modifier.height(16.dp))

                            Row {

                                SliderWithLabel(
                                    modifier = Modifier.weight(1f),
                                    label = "Dash",
                                    value = dash,
                                    onValueChange = { dash = it },
                                    valueRange = 1f..if (state.useLength) pathLength else 200f,
                                )

                                SliderWithLabel(
                                    modifier = Modifier.weight(1f),
                                    label = "Space",
                                    value = space,
                                    onValueChange = { space = it },
                                    valueRange = 1f..if (state.useLength) pathLength else 200f,
                                )
                            }

                            SliderWithLabel(
                                label = "Phase",
                                value = phase,
                                onValueChange = { phase = it },
                                valueRange = 0f..if (state.useLength) pathLength else 600f,
                            )
                        }

                    }
                )
            ),
            currentBlock = 0
        )

    }

}

@Composable
fun SliderWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: (Float) -> String = { "${it.toInt()}" },
) {
    Column(
        modifier = modifier
    ) {
        Text(
            label,
            modifier = Modifier
                .circler(
                    Slate50
                )
                .padding(horizontal = 4.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.labelMedium
        )
        ChromaSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            displayValue = displayValue
        )
    }
}
