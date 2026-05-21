package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.AccordionText
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import com.sinasamaki.chromadecks.ui.util.progress
import kotlinx.coroutines.delay
import kotlin.random.Random

internal data class OutroSlideState(val placeholder: Unit = Unit)

internal class OutroSlide : ListSlideAdvanced<OutroSlideState>() {

    override val initialState get() = OutroSlideState()

    @Composable
    override fun content(state: OutroSlideState) {
        val progress = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                progress.snapTo(1f)
                delay(1001)
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
                )
                delay(1000)
            }
        }

        val p = progress.value

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Zinc950
                )
        ) {
            val density = LocalDensity.current
            val slideWidthPx = with(density) { maxWidth.toPx() }
            val slideHeightPx = with(density) { maxHeight.toPx() }

            AccordionText(
                text = "sinasamaki.com",
                animationProgress = (0.6f.. (1f)).progress(p),
                modifier = Modifier.align(Alignment.Center),
            )
            images.forEachIndexed { index, url ->
//                FlyingImage(
//                    url = url,
//                    index = index,
//                    numImages = images.size,
//                    progress = p,
//                    slideWidthPx = slideWidthPx,
//                    slideHeightPx = slideHeightPx,
//                )
            }

        }
    }
}

@Composable
private fun BoxScope.FlyingImage(
    url: String,
    index: Int,
    numImages: Int,
    progress: Float,
    slideWidthPx: Float,
    slideHeightPx: Float,
) {
    val density = LocalDensity.current
    val imageWidthPx = slideWidthPx * 0.3f
    val xRange = slideWidthPx - imageWidthPx
//    val randomX = remember(index) { xRange * (index / numImages.toFloat()) }
    val randomX = remember(index) { Random.nextFloat() * xRange - xRange / 2f }

    val windowSize = 0.5f
    val stagger = 0.05f
    val startOffset = index * stagger
    val localProgress = ((progress - startOffset) / windowSize).coerceIn(0f, 1f)

    val visibility = imageVisibility(localProgress)//.toInt().toFloat()
    val blur = lerp(50f, 0f, visibility)
    val yTranslation = lerp(slideHeightPx * 0.01f, -slideHeightPx * 0.01f, localProgress)

    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer {
                translationX = randomX
                translationY = yTranslation
                rotationX = lerp(0f, 5f, progress)
            }
            .scale(lerp(1f, 1.2f, visibility))
//            .blur(radius = blur.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .alpha(visibility)
            .align(Alignment.Center)
            .width(with(density) { imageWidthPx.toDp() })
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
    )
}

// Blur/fade in quickly, hold clear, then blur/fade out
private fun imageVisibility(localProgress: Float): Float {
    val inEnd = 0.05f
    val holdEnd = 0.8f
    return when {
        localProgress <= inEnd -> localProgress / inEnd
        localProgress <= holdEnd -> 1f
        else -> 1f - (localProgress - holdEnd) / (1f - holdEnd)
    }.coerceIn(0f, 1f)
}

private val images = listOf(
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2026/03/dialCustomization.png",
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2025/05/RibbonModifier-2.png",
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2024/10/stretchy_slider.png",
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2024/07/Untitled-3.png",
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2024/04/hapticfeedback-1.png",
    "https://storage.ghost.io/c/11/5c/115c7f82-cbbe-4cf5-b8f9-506e7a681ffa/content/images/size/w2000/2026/04/airbnb.png",
)
