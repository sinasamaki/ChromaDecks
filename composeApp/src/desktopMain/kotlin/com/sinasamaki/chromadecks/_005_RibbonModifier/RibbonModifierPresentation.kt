package com.sinasamaki.chromadecks._005_RibbonModifier

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.CirclePathSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.CubicPathSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.ExplodedLayersSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.GradientSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.LayerCodeSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.RibbonGallerySlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.RibbonHeroSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.RibbonRevealSlide
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.RibbonTitleSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.slideanimations.translateOutX
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Blue600
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import com.sinasamaki.chromadecks.ui.theme.Cyan500
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import com.sinasamaki.chromadecks.ui.theme.Fuchsia600
import com.sinasamaki.chromadecks.ui.theme.Fuchsia900
import com.sinasamaki.chromadecks.ui.theme.Indigo900
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Pink600
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Violet500
import com.sinasamaki.chromadecks.ui.theme.Violet900
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc900

fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
        onCloseRequest = ::exitApplication,
        content = {
            RibbonModifierPresentation()
        },
    )
}

internal val RIBBON_COLORS = listOf(
    Cyan500,
    Sky500,
    Blue600,
    Indigo900,
    Violet900,
    Fuchsia900,
    Pink600,
    Rose500,
    Orange400,
    Amber400,
)

@Composable
fun RibbonModifierPresentation() {
    var currentIndex by remember { mutableStateOf(0) }

    ChromaContainer(
        codeColors = RibbonCodeColors,
        colors = lightColorScheme(),
        letterbox = Zinc200,
    ) {
        Scaffold(
            containerColor = Zinc50,
            contentColor = Zinc900,
        ) {
            SlidesPresenter2(
                modifier = Modifier,
                scrollAnimationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioNoBouncy,
                ),
                slides = remember {
                    listOf(
//                        RibbonHeroSlide(),
                        RibbonTitleSlide(),
                        CirclePathSlide(),
                        CubicPathSlide(),
                        ExplodedLayersSlide(),
                        LayerCodeSlide(),
                        GradientSlide(),
                        RibbonRevealSlide(),
                        RibbonGallerySlide(),
                    )
                },
                onCurrentIndexChange = { currentIndex = it },
                background = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Zinc50)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Violet500.copy(alpha = .07f),
                                        Rose500.copy(alpha = .04f),
                                        White.copy(alpha = 0f),
                                    ),
                                    center = Offset(.5f, .35f) * 2000f,
                                    radius = 1600f,
                                )
                            )
                    )
                },
                animator = { content ->
                    Box(
                        Modifier
                            .parallax(factor = 1f)
                            .translateInX(initial = 1f)
                            .translateOutX(initial = -.7f)
                            .fadeIn()
                            .fadeOut()
                    ) {
                        content()
                    }
                },
            )
        }
    }
}
