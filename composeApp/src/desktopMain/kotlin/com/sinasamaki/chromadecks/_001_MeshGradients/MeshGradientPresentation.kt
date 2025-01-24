package com.sinasamaki.chromadecks._001_MeshGradients

import androidx.compose.foundation.background
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter
import com.sinasamaki.chromadecks._001_MeshGradients.slides.CodeSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.EndSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.FirstSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.InterpolationSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.TitleSlide
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import com.sinasamaki.chromadecks._001_MeshGradients.slides.DefinitionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.EndSlide2
import com.sinasamaki.chromadecks._001_MeshGradients.slides.ExamplesSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.FunctionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.HighResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.LowResolutionSlide
import com.sinasamaki.chromadecks._001_MeshGradients.slides.PathInterpolationSlide
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import org.jetbrains.skia.Shader


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