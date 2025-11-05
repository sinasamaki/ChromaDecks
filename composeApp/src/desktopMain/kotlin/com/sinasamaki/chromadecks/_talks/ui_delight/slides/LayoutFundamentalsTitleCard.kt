package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

class LayoutFundamentalsTitleCardState
class LayoutFundamentalsTitleCard: ListSlideAdvanced<LayoutFundamentalsTitleCardState>() {

    override val initialState: LayoutFundamentalsTitleCardState
        get() = LayoutFundamentalsTitleCardState()

    @Composable
    override fun content(state: LayoutFundamentalsTitleCardState) {
        TitleCardFrame(
            title = "Layout Fundamentals",
            description = "Understanding Compose layout system",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }
}
