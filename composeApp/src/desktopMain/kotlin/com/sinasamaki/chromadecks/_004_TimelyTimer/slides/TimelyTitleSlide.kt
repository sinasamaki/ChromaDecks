package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.slideanimations.blurIn
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc900

internal data class TimelyTitleState(val placeholder: Unit = Unit)

internal class TimelyTitleSlide : ListSlideAdvanced<TimelyTitleState>() {

    override val initialState get() = TimelyTitleState()

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier
                .parallax(.9f)
                .fadeIn()
                .blurOut(initial = 200f)
                .blurIn(
                    initial = 100f
                )
            ) {
                content()
            }
        }

    @Composable
    override fun content(state: TimelyTitleState) {
        Box(Modifier.fillMaxSize().background(Transparent)) {
            TitleFrame(
                modifier = Modifier.fillMaxSize(),
                title = "timely timer",
                description = "beautiful dial timer, with intricate animations",
                hint = "inspired by Bitspin",
                bookNumber = 4,
                contentColor = Slate50,
//                animationProgress = .5f
            )
        }
    }
}
