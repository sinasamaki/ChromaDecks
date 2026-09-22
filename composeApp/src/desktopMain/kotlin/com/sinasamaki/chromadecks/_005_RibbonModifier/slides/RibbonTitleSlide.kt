package com.sinasamaki.chromadecks._005_RibbonModifier.slides

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
import com.sinasamaki.chromadecks.ui.theme.Zinc900

internal data class RibbonTitleState(val placeholder: Unit = Unit)

internal class RibbonTitleSlide : ListSlideAdvanced<RibbonTitleState>() {

    override val initialState get() = RibbonTitleState()

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(
                Modifier
                    .parallax(.9f)
                    .fadeIn()
                    .blurOut(initial = 200f)
                    .blurIn(initial = 100f)
            ) {
                content()
            }
        }

    @Composable
    override fun content(state: RibbonTitleState) {
        Box(Modifier.fillMaxSize()) {
            TitleFrame(
                modifier = Modifier.fillMaxSize(),
                title = "ribbon modifier",
                description = "a helix that wraps itself around any composable",
                hint = "from SubAtomic",
                bookNumber = 5,
                contentColor = Zinc900,
            )
        }
    }
}
