package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.sinasamaki.chromadecks._talks.ui_delight.components.AnimatedListItem
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class FinishedElementSlideState
class FinishedElementSlide : ListSlideAdvanced<FinishedElementSlideState>() {

    override val initialState: FinishedElementSlideState
        get() = FinishedElementSlideState()

    @Composable
    override fun content(state: FinishedElementSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val density = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density.density * 1.7f, density.fontScale * 1.7f)
            ) {
                AnimatedListItem(
                    modifier = Modifier.fillMaxWidth(.5f)
                )
            }
        }
    }
}
