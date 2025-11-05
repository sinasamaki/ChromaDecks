package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

class HapticFeedbackTitleCardState
class HapticFeedbackTitleCard: ListSlideAdvanced<HapticFeedbackTitleCardState>() {

    override val initialState: HapticFeedbackTitleCardState
        get() = HapticFeedbackTitleCardState()

    @Composable
    override fun content(state: HapticFeedbackTitleCardState) {
        TitleCardFrame(
            title = "Haptic Feedback",
            description = "Tactile user experience",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }
}
