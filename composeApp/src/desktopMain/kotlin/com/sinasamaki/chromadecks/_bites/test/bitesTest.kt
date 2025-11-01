package com.sinasamaki.chromadecks._bites.test

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.ChromaTheme
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import kotlin.math.roundToInt

fun main() {
    singleWindowApplication(
        state = WindowState(
            size = DpSize(1080.dp / 2, 1920.dp / 2),
            position = WindowPosition.Aligned(alignment = Alignment.CenterEnd)
        ),
        title = "BitesTest"
    ) {
        DevelopmentEntryPoint {
            ChromaTheme {
                BitesTest()
            }
        }
    }
}

@Composable
private fun BitesTest(modifier: Modifier = Modifier) {
    var state: BiteDisplayState by remember { mutableStateOf(BiteDisplayState.Visual) }
    BiteDisplay(
        modifier = modifier
            .background(Zinc950)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        state = when (state) {
                            BiteDisplayState.Code -> BiteDisplayState.Visual
                            BiteDisplayState.Visual -> BiteDisplayState.Code
                        }
                    }
                )
            },
        state = state,
        code = {
            CodeBlock(
                code = """
            Box(
              modifier = Modifier
                .size(300.dp)
                .background(Green500)
            )
                """.trimIndent(),
                style = MaterialTheme.typography.labelLarge
            )
        },
        visual = {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(Green500)
            ) {
                Text("Hello", color = Black, style = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Composable
fun BiteDisplay(
    modifier: Modifier = Modifier,
    state: BiteDisplayState,
    code: @Composable () -> Unit,
    visual: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
    ) {



        val visualBlur by animateDpAsState(
            targetValue = when (state) {
                BiteDisplayState.Visual -> 0.dp
                BiteDisplayState.Code -> 40.dp
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        val visualAlpha by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Visual -> 1f
                BiteDisplayState.Code -> .3f
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        val visualScale by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Visual -> 1f
                BiteDisplayState.Code -> .8f
            },
            animationSpec = when (state) {
                BiteDisplayState.Visual -> spring(stiffness = Spring.StiffnessLow)
                BiteDisplayState.Code -> spring(stiffness = Spring.StiffnessVeryLow)
            }
        )

        val visualTranslate by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Visual -> 0f
                BiteDisplayState.Code -> -300f
            },
            animationSpec = when (state) {
                BiteDisplayState.Visual -> spring(stiffness = Spring.StiffnessLow)
                BiteDisplayState.Code -> spring(stiffness = Spring.StiffnessVeryLow)
            }
        )



        val codeBlur by animateDpAsState(
            targetValue = when (state) {
                BiteDisplayState.Code -> 0.dp
                BiteDisplayState.Visual -> 30.dp
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        val codeAlpha by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Code -> 1f
                BiteDisplayState.Visual -> .3f
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        val codeScale by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Code -> 1f
                BiteDisplayState.Visual -> 1.3f
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        val codeTranslate by animateFloatAsState(
            targetValue = when (state) {
                BiteDisplayState.Code -> 0f
                BiteDisplayState.Visual -> 100f
            },
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        Box(
            Modifier
                .fillMaxSize()
                .scale(visualScale)
                .offset { IntOffset(0, visualTranslate.roundToInt()) }
                .alpha(visualAlpha)
                .blur(
                    visualBlur,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                ),
            contentAlignment = Alignment.Center,
        ) {
            visual()
        }

        Box(
            Modifier
                .fillMaxSize()
                .scale(codeScale)
                .offset { IntOffset(0, codeTranslate.roundToInt()) }
                .alpha(codeAlpha)
                .blur(
                    codeBlur,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            code()
        }
    }
}

sealed class BiteDisplayState {
    data object Code : BiteDisplayState()
    data object Visual : BiteDisplayState()
}