package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.AnimatedListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class HapticFeedbackSlideState
class HapticFeedbackSlide: ListSlideAdvanced<HapticFeedbackSlideState>() {

    override val initialState: HapticFeedbackSlideState
        get() = HapticFeedbackSlideState()

    @Composable
    override fun content(state: HapticFeedbackSlideState) {
        ListItemDisplay(
            code = """
                val haptic = LocalHapticFeedback.current
                LaunchedEffect(willTrigger) {
                    haptic.performHapticFeedback(
                        if (willTrigger) {
                            HapticFeedbackType.LongPress
                        } else {
                            HapticFeedbackType.SegmentTick
                        }
                    )
                }
                """.trimIndent()
        ) {
            AnimatedListItem()
        }
    }
}
