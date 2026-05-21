package com.sinasamaki.chromadecks._talks.ui_delight

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AboutMeSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimatedListItemSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimationsUsageSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimationsTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomSwipeGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomTapGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.GesturesTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.FinishedElementSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.IntroductionSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.KotlinConfIntroSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.VoteSlide
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
import com.sinasamaki.chromadecks.ui.slideanimations.blurIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.scaleIn
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Orange300
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Pink500
import com.sinasamaki.chromadecks.ui.theme.Purple300
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Slate500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Zinc900

fun main() {
    singleWindowApplication(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "KotlinConf",
    ) {
        UIDelightPresentation()
    }
}

@Composable
fun UIDelightPresentation() {
    ChromaContainer(
        aspectRatio = 16f / 9f,
        codeColors = CodeColors(
            keyword = Purple300,
            string = Orange300,
            number = Pink300,
            function = Purple400,
            param = Orange400,
            comment = Slate500,
        ),
    ) {
        Surface(
//            containerColor = Zinc900,
            contentColor = Slate50,
        ) {
            SlidesPresenter2(
                modifier = Modifier,
                slides = remember {
                    listOf(
//                        AboutMeSlide(),
//                        ThankYouTitleCard(),
//                        HapticFeedbackSlide(),
//
//                        ListItemSlide(),
//                        TitleSlide(),

                        KotlinConfIntroSlide(),
//                        IntroductionSlide(),
                        AboutMeSlide(),
//                        MyAnimationsSlide(),
//                        WhySlide(),
                        FinishedElementSlide(),
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
                        AnimationsUsageSlide(),

                        HapticFeedbackTitleCard(),
                        HapticFeedbackSlide(),

                        VoteSlide(),
//                        ThankYouTitleCard(),
                    )
                },
                scrollAnimationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                ),
                animator = { content ->
                    Box(
                        Modifier
                            .parallax(
                                factor = .8f
                            )
                            .fadeIn(
                                initial = 0f,
                                startAt = 0f
                            )
                            .fadeOut(
                                initial = 0f,
                                startAt = 0f
                            )
                            .blurIn(initial = 100f)
                            .scaleIn(
                                initial = .75f
                            )
                            .background(
                                color = Zinc900
                            )
                    ) {
                        content()
                    }
                }
            )
        }
    }
}