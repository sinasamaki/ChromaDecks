package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.ui.components.CaratDisplay
import com.sinasamaki.chromadecks.ui.components.InfluenceCircle
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Purple300
import com.sinasamaki.chromadecks.ui.theme.Purple500

class CaratDisplaySlideState
class CaratDisplaySlide : ListSlideAdvanced<CaratDisplaySlideState>() {

    override val initialState: CaratDisplaySlideState
        get() = CaratDisplaySlideState()

    @Composable
    override fun content(state: CaratDisplaySlideState) {
        ListItemDisplay(
            tabs = listOf(
                "CaratDisplay.kt" to """
                    CaratDisplay(
                        modifier = Modifier.fillMaxSize(),
                        colors = listOf(Lime500, Purple500, Purple300),
                        circles = { size ->
                            listOf(
                                InfluenceCircle(
                                    center = size.center,
                                    radius = lerp(-200f, 900f, reveal),
                                )
                            )
                        }
                    )
                """.trimIndent()
            )
        ) {
            val reveal by rememberInfiniteTransition(label = "carat").animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "carat-reveal",
            )
            CaratDisplay(
                modifier = Modifier.fillMaxSize(),
                colors = listOf(Lime500, Purple500, Purple300),
                circles = { size ->
                    listOf(
                        InfluenceCircle(
                            center = size.center,
                            radius = size.center.x + lerp(-200f, 500f, reveal),
                        )
                    )
                }
            )
        }
    }
}
