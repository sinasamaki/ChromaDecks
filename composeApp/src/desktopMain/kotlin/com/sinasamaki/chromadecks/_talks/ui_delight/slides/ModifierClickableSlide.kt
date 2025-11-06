package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.BigClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.JustRightClickArea
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks._talks.ui_delight.components.SmallClickArea
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class ModifierClickableSlideState
class ModifierClickableSlide : ListSlideAdvanced<ModifierClickableSlideState>() {

    override val initialState: ModifierClickableSlideState
        get() = ModifierClickableSlideState()

    @Composable
    override fun content(state: ModifierClickableSlideState) {
        Column {
            ListItemDisplay(
                modifier = Modifier.weight(1f),
                code = """
                    .background(
                        color = Zinc900,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
                    .clickable { selected = !selected },
                """.trimIndent()
            ) {
                SmallClickArea()
            }
            ListItemDisplay(
                modifier = Modifier.weight(1f),
                code = """
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable { selected = !selected }
                        .padding(4.dp)
                """.trimIndent()
            ) {
                BigClickArea()
            }
            ListItemDisplay(
                modifier = Modifier.weight(1f),
                code = """
                    .background(
                        color = Zinc900,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { selected = !selected }
                    .padding(16.dp),
                """.trimIndent()
            ) {
                JustRightClickArea()
            }
        }
    }
}
