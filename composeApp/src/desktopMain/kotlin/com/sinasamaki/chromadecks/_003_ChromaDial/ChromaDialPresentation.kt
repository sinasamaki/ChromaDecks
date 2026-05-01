package com.sinasamaki.chromadecks._003_ChromaDial

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._003_ChromaDial.slides.AddDependencySlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.ChromaDialTeaserSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.DefaultDialSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.DialLambdasSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.DialShowcasesSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.IntroSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.ThumbCustomizationSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.TitleSlide
import com.sinasamaki.chromadecks._003_ChromaDial.slides.TrackCustomizationSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.slideanimations.blurIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.scaleIn
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Lime600
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Zinc900


fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
        onCloseRequest = ::exitApplication,
        content = {
            ChromaDialPresentation()
        },
    )
}


@Composable
fun ChromaDialPresentation() {
    ChromaContainer(
        codeColors = CodeColors(
            keyword = Lime500,
            string = Lime300,
            number = Lime600,
            function = Lime500,
            param = Lime200,
            comment = Green400.copy(alpha = .5f),
        )
    ) {
        Scaffold(
            containerColor = Zinc900,
            contentColor = Slate50,
        ) {
            SlidesPresenter2(
                modifier = Modifier,
                slides = remember {
                    listOf(
                        ChromaDialTeaserSlide(),
                        DialShowcasesSlide(),
                        IntroSlide(),
                        AddDependencySlide(),
                        DefaultDialSlide(),
                        DialLambdasSlide(),
                        ThumbCustomizationSlide(),
                        TrackCustomizationSlide(),

                        TitleSlide(),
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
