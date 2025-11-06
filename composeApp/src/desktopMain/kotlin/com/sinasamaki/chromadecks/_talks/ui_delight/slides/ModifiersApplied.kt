package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.BigClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.JustRightClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.SmallClickArea
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class ModifierAppliedSlideState
class ModifierAppliedSlide : ListSlideAdvanced<ModifierAppliedSlideState>() {

    override val initialState: ModifierAppliedSlideState
        get() = ModifierAppliedSlideState()

    @Composable
    override fun content(state: ModifierAppliedSlideState) {
        ListItemDisplay(
            code = """
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc100.copy(alpha = .3f),
                                Zinc100.copy(alpha = .1f),
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .background(
                        color = Zinc900,
                        shape = RoundedCornerShape(24.dp)
                    )
                    
                    ...
                    
                    Spacer(Modifier.width(10.dp))

                """.trimIndent()
        ) {
            SmallClickArea()
        }
    }
}
