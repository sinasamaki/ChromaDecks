package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class AnimatedListItemSlideState
class AnimatedListItemSlide: ListSlideAdvanced<AnimatedListItemSlideState>() {

    override val initialState: AnimatedListItemSlideState
        get() = AnimatedListItemSlideState()

    @Composable
    override fun content(state: AnimatedListItemSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}
