package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class LayoutFundamentalsSlideState
class LayoutFundamentalsSlide: ListSlideAdvanced<LayoutFundamentalsSlideState>() {

    override val initialState: LayoutFundamentalsSlideState
        get() = LayoutFundamentalsSlideState()

    @Composable
    override fun content(state: LayoutFundamentalsSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}
