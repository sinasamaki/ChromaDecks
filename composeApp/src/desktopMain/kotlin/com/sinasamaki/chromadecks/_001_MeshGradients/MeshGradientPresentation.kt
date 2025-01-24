package com.sinasamaki.chromadecks._001_MeshGradients

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._001_MeshGradients.slides.CodeSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.DefinitionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.EndSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.FunctionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.HighResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.LowResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.PathInterpolationSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.TitleSlide
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter
import com.sinasamaki.chromadecks.ui.theme.Zinc900


@Composable
fun MeshGradientPresentation(modifier: Modifier = Modifier) {

    SlidesPresenter(
        modifier = modifier
            .background(
                color = Zinc900
            ),
        slides = remember {
            listOf(
                TitleSlide(),
                DefinitionSlide(),
                FunctionSlide(),
                CodeSlide(),
                LowResolutionSlide(),
                PathInterpolationSlide(),
                HighResolutionSlide(),
                EndSlide(),
            )
        }
    )

}