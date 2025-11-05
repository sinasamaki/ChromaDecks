package com.sinasamaki.chromadecks._talks.ui_delight

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ListItemSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.TitleSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.Slate50

fun main() {
    singleWindowApplication(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "Droidcon KE",
    ) {
        ChromaContainer {
            UIDelightPresentation()
        }
    }
}

@Composable
fun UIDelightPresentation() {
    Surface (
//        containerColor = Zinc900,
        contentColor = Slate50,
    ) {
        SlidesPresenter2(
            modifier = Modifier,
            slides = remember {
                listOf(
                    ListItemSlide(),
                    TitleSlide(),

                    // IntroductionSlide
                    // MyAnimationsSlide
                    // WhySlide
                    // LayoutFundamentalsTitleCard
                    // LayoutFundamentalsSlide
                    // ListItemLayoutSlide
                    // ModifiersTitleCard
                    // ModifierClickableSlide
                    // GesturesTitleCard
                    // CustomTapGestureSlide
                    // CustomSwipeGestureSlide
                    // AnimationsTitleCard
                    // AnimatedListItemSlide
                    // HapticFeedbackTitleCard
                    // HapticFeedbackSlide

                )
            }
        )
    }
}