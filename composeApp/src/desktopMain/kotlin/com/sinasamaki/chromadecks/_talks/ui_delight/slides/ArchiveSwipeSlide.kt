package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.ArchiveSwipeListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class ArchiveSwipeSlideState
class ArchiveSwipeSlide : ListSlideAdvanced<ArchiveSwipeSlideState>() {

    override val initialState: ArchiveSwipeSlideState
        get() = ArchiveSwipeSlideState()

    @Composable
    override fun content(state: ArchiveSwipeSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "ArchiveSwipe.kt" to """
                    anchorState.animateTo(
                        targetValue = AnchorState.PeekArchive,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                        )
                    )
                """.trimIndent()
            )
        ) {
            ArchiveSwipeListItem()
        }
    }
}
