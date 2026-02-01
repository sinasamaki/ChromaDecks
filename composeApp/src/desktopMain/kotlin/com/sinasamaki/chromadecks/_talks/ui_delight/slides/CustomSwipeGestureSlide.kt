package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.SmallClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.WithoutAnimations
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class CustomSwipeGestureSlideState
class CustomSwipeGestureSlide: ListSlideAdvanced<CustomSwipeGestureSlideState>() {

    override val initialState: CustomSwipeGestureSlideState
        get() = CustomSwipeGestureSlideState()

    @Composable
    override fun content(state: CustomSwipeGestureSlideState) {
        ListItemDisplay(
            code = """
                    val state = rememberSwipeToDismissBoxState()
                    val willTrigger by remember {
                        derivedStateOf {
                            state.targetValue != SwipeToDismissBoxValue.Settled
                        }
                    }
                    
                    ...
                    
                    .dropShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) .2f else 0f
                    }
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) 1f else .2f
                    }
                """.trimIndent()
        ) {
            WithoutAnimations()
        }
    }
}
