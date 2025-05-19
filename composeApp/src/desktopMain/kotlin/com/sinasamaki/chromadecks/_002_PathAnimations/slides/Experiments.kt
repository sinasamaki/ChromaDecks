package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks._002_PathAnimations.centerPath
import com.sinasamaki.chromadecks._002_PathAnimations.createInBetweenPaths
import com.sinasamaki.chromadecks._002_PathAnimations.createPath
import com.sinasamaki.chromadecks._002_PathAnimations.createRoseCurve
import com.sinasamaki.chromadecks._002_PathAnimations.getDegrees
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarToCart
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.nextFloat
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.components.createHeartPath
import com.sinasamaki.chromadecks.ui.components.createHeartPath2
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Amber500
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Emerald300
import com.sinasamaki.chromadecks.ui.theme.Emerald600
import com.sinasamaki.chromadecks.ui.theme.Fuchsia200
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import com.sinasamaki.chromadecks.ui.theme.Fuchsia700
import com.sinasamaki.chromadecks.ui.theme.Gray200
import com.sinasamaki.chromadecks.ui.theme.Green300
import com.sinasamaki.chromadecks.ui.theme.Green400
import com.sinasamaki.chromadecks.ui.theme.Green700
import com.sinasamaki.chromadecks.ui.theme.Indigo300
import com.sinasamaki.chromadecks.ui.theme.Indigo400
import com.sinasamaki.chromadecks.ui.theme.Indigo50
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Indigo700
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Orange300
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Purple300
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Red300
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.Rose300
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Rose50
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky300
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Teal200
import com.sinasamaki.chromadecks.ui.theme.Teal300
import com.sinasamaki.chromadecks.ui.theme.Teal400
import com.sinasamaki.chromadecks.ui.theme.Teal500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.sin
import kotlin.random.Random

internal data class ExperimentsState(
    val index: Int = 2,
)

internal class Experiments : ListSlideAdvanced<ExperimentsState>() {

    override val initialState: ExperimentsState
        get() = ExperimentsState()

    override val stateMutations: List<ExperimentsState.() -> ExperimentsState>
        get() = listOf()

    @Composable
    override fun content(state: ExperimentsState) {
        when (state.index) {
            0 -> {
                CurveLine()
            }

            1 -> {
                LineExplode()
            }

            2 -> {
                ButterfliesTest()
            }

            3 -> {
                TestAnimation()
            }

            4 -> {
                PromoTest().content()
            }

            else -> {}
        }
    }
}

@Composable
private fun ButterfliesTest(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Zinc950)
    ) {

        val initialAnimation = remember { Animatable(0f) }
        val logoAnimation = remember { Animatable(0f) }

//        LaunchedEffect(
//            Unit
//        ) {
//            while (true) {
//                logoAnimation.animateTo(1f, tween(durationMillis = 900))
//                logoAnimation.animateTo(0f, tween(durationMillis = 300))
//                delay(500)
//            }
//        }

        val startMoving by remember {
            derivedStateOf {
                initialAnimation.value >= .9f
            }
        }

        val progress = remember { Animatable(0f) }
        var butterfliesAlpha by remember { mutableStateOf(1f) }
        var logoAlpha by remember { mutableStateOf(1f) }
        var websiteAlpha by remember { mutableStateOf(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                logoAlpha = 0f
                websiteAlpha = 0f
                initialAnimation.snapTo(0f)
                progress.snapTo(0f)
                delay(1000)
                butterfliesAlpha = 1f
                initialAnimation.animateTo(1f, tween(2000))

                launch {
                    delay(18_000)
                    logoAlpha = 1f
                    logoAnimation.animateTo(1f, tween(durationMillis = 900))
                    butterfliesAlpha = 0f
                    websiteAlpha = 1f
                    logoAnimation.animateTo(0f, tween(durationMillis = 300))
                }
                progress.animateTo(1f, tween(19_000, easing = CubicBezierEasing(.5f, 0f, .5f, .3f)))
                progress.animateTo(0f)
                delay(5_000)
            }
        }

        Box(
            modifier = Modifier
                .alpha(logoAlpha)
                .align(
                    androidx.compose.ui.Alignment.Center,
                )
                .width(100.dp)
                .graphicsLayer {
                    val scale = lerp(1f, 1.5f, logoAnimation.value)
                    scaleX = scale
                    scaleY = scale
                }
                .aspectRatio(6 / 9f)
                .drawWithCache {
                    val path = createHeartPath2(size)

                    val path2 = createHeartPath2(size * 5f)
                    path2.centerPath(size.center)

                    val paths = createInBetweenPaths(
                        startPath = path,
                        endPath = path2,
                        loops = 50,
                        res = 100,
                    )
                    onDrawBehind {
//                        drawPath(
//                            path = path,
//                            color = Green400,
//                            style = Stroke()
//                        )

                        paths
                            .take((logoAnimation.value * paths.size).toInt().coerceAtLeast(0))
                            .plus(path)
                            .forEach {
                                drawPath(
                                    path = it,
                                    color = Red500,
                                    alpha = 1f,
                                    style = Stroke(
                                        width = 2f,
                                    )
                                )
                            }
                    }
                }
        )

        Text(
            "sinasamaki.com",
            modifier = Modifier
                .alpha(websiteAlpha)
                .align(Alignment.Center)
                .offset(y = 200.dp),
            color = Red500,
            style = MaterialTheme.typography.displaySmall
        )


        Box(
            modifier = Modifier
                .alpha(butterfliesAlpha)
                .scale(lerp(1f, 3.5f, logoAnimation.value))
        ) {
            listOf(
                Sky300,
                Sky400,
                Sky500,
                Sky600,

                Rose300,
                Rose400,
                Rose500,

                Indigo300,
                Indigo400,
                Indigo500,

                Purple300,
                Purple400,
                Purple500,

                Teal300,
                Teal400,
                Teal500,


                White,

                Pink300,
            ).forEach { color ->
                SingleButterfly(
                    color = color,
                    progress = progress,
                    startMoving = startMoving,
                    initialAnimation = initialAnimation
                )
            }
        }
    }
}

@Composable
fun SingleButterfly(
    modifier: Modifier = Modifier,
    color: Color,
    startMoving: Boolean,
    initialAnimation: Animatable<Float, AnimationVector1D>,
    progress: Animatable<Float, AnimationVector1D>
) {

    var coords by remember {
        mutableStateOf(Pair(Offset.Zero, 0f))
    }


    val position by animateOffsetAsState(
        targetValue = if (startMoving) coords.first else Offset.Zero,
        animationSpec = spring(
            stiffness = Spring.StiffnessVeryLow
        )
    )

    val rotation by animateFloatAsState(
        targetValue = coords.second,
        animationSpec = snap()
    )



    Box(
        modifier
            .fillMaxSize()
            .drawWithCache {
                val canvasSize = size
                val path = createPath(
                    points = buildList {
                        add(Offset(.5f, .5f))
                        add(Offset(.5f, .2f))
                        add(Offset(.8f, .2f))
                        add(Offset(.8f, .5001f))

                        add(
                            Offset(
                                x = Random.nextFloat(.5f, .6f),
                                y = Random.nextFloat(.8f, .9f),
                            )
                        )
                        for (i in 0..20) {
                            add(
                                Offset(
                                    x = Random.nextFloat(-.1f, 1.1f),
                                    y = Random.nextFloat(-.1f, 1.1f),
                                )
                            )
                        }
                    }.map { it.times(canvasSize) }
                ).first

                val measure = PathMeasure()
                measure.setPath(path, false)


                val distance = 18_000f
                val segment = Path()
                measure.getSegment(
                    0f, distance, segment
                )

                measure.setPath(segment, false)

                onDrawBehind {

                    val distance = (measure.length * progress.value)
                    coords = measure.getPosition(distance) - center to getDegrees(
                        measure.getTangent(distance)
                    )
//                    drawPath(
//                        path = segment,
//                        color = Slate50,
//                        style = Stroke(
//                            width = 1f
//                        )
//                    )
                }
            }
    ) {
        val flapAnimation = remember { Animatable(initialValue = 30f) }

        LaunchedEffect(startMoving) {
            while (startMoving) {
                val duration = 100//Random.nextInt(100, 500)
                flapAnimation.animateTo(
                    targetValue = 45f,
                    animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                )
                flapAnimation.animateTo(
                    -45f,
                    animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                )
            }
            flapAnimation.snapTo(0f)
        }

        Box(
            Modifier
                .size(100.dp)
                .align(Alignment.Center)
//                .offset(-50.dp, -50.dp)
                .offset { position.toIntOffset() }
                .rotate(rotation - 90f)
        ) {
            Wing(
                flap = flapAnimation.value,
                isLeft = true,
                strokeAnimation = initialAnimation,
                color = color,
            )
            Wing(
                flap = flapAnimation.value,
                isLeft = false,
                strokeAnimation = initialAnimation,
                color = color,
            )
        }

    }
}

@Composable
private fun Wing(
    modifier: Modifier = Modifier,
    flap: Float,
    isLeft: Boolean,
    strokeAnimation: Animatable<Float, AnimationVector1D>,
    color: Color,
) {
    Box(
        modifier
            .fillMaxSize()
            .graphicsLayer {
                cameraDistance = 13f
                rotationY = flap * if (isLeft) 1 else -1
            }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .scale(1.2f)
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Rectangle(
                                when {
                                    isLeft -> Rect(
                                        topLeft = Offset.Zero,
                                        bottomRight = Offset(size.width / 2, size.height)
                                    )

                                    else -> Rect(
                                        topLeft = Offset(size.width / 2, 0f),
                                        bottomRight = Offset(size.width, size.height)
                                    )
                                }
                            )
                        }
                    }
                }
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .drawBehind {
                        val butterfly = createRoseCurve(
                            center,
                            size.width / 2,
                            petals = 2,
                        )
                        val measure = PathMeasure()
                        measure.setPath(butterfly, false)


                        drawPath(
                            path = butterfly,
                            color = color,
                            style = Stroke(
                                width = 30f,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(
                                        measure.length * strokeAnimation.value, measure.length
                                    )
                                )
                            ),
                            alpha = when {
                                isLeft -> (1f - (flap.coerceAtLeast(0f) / 180f))
                                else -> (1f - (flap.coerceAtMost(0f).absoluteValue / 180f))
                            }
                        )
                    }
            )

//            Image(
//                painter = painterResource(R.drawable.img),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize()
//            )
//            Image(
//                painter = painterResource(R.drawable.img),
//                contentDescription = null,
//                colorFilter = ColorFilter.tint(
//                    color = Color.Black.copy(
//                        alpha = when {
//                            isLeft -> (flap.coerceAtLeast(0f) / 180f)
//                            else -> (flap.coerceAtMost(0f).absoluteValue / 180f)
//                        }
//                    ),
//                    blendMode = BlendMode.SrcIn
//                ),
//                modifier = Modifier.fillMaxSize(),
//            )
        }
    }
}

@Composable
private fun LineExplode(modifier: Modifier = Modifier) {
    var points by remember {
        mutableStateOf(
            buildList {
                add(
                    Offset(
                        x = 0f,
                        y = 0f,
                    )
                )
                add(
                    Offset(
                        x = .5f,
                        y = .5f
                    )
                )

                add(
                    Offset(
                        x = 1f,
                        y = 0f
                    )
                )
            },
        )
    }

    val curvatureAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            curvatureAnimatable.snapTo(0f)
            delay(500)
            curvatureAnimatable.animateTo(
                .9f,
                animationSpec = tween(
                    900,
                    easing = CubicBezierEasing(0f, .2f, 0f, 1f),
                )
            )
            delay(500)
            points = buildList {
                val startDegrees = Random.nextFloat(0f, 360f)
                val start = polarToCart(startDegrees, 1.1f, Offset(.5f, .5f))
                add(start)
                add(
                    Offset(
                        x = .5f,
                        y = .5f,
                    )
                )
                val endDegrees = startDegrees + Random.nextFloat(60f, 120f)
                val end = polarToCart(endDegrees, 1.1f, Offset(.5f, .5f))
                add(end)

            }
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .drawWithCache {
                val lineColor = Zinc950
                val spotColor = Zinc50
                val (path, controls) = createPath(
                    points = points.map { it.times(size) },
                    curvature = curvatureAnimatable.value
                )
                onDrawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc50,
                                Teal200,
                            )
                        )
                    )
                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(
                            width = 40f,
                            join = StrokeJoin.Round
                        )
                    )

                    controls.forEach { control ->

                        drawLine(
                            color = lineColor,
                            start = control.first,
                            end = control.second,
                            strokeWidth = 40f,
                        )

                        drawPoints(
                            points = listOf(control.first, control.second),
                            color = lineColor,
                            pointMode = PointMode.Points,
                            cap = StrokeCap.Round,
                            strokeWidth = 80f,
                        )

                        drawPoints(
                            points = listOf(control.first, control.second),
                            color = spotColor,
                            pointMode = PointMode.Points,
                            cap = StrokeCap.Round,
                            strokeWidth = 40f,
                        )


                    }
                }
            }
    )
}

@Composable
private fun CurveLine(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition()
    val y by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {

                val bottomPath = Path()

                bottomPath.moveTo(
                    x = 0f,
                    y = size.height,
                )

                bottomPath.lineTo(
                    x = size.width,
                    y = size.height
                )

                val topPath = Path()

                topPath.moveTo(
                    x = size.height * 0.0f,
                    y = 0f,
                )

                topPath.cubicTo(
                    x1 = size.width * .25f, y1 = size.height * .25f,
                    x2 = size.width * .75f, y2 = -size.height * 1.5f * y,
                    x3 = size.width * .99f, y3 = size.height * .05f,
                )

                val paths = createInBetweenPaths(
                    startPath = bottomPath,
                    endPath = topPath,
                    loops = 60
                )

                onDrawBehind {
                    drawRect(
                        color = Zinc950
                    )
                    rotate(
                        degrees = 90f * 0
                    ) {
                        scale(
                            scale = 1.5f,
                            pivot = Offset(
                                size.width * .9f, size.height
                            )
                        ) {
                            paths.forEach { path ->
                                drawPath(
                                    path = path,
                                    color = Teal200,
                                    style = Stroke(
                                        width = 40f
                                    )
                                )
                            }

                        }
                    }
                }
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
            x.animateTo(Random.nextFloat(), tween(100))
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
//                val path0 = RandomPath(canvasSize, center)
//                val path1 = RandomPath(canvasSize, center)
//                val path2 = RandomPath(canvasSize, center)
//                val path3 = RandomPath(canvasSize, center)

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

                    drawRect(color = Black)
                    orb.forEachIndexed { index, (path, measure) ->

                        val distance =
                            (measure.length * sin(((index * 10f) / orb.size) * (x.value * .2f))).coerceIn(
                                0.4f,
                                measure.length
                            )
                        drawPath(
                            path = path,
                            color = Sky400,
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
                            color = Sky400,
                            center = measure.getPosition(distance),
                            radius = 10.dp.toPx() * sin(((index * 10f) / orb.size) * x.value),
//                            style = Stroke()
                        )
                    }


                }
            }
    )


}
