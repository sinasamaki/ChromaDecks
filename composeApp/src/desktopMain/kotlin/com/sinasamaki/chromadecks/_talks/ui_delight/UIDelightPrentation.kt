package com.sinasamaki.chromadecks._talks.ui_delight

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AboutMeSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimatedListItemSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimationsTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomSwipeGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomTapGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.GesturesTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.IntroductionSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.LayoutFundamentalsSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.LayoutFundamentalsTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ListItemLayoutSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ListItemSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifierAppliedSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifierClickableSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifiersTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.MyAnimationsSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ThankYouTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.TitleSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.WhySlide
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
    Surface(
//        containerColor = Zinc900,
        contentColor = Slate50,
    ) {
        SlidesPresenter2(
            modifier = Modifier,
            slides = remember {
                listOf(
//                    AboutMeSlide(),
//                    ThankYouTitleCard(),
//                    HapticFeedbackSlide(),
//
//                    ListItemSlide(),
//                    TitleSlide(),


                    IntroductionSlide(),
                    AboutMeSlide(),
//                    MyAnimationsSlide(),
//                    WhySlide(),
                    LayoutFundamentalsTitleCard(),
                    LayoutFundamentalsSlide(),
                    ListItemLayoutSlide(),

                    ModifiersTitleCard(),
                    ModifierAppliedSlide(),
                    ModifierClickableSlide(),

                    GesturesTitleCard(),
                    CustomTapGestureSlide(),
                    CustomSwipeGestureSlide(),

                    AnimationsTitleCard(),
                    AnimatedListItemSlide(),

                    HapticFeedbackTitleCard(),
                    HapticFeedbackSlide(),

                    ThankYouTitleCard(),
                )
            }
        )
    }
}