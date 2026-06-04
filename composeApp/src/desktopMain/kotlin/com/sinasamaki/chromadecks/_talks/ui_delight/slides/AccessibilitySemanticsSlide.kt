package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.AnimatedListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class AccessibilitySemanticsSlideState
class AccessibilitySemanticsSlide : ListSlideAdvanced<AccessibilitySemanticsSlideState>() {

    override val initialState: AccessibilitySemanticsSlideState
        get() = AccessibilitySemanticsSlideState()

    @Composable
    override fun content(state: AccessibilitySemanticsSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "ListItem.kt" to """
                    .semantics {
                        contentDescription = label

                        customActions = listOf(
                            CustomAccessibilityAction("Archive ${'$'}label") {
                                archiveItem()
                            },
                            CustomAccessibilityAction("Delete ${'$'}label") {
                                deleteItem()
                            }
                        )
                    }
                """.trimIndent()
            )
        ) {
            AnimatedListItem()
        }
    }
}
