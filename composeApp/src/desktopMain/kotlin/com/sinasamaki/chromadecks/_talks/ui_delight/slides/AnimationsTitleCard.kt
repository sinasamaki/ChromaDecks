package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400

class AnimationsTitleCardState
class AnimationsTitleCard: ListSlideAdvanced<AnimationsTitleCardState>() {

    override val initialState: AnimationsTitleCardState
        get() = AnimationsTitleCardState()

    @Composable
    override fun content(state: AnimationsTitleCardState) {
        TitleCardFrame(
            title = "Animations",
            description = "Bringing UI to life",
            backgroundColor = Color(0xff_3030ff),
            borderColor = Lime400,
        )
    }
}
