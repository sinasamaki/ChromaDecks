package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class CustomSwipeGestureSlideState
class CustomSwipeGestureSlide: ListSlideAdvanced<CustomSwipeGestureSlideState>() {

    override val initialState: CustomSwipeGestureSlideState
        get() = CustomSwipeGestureSlideState()

    @Composable
    override fun content(state: CustomSwipeGestureSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}
