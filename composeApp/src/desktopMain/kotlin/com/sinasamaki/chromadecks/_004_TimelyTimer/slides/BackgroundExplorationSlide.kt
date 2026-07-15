package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._004_TimelyTimer.components.TimelyDial
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.LocalSlideState

internal data class BackgroundExplorationState(val placeholder: Unit = Unit)

internal class BackgroundExplorationSlide : ListSlideAdvanced<BackgroundExplorationState>() {

    override val initialState get() = BackgroundExplorationState()

    @Composable
    override fun content(state: BackgroundExplorationState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            TimelyDial(swatch = swatch)
        }
    }
}
