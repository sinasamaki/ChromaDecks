package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class WhySlideState
class WhySlide: ListSlideAdvanced<WhySlideState>() {

    override val initialState: WhySlideState
        get() = WhySlideState()

    @Composable
    override fun content(state: WhySlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}
