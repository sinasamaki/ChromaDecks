package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.VideoPlayer
import com.sinasamaki.chromadecks.ui.theme.Black

class SubAtomicCTASlideState
class SubAtomicCTASlide : ListSlideAdvanced<SubAtomicCTASlideState>() {

    override val initialState: SubAtomicCTASlideState
        get() = SubAtomicCTASlideState()

    @Composable
    override fun content(state: SubAtomicCTASlideState) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            VideoPlayer(
                fileName = "subatomic-sizzle.mp4",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(48.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}
