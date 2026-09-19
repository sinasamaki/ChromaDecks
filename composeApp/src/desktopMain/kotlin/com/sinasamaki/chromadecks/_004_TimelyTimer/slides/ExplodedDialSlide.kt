package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._004_TimelyTimer.components.ExplodedTimelyDial
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.slideanimations.translateOutY

internal data class ExplodedDialState(
    val rotationX: Float,
    val rotationZ: Float,
    val spacing: Dp,
)

internal class ExplodedDialSlide : ListSlideAdvanced<ExplodedDialState>() {

    override val initialState: ExplodedDialState
        get() = ExplodedDialState(
            rotationX = 0f,
            rotationZ = 0f,
            spacing = 0.dp,
        )

    override val stateMutations: List<ExplodedDialState.() -> ExplodedDialState>
        get() = listOf(
            {
                copy(
                    rotationX = 60f,
//                    rotationZ = 45f,
                    spacing = 128.dp,
                )
            },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(
                Modifier
                    .translateOutY(initial = -.1f)
                    .translateInX()
                    .parallax(1f)
                    .fadeOut()
                    .blurOut()
            ) {
                content()
            }
        }

    @Composable
    override fun content(state: ExplodedDialState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)

        val rotationX by animateFloatAsState(
            state.rotationX,
            spring(stiffness = Spring.StiffnessLow),
            label = "rotationX"
        )
        val rotationZ by animateFloatAsState(
            state.rotationZ,
            spring(stiffness = Spring.StiffnessLow),
            label = "rotationZ"
        )
        val spacing by animateDpAsState(
            state.spacing,
            spring(
                stiffness = Spring.StiffnessVeryLow,
            ),
            label = "spacing"
        )

        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            ExplodedTimelyDial(
                swatch = swatch,
                rotationX = rotationX,
                rotationY = 0f,
                rotationZ = rotationZ,
                spacing = spacing,
                modifier = Modifier
                    .fillMaxSize(.4f)
                    .aspectRatio(1f),
            )
        }
    }
}
