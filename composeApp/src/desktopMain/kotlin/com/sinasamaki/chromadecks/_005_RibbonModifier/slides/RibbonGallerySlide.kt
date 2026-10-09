package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.COOL
import com.sinasamaki.chromadecks._005_RibbonModifier.DUSK
import com.sinasamaki.chromadecks._005_RibbonModifier.RIBBON_COLORS
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.theme.Zinc500
import com.sinasamaki.chromadecks.ui.theme.Zinc900

private data class GalleryRow(
    val label: String,
    val colors: List<Color>,
    val loops: Int,
    val delay: Int,
)

private val ROWS = listOf(
    GalleryRow("Morning run", RIBBON_COLORS, loops = 4, delay = 0),
    GalleryRow("Read 10 pages", COOL, loops = 3, delay = 400),
    GalleryRow("Drink water", DUSK, loops = 5, delay = 800),
)

internal data class RibbonGalleryState(
    val wrapped: Boolean,
    val callToAction: Boolean,
)

internal class RibbonGallerySlide : ListSlideAdvanced<RibbonGalleryState>() {

    override val initialState
        get() = RibbonGalleryState(wrapped = true, callToAction = false)

    override val stateMutations: List<RibbonGalleryState.() -> RibbonGalleryState>
        get() = listOf(
            { copy(callToAction = true) },
            { copy(wrapped = false) },
            { copy(wrapped = true) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).fadeIn().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: RibbonGalleryState) {
        val callToAction by animateFloatAsState(
            targetValue = if (state.callToAction) 1f else 0f,
            animationSpec = tween(durationMillis = 700),
            label = "cta",
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 96.dp, vertical = 72.dp),
            verticalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ROWS.forEach { row ->
                val progress by animateFloatAsState(
                    targetValue = if (state.wrapped) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 2400,
                        delayMillis = if (state.wrapped) row.delay else 0,
                        easing = FastOutSlowInEasing,
                    ),
                    label = "gallery-${row.label}",
                )

                HabitRow(
                    label = row.label,
                    modifier = Modifier
                        .width(500.dp)
                        .height(124.dp)
                        .ribbon(
                            colors = row.colors,
                            stroke = 18.dp,
                            loops = row.loops,
                            progress = { progress },
                        ),
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp).alpha(callToAction),
            ) {
                Text(
                    text = "Download SubAtomic",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Zinc900,
                )
                Text(
                    text = "habits, one atom at a time",
                    style = MaterialTheme.typography.labelLarge,
                    color = Zinc500,
                )
            }
        }
    }
}
