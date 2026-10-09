package com.sinasamaki.chromadecks._006_SpinAnimation.slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._006_SpinAnimation.components.HabitLabel
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SlideHeading
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinButton
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

internal data class SpinHeroState(
    val constraints: Boolean,
)

internal class SpinHeroSlide : ListSlideAdvanced<SpinHeroState>() {

    override val initialState get() = SpinHeroState(constraints = false)

    override val stateMutations: List<SpinHeroState.() -> SpinHeroState>
        get() = listOf(
            { copy(constraints = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: SpinHeroState) {
        val constraints by animateFloatAsState(
            targetValue = if (state.constraints) 1f else 0f,
            animationSpec = tween(durationMillis = 500),
            label = "constraints",
        )

        Box(
            modifier = Modifier.fillMaxSize().padding(120.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(96.dp),
            ) {
                SpinButton(
                    modifier = Modifier.width(640.dp).height(200.dp),
                ) {
                    HabitLabel(text = "spin me", fontSize = 64.sp)
                }

                Row(
                    modifier = Modifier.alpha(constraints),
                    horizontalArrangement = Arrangement.spacedBy(72.dp),
                ) {
                    SlideHeading("always a pill")
                    SlideHeading("one axis")
                }
            }
        }
    }
}
