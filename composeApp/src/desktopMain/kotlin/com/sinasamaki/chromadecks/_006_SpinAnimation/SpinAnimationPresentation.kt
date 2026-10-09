package com.sinasamaki.chromadecks._006_SpinAnimation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._006_SpinAnimation.slides.SpinBuildSlide
import com.sinasamaki.chromadecks._006_SpinAnimation.slides.SpinHeroSlide
import com.sinasamaki.chromadecks._006_SpinAnimation.slides.SpinOutroSlide
import com.sinasamaki.chromadecks._006_SpinAnimation.slides.SpinTitleSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.slideanimations.translateOutX
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer

fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "ChromaDecks",
        onCloseRequest = ::exitApplication,
        content = {
            SpinAnimationPresentation()
        },
    )
}

@Composable
fun SpinAnimationPresentation() {
    ChromaContainer(
        codeColors = SpinCodeColors,
        colors = darkColorScheme(
            background = SpinGround,
            surface = SpinGround,
            onBackground = SpinRed,
            onSurface = SpinRed,
        ),
        letterbox = SpinGround,
    ) {
        Scaffold(
            containerColor = SpinGround,
            contentColor = SpinRed,
        ) {
            SlidesPresenter2(
                modifier = Modifier,
                scrollAnimationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioNoBouncy,
                ),
                slides = remember {
                    listOf(
                        SpinTitleSlide(),
                        SpinHeroSlide(),
                        SpinBuildSlide(),
                        SpinOutroSlide(),
                    )
                },
                background = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(SpinGround)
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
