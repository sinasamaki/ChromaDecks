package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Rose800
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Zinc900


internal data class FunctionSlideState(
    val show: Boolean,
    val functionSignature: String,
)

private val partial = """
    drawVertices
""".trimIndent()
private val full = """
    drawVertices(
        vertices: Vertices,
        blendMode: BlendMode,
        paint: Paint,
    ): Unit
""".trimIndent()

internal class FunctionSlide : ListSlide<FunctionSlideState>() {

    override val states: List<FunctionSlideState>
        get() = listOf(
            FunctionSlideState(
                show = false,
                functionSignature = partial,
            ),
            FunctionSlideState(
                show = true,
                functionSignature = partial,
            ),
            FunctionSlideState(
                show = true,
                functionSignature = full,
            )
        )

    @Composable
    override fun content(state: FunctionSlideState) {

        val infinite = rememberInfiniteTransition()

//        val t by infinite.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(
//                    durationMillis = 3000
//                ),
//                repeatMode = RepeatMode.Restart
//            )
//        )

        val t by animateFloatAsState(
            targetValue = if (state.show) 1f else 0f,
            animationSpec = tween(
                durationMillis = 3000
            ),
        )

        Box(
            Modifier
                .clip(RectangleShape)
                .meshGradient(
                    points = listOf(
                        listOf(
                            Offset(0f, -1f) to Slate50,
                            Offset(.5f, -1f) to Slate50,
                            Offset(1f, -1f) to Slate50,
                        ),
                        listOf(
                            Offset(0f, lerp(1f, -3f, t)) to Slate50,
                            Offset(.5f, lerp(1f, -3f, t)) to Slate50,
                            Offset(1f, lerp(1f, -3f, t)) to Slate50,
                        ),
                        listOf(
                            Offset(0f, lerp(1.1f, -.7f, t)) to Yellow500,
                            Offset(.5f, lerp(1.5f, -2.8f, t)) to Yellow500,
                            Offset(1f, lerp(1.1f, -.9f, t)) to Yellow500,
                        ),
                        listOf(
                            Offset(0f, lerp(1.2f, -.2f, t)) to Rose800,
                            Offset(.5f, lerp(1.6f, -1.5f, t)) to Rose800,
                            Offset(1f, lerp(1.2f, -.9f, t)) to Rose800,
                        ),
                        listOf(
                            Offset(0f, lerp(1.5f, 0f, t)) to Zinc900,
                            Offset(.5f, lerp(1.7f, -.5f, t)) to Zinc900,
                            Offset(1f, lerp(1.3f, 0f, t)) to Zinc900,
                        ),
                        listOf(
                            Offset(0f, 2f) to Zinc900,
                            Offset(.5f, 2f) to Zinc900,
                            Offset(1f, 2f) to Zinc900,
                        )

                    ),
                    resolutionX = 32,
                    resolutionY = 32,
                )
                .fillMaxSize()
                .padding(start = 128.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            CodeBlock(
                modifier = Modifier.fillMaxWidth(),
                code = state.functionSignature,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Slate50
                ),
                highlightAnimation = false,
                useIndexAsKey = true,
            )
        }
    }
}