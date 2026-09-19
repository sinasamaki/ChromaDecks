package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.timely_1
import chromadecks.composeapp.generated.resources.timely_2
import chromadecks.composeapp.generated.resources.timely_3
import chromadecks.composeapp.generated.resources.timely_4
import chromadecks.composeapp.generated.resources.timely_5
import chromadecks.composeapp.generated.resources.timely_6
import com.sinasamaki.chromadecks._004_TimelyTimer.components.TimelyDial
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.theme.Black
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private const val SCREENSHOT_ASPECT = 768f / 1134f

// Where the shots start out: far enough along their own angle to be off screen, so they sail
// inward to their resting distance.
private const val OFFSCREEN_DISTANCE = 1600f

private data class ShotPlacement(
    val shot: DrawableResource,
    val angle: Float,
    val distance: Float,
    val height: Float,
    val rotation: Float,
)

private val PLACEMENTS = listOf(
    ShotPlacement(Res.drawable.timely_1, angle = 185f, distance = 470f, height = 430f, rotation = -8f),
    ShotPlacement(Res.drawable.timely_2, angle = 232f, distance = 330f, height = 400f, rotation = -12f),
    ShotPlacement(Res.drawable.timely_3, angle = 300f, distance = 320f, height = 420f, rotation = 10f),
    ShotPlacement(Res.drawable.timely_4, angle = 350f, distance = 480f, height = 440f, rotation = -6f),
    ShotPlacement(Res.drawable.timely_5, angle = 40f, distance = 430f, height = 410f, rotation = 9f),
    ShotPlacement(Res.drawable.timely_6, angle = 110f, distance = 330f, height = 390f, rotation = -10f),
)

internal data class TimelyGalleryState(val shotsVisible: Boolean)

internal class TimelyGallerySlide : ListSlideAdvanced<TimelyGalleryState>() {

    override val initialState get() = TimelyGalleryState(shotsVisible = false)

    override val stateMutations: List<TimelyGalleryState.() -> TimelyGalleryState>
        get() = listOf(
            { copy(shotsVisible = true) },
        )

    @Composable
    override fun content(state: TimelyGalleryState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)

        Box(
            modifier = Modifier.fillMaxSize().background(Black),
            contentAlignment = Alignment.Center,
        ) {
            TimelyDial(swatch = swatch)

            PLACEMENTS.forEachIndexed { index, placement ->
                // The distance itself is what springs, in dp, so the visibility threshold is a real
                // distance — it settles at a whole dp instead of chasing a fraction of a fraction.
                val travelled by animateFloatAsState(
                    targetValue = if (state.shotsVisible) placement.distance else OFFSCREEN_DISTANCE,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow - index * 20f,
                        visibilityThreshold = 1f,
                    ),
                    label = "shotTravel$index",
                )

                val appear = ((OFFSCREEN_DISTANCE - travelled) /
                        (OFFSCREEN_DISTANCE - placement.distance)).coerceIn(0f, 1f)
                val radians = placement.angle * (PI.toFloat() / 180f)

                Image(
                    painter = painterResource(placement.shot),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(placement.height.dp)
                        .aspectRatio(SCREENSHOT_ASPECT)
                        .graphicsLayer {
                            translationX = (cos(radians) * travelled).dp.toPx()
                            translationY = (sin(radians) * travelled).dp.toPx()
                            rotationZ = placement.rotation * appear
                            scaleX = 1.15f - .15f * appear
                            scaleY = scaleX
                            alpha = appear
                        }
                        .clip(RoundedCornerShape(20.dp)),
                )
            }
        }
    }
}
