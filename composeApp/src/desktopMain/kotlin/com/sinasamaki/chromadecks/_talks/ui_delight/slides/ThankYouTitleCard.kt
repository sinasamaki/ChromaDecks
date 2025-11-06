package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Purple600
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Yellow600
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc950

class ThankYouTitleCardState
class ThankYouTitleCard: ListSlideAdvanced<ThankYouTitleCardState>() {

    override val initialState: ThankYouTitleCardState
        get() = ThankYouTitleCardState()

    @Composable
    override fun content(state: ThankYouTitleCardState) {
        TitleCardFrame(
            title = "sinasamaki.com",
            description = "",
            backgroundColor = Zinc950,
            borderColor = Zinc700,
        )
    }
}
