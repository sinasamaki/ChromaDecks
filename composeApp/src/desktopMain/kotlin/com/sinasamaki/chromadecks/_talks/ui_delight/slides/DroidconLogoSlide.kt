package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.sinasamaki.chromadecks._talks.ui_delight.components.DroidconLogo3D
import com.sinasamaki.chromadecks._talks.ui_delight.components.DroidconLogoColors
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Emerald200
import com.sinasamaki.chromadecks.ui.theme.Green600
import com.sinasamaki.chromadecks.ui.theme.Sky950
import kotlinx.coroutines.delay

class DroidconLogoSlideState
class DroidconLogoSlide : ListSlideAdvanced<DroidconLogoSlideState>() {

    override val initialState: DroidconLogoSlideState
        get() = DroidconLogoSlideState()

    @Composable
    override fun content(state: DroidconLogoSlideState) {
        var rotation by remember { mutableFloatStateOf(0f) }
        var tilt by remember { mutableFloatStateOf(-18f) }
        val rightAntennaDirection = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                repeat(10) {
                    rightAntennaDirection.animateTo(
                        targetValue = -10f,
                        animationSpec = tween(120, easing = EaseInOutSine),
                    )
                    rightAntennaDirection.animateTo(
                        targetValue = 10f,
                        animationSpec = tween(120, easing = EaseInOutSine),
                    )
                }
                rightAntennaDirection.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(120, easing = EaseInOutSine),
                )
                delay(2000)
            }
        }

        DroidconLogo3D(
            rotation = rotation,
            tilt = tilt,
            colors = DroidconLogoColors(
                base = Green600,
                shadow = Sky950,
                highlight = Emerald200,
            ),
            rightAntennaDirectionOffset = rightAntennaDirection.value,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF101321),
                            Color(0xFF0B2068),
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f),
                    )
                )
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        rotation += dragAmount.x * 0.4f
                        tilt = (tilt - dragAmount.y * 0.2f).coerceIn(-18f, 18f)
                    }
                },
        )
    }
}
