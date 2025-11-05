package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

internal class TitleSlideState
internal class TitleSlide : ListSlideAdvanced<TitleSlideState>() {
    override val initialState: TitleSlideState
        get() = TitleSlideState()

    @Composable
    override fun content(state: TitleSlideState) {
        TitleCardFrame(
            title = "Title",
            description = "background",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }

}