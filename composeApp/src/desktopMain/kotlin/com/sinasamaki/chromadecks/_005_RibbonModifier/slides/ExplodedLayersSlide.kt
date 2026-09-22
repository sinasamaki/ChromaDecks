package com.sinasamaki.chromadecks._005_RibbonModifier.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.SPECTRUM
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonExploded
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Zinc500

internal data class ExplodedLayersState(
    /** False is the single-pass ribbon: everything drawn over the row, like a spiral on top. */
    val sandwiched: Boolean,
    val rotationX: Float,
    val spacing: Dp,
    val planes: Float,
    val tint: Float,
)

/**
 * The centrepiece: the ribbon pulled apart into one plane per half turn, with the row on the
 * plane in the middle, then collapsed back down so it reads as wrapped.
 */
internal class ExplodedLayersSlide : ListSlideAdvanced<ExplodedLayersState>() {

    override val initialState: ExplodedLayersState
        get() = ExplodedLayersState(
            sandwiched = false,
            rotationX = 0f,
            spacing = 0.dp,
            planes = 0f,
            tint = 0f,
        )

    override val stateMutations: List<ExplodedLayersState.() -> ExplodedLayersState>
        get() = listOf(
            { copy(sandwiched = true) },
            { copy(rotationX = 58f, spacing = 104.dp, planes = 1f) },
            { copy(tint = 1f) },
            { copy(rotationX = 0f, spacing = 0.dp, planes = 0f, tint = 0f) },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeOut().blurOut()) { content() }
        }

    @Composable
    override fun content(state: ExplodedLayersState) {
        val rotationX by animateFloatAsState(
            targetValue = state.rotationX,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "rotationX",
        )
        val spacing by animateDpAsState(
            targetValue = state.spacing,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
            label = "spacing",
        )
        val planes by animateFloatAsState(
            targetValue = state.planes,
            animationSpec = tween(durationMillis = 700),
            label = "planes",
        )
        val tint by animateFloatAsState(
            targetValue = state.tint,
            animationSpec = tween(durationMillis = 700),
            label = "tint",
        )

        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            RibbonExploded(
                rotationX = rotationX,
                spacing = spacing,
                width = 420.dp,
                height = 112.dp,
                colors = SPECTRUM,
                stroke = 20.dp,
                loops = 3,
                sandwiched = state.sandwiched,
                planes = planes,
                tint = tint,
            ) {
                HabitRow(modifier = Modifier.fillMaxSize())
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .alpha(tint),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LegendDot(color = Indigo500, label = "behind the row")
                LegendDot(color = Rose500, label = "in front of it")
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(18.dp).background(color, CircleShape))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Zinc500,
        )
    }
}
