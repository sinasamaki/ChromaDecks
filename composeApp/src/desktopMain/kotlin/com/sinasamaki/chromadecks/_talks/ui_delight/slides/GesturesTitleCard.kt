package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

class GesturesTitleCardState
class GesturesTitleCard: ListSlideAdvanced<GesturesTitleCardState>() {

    override val initialState: GesturesTitleCardState
        get() = GesturesTitleCardState()

    @Composable
    override fun content(state: GesturesTitleCardState) {
        TitleCardFrame(
            title = "Gestures",
            description = "Custom touch interactions",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }
}
