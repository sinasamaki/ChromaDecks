package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._talks.ui_delight.components.ArchiveSwipeListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.DeleteDragListItem
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class AnimationsUsageSlideState
class AnimationsUsageSlide : ListSlideAdvanced<AnimationsUsageSlideState>() {

    override val initialState: AnimationsUsageSlideState
        get() = AnimationsUsageSlideState()

    @Composable
    override fun content(state: AnimationsUsageSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val density = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(density.density * 1.5f, density.fontScale * 1.5f)
            ) {
                Column(
                    modifier = Modifier.width(400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ArchiveSwipeListItem()
                    DeleteDragListItem()
                }
            }
        }
    }
}
