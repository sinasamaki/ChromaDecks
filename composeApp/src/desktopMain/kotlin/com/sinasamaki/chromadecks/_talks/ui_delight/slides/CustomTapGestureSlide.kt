package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.CustomClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.SmallClickArea
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class CustomTapGestureSlideState
class CustomTapGestureSlide: ListSlideAdvanced<CustomTapGestureSlideState>() {

    override val initialState: CustomTapGestureSlideState
        get() = CustomTapGestureSlideState()

    @Composable
    override fun content(state: CustomTapGestureSlideState) {
        ListItemDisplay(
            code = """
                    val interaction = remember { 
                        MutableInteractionSource() 
                    }
                    val isHovered by interaction.collectIsHoveredAsState()
                    val isPressed by interaction.collectIsPressedAsState()
                    Row(
                        modifier = modifier
                            .hoverable(interaction)
                            .clickable(
                                interactionSource = interaction,
                                indication = null
                            ) {
                                selected = !selected
                            }
                            
                    ...
                    
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        color = Blue500
                        radius = when {
                            isPressed -> 50f
                            isHovered -> 30f
                            else -> 0f
                        }
                        alpha = when {
                            isPressed -> .8f
                            isHovered -> .4f
                            else -> 0f
                        }
                    }
                """.trimIndent()
        ) {
            CustomClickArea()
        }
    }
}
