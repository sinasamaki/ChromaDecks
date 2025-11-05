package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

class ModifiersTitleCardState
class ModifiersTitleCard: ListSlideAdvanced<ModifiersTitleCardState>() {

    override val initialState: ModifiersTitleCardState
        get() = ModifiersTitleCardState()

    @Composable
    override fun content(state: ModifiersTitleCardState) {
        TitleCardFrame(
            title = "Modifiers",
            description = "Building blocks of UI customization",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }
}
