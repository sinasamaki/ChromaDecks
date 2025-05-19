package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.createShape
import com.sinasamaki.chromadecks._002_PathAnimations.createSpring
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.nextFloat
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Pink500
import com.sinasamaki.chromadecks.ui.theme.Purple600
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Rose600
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow700
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import kotlin.math.sin
import kotlin.random.Random

enum class IntroAnimation {
    Initial,
    PathIn,
    PathOut,
}

data class PathIntroState(
    val state: IntroAnimation = IntroAnimation.Initial
)

class PathIntroSlide : ListSlideAdvanced<PathIntroState>() {

    override val initialState: PathIntroState
        get() = PathIntroState(state = IntroAnimation.PathOut)

    override val stateMutations: List<PathIntroState.() -> PathIntroState>
        get() = listOf(
//            { copy(state = IntroAnimation.PathIn) },
//            { copy(state = IntroAnimation.PathOut) },
        )

    @Composable
    override fun content(state: PathIntroState) {

        Box(
            Modifier
                .fillMaxSize()
                .background(Zinc200)
        ) {

            val infinite = rememberInfiniteTransition()
            val progress by infinite.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 5_000
                    )
                )
            )


            TitleFrame(
                modifier = Modifier
                    .scale(
                        lerp(
                            .5f, 1f, progress
                        )
                    )
                    .background(
                        Slate50,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .alpha(
                        when (state.state) {
                            IntroAnimation.Initial -> 0f
                            IntroAnimation.PathIn -> 0f
                            IntroAnimation.PathOut -> 1f
                        }
                    )
                    .fillMaxSize(),
                title = "path \nanimations",
                description = "creating path objects\n" +
                        "& animating along them",
                hint = "polarLineTo()",
                bookNumber = 2,
                contentColor = Slate950
            )

            listOf(
                Green500,
                Rose600,
                Purple600,
                Orange400,
                Red500,
                Yellow700,
            ).forEachIndexed { i, color ->
                val sides = i + 3
                FlyingShape(
                    color = color,
                    progress = progress
                ) {
                    createShape(sides, 300f, center)
                }
            }

            FlyingShape(
                color = Green500,
                progress = progress
            ) {
                createSpring(
                    center - Offset(400f, 0f),
                    center + Offset(400f, 0f),
                    radius = 100f
                )
            }


            FlyingShape(
                color = White,
                progress = progress
            ) {
                Path().apply {
                    addOval(
                        oval = Rect(
                            center = center,
                            radius = 300f
                        )
                    )
                }
            }


            val duration = remember { 1_000 }
            val pathAdvance by animateFloatAsState(
                targetValue = when (state.state) {
                    IntroAnimation.Initial -> 0f
                    IntroAnimation.PathIn -> 1f
                    IntroAnimation.PathOut -> 1f
                },
                animationSpec = tween(
                    durationMillis = duration
                )
            )

            val pathPhase by animateFloatAsState(
                targetValue = when (state.state) {
                    IntroAnimation.Initial -> 0f
                    IntroAnimation.PathIn -> 0f
                    IntroAnimation.PathOut -> 1f
                },
                animationSpec = tween(
                    durationMillis = duration
                )
            )

//            TestAnimation()

            Box(
                Modifier
                    .fillMaxSize()
                    .drawWithCache {

                        val canvasSize = size
//                        val points = buildList {
//                            for (i in 0..100) {
//                                add(
//                                    Offset(
//                                        x = Random.nextInt(
//                                            (canvasSize.width * -.1f).toInt(),
//                                            (canvasSize.width * 1.1f).toInt(),
//                                        ).toFloat(),
//                                        y = Random.nextInt(
//                                            (canvasSize.height * -.1f).toInt(),
//                                            (canvasSize.height * 1.1f).toInt(),
//                                        ).toFloat(),
//                                    )
//                                )
//                            }
//                        }
//
//                        val (path, _) = createPath(points)

                        val path = Path()

                        for (i in 0..10) {
                            path.moveTo(size.center)
                            for (j in 0..4) {
                                val jFloat = j / 3f
                                path.polarLineTo(
                                    ((i / 10f) * 360) + (jFloat * 100),
                                    (size.width / 2) * jFloat,
                                    origin = size.center
                                )
                            }
                        }

                        val measure = PathMeasure().apply { setPath(path, false) }
                        val length = measure.length

                        onDrawBehind {

                            drawPath(
                                path = path,
                                color = Sky600,
                                style = Stroke(
                                    width = 40.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                    pathEffect = PathEffect.chainPathEffect(
                                        inner = PathEffect.cornerPathEffect(100.dp.toPx()),
                                        outer = PathEffect.dashPathEffect(
                                            intervals = floatArrayOf(
                                                length * pathAdvance, length
                                            ),
                                            phase = length * pathPhase
                                        )
                                    )
                                )

                            )

                        }
                    }
            )
        }

    }

}

@Composable
private fun FlyingShape(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float,
    shape: DrawScope.() -> Path,
) {

    val target by remember {
        derivedStateOf {
            Offset(
                x = Random.nextFloat(-2f, 1.5f),
                y = Random.nextFloat(-2f, 1.5f),
            )
        }
    }

    val start by remember {
        derivedStateOf {
            Offset(
                x = Random.nextFloat(.1f, .9f),
                y = Random.nextFloat(.1f, .9f),
            )
        }
    }

    val rotation by remember {
        derivedStateOf {
            Random.nextFloat(90f, 360f * 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                val offset = androidx.compose.ui.geometry.lerp(
                    start = start,
                    stop = target.times(size),
                    fraction = progress,
                )
                translationX = offset.x
                translationY = offset.y

                rotationZ = lerp(0f, rotation, progress)
                rotationY = lerp(0f, 90f, progress)
                rotationX = lerp(0f, 90f, progress)

                val scale = lerp(1f, .3f, progress)
                scaleX = scale
                scaleY = scale
            }
            .drawBehind {
                val shape = this.shape()

                drawPath(
                    path = shape,
                    color = color,
                    alpha = .9f,
                    blendMode = BlendMode.Hardlight
                )

                drawPath(
                    path = shape,
                    color = color,
                    alpha = .3f,
                    blendMode = BlendMode.Hardlight,
                    style = Stroke(
                        width = 80f,
                        join = StrokeJoin.Round
                    )
                )

                drawPath(
                    path = shape,
                    color = color,
                    alpha = .7f,
                    style = Stroke(
                        width = 4f,
                        join = StrokeJoin.Round
                    )
                )
            }
    )


}


@Composable
private fun TestAnimation() {

//    val infinite = rememberInfiniteTransition()
//
//    val x by infinite.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1_000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart,
//        )
//    )

    val x = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
//            x.snapTo(0f)
            x.animateTo(Random.nextFloat(), tween(200))
//            delay(500)
        }
    }

    var center by remember { mutableStateOf(Offset.Zero) }

//    val x = xA.value

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        center = offset
                    }
                )
            }
            .drawWithCache {
                val canvasSize = size
                val path0 = RandomPath(canvasSize, center)
                val path1 = RandomPath(canvasSize, center)
                val path2 = RandomPath(canvasSize, center)
                val path3 = RandomPath(canvasSize, center)

                val (wavePath, _) = createPath(
                    points = listOf(
                        Offset(size.width * -0.5f, 500f),
                        Offset(size.width * 0.0f, 500f),
                        Offset(size.width * 0.25f, -100f),
                        Offset(size.width * 0.75f, 80f),
                        Offset(size.width * 1.0f, -580f),
                        Offset(size.width * 1.5f, 0f),
                    )
                )

                val length = PathMeasure().let {
                    it.setPath(wavePath, false)
                    it.length
                }

                val paths = buildList {
                    val lines = 8
                    for (i in 0..lines) {
                        val yProgress = i / lines.toFloat()
                        add(
                            wavePath.copy().apply {
                                translate(
                                    Offset(
                                        yProgress * -100f, (canvasSize.height * 1.5f) * yProgress
                                    )
                                )
                            }
                        )
                    }
                }

                val p = paths
                    .mapIndexed { i, it -> it to i }
                    .shuffled()


                val orb: MutableList<Pair<Path, PathMeasure>> = mutableListOf()

                for (i in 0..50) {
                    val path = Path()
                    path.moveTo(center)
                    path.polarLineTo(
                        degrees = Random.nextFloat() * 360f,
                        distance = 300.dp.toPx(),
                        origin = center
                    )
                    orb.add(
                        path to PathMeasure().apply { setPath(path, false) }
                    )
                }

                onDrawBehind {

//                    path0.drawRandomPath(
//                        drawScope = this,
//                        colors = listOf(
//                            Black,
//                            Slate50,
//                        ),
//                        x = x.value,
//                    )
//
//
//                    path1.drawRandomPath(
//                        drawScope = this,
//                        colors = listOf(
//                            Sky500,
//                            Yellow500,
//                        ),
//                        x = x.value
//                    )
//
//                    path2.drawRandomPath(
//                        drawScope = this,
//                        colors = listOf(
//                            Yellow500,
//                            Red500,
//                        ),
//                        x = x.value
//                    )
//
//                    path3.drawRandomPath(
//                        drawScope = this,
//                        colors = listOf(
//                            Pink500,
//                            Indigo500,
//                        ),
//                        x = x.value
//                    )


//                    p.forEachIndexed { i, it ->
//                        val progressColor = (it.second / paths.size.toFloat())
//                        val progress = (i / paths.size.toFloat())
//                        drawPath(
//                            path = it.first,
//                            color = androidx.compose.ui.graphics.lerp(
//                                Lime500, Teal800,
//                                progressColor
//                            ),
//                            style = Stroke(
//                                width = 200.dp.toPx(),
//                                cap = StrokeCap.Round,
//                                pathEffect = PathEffect.dashPathEffect(
//                                    intervals = floatArrayOf(
//                                        length * (1f - (x.value)) * (1f + (progress * 1.6f)),
//                                        length
//                                    ),
//                                )
//                            )
//                        )
//                    }

                    orb.forEachIndexed { index, (path, measure) ->

                        val distance =
                            (measure.length * sin(((index * 10f) / orb.size) * x.value)).coerceIn(
                                0.1f,
                                measure.length
                            )
                        drawPath(
                            path = path,
                            color = Pink500,
                            style = Stroke(
                                width = 1f,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        distance,
                                        measure.length
                                    )
                                )
                            )
                        )

                        drawCircle(
                            color = Pink500,
                            center = measure.getPosition(distance),
                            radius = 10.dp.toPx() * sin(((index * 10f) / orb.size) * x.value),
                            style = Stroke()
                        )
                    }


                }
            }
    )


}

private class RandomPath(size: Size, center: Offset) {

    val path: Path
    val length: Float
    val measure: PathMeasure

    init {
        val canvasSize = size
        val points = buildList {
            add(center)
            for (i in 0..5) {
                add(
                    Offset(
                        x = Random.nextInt(
                            (canvasSize.width * -.1f).toInt(),
                            (canvasSize.width * 1.1f).toInt(),
                        ).toFloat(),
                        y = Random.nextInt(
                            (canvasSize.height * -.1f).toInt(),
                            (canvasSize.height * 1.1f).toInt(),
                        ).toFloat(),
                    )
                )
            }
        }

        val (path, _) = createPath(points)

        measure = PathMeasure().apply { setPath(path, false) }
        length = measure.length
        this.path = path
    }

    fun drawRandomPath(
        drawScope: DrawScope,
        colors: List<Color>,
        x: Float,
        strokeWidth: Dp = 18.dp,
    ) {
        drawScope.apply {

            val iter = 1000
            for (i in 0..iter) {
                val xProg = i / iter.toFloat()
                if (xProg < 1f - x)
                    drawCircle(
                        color = androidx.compose.ui.graphics.lerp(
                            colors[0],
                            colors[1],
                            xProg,
                        ),
//                        brush = Brush.verticalGradient(
//                            colors = androidx.compose.ui.graphics.lerp(
//                                colors[0],
//                                colors[1],
//                                xProg,
//                            ),
//                        ),
                        center = measure.getPosition(xProg * length),
                        radius = lerp(10.dp.toPx(), 200.dp.toPx(), xProg)
                    )
            }

//            drawPath(
//                path = path,
//                brush = Brush.verticalGradient(
//                    colors = colors
//                ),
//                style = Stroke(
//                    width = strokeWidth.toPx(),
//                    cap = StrokeCap.Round,
//                    join = StrokeJoin.Round,
//                    pathEffect = PathEffect.chainPathEffect(
//                        inner = PathEffect.cornerPathEffect(100.dp.toPx()),
//                        outer = PathEffect.dashPathEffect(
//                            intervals = floatArrayOf(
//                                length * (1f - x), length
//                            ),
//                        )
//                    )
//                ),
//            )
        }
    }
}