package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Orange500

class AccessibilityTitleCardState
class AccessibilityTitleCard : ListSlideAdvanced<AccessibilityTitleCardState>() {

    override val initialState: AccessibilityTitleCardState
        get() = AccessibilityTitleCardState()

    @Composable
    override fun content(state: AccessibilityTitleCardState) {
        TitleCardFrame(
            title = "Accessibility",
            description = "",
            backgroundColor = Indigo500,
            borderColor = Orange500,
        )
    }
}
