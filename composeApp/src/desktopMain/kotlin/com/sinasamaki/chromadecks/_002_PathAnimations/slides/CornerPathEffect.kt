package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.drawScaledPath
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.ChromaSlider
import com.sinasamaki.chromadecks.ui.theme.Emerald400
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Lime50
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Pink500
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import kotlin.random.Random

class CornerPathEffectState()
class CornerPathEffect : ListSlideAdvanced<CornerPathEffectState>() {

    override val initialState: CornerPathEffectState
        get() = CornerPathEffectState()

    override val stateMutations: List<CornerPathEffectState.() -> CornerPathEffectState>
        get() = listOf()

    @Composable
    override fun content(state: CornerPathEffectState) {

        var radius by remember { mutableStateOf(0f) }

        CodeBlockCarousel(
            codeBlocks = listOf(
                CodeBlockData(
                    code = """
                        drawPath(
                            path = path,
                            color = Pink500,
                            style = Stroke(
                                width = 30f,
                                pathEffect = PathEffect.cornerPathEffect(
                                    radius = ${radius.toInt()}f
                                )
                            )
                        )
                    """.trimIndent(),
                    implementation = {
                        val points by remember {
                            derivedStateOf {
                                buildList {
                                    add(Offset(-100f, 10f))
                                    add(Offset(10f, 10f))
                                    add(Offset(10f, 90f))
                                    add(Offset(30f, 90f))
                                    add(Offset(30f, 20f))
                                    add(Offset(60f, 20f))
                                    add(Offset(60f, 80f))
                                    add(Offset(80f, 80f))
                                    add(Offset(80f, 40f))
                                    add(Offset(200f, 40f))
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

                                        drawPath(
                                            path = path,
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Green500, Yellow300, Orange600
                                                )
                                            ),
                                            style = Stroke(
                                                width = 100f,
//                                                cap = StrokeCap.Round,
                                                pathEffect = when (radius) {
                                                    0f -> null
                                                    else -> PathEffect.cornerPathEffect(
                                                        radius = radius
                                                    )
                                                }
                                            )
                                        )
                                    }
                            )
                            Spacer(Modifier.height(16.dp))

                            Text(
                                "Radius",
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .align(Alignment.Start),
                                style = MaterialTheme.typography.labelMedium
                            )
                            ChromaSlider(
                                value = radius,
                                onValueChange = {
                                    radius = it
                                },
                                valueRange = 0f..200f,
                                displayValue = {
                                    "${it.toInt()}"
                                }
                            )
                        }

                    }
                )
            ),
            currentBlock = 0
        )

    }
}