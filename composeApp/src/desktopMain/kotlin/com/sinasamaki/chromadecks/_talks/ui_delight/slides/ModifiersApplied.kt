package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.subtitle
import com.sinasamaki.chromadecks._talks.ui_delight.components.time
import com.sinasamaki.chromadecks._talks.ui_delight.components.title
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc500
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc900

private const val BackgroundStep = 1
private const val PaddingStep = 2
private const val SpacingStep = 3

data class ModifierAppliedSlideState(
    val step: Int = 0,
)

class ModifierAppliedSlide : ListSlideAdvanced<ModifierAppliedSlideState>() {

    override val initialState: ModifierAppliedSlideState
        get() = ModifierAppliedSlideState()

    override val stateMutations: List<ModifierAppliedSlideState.() -> ModifierAppliedSlideState>
        get() = listOf(
            { copy(step = BackgroundStep) },
            { copy(step = PaddingStep) },
            { copy(step = SpacingStep) },
        )

    @Composable
    override fun content(state: ModifierAppliedSlideState) {
        ListItemDisplay(
            tabs = listOf("ListItem.kt" to listItemCode(state.step)),
        ) {
            ModifierBuildListItem(step = state.step)
        }
    }
}

@Composable
private fun ModifierBuildListItem(step: Int) {
    val background by animateFloatAsState(
        targetValue = if (step >= BackgroundStep) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
    )
    val padding by animateDpAsState(
        targetValue = if (step >= PaddingStep) 16.dp else 0.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioNoBouncy,
        ),
    )
    val spacing by animateFloatAsState(
        targetValue = if (step >= SpacingStep) 1f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy,
        ),
    )

    val shape = RoundedCornerShape(24.dp)
    val vertical = BiasAlignment.Vertical(lerp(-1f, 0f, spacing))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f * background),
                        Zinc100.copy(alpha = .1f * background),
                    )
                ),
                shape = shape,
            )
            .background(
                color = Zinc900.copy(alpha = background),
                shape = shape,
            )
            .padding(padding),
    ) {
        Box(
            modifier = Modifier
                .align(vertical)
                .size(56.dp)
                .background(
                    color = Zinc600,
                )
        )
        Spacer(Modifier.width(10.dp * spacing.coerceAtLeast(0f)))
        Column(
            modifier = Modifier.align(vertical)
        ) {
            Text(
                text = title,
                color = Zinc200,
            )
            Spacer(Modifier.height(4.dp * spacing.coerceAtLeast(0f)))
            Text(
                text = subtitle,
                color = Zinc400,
            )
        }
        Spacer(Modifier.width(8.dp * spacing.coerceAtLeast(0f)))
        Box(
            modifier = Modifier
                .weight(1f)
                .align(vertical),
            contentAlignment = BiasAlignment(
                horizontalBias = lerp(-1f, 1f, spacing),
                verticalBias = 0f,
            ),
        ) {
            Text(
                text = time,
                color = Zinc500,
            )
        }
    }
}

private fun listItemCode(step: Int): String {
    val lines = mutableListOf<String>()
    fun add(minStep: Int, text: String) {
        if (step >= minStep) lines += text
    }

    add(0, "Row(")
    add(0, "    modifier = modifier")
    add(0, "        .fillMaxWidth()")
    add(BackgroundStep, "        .border(1.dp, borderBrush, shape)")
    add(BackgroundStep, "        .background(Zinc900, shape)")
    add(PaddingStep, "        .padding(16.dp)")
    lines += ") {"
    lines += "    Box(Modifier.size(56.dp).background(Zinc600))"
    add(SpacingStep, "    Spacer(Modifier.width(10.dp))")
    if (step >= SpacingStep) {
        lines += "    Column("
        lines += "        modifier = Modifier.weight(1f)"
        lines += "    ) {"
    } else {
        lines += "    Column {"
    }
    add(0, "        Text(title, color = Zinc200)")
    add(SpacingStep, "        Spacer(Modifier.height(4.dp))")
    add(0, "        Text(subtitle, color = Zinc400)")
    add(0, "    }")
    add(SpacingStep, "    Spacer(Modifier.width(8.dp))")
    add(0, "    Text(time, color = Zinc500)")
    add(0, "}")
    return lines.joinToString("\n")
}
