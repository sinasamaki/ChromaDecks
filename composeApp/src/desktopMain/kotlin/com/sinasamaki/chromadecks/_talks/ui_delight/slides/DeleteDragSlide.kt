package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.DeleteDragListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class DeleteDragSlideState
class DeleteDragSlide : ListSlideAdvanced<DeleteDragSlideState>() {

    override val initialState: DeleteDragSlideState
        get() = DeleteDragSlideState()

    @Composable
    override fun content(state: DeleteDragSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "DeleteDrag.kt" to """
                    
                    val overscroll = remember { BouncyOverscroll() }
                    
                    ...
                    
                    .overscroll(overscroll)
                    .anchoredDraggable(
                        state = anchorState,
                        orientation = Orientation.Horizontal,
                        overscrollEffect = overscroll,
                    )
                """.trimIndent()
            )
        ) {
            DeleteDragListItem()
        }
    }
}
