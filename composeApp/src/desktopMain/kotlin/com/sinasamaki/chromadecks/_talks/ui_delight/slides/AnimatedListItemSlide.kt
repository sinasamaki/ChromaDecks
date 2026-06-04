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

data class AnimatedListItemSlideState(
    val tabIndex: Int = 0,
)
class AnimatedListItemSlide: ListSlideAdvanced<AnimatedListItemSlideState>() {

    override val initialState: AnimatedListItemSlideState
        get() = AnimatedListItemSlideState()

    override val stateMutations: List<AnimatedListItemSlideState.() -> AnimatedListItemSlideState>
        get() = listOf(
            { copy(tabIndex = 1) },
            { copy(tabIndex = 2) },
        )

    @Composable
    override fun content(state: AnimatedListItemSlideState) {
        ListItemDisplay(
            tabIndex = state.tabIndex,
            tabs = listOf(
                "AsState.kt" to """
                    val selectedAnimation by animateFloatAsState(
                        targetValue = if (selected) .4f else 0f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow
                        )
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
                        color = Blue950.copy(alpha = selectedAnimation),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        color = Blue500
                        radius = lerp(0f, 40f, focusAnimation)
                        alpha = lerp(0f, 1f, focusAnimation)
                    }
                """.trimIndent(),
                "Visibility.kt" to """
                    AnimatedVisibility(
                        visible = selected,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut(),
                    ) {
                        SelectedIcon()
                    }
                """.trimIndent(),
                "CustomAnimation.kt" to """
                    val rotation by transition.animateFloat { state ->
                        when (state) {
                            EnterExitState.PreEnter -> -10f
                            EnterExitState.Visible -> 0f
                            EnterExitState.PostExit -> 10f
                        }
                    }
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(rotation),
                    )
                """.trimIndent(),
            )
        ) {
            AnimatedListItem()
        }
    }
}
