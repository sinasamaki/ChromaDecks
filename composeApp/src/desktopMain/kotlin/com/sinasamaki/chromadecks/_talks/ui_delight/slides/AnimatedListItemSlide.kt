package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.AnimatedListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.SmallClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.WithoutAnimations
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class AnimatedListItemSlideState
class AnimatedListItemSlide: ListSlideAdvanced<AnimatedListItemSlideState>() {

    override val initialState: AnimatedListItemSlideState
        get() = AnimatedListItemSlideState()

    @Composable
    override fun content(state: AnimatedListItemSlideState) {
        ListItemDisplay(
            code = """
                    val selectedAnimation by animateFloatAsState(
                        targetValue = if (selected) 1f else 0f,
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    )
                    val focusAnimation by animateFloatAsState(
                        targetValue = when {
                            isPressed -> 1f
                            isHovered -> .5f
                            else -> 0f
                        }
                    )
                    
                    ....
                    
                    .background(
                        color = Blue950.copy(alpha = lerp(0f, .4f, selectedAnimation)),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        color = Blue500
                        radius = lerp(0f, 40f, focusAnimation)
                        alpha = lerp(0f, 1f, focusAnimation)
                    }
                    
                    ....
                    
                    AnimatedVisibility(
                        visible = selected,
                        enter = scaleIn(
                            initialScale = .1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow,
                            )
                        ) + fadeIn(),
                        exit = scaleOut(targetScale = .1f) + fadeOut(),
                    ) {
                        SelectedIcon()
                    }
                """.trimIndent()
        ) {
            AnimatedListItem()
        }
    }
}
