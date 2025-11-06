package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Indigo50
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Orange800
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple600

class HapticFeedbackTitleCardState
class HapticFeedbackTitleCard: ListSlideAdvanced<HapticFeedbackTitleCardState>() {

    override val initialState: HapticFeedbackTitleCardState
        get() = HapticFeedbackTitleCardState()

    @Composable
    override fun content(state: HapticFeedbackTitleCardState) {
        TitleCardFrame(
            title = "Haptic Feedback",
            description = "Haptic feedback in Jetpack Compose provides tactile responses that enhance user interactions by engaging the sense of touch, creating a more immersive and satisfying experience that bridges the digital and physical worlds. The LocalHapticFeedback composition local gives access to the HapticFeedback interface, which exposes the performHapticFeedback method that accepts HapticFeedbackType constants like Click, LongPress, TextHandleMove, and others that correspond to different vibration patterns appropriate for various interaction types. Implementing haptic feedback is straightforward—obtain the haptic feedback instance using val hapticFeedback = LocalHapticFeedback.current, then call hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress) within your interaction handlers like onClick or pointerInput gesture detectors to provide immediate tactile confirmation of user actions.",
            backgroundColor = Indigo500,
            borderColor = Orange500,
        )
    }
}
