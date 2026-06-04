package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Orange500

class WhyTitleCardState
class WhyTitleCard : ListSlideAdvanced<WhyTitleCardState>() {

    override val initialState: WhyTitleCardState
        get() = WhyTitleCardState()

    @Composable
    override fun content(state: WhyTitleCardState) {
        TitleCardFrame(
            title = "Why?",
            description = "",
            backgroundColor = Indigo500,
            borderColor = Orange500,
        )
    }
}
