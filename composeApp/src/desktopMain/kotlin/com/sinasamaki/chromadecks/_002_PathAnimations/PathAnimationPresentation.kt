package com.sinasamaki.chromadecks._002_PathAnimations

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.sinasamaki.chromadecks._002_PathAnimations.slides.Butterflies
import com.sinasamaki.chromadecks._002_PathAnimations.slides.CartVsPolar
import com.sinasamaki.chromadecks._002_PathAnimations.slides.ChainPathEffect
import com.sinasamaki.chromadecks._002_PathAnimations.slides.ChapterOverview
import com.sinasamaki.chromadecks._002_PathAnimations.slides.CornerPathEffect
import com.sinasamaki.chromadecks._002_PathAnimations.slides.DashPathEffect
import com.sinasamaki.chromadecks._002_PathAnimations.slides.OtherPathFunctions
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PathEffectTitle
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PathEffectsOverview
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PathIntroSlide
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PathMeasureTitle
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PathMeasuring
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PencilPaper
import com.sinasamaki.chromadecks._002_PathAnimations.slides.PolarExamples
import com.sinasamaki.chromadecks._002_PathAnimations.slides.QuadraticCubic
import com.sinasamaki.chromadecks._002_PathAnimations.slides.StampPathEffect
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Zinc900


fun main() {
    singleWindowApplication(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
    ) {
        ChromaContainer {
            PathAnimationPresentation()
        }
    }
}

@Composable
fun PathAnimationPresentation() {
    Scaffold(
        containerColor = Zinc900,
        contentColor = Slate50,
    ) {
        SlidesPresenter2(
            modifier = Modifier,
            slides = remember {
                listOf(
//                    IntroReel(),
//                    Experiments(),

                    PathIntroSlide(),
                    ChapterOverview(),

                    PencilPaper(),
                    QuadraticCubic(),
                    OtherPathFunctions(),
                    CartVsPolar(),
                    PolarExamples(),


                    PathEffectTitle(),
                    PathEffectsOverview(),
                    CornerPathEffect(),
                    StampPathEffect(),
                    DashPathEffect(),
                    ChainPathEffect(),

                    PathMeasureTitle(),
                    PathMeasuring(),

                    Butterflies(),

//                MouseOffset(),
//                PromoTest3(),
//                PromoTest2(),
//                PromoTest()
                )
            }
        )
    }
}