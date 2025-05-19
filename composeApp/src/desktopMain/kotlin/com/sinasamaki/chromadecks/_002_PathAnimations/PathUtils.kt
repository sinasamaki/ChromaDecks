package com.sinasamaki.chromadecks._002_PathAnimations

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.ui.theme.Red500
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import androidx.compose.ui.geometry.lerp as lerpOffset

fun Path.moveTo(offset: Offset) = moveTo(offset.x, offset.y)
fun Path.lineTo(offset: Offset) = lineTo(offset.x, offset.y)
fun Path.cubicTo(
    control1: Offset,
    control2: Offset,
    offset: Offset,
) = cubicTo(
    control1.x, control1.y,
    control2.x, control2.y,
    offset.x, offset.y,
)

fun Path.relativeLineTo(offset: Offset) = relativeLineTo(offset.x, offset.y)
fun Path.quadraticTo(
    control1: Offset,
    offset: Offset,
) = quadraticTo(
    control1.x, control1.y,
    offset.x, offset.y,
)

fun DrawScope.scalePath(
    path: Path,
    scaleX: Float = 100f,
    scaleY: Float = 100f,
) = path.copy().apply {
    transform(
        Matrix().apply {
            scale(
                x = size.width / scaleX,
                y = size.height / scaleY,
            )
        }
    )
}

fun createPath(
    points: List<Offset>,
    curvature: Float = 1f,
): Pair<Path, List<Pair<Offset, Offset>>> {
    if (points.size < 2) return Path() to listOf()
    if (curvature < 0.001f) {
        return Path().apply {
            points.forEachIndexed { i, point ->
                when (i) {
                    0 -> moveTo(point)
                    else -> lineTo(point)
                }
            }
        } to points.map {
            it to it
        }
    }
    val controlPoints = mutableListOf<Pair<Offset, Offset>>()
    val path = Path().apply {
        moveTo(points[0])
        var nextControl1 = Offset.Zero
        for (i in 1..<points.lastIndex) {
            val mid1 = ((points[i] + points[i - 1]) / 2f) * curvature
            val mid2 = ((points[i] + points[i + 1]) / 2f) * curvature
            val mid3 = (mid1 + mid2) / 2f
            val delta = (mid3 - points[i])// * curvature
            if (i == 1) {
                val control1 = points[0] - delta
                val control2 = mid1 - delta
                cubicTo(
                    control1,
                    control2,
                    points[i]
                )
//                controlPoints.add(points[0] to control1)
                nextControl1 = mid2 - delta
                controlPoints.add(control2 to nextControl1)
            } else {
                val control2 = mid1 - delta
                cubicTo(
                    nextControl1,
                    control2,
                    points[i]
                )
                nextControl1 = mid2 - delta
                controlPoints.add(control2 to nextControl1)
            }
//                lineTo(points[i])
        }
        quadraticTo(
            nextControl1,
            points.last()
        )
//        controlPoints.add(nextControl1 to points.last())

    }

    return path to controlPoints
}

fun polarToCart(
    degrees: Float,
    distance: Float,
    origin: Offset = Offset.Zero
): Offset = Offset(
    x = distance * cos(-degrees * (PI / 180)).toFloat(),
    y = distance * sin(-degrees * (PI / 180)).toFloat(),
) + origin


fun Path.polarMoveTo(
    degrees: Float,
    distance: Float,
    origin: Offset = Offset.Zero
) {
    moveTo(polarToCart(degrees, distance, origin))
}

fun Path.polarLineTo(
    degrees: Float,
    distance: Float,
    origin: Offset = Offset.Zero
) {
    lineTo(polarToCart(degrees, distance, origin))
}

fun Path.relativePolarLineTo(
    degrees: Float,
    distance: Float
) {
    val actualDegrees = -((180f - degrees) + getLastAngle)
    val delta = polarToCart(actualDegrees, distance)
    relativeLineTo(delta)
}

val Path.getLastAngle: Float
    get() {
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(this, false)

        val length = pathMeasure.length
        if (length == 0f) return 180f

        return pathMeasure.getTangent(length).let {
            (atan2(it.y, it.x) * (180 / PI)).toFloat()
        }
    }

val Path.getLastPosition: Offset
    get() {
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(this, false)

        val length = pathMeasure.length
        if (length == 0f) return Offset.Zero

        return pathMeasure.getPosition(length)
    }

fun createShape(sides: Int, length: Float, center: Offset): Path {
    val totalDegrees = (sides - 2) * 180f
    val cornerDegree = totalDegrees / sides
    val shape = Path()

    for (i in 1..sides - 1) {
        shape.relativePolarLineTo(
            degrees = cornerDegree,
            distance = length
        )
    }
    shape.close()

    shape.centerPath(center)
    return shape
}

fun Path.centerPath(pivot: Offset) = translate(
    offset = Offset(
        x = pivot.x - getBounds().center.x,
        y = pivot.y - getBounds().center.y
    )
)

fun createSpring(
    start: Offset,
    end: Offset,
    radius: Float,
    startAngle: Float = 90f,
    loops: Float = 5f,
    resolution: Int = 1000
): Path {
    val spring = Path()
    spring.moveTo(start)
    (0..resolution).forEach { i ->
        val f = i / resolution.toFloat()
        val min = min(startAngle, (360f * loops) - startAngle)
        val max = max(startAngle, (360f * loops) - startAngle)
        val degree = lerp(
            start = min,
            stop = max,
            fraction = f
        )
        if (i == 0) {
            spring.polarMoveTo(
                degrees = degree,
                distance = radius,
                origin = start
            )
        }
        spring.polarLineTo(
            degrees = degree,
            distance = radius,
            origin = lerpOffset(
                start = start,
                stop = end,
                fraction = f,
            ),
        )

    }

    return spring
}

fun createSpring(
    path: Path,
    radius: Float,
    startAngle: Float = 90f,
    loops: Float = 5f,
    resolution: Int = 1000
): Path {
    val spring = Path()
    val measure = PathMeasure()
    measure.setPath(path, false)
    spring.moveTo(measure.getPosition(0f))
    (0..resolution).forEach { i ->
        val f = i / resolution.toFloat()
        val min = min(startAngle, (360f * loops) - startAngle)
        val max = max(startAngle, (360f * loops) - startAngle)
        val degree = lerp(
            start = min,
            stop = max,
            fraction = f
        )
        val currentOrigin = measure.getPosition(measure.length * f)
        if (i == 0) {
            spring.polarMoveTo(
                degrees = degree,
                distance = radius,
                origin = currentOrigin
            )
        }
        spring.polarLineTo(
            degrees = degree,
            distance = radius,
            origin = currentOrigin,
        )

    }

    return spring
}


fun createSpiral(
    center: Offset,
    radius: Float,
    startRadius: Float = 0f,
    startAngle: Float = 0f,
    loops: Float = 5f,
    sides: Int = 6,
): Path {
    val spiral = Path()
    spiral.moveTo(center)
    spiral.polarMoveTo(
        degrees = startAngle,
        distance = startRadius,
        origin = center,
    )
    val resolution = sides * loops.toInt()
    (0..resolution).forEach { i ->
        val f = i / resolution.toFloat()
        val degree = lerp(
            start = startAngle,
            stop = 360f * loops,
            fraction = f
        )
        val distance = lerp(
            start = startRadius,
            stop = radius,
            fraction = f
        )
        spiral.polarLineTo(
            degrees = degree,
            distance = distance,
            origin = center,
        )
    }

    return spiral
}

fun createRoseCurve(
    center: Offset,
    radius: Float,
    petals: Int,
    resolution: Int = 300
): Path {
    return Path().apply {
        moveTo(center)
        for (i in 0..resolution) {
            val degrees = (i / resolution.toFloat()) * 360f
            polarLineTo(
                degrees = degrees,
                distance = (radius * sin((degrees * PI / 180) * petals)).toFloat(),
                origin = center
            )
        }
    }
}


fun Modifier.spring(
    color: Color = Color.Blue,
    stroke: Dp = 4.dp,
    loops: Int = 7,
    progress: Float = 1f,
    flip: Boolean = false,
): Modifier {
    return drawWithContent {
        val loops = loops - .5f
        val spiralPath = createSpring(
            start = Offset(0f, size.height * .5f),
            end = Offset(size.width, size.height * .5f),
            radius = (size.height * .5f) + stroke.toPx(),
            startAngle = -90f,
            loops = loops,
        )

        val spiralMeasure = PathMeasure()
        spiralMeasure.setPath(spiralPath, false)

        scale(
            scaleX = 1f,
            scaleY = if (flip) -1f else 1f
        ) {
            drawPath(
                path = spiralPath,
                color = color,
                style = Stroke(
                    width = stroke.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.chainPathEffect(
                        PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                spiralMeasure.length / ((loops * 2) + 1),
                                spiralMeasure.length / ((loops * 2) + 1),
                            )
                        ),
                        PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                spiralMeasure.length * progress,
                                spiralMeasure.length,
                            )
                        )
                    )
                )
            )
        }
        drawContent()
        scale(
            scaleX = 1f,
            scaleY = if (flip) -1f else 1f
        ) {
            drawPath(
                path = spiralPath,
                color = color,
                style = Stroke(
                    width = stroke.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.chainPathEffect(
                        PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                spiralMeasure.length / ((loops * 2) + 1),
                                spiralMeasure.length / ((loops * 2) + 1),
                            ),
                            phase = -(spiralMeasure.length / ((loops * 2) + 1))
                        ),
                        PathEffect.dashPathEffect(
                            intervals = floatArrayOf(
                                spiralMeasure.length * progress,
                                spiralMeasure.length,
                            )
                        )
                    )
                )
            )
        }

    }
}


fun getDegrees(tan: Offset): Float {
    return (atan2(tan.y, tan.x) * (180f / PI)).toFloat()
}


fun DrawScope.drawArrowOnPath(
    path: Path,
    progress: Float = 1f,
    style: DrawStyle = Fill,
) {
    val arrowHeadPath = Path()
    arrowHeadPath.moveTo(0f, -100f)
    arrowHeadPath.lineTo(100f, 0f)
    arrowHeadPath.lineTo(0f, 100f)

    val measure = PathMeasure()
    measure.setPath(path, false)

    val pos = measure.getPosition(progress * measure.length)
    val tan = measure.getTangent(progress * measure.length)

//    arrowHeadPath.moveTo(pos)
//    arrowHeadPath.relativeLineTo(tan.x, tan.y)

//    drawCircle(
//        color = Red500,
//        center = pos,
//        radius = 10f
//    )

    rotate(
        getDegrees(tan),
        pivot = pos
    ) {


        translate(
            left = pos.x,
            top = pos.y
        ) {
            drawPath(
                arrowHeadPath,
                color = Red500,
//            style = Stroke(
//                width = 40f,
//                cap = StrokeCap.Round,
//                join = StrokeJoin.Round,
//            )
            )
        }
    }
}


fun createInBetweenPaths(
    startPath: Path,
    endPath: Path,
    loops: Int = 30,
    res: Int = 100,
): List<Path> {

    val tweenPaths = mutableListOf<Path>()

    val startMeasure = PathMeasure()
    startMeasure.setPath(startPath, false)

    val endMeasure = PathMeasure()
    endMeasure.setPath(endPath, false)


    for (loop in 0..loops) {

        val loopPath = Path()

        for (p in 0..res) {
            val t = p / res.toFloat()

            val p1 = startMeasure.getPosition(t * startMeasure.length)
            val p2 = endMeasure.getPosition(t * endMeasure.length)


            lerpOffset(
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
        tweenPaths.add(loopPath)
    }

    return tweenPaths
}