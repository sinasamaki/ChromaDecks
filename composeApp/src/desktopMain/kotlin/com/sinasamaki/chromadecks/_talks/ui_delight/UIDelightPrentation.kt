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
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AccessibilitySemanticsSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AccessibilityTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimatedListItemSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.AnimationsTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ArchiveSwipeSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CaratDisplaySlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CodeHighlightSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CubeCodeSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomSwipeGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.CustomTapGestureSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.DeleteDragSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.FinishedElementSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.GesturesTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.HapticFeedbackTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.IntroductionSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.KotlinConfIntroSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.LayoutFundamentalsSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.LayoutFundamentalsTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ListItemLayoutSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.MDevCampIntroSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifierAppliedSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifierClickableSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ModifiersTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.PillAnimationSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.ThankYouTitleCard
import com.sinasamaki.chromadecks._talks.ui_delight.slides.TitleSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.VoteSlide
import com.sinasamaki.chromadecks._talks.ui_delight.slides.WhyTitleCard
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.slideanimations.blurIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.scaleIn
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime600
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate500
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
            keyword = Lime500,
            string = Lime600,
            number = Lime400,
            function = Purple400,
            param = Lime200,
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
                        MDevCampIntroSlide(),
                        IntroductionSlide(),
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
                        ArchiveSwipeSlide(),
                        DeleteDragSlide(),

                        AccessibilityTitleCard(),
                        AccessibilitySemanticsSlide(),

                        HapticFeedbackTitleCard(),
                        HapticFeedbackSlide(),

                        WhyTitleCard(),

                        PillAnimationSlide(),

                        CaratDisplaySlide(),
                        CubeCodeSlide(),
                        FinishedElementSlide(),
                        CodeHighlightSlide(),

                        VoteSlide(),
                        ThankYouTitleCard(),
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