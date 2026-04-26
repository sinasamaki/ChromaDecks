package com.sinasamaki.chromadecks._bites.teaser

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.ui.zIndex
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.leshan
import com.sinasamaki.chromadecks._talks.ui_delight.components.Cube
import com.sinasamaki.chromadecks._talks.ui_delight.components.CubeFace
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.components.HeartLogo
import com.sinasamaki.chromadecks.ui.components.SlidesPresenter2
import com.sinasamaki.chromadecks.ui.components.VideoPlayer
import com.sinasamaki.chromadecks.ui.modifiers.grain
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.ChromaTheme
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange100
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Purple900
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt
import kotlin.random.Random

fun main() {
    singleWindowApplication(
        state = WindowState(
            size = DpSize(1080.dp / 2, 1920.dp / 2),
            position = WindowPosition.Aligned(alignment = Alignment.CenterEnd)
        ),
        title = "BitesTest"
    ) {
        ChromaTheme {
            SlidesPresenter2(
                modifier = Modifier
                    .aspectRatio(9 / 16f)
                    .background(Black),
                slides = remember {
                    listOf(
                        TeaserSlide()
                    )
                }
            )
        }
    }
}

data class TeaserState(val index: Int = 0)
private class TeaserSlide() : ListSlideAdvanced<TeaserState>() {

    override val initialState: TeaserState
        get() = TeaserState()

    override val stateMutations: List<TeaserState.() -> TeaserState>
        get() = listOf(
            { copy(index = 1) },
            { copy(index = 2) },
            { copy(index = 3) },
            { copy(index = 4) },
            { copy(index = 5) },
            { copy(index = 6) },
        )

    @Composable
    override fun content(state: TeaserState) {
        val graphicsLayer = rememberGraphicsLayer()
        Box(
            Modifier
                .zIndex(-10f)
                .fillMaxSize()
                .grain(bitmapAlpha = .21f)
                .blur(
                    radius = 20.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .drawBehind {
                    graphicsLayer.blendMode = BlendMode.Screen
                    drawLayer(graphicsLayer)
                }
        )
        Box(
            Modifier
                .zIndex(10f)
                .fillMaxSize()
//                .grain(bitmapAlpha = .21f)
                .blur(
                    radius = 40.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .alpha(.2f)
                .drawBehind {
                    graphicsLayer.blendMode = BlendMode.Screen
                    drawLayer(graphicsLayer)
                }
        )
        AnimatedContent(
            targetState = state.index,
            modifier = Modifier
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    graphicsLayer.blendMode = BlendMode.SrcOver
                    drawLayer(graphicsLayer)
                }
                .stars()
                .grain(bitmapAlpha = .15f)
                .fillMaxSize(),
            transitionSpec = {
                val frac = .1f
                val start = if (targetState > initialState) frac else -frac
                val end = if (targetState > initialState) -frac else frac
                slideInVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = 100,
                    ),
                    initialOffsetY = { (it * start).toInt() }
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 1,
                        delayMillis = 200,
                    )
                ) togetherWith slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                    ),
                    targetOffsetY = { (it * end).toInt() }
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 1,
                        delayMillis = 200,
                    )
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (it) {
                    0 -> Box(Modifier.fillMaxSize())
                    1 -> TeaserIntro()
                    2 -> Logo()
                    3 -> MyReel()
                    4 -> CubeShow()
                    5 -> CodeTeaser()
                    6 -> TeaserOutro()
                }
            }
        }
    }

}

@Composable
private fun TeaserIntro() {
    var showEmoji by remember { mutableStateOf(false) }
    var showPhoto by remember { mutableStateOf(false) }
    var showName by remember { mutableStateOf(false) }

    // Staggered entrance
    LaunchedEffect(Unit) {
        showEmoji = true
        delay(300)
        showPhoto = true
        delay(300)
        showName = true
    }

    // Wave rotation, triggered once the emoji is visible
    val waveRotation = remember { Animatable(0f) }
    LaunchedEffect(showEmoji) {
        if (!showEmoji) return@LaunchedEffect
        delay(300) // let the entrance animation settle
        repeat(4) {
            launch {
                waveRotation.animateTo(
                    25f,
                    spring(stiffness = Spring.StiffnessMedium)
                )
            }
            delay(100)
            launch {
                waveRotation.animateTo(
                    -10f,
                    spring(stiffness = Spring.StiffnessMedium)
                )
            }
            delay(100)
        }
        waveRotation.animateTo(
            0f,
            spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioHighBouncy
            )
        )
    }

    val enterTransition = scaleIn(
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        transformOrigin = TransformOrigin(.5f, 1f)
    ) + fadeIn(tween(10)) +
            expandVertically(
                animationSpec = tween(800, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Bottom,
            )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 👋 Wave emoji
        AnimatedVisibility(
            visible = showEmoji,
            modifier = Modifier.graphicsLayer {
                rotationZ = waveRotation.value
                transformOrigin = TransformOrigin(0.5f, 1.0f)
            },
            enter = enterTransition,
        ) {
            androidx.compose.material3.Text(
                text = "👋",
                fontSize = 80.sp,
//                modifier = Modifier.graphicsLayer {
//                    rotationZ = waveRotation.value
//                    transformOrigin = TransformOrigin(0.5f, 1.0f)
//                }
            )
        }

        Spacer(Modifier.height(36.dp))

        // Profile photo
        AnimatedVisibility(visible = showPhoto, enter = enterTransition) {
            Image(
                painter = painterResource(Res.drawable.leshan),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Name
        AnimatedVisibility(visible = showName, enter = enterTransition) {
            androidx.compose.material3.Text(
                text = "Ian Leshan",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Zinc100,
                    fontSize = 48.sp,
                )
            )
        }
    }
}

@Composable
private fun Logo() {
    HeartLogo()
}

@Composable
private fun AnimatedContentScope.CubeShow() {
    val angleY by rememberInfiniteTransition()
        .animateFloat(
            initialValue = 30f,
            targetValue = 40f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
//                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Reverse,
            )
        )

    val translationY by rememberInfiniteTransition()
        .animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000
                ),
                repeatMode = RepeatMode.Reverse,
            )
        )

    val transitionOffset by transition.animateFloat(
        transitionSpec = {
            spring(
                stiffness = Spring.StiffnessVeryLow * .3f,
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        }
    ) {
        when (it) {
            EnterExitState.PreEnter -> -150f
            EnterExitState.Visible -> 0f
            EnterExitState.PostExit -> 90f
        }
    }

    val transitionScale by transition.animateFloat(
        transitionSpec = {
            spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        }
    ) {
        when (it) {
            EnterExitState.PreEnter -> .9f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> .9f
        }
    }

    Cube(
        modifier = Modifier
            .scale(transitionScale)
            .offset {
                IntOffset(0, translationY.roundToInt())
            }
            .size(320.dp),
        angleY = angleY + transitionOffset,
        angleX = -15f,
    ) { face ->
        Box(
            Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        colors = when (face) {
                            CubeFace.FRONT -> listOf(
                                Purple500.copy(alpha = 1f),
                                Purple500.copy(alpha = 1f)
                            )

                            CubeFace.BACK -> listOf(
                                Purple500.copy(alpha = .1f),
                                Purple500.copy(alpha = .1f)
                            )

                            CubeFace.TOP -> listOf(
                                Purple500.copy(alpha = .2f),
                                Purple500.copy(alpha = .1f)
                            )

                            CubeFace.LEFT, CubeFace.RIGHT, CubeFace.BOTTOM -> listOf(
                                Purple500.copy(alpha = .1f),
                                Purple500.copy(alpha = 1f)
                            )

                            else -> listOf(Purple900.copy(alpha = .5f), Purple900.copy(alpha = .5f))
                        }
                    ),
                    shape = RectangleShape
//                    color = Violet500,
                )
                .background(
                    color = Black.copy(alpha = .95f)
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colors = when (face) {
                            CubeFace.FRONT -> listOf(
                                Purple500.copy(alpha = .1f),
                                Purple500.copy(alpha = .1f)
                            )

                            CubeFace.BACK -> listOf(
                                Purple500.copy(alpha = .1f),
                                Purple500.copy(alpha = .1f)
                            )

                            CubeFace.TOP -> listOf(
                                Purple500.copy(alpha = .2f),
                                Purple500.copy(alpha = .1f)
                            )

                            CubeFace.LEFT, CubeFace.RIGHT, CubeFace.BOTTOM -> listOf(
                                Purple500.copy(alpha = .4f),
                                Purple500.copy(alpha = .1f)
                            )

                            else -> listOf(Purple900.copy(alpha = .5f), Purple900.copy(alpha = .5f))
                        }
                    )
//                    color = Purple500.copy(alpha = .1f)
                )
                .padding(12.dp)
        ) {
            Text(
                "mDevCamp\n⋅⋅⋅ \nJune 03-04",
                modifier = Modifier
                    .drawWithContent {
                        val bounds = Rect(
                            offset = Offset(-size.width, -size.height),
                            size = Size(size.width * 3f, size.height * 3f),
                        )
                        layer(bounds) {
                            this@drawWithContent.drawContent()
                            val offset = Offset(angleY * 80f, angleY * 80f)
                            drawRect(
                                topLeft = Offset(-size.width, -size.height),
                                size = Size(size.width * 3f, size.height * 3f),
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Orange600,
                                        Orange400,
                                        Orange100,
                                        Orange400,
                                        Orange600,
                                    ),
                                    tileMode = TileMode.Mirror,
                                    start = Offset.Zero + offset,
                                    end = Offset(size.width, size.height) + offset,
                                ),
                                blendMode = BlendMode.SrcIn
                            )
                        }
                    },
                style = MaterialTheme.typography.labelLarge.copy(
                    color = when (face) {
                        CubeFace.FRONT -> Orange500
                        else -> Zinc100.copy(alpha = 0f)
                    },
                    shadow = Shadow(
                        color = when (face) {
                            CubeFace.FRONT -> White
                            else -> Zinc100.copy(alpha = .5f)
                        },
                        blurRadius = when (face) {
                            CubeFace.FRONT -> 20f
                            else -> 20f
                        },
                    ),
                    fontSize = 46.sp,
                ),
            )
        }

    }
}


private val reelVideos = listOf(
    "slider4.mp4",
//    "cameraDial.mp4",
    "materialDial.mp4",
    "glowswipetodsmiss.mp4",
    "FinalRibbon.mp4",
    "shipatonButtons.mp4",
//    "rubberBandSlider.mp4",
//    "glowSlider.mp4",
    "stripedSlider.mp4",
//    "blueDial.mp4",
)

@Composable
fun MyReel(modifier: Modifier = Modifier) {
    VideoPlayer(
        fileNames = reelVideos,
        modifier = modifier.fillMaxWidth().padding(96.dp)
            .clip(RoundedCornerShape(24.dp))
    )
}

private val codeTeaserSteps = listOf(
    """
Box(
    modifier = Modifier
        .size(200.dp)
)
    """.trim(),
    """
Box(
    modifier = Modifier
        .size(200.dp)
        .background(
            color = Lime400,
        )
)
    """.trim(),
    """
Box(
    modifier = Modifier
        .size(200.dp)
        .background(
            color = Lime400,
            shape = RoundedCornerShape(32.dp),
        )
)
    """.trim(),
    """
Box(
    modifier = Modifier
        .size(200.dp)
        .background(
            color = Lime400,
            shape = RoundedCornerShape(32.dp),
        )
        .border(
            width = 2.dp,
            color = Yellow400,
            shape = RoundedCornerShape(32.dp),
        )
)
    """.trim(),
    """
Box(
    modifier = Modifier
        .size(200.dp)
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(Lime400, Sky500),
            ),
            shape = RoundedCornerShape(32.dp),
        )
        .border(
            width = 2.dp,
            color = Yellow400,
            shape = RoundedCornerShape(32.dp),
        )
)
    """.trim(),
)

@Composable
fun CodeTeaser(modifier: Modifier = Modifier) {
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (step < codeTeaserSteps.lastIndex) {
            delay(1000)
            step = (step + 1) % codeTeaserSteps.size
        }
    }

    val cornerShape = RoundedCornerShape(32.dp)
    val gradient = Brush.horizontalGradient(listOf(Lime400, Sky500))

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxWidth().weight(2f), contentAlignment = Alignment.Center) {
            CodeBlock(
                code = codeTeaserSteps[step],
                modifier = Modifier.padding(32.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) }
            ) { s ->
                Box(
                    modifier = when (s) {
                        0 -> Modifier.size(200.dp)
                        1 -> Modifier.size(200.dp)
                            .background(color = Lime400)

                        2 -> Modifier.size(200.dp)
                            .background(color = Lime400, shape = cornerShape)

                        3 -> Modifier.size(200.dp)
                            .background(color = Lime400, shape = cornerShape)
                            .border(width = 2.dp, color = Yellow400, shape = cornerShape)

                        else -> Modifier.size(200.dp)
                            .background(brush = gradient, alpha = .3f, shape = cornerShape)
                            .border(width = 2.dp, color = Yellow400, shape = cornerShape)
                    }
                )
            }
        }
    }
}


@Composable
private fun TeaserOutro() {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1 — gentle S-curve across upper third
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.25f)
                    cubicTo(w * 0.25f, h * 0.05f, w * 0.65f, h * 0.45f, w, h * 0.22f)
                },
                color = Orange400,
                phase = phase,
                phaseOffset = 0.0f,
                totalDistance = 2800f,
                segmentLength = 180f,
                strokeWidth = 16f,
            )

            // 2 — sweeping curve across lower third
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.72f)
                    cubicTo(w * 0.3f, h * 0.95f, w * 0.7f, h * 0.55f, w, h * 0.78f)
                },
                color = Purple400,
                phase = phase,
                phaseOffset = 0.3f,
                totalDistance = 3200f,
                segmentLength = 220f,
                strokeWidth = 14f,
            )

            // 3 — diagonal ribbon from top-left to bottom-right
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.1f)
                    cubicTo(w * 0.4f, h * 0.35f, w * 0.6f, h * 0.65f, w, h * 0.9f)
                },
                color = Red400,
                phase = phase,
                phaseOffset = 0.6f,
                totalDistance = 3600f,
                segmentLength = 160f,
                strokeWidth = 12f,
            )

            // 4 — wide arc through the middle
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.5f)
                    cubicTo(w * 0.2f, h * 0.15f, w * 0.8f, h * 0.85f, w, h * 0.5f)
                },
                color = Lime400,
                phase = phase,
                phaseOffset = 0.15f,
                totalDistance = 4000f,
                segmentLength = 250f,
                strokeWidth = 18f,
            )

            // 5 — shallow wave from bottom-left to upper-right
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.85f)
                    cubicTo(w * 0.35f, h * 0.7f, w * 0.55f, h * 0.35f, w, h * 0.18f)
                },
                color = Yellow400,
                phase = phase,
                phaseOffset = 0.75f,
                totalDistance = 3000f,
                segmentLength = 140f,
                strokeWidth = 14f,
            )

            // 6 — tight S-curve through centre band
            drawAnimatedPath(
                path = Path().apply {
                    moveTo(0f, h * 0.42f)
                    cubicTo(w * 0.45f, h * 0.25f, w * 0.55f, h * 0.75f, w, h * 0.58f)
                },
                color = Pink400,
                phase = phase,
                phaseOffset = 0.45f,
                totalDistance = 2500f,
                segmentLength = 200f,
                strokeWidth = 12f,
            )
        }

        androidx.compose.material3.Text(
            text = "See you there",
            style = MaterialTheme.typography.displayLarge.copy(
                color = Zinc100,
                fontSize = 56.sp,
            )
        )
    }
}

/**
 * Draws a dashed segment that travels along [path].
 *
 * [phase] is the shared 0→1 driver from a single infiniteTransition.
 * [phaseOffset] (0..1) shifts each path to a different point in its cycle,
 * so paths appear staggered even though they share the same animation clock.
 * [totalDistance] controls the total dash-cycle length (segment + gap).
 */
private fun DrawScope.drawAnimatedPath(
    path: Path,
    color: androidx.compose.ui.graphics.Color,
    phase: Float,
    phaseOffset: Float = 0f,
    totalDistance: Float = 3000f,
    segmentLength: Float = 150f,
    strokeWidth: Float = 12f,
) {
    val gapLength = totalDistance - segmentLength
    val effectivePhase = -((phase + phaseOffset) % 1f) * totalDistance
    drawPath(
        path = path,
        color = color.copy(alpha = 0.75f),
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(segmentLength, gapLength),
                phase = effectivePhase,
            )
        )
    )
}

@Composable
fun Modifier.stars(): Modifier {

    var time by remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            time = Random.nextLong()
        }
    }
    return drawBehind {
        time * 1f
        for (i in 0..100) {
            drawCircle(
                color = Zinc200.copy(
                    alpha = Random.nextFloat() * .5f
                ),
                radius = Random.nextInt(1, 5) * Random.nextFloat(),
                center = Offset(
                    Random.nextInt(size.width.toInt()).toFloat(),
                    Random.nextInt(size.height.toInt()).toFloat(),
                )
            )
        }
    }
}