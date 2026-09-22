package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.SPECTRUM
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.scaleOut

internal data class RibbonHeroState(val wrapped: Boolean)

/** Opens on the finished effect: this is the thing we are about to build. */
internal class RibbonHeroSlide : ListSlideAdvanced<RibbonHeroState>() {

    override val initialState get() = RibbonHeroState(wrapped = true)

    override val stateMutations: List<RibbonHeroState.() -> RibbonHeroState>
        get() = listOf(
            { copy(wrapped = false) },
            { copy(wrapped = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).scaleOut(initial = .85f).fadeOut()) { content() }
        }

    @Composable
    override fun content(state: RibbonHeroState) {
        val progress by animateFloatAsState(
            targetValue = if (state.wrapped) 1f else 0f,
            animationSpec = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            label = "hero-progress",
        )

        Box(
            modifier = Modifier.fillMaxSize().padding(160.dp),
            contentAlignment = Alignment.Center,
        ) {
            HabitRow(
                modifier = Modifier
                    .width(540.dp)
                    .height(132.dp)
                    .ribbon(
                        colors = SPECTRUM,
                        stroke = 22.dp,
                        loops = 4,
                        progress = { progress },
                    ),
            )
        }
    }
}
