package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import chromadecks.composeapp.generated.resources.Res
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.toPx
import kotlinx.coroutines.delay
import org.jetbrains.compose.animatedimage.AnimatedImage
import org.jetbrains.compose.animatedimage.animate
import org.jetbrains.compose.animatedimage.loadAnimatedImage
import kotlin.random.Random

class MyAnimationsSlideState
class MyAnimationsSlide : ListSlideAdvanced<MyAnimationsSlideState>() {

    override val initialState: MyAnimationsSlideState
        get() = MyAnimationsSlideState()

    @Composable
    override fun content(state: MyAnimationsSlideState) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            val width = maxWidth.toPx
            val height = maxHeight.toPx
            Row {
                listOf(
                    "slider",
                    "card",
                    "graph",
                    "bubble",
                    "glitchVisible",
                    "colorSlider",
                    "polar",
                    "ringSlider",
                ).forEachIndexed { index, gif ->
                    val x = remember { Animatable(0f) }
                    val y = remember { Animatable(0f) }

                    LaunchedEffect(Unit) {
                        while (true) {
                            x.snapTo(Random.nextFloat())
                            delay(2000)
                            y.snapTo(Random.nextFloat())
                        }
                    }

                    GifImage(
                        modifier = Modifier
                            .graphicsLayer {
//                                translationX = lerp(width / 2, -width / 2, x.value)
//                                translationY = lerp(height / 2, -height / 2, y.value)
                            }
                            .offset {
                                IntOffset(0, 0)
                            },
                        gif = gif
                    )
                }
            }
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    gif: String,
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {

        var image: AnimatedImage? by remember { mutableStateOf(null) }
        LaunchedEffect(Unit) {
            image = loadAnimatedImage(Res.getUri("drawable/${gif}.gif"))
        }

        image?.let { image ->
            Image(
                bitmap = image.animate(),
                contentDescription = "Animated GIF",
                modifier = Modifier.fillMaxSize(.4f).aspectRatio(1f)
            )
        }
    }
}
