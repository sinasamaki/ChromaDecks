package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CaratDisplay
import com.sinasamaki.chromadecks.ui.components.InfluenceCircle
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlinx.coroutines.delay

internal data class IntroSlideState(val placeholder: Unit = Unit)

internal class IntroSlide : ListSlideAdvanced<IntroSlideState>() {

    override val initialState get() = IntroSlideState()

    @Composable
    override fun content(state: IntroSlideState) {
        val radius = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                radius.snapTo(0f)
                radius.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
                )
                delay(5000)
            }
        }
        CaratDisplay(
            Modifier
                .fillMaxSize(),
            aspectRatio = 3 / 4f,
            colors = listOf(
                Zinc300, Lime400, Green400,
            )
        ) {
//            val progress = StepsEasing(20).transform(radius.value)
            val progress = radius.value
            listOf(
//                InfluenceCircle(
//                    it.center + Offset(-500f * radius.value, 0f),
//                    radius = it.width * radius.value
//                ),
                InfluenceCircle(
                    Offset(lerp(it.width * .8f, it.width * 1.4f, progress), it.height * (1f - progress)),
                    radius = lerp(it.width, it.width * .3f, progress)
                ),
                InfluenceCircle(
                    Offset(
                        lerp(it.width, -it.width * .4f, progress),
                        lerp(it.height * 1.5f, 0f, progress)
                    ),
                    radius = lerp(it.width, it.width * .3f, progress)
                ),
                InfluenceCircle(
                    Offset(it.center.x, lerp(0f, it.height * 1.5f, progress)),
                    radius = lerp(it.width, it.width * .3f, progress)
                ),
            )
        }

        TitleFrame(
            modifier = Modifier.fillMaxSize(),
            title = "chromadial",
            description = "custom dial slider with\nsmooth animations and boundless customizations",
            hint = "chromadial.sinasamaki.com",
            bookNumber = 3,
            animationProgress = radius.value,
            contentColor = Zinc50,
        )

    }
}

