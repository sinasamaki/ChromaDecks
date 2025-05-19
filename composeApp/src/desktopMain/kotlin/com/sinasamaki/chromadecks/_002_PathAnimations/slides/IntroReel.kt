package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.relativePolarLineTo
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.nextFloat
import com.sinasamaki.chromadecks.extensions.times
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Orange800
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Sky300
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class IntroReelState()
class IntroReel : ListSlideAdvanced<IntroReelState>() {

    override val initialState: IntroReelState
        get() = IntroReelState()

    override val stateMutations: List<IntroReelState.() -> IntroReelState>
        get() = listOf()

    @Composable
    override fun content(state: IntroReelState) {

        Vizz(Modifier.fillMaxSize())
        return
        Text(
            text = "IntroReel Slide",
            style = MaterialTheme.typography.labelLarge
        )


        Column {
            LazyRow {

                item {
                    Box(
                        Modifier
                            .size(500.dp)
                            .drawBehind {
                                drawRect(
                                    color = Orange800,
                                )

                                val square = Path()
                                val circle = Path()

//                            square.addRect(
//                                rect = Rect(
//                                    center = center,
//                                    radius = 200f
//                                )
//                            )

//                            square.polarMoveTo(
//                                degrees = 270f,
//                                distance = 100f,
//                                center,
//                            )
//                            for (i in 1..4) {
//                                square.relativePolarLineTo(
//                                    degrees = 90f,
//                                    distance = 100f,
//                                )
//                            }


                                square.addOval(
                                    oval = Rect(
                                        offset = center,
                                        size = Size(200f, 300f)
                                    )
                                )
                                circle.addOval(
                                    oval = Rect(
                                        center = center,
                                        radius = 400f
                                    )
                                )


//                            circle.transform(
//                                Matrix().apply {
////                                    translate(x = 200f)
////                                    rotate(10f, Offset.Zero)
////                                    rotate(1f, center)
//                                }
//                            )

                                val squareMeasure = PathMeasure()
                                squareMeasure.setPath(square, true)

                                val circleMeasure = PathMeasure()
                                circleMeasure.setPath(circle, true)

                                val loops = 20
                                val res = 100

                                for (loop in 0..loops) {

                                    val loopPath = Path()

                                    for (p in 0..res) {
                                        val t = p / res.toFloat()

                                        val p1 = squareMeasure.getPosition(t * squareMeasure.length)
                                        val p2 = circleMeasure.getPosition(t * circleMeasure.length)


                                        lerp(
                                            start = p1,
                                            stop = p2,
                                            fraction = loop / loops.toFloat(),
                                        ).let {
                                            when (p) {
                                                0 -> loopPath.moveTo(it)
                                                else -> loopPath.lineTo(it)
                                            }
                                        }

                                        drawLine(
                                            color = Orange500.copy(alpha = .1f),
                                            start = p1,
                                            end = p2,
                                        )

                                        if (p == 0) {
                                            drawPoints(
                                                points = listOf(p1, p2),
                                                color = Orange400,
                                                pointMode = PointMode.Points,
                                                strokeWidth = 30f,
                                            )
                                        }

                                    }
                                    drawPath(
                                        path = loopPath,
                                        color = Sky400.copy(
                                            alpha = androidx.compose.ui.util.lerp(
                                                start = 0f,
                                                stop = 1f,
                                                fraction = loop / loops.toFloat(),
                                            )
                                        ),
                                        style = Stroke(
                                            width = 1.dp.toPx()
                                        )
                                    )
                                }

                                listOf(square, circle).forEach { path ->
                                    drawPath(
                                        path = path,
                                        color = Sky400,
                                        style = Stroke(
                                            width = 1.dp.toPx()
                                        )
                                    )
                                }
                            }
                    )
                }

                item {
                    Box(
                        Modifier
                            .size(500.dp)
                            .drawBehind {
                                drawRect(
                                    color = Zinc900,
                                )

                                val triangle = Path()

//                            triangle.polarMoveTo(
//                                origin = center,
//                                degrees = 0f,
//                                distance = 0f
//                            )

                                triangle.moveTo(center)

                                triangle.relativePolarLineTo(
                                    30f,
                                    200f,
                                )

                                triangle.relativePolarLineTo(
                                    120f,
                                    300f,
                                )

                                triangle.close()

                                triangle.iterator().forEach { it ->

                                    it.weight

                                }

//                            triangle.relativePolarLineTo(
//                                30f,
//                                200f,
//                            )

//                            triangle.polarLineTo(
//                                origin = center,
//                                degrees = 180f,
//                                distance = 200f
//                            )
//
//                            triangle.polarLineTo(
//                                origin = center,
//                                degrees = 270f,
//                                distance = 200f
//                            )
//
//                            triangle.close()

                                drawCircle(
                                    color = Yellow300,
                                    center = center,
                                    style = Stroke(
                                        width = 2.dp.toPx()
                                    ),
                                    radius = 200f
                                )

                                drawPath(
                                    path = triangle,
                                    color = Sky300,
                                    style = Stroke(
                                        width = 1.dp.toPx()
                                    )
                                )


                            }
                    )
                }

                item {


                    val infinite = rememberInfiniteTransition()
                    val animation by infinite.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 1000,
                                easing = LinearEasing,
                            )
                        )
                    )

                    Box(
                        Modifier
                            .size(500.dp)
                            .drawBehind {
                                drawRect(Slate950)

                                sinwave(animation)
//                            sinwave(animation, -1f)
                            }
                    )
                }


            }


        }

    }

    private fun DrawScope.sinwave(animation: Float, offset: Float = 1f) {
        val res = 100
        val path = Path()
        path.moveTo(0f, center.y)
        var y0 = 0f
        for (i in 0..res) {
            val prog = (i / res.toFloat())
            var y = 100 * sin(prog * 20 - (animation * 3.14f * 2 * offset))
            y = y * when {
                prog < .5f -> prog / .5f
                else -> (.5f - (prog - .5f)) / .5f
            }
            y += center.y
            path.lineTo(
                x = (prog) * size.width,
                y = y,
            )
            if (i == 0) y0 = y
        }

        val gradient = Path()
        gradient.moveTo(0f, size.height)
        gradient.lineTo(0f, y0)
        gradient.addPath(path)
        gradient.lineTo(size.width, size.height)
        gradient.lineTo(0f, size.height)
        gradient.close()

        drawPath(
            gradient,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Red500.copy(alpha = .1f),
                    Red500.copy(alpha = .1f),
                    Red500.copy(alpha = 0f),
                    Red500.copy(alpha = 0f),
                )
            )
        )

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Slate50,
                    Slate50.copy(alpha = 0f)
                )
            ),
            style = Stroke(
                width = 4.dp.toPx()
            )
        )
    }
}


@Composable
fun Vizz(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition()


    val scope = rememberCoroutineScope()
    var y1 by remember { mutableStateOf(0f) }
    var y2 by remember { mutableStateOf(0f) }
    var y3 by remember { mutableStateOf(0f) }
    var y4 by remember { mutableStateOf(0f) }
//    DisposableEffect(Unit) {
//        println("START")
//        val audioInputStream = getMicrophoneInputStream()
//        val job = scope.launch {
//            if (audioInputStream == null) {
//                println("Failed to obtain audio input stream. Exiting.")
//            } else {
//                processAudioData(audioInputStream, 4).collect { frequencies ->
//                    y1 = -frequencies[0].toFloat() / 1.8f
//                    y2 = frequencies[1].toFloat() / 1f
//                    y3 = -frequencies[2].toFloat() / .6f
//                    y4 = frequencies[3].toFloat() / .1f
//                }
//            }
//        }
//
//        onDispose {
//            println("DISPOSING . . . ")
//            scope.launch { job.cancelAndJoin() }
////            job.cancel()
//            audioInputStream?.close()
//            println(" = = = = ")
//            println("DISPOSED!!")
//        }
//    }

    LaunchedEffect(
        Unit
    ) {
        while (true) {
            delay(100)
            y1 = Random.nextFloat(.1f, .4f)
            y2 = -Random.nextFloat(.1f, .4f)
            y3 = Random.nextFloat(.1f, .4f)
            y4 = -Random.nextFloat(.1f, .4f)
        }
    }

    var spring = remember {
        spring<Float>(stiffness = Spring.StiffnessMediumLow, visibilityThreshold = .0000001f)

//        snap<Float>()
    }
    val animatedY1 by animateFloatAsState(
        targetValue = y1,
        animationSpec = spring,
        visibilityThreshold = .0000001f
    )
    val animatedY2 by animateFloatAsState(
        targetValue = y2,
        animationSpec = spring,
        visibilityThreshold = .0000001f
    )
    val animatedY3 by animateFloatAsState(
        targetValue = y3,
        animationSpec = spring,
        visibilityThreshold = .0000001f
    )
    val animatedY4 by animateFloatAsState(
        targetValue = y4,
        animationSpec = spring,
        visibilityThreshold = .0000001f
    )

//    val y1 by infinite.animateFloat(
//        initialValue = -.5f,
//        targetValue = .5f,
//        animationSpec = infiniteRepeatable(
//            repeatMode = RepeatMode.Reverse,
//            animation = tween(
//                durationMillis = remember { Random.nextInt(1000, 4000) },
//                easing = FastOutSlowInEasing
//            )
//        )
//    )

//    val y4 by infinite.animateFloat(
//        initialValue = -.5f,
//        targetValue = .5f,
//        animationSpec = infiniteRepeatable(
//            repeatMode = RepeatMode.Reverse,
//            animation = tween(
//                durationMillis = remember { Random.nextInt(1000, 4000) },
//                easing = FastOutSlowInEasing
//            )
//        )
//    )
//
//    val y2 by infinite.animateFloat(
//        initialValue = -.5f,
//        targetValue = .5f,
//        animationSpec = infiniteRepeatable(
//            repeatMode = RepeatMode.Reverse,
//            animation = tween(
////                durationMillis = remember { Random.nextInt(1000, 2000) },
//                durationMillis = 10_000,
//                easing = FastOutSlowInEasing
//            )
//        )
//    )
//
//    val y3 by infinite.animateFloat(
//        initialValue = -.5f,
//        targetValue = .5f,
//        animationSpec = infiniteRepeatable(
//            repeatMode = RepeatMode.Reverse,
//            animation = tween(
////                durationMillis = remember { Random.nextInt(1000, 2000) },
//                durationMillis = 10_000,
//                easing = FastOutSlowInEasing
//            )
//        )
//    )

    val points by remember {
        derivedStateOf {
            buildList {
                val div = 5
                listOf(animatedY1, animatedY2, animatedY3, animatedY4).forEachIndexed { index, y ->
                    val frac = (index + 1) / 6f
                    add(
                        Offset(
                            frac,
                            y
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(500.dp)
            .width(1000.dp)
            .drawBehind {
                drawRect(
                    color = Black,
                )


                val canvasSize = size
//                val path1 = Path()
//                path1.addPath(
//                    createPath(
//                        points = points.map {
//                            Offset(
//                                x = it.x * size.width,
//                                y = it.y * size.height
//                            )
//                        }
//                    ).first
//                )
//                path1.lineTo(size.width, 0f)
//                val path1 = createPath(
//                    points = points.map {
//                        Offset(
//                            x = it.x * size.width,
//                            y = it.y * size.height
//                        )
//                    }
//                ).first

                val path1 = Path().apply {
                    points.forEachIndexed { index, point ->
                        lineTo(point.times(size))
                    }
                    lineTo(size.width, 0f)
                }


                val path2 = path1.copy().apply {
                    transform(
                        matrix = Matrix().apply {
                            scale(y = -1f)
                        }
                    )
                }

                path1.translate(Offset(x = 0f, y = center.y))
                path2.translate(Offset(x = 0f, y = center.y))


                val measure1 = PathMeasure()
                measure1.setPath(path1, false)

                val measure2 = PathMeasure()
                measure2.setPath(path2, false)

                val loops = 50
                val res = 4

                val loopMeasure = PathMeasure()
                for (loop in 0..loops) {

                    val loopPath = Path()

                    for (p in 0..res) {
                        val t = p / res.toFloat()

                        val p1 = measure1.getPosition(t * measure1.length)
                        val p2 = measure2.getPosition(t * measure2.length)


                        lerp(
                            start = p1,
                            stop = p2,
                            fraction = loop / loops.toFloat(),
                        ).let {
                            when (p) {
                                0 -> loopPath.moveTo(it)
                                else -> loopPath.lineTo(it)
                            }
                        }
                    }
                    drawPath(
                        path = loopPath,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Red500,
                                Yellow300,
                                Rose400,
                            )
                        ),
                        alpha = 1f,
                        style = Stroke(
//                                    width = 1.dp.toPx()
                            width = 4f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round,
                            pathEffect = PathEffect.cornerPathEffect(1000f)
                        )
                    )
                    loopMeasure.setPath(loopPath, false)
                    val circles = 5
                    val length = loopMeasure.length / circles.toFloat()
                    for (i in 0..circles) {
//                        val pos =
//                            loopMeasure.getPosition(loopMeasure.length * (i / circles.toFloat()))
//                        val start = loopMeasure.length * (i / circles.toFloat())
//
//                        val segment = Path()
//                        loopMeasure.getSegment(
//                            startDistance = start,
//                            stopDistance = start + length,
//                            destination = segment
//                        )
//                        drawPath(
//                            path = segment,
//                            color = androidx.compose.ui.graphics.lerp(
//                                start = Rose400, stop = Yellow500,
//                                fraction = i / circles.toFloat()
//                            ),
//                            style = Stroke(
//                                width = androidx.compose.ui.util.lerp(
//                                    start = 0f,
//                                    stop = 30f,
//                                    fraction = (pos.y - (size.height / 2)).absoluteValue / (size.height / 2)
//                                ),
//                                cap = StrokeCap.Round,
//                                join = StrokeJoin.Round,
//                                pathEffect = PathEffect.cornerPathEffect(1000f)
//                            )
//                        )
//                        drawCircle(
//                            color = Lime400,
//                            center = pos,
//                            radius = 10f,
////                            radius = androidx.compose.ui.util.lerp(
////                                start = 0f,
////                                stop = 30f,
////                                fraction = (pos.y - (size.height / 2)).absoluteValue / (size.height / 2)
////                            ),
//                            alpha = androidx.compose.ui.util.lerp(
//                                start = 1f,
//                                stop = 0f,
//                                fraction = (pos.y - (size.height / 2)).absoluteValue / (size.height / 2)
//                            )
//                        )
                    }

                }

//                        listOf(path1, path2).forEach { path ->
//                            drawPath(
//                                path = path,
//                                color = Sky400,
//                                style = Stroke(
//                                    width = 3.dp.toPx(),
//                                    pathEffect = PathEffect.cornerPathEffect(1000f)
//
//                                )
//                            )
//                        }
            }
    )
}


fun Matrix.rotate(degrees: Float, pivot: Offset) {
    val radians = degrees * PI / 180.0
    val c = cos(radians).toFloat()
    val s = sin(radians).toFloat()

    val a00 = this[0, 0];
    val a10 = this[1, 0]
    val v00 = c * a00 + s * a10
    val v10 = -s * a00 + c * a10

    val a01 = this[0, 1];
    val a11 = this[1, 1]
    val v01 = c * a01 + s * a11
    val v11 = -s * a01 + c * a11

    val a02 = this[0, 2];
    val a12 = this[1, 2]
    val v02 = c * a02 + s * a12
    val v12 = -s * a02 + c * a12

    val a03 = this[0, 3];
    val a13 = this[1, 3]

    val tx = a03 - pivot.x
    val ty = a13 - pivot.y

    val rotatedTx = c * tx + s * ty
    val rotatedTy = -s * tx + c * ty

    val v03 = pivot.x + rotatedTx
    val v13 = pivot.y + rotatedTy

    this[0, 0] = v00; this[1, 0] = v10
    this[0, 1] = v01; this[1, 1] = v11
    this[0, 2] = v02; this[1, 2] = v12
    this[0, 3] = v03; this[1, 3] = v13
}

suspend fun audioTest() {
    try {
        val audioFormat =
            AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 2, 4, 44100f, false)

        val dataInfo = DataLine.Info(TargetDataLine::class.java, audioFormat)

        if (!AudioSystem.isLineSupported(dataInfo)) {
            print("NOT SUPPORTED!!")
        }

        val targetLine: TargetDataLine = AudioSystem.getLine(dataInfo) as TargetDataLine
        targetLine.open()

        for (i in 3 downTo 1) {
            print("$i")
            delay(1000)
        }

        print("start")

        targetLine.start()

        val inputStream = AudioInputStream(targetLine)

//        inputStream.
    } catch (e: Exception) {
        print(e.message)
    }
}