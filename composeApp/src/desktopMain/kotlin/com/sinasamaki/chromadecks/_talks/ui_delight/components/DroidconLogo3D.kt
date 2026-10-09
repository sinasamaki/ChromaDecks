package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp as lerpOffset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@Immutable
data class DroidconLogoColors(
    val shadow: Color = Color(0xFF2F9E3C),
    val base: Color = Color(0xFF6EE84C),
    val highlight: Color = Color(0xFFE4FFC9),
    val rim: Color = Color(0xFF9DB4FF),
    val bounce: Color = Color(0xFF3B64F0),
    val edge: Color = Color.Black.copy(alpha = .1f),
    val wireframe: Color = Color.White,
    val groundShadow: Color = Color(0xFF101C66),
    val normalFacing: Color = Color(0xFF3DDC5A),
    val normalAway: Color = Color(0xFFFF4545),
)

@Composable
fun DroidconLogo3D(
    modifier: Modifier = Modifier,
    rotation: Float = 0f,
    tilt: Float = 0f,
    rotationZ: Float = 0f,
    translationX: Float = 0f,
    translationY: Float = 0f,
    colors: DroidconLogoColors = DroidconLogoColors(),
    leftAntennaPositionOffset: Float = 0f,
    leftAntennaDirectionOffset: Float = 0f,
    rightAntennaPositionOffset: Float = 0f,
    rightAntennaDirectionOffset: Float = 0f,
    thickness: Float = 0.3f,
    paintFaces: Boolean = true,
    useNormals: Boolean = true,
    strokeProgress: Float = 1f,
    panelProgress: Float = 1f,
    panelPaintProgress: Float = 1f,
    normalsProgress: Float = 0f,
) {
    val outline = remember(
        leftAntennaPositionOffset,
        leftAntennaDirectionOffset,
        rightAntennaPositionOffset,
        rightAntennaDirectionOffset,
    ) {
        droidconOutline(
            leftPositionDegrees = leftAntennaPositionOffset,
            leftDirectionDegrees = leftAntennaDirectionOffset,
            rightPositionDegrees = rightAntennaPositionOffset,
            rightDirectionDegrees = rightAntennaDirectionOffset,
        )
    }
    Canvas(modifier = modifier) {
        drawExtrudedLogo(
            outline = outline,
            rotation = rotation,
            tilt = tilt,
            rotationZ = rotationZ,
            translationX = translationX,
            translationY = translationY,
            colors = colors,
            halfDepth = thickness / 2f,
            paintFaces = paintFaces,
            useNormals = useNormals,
            strokeProgress = strokeProgress.coerceIn(0f, 1f),
            panelProgress = panelProgress.coerceIn(0f, 1f),
            panelPaintProgress = if (paintFaces) panelPaintProgress.coerceIn(0f, 1f) else 0f,
            normalsProgress = normalsProgress.coerceIn(0f, 1f),
        )
    }
}

private const val OuterRadius = 1f
private const val InnerRadius = 0.62f
private const val AntennaHalfWidth = 0.13f
private const val AntennaLength = 1.42f
private const val ArcStepsPerHalfTurn = 96
private const val CenterShift = 0.5f
private const val Focal = 4.5f
private const val PanelNormalLength = 0.18f
private const val CapNormalLength = 0.45f

private val LightPosition = Vec3(-3f, -3.5f, 5f)
private val Camera = Vec3(0f, 0f, Focal)
private val BounceDirection = Vec3(0.3f, 1f, 0.3f).normalized()

private data class Vec3(val x: Float, val y: Float, val z: Float) {
    operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)
    operator fun times(s: Float) = Vec3(x * s, y * s, z * s)
    infix fun dot(o: Vec3) = x * o.x + y * o.y + z * o.z
    fun normalized(): Vec3 {
        val length = sqrt(this dot this)
        return if (length == 0f) this else this * (1f / length)
    }
}

private class OutlineEdge(
    val a: Offset,
    val b: Offset,
    val normalA: Offset,
    val normalB: Offset,
)

private class Panel(
    val index: Int,
    val path: Path,
    val brush: Brush,
    val depth: Float,
    val center: Vec3,
    val normal: Vec3,
    val facing: Boolean,
)

private fun droidconOutline(
    leftPositionDegrees: Float,
    leftDirectionDegrees: Float,
    rightPositionDegrees: Float,
    rightDirectionDegrees: Float,
): List<OutlineEdge> {
    val edges = mutableListOf<OutlineEdge>()
    val pi = PI.toFloat()

    fun radians(degrees: Float) = degrees / 180f * pi

    fun radial(t: Float) = Offset(cos(t), sin(t))
    fun polar(r: Float, t: Float) = radial(t) * r
    fun angleOf(p: Offset) = atan2(p.y, p.x).let { if (it < pi / 2f) it + 2f * pi else it }

    fun arc(radius: Float, from: Float, to: Float, outward: Boolean) {
        val steps = max(2, (abs(to - from) / pi * ArcStepsPerHalfTurn).roundToInt())
        val sign = if (outward) 1f else -1f
        for (i in 0 until steps) {
            val t0 = lerp(from, to, i / steps.toFloat())
            val t1 = lerp(from, to, (i + 1) / steps.toFloat())
            edges += OutlineEdge(
                a = polar(radius, t0),
                b = polar(radius, t1),
                normalA = radial(t0) * sign,
                normalB = radial(t1) * sign,
            )
        }
    }

    fun line(a: Offset, b: Offset) {
        val d = b - a
        val length = d.getDistance()
        if (length < 1e-5f) return
        val normal = Offset(d.y / length, -d.x / length)
        edges += OutlineEdge(a, b, normal, normal)
    }

    val anchorRadius = (OuterRadius + InnerRadius) / 2f
    val antennaReach = AntennaLength - anchorRadius
    val top = pi * 1.5f
    val antennas = listOf(
        top - (pi / 4f + radians(leftPositionDegrees)) to -radians(leftDirectionDegrees),
        top + (pi / 4f + radians(rightPositionDegrees)) to radians(rightDirectionDegrees),
    )

    var t = pi
    antennas.forEach { (anchorAngle, turn) ->
        val anchor = polar(anchorRadius, anchorAngle)
        val dir = radial(anchorAngle + turn)
        val perp = Offset(-dir.y, dir.x)
        val sides = listOf(-AntennaHalfWidth, AntennaHalfWidth).map { s ->
            val q = anchor + perp * s
            val qd = q.x * dir.x + q.y * dir.y
            val qq = q.x * q.x + q.y * q.y
            val along = -qd + sqrt((qd * qd - (qq - OuterRadius * OuterRadius)).coerceAtLeast(0f))
            val base = q + dir * along
            val tip = q + dir * antennaReach
            Triple(angleOf(base), base, tip)
        }.sortedBy { it.first }
        val (startAngle, startBase, startTip) = sides[0]
        val (endAngle, endBase, endTip) = sides[1]
        arc(OuterRadius, t, startAngle, outward = true)
        line(startBase, startTip)
        line(startTip, endTip)
        line(endTip, endBase)
        t = endAngle
    }
    arc(OuterRadius, t, 2 * pi, outward = true)
    line(Offset(OuterRadius, 0f), Offset(InnerRadius, 0f))
    arc(InnerRadius, 2 * pi, pi, outward = false)
    line(Offset(-InnerRadius, 0f), Offset(-OuterRadius, 0f))
    return edges
}

private fun shade(position: Vec3, normal: Vec3, colors: DroidconLogoColors): Color {
    val toLight = (LightPosition - position).normalized()
    val toCamera = (Camera - position).normalized()
    val halfway = (toLight + toCamera).normalized()
    val diffuse = (normal dot toLight).coerceAtLeast(0f)
    val specular = (normal dot halfway).coerceAtLeast(0f).pow(40f)
    val fresnel = (1f - (normal dot toCamera).coerceIn(0f, 1f)).pow(3f)
    val bounce = (normal dot BounceDirection).coerceAtLeast(0f)

    var color = lerp(colors.shadow, colors.base, (0.4f + 0.75f * diffuse).coerceIn(0f, 1f))
    color = lerp(color, colors.bounce, bounce * 0.12f)
    color = lerp(color, colors.rim, fresnel * 0.3f)
    color = lerp(color, colors.highlight, specular * 0.8f)
    return color
}

private fun DrawScope.drawExtrudedLogo(
    outline: List<OutlineEdge>,
    rotation: Float,
    tilt: Float,
    rotationZ: Float,
    translationX: Float,
    translationY: Float,
    colors: DroidconLogoColors,
    halfDepth: Float,
    paintFaces: Boolean,
    useNormals: Boolean,
    strokeProgress: Float,
    panelProgress: Float,
    panelPaintProgress: Float,
    normalsProgress: Float,
) {
    val yaw = rotation / 180f * PI.toFloat()
    val pitch = tilt / 180f * PI.toFloat()
    val roll = rotationZ / 180f * PI.toFloat()
    val cosRoll = cos(roll)
    val sinRoll = sin(roll)
    val cosYaw = cos(yaw)
    val sinYaw = sin(yaw)
    val cosPitch = cos(pitch)
    val sinPitch = sin(pitch)

    fun rotate(v: Vec3): Vec3 {
        val x0 = v.x * cosRoll - v.y * sinRoll
        val y0 = v.x * sinRoll + v.y * cosRoll
        val x1 = x0 * cosYaw + v.z * sinYaw
        val z1 = -x0 * sinYaw + v.z * cosYaw
        return Vec3(
            x = x1,
            y = y0 * cosPitch - z1 * sinPitch,
            z = y0 * sinPitch + z1 * cosPitch,
        )
    }

    val unit = size.minDimension * 0.34f
    val shift = translationX / unit
    val lift = translationY / unit

    fun world(p: Offset, z: Float) =
        rotate(Vec3(p.x, p.y + CenterShift, z)) + Vec3(shift, lift, 0f)
    val origin = center

    fun project(v: Vec3): Offset {
        val k = Focal / (Focal - v.z) * unit
        return Offset(origin.x + v.x * k, origin.y + v.y * k)
    }

    fun facesCamera(position: Vec3, normal: Vec3) = (normal dot (Camera - position)) > 0f

    val wireStroke = Stroke(width = 1.dp.toPx(), join = StrokeJoin.Round)
    val count = outline.size

    fun panelReveal(index: Int, progress: Float) =
        (progress * (count + 8) - index).coerceIn(0f, 8f) / 8f

    val capsPainted = paintFaces && panelPaintProgress >= 1f

    if (capsPainted) {
        val projectedFront = outline.map { project(world(it.a, halfDepth)) }
        val minX = projectedFront.minOf { it.x }
        val maxX = projectedFront.maxOf { it.x }
        val shadowCenter = Offset(origin.x + translationX, origin.y + unit * 0.95f)
        val spread = (1f - lift * 0.6f).coerceAtLeast(0.5f)
        val shadowRadius = max((maxX - minX) * 0.55f, unit * 0.4f) * spread
        val shadowAlpha = 1f / (spread * spread)
        scale(scaleX = 1f, scaleY = 0.16f, pivot = shadowCenter) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colors.groundShadow.copy(alpha = (.4f * shadowAlpha).coerceAtMost(1f)),
                        colors.groundShadow.copy(alpha = (.13f * shadowAlpha).coerceAtMost(1f)),
                        Color.Transparent,
                    ),
                    center = shadowCenter,
                    radius = shadowRadius,
                ),
                radius = shadowRadius,
                center = shadowCenter,
            )
        }
    }

    val panels = if (panelProgress <= 0f) emptyList() else outline.mapIndexedNotNull { index, edge ->
        val frontA = world(edge.a, halfDepth)
        val frontB = world(edge.b, halfDepth)
        val backA = world(edge.a, -halfDepth)
        val backB = world(edge.b, -halfDepth)
        val normalA = rotate(Vec3(edge.normalA.x, edge.normalA.y, 0f))
        val normalB = rotate(Vec3(edge.normalB.x, edge.normalB.y, 0f))
        val midA = (frontA + backA) * 0.5f
        val midB = (frontB + backB) * 0.5f
        val panelCenter = (midA + midB) * 0.5f
        val panelNormal = (normalA + normalB).normalized()
        val facing = facesCamera(panelCenter, panelNormal)
        if (useNormals && !facing) {
            return@mapIndexedNotNull null
        }

        val path = Path().apply {
            moveTo(project(frontA))
            lineTo(project(frontB))
            lineTo(project(backB))
            lineTo(project(backA))
            close()
        }
        val start = project(midA)
        val end = project(midB)
        val colorA = shade(midA, normalA, colors)
        val colorB = shade(midB, normalB, colors)
        val brush = if ((end - start).getDistance() < 0.5f) {
            SolidColor(colorA)
        } else {
            Brush.linearGradient(listOf(colorA, colorB), start = start, end = end)
        }
        Panel(index, path, brush, panelCenter.z, panelCenter, panelNormal, facing)
    }

    val caps = listOf(-halfDepth, halfDepth)
        .map { capZ ->
            val normal = rotate(Vec3(0f, 0f, if (capZ >= 0f) 1f else -1f))
            val capCenter = world(Offset(0f, -0.5f), capZ)
            Triple(capZ, normal, facesCamera(capCenter, normal))
        }
        .filter { (_, _, visible) -> visible || !useNormals }
        .sortedBy { (_, _, visible) -> visible }

    fun capPath(capZ: Float) = Path().apply {
        outline.forEachIndexed { i, edge ->
            val p = project(world(edge.a, capZ))
            if (i == 0) moveTo(p) else lineTo(p)
        }
        close()
    }

    fun drawCap(capZ: Float, normal: Vec3) {
        val path = capPath(capZ)
        if (capsPainted) {
            val sampleStart = Offset(-1.3f, -1.15f)
            val sampleEnd = Offset(1.3f, 0.15f)
            val samples = 8
            val worldSamples = (0..samples).map { i ->
                world(lerpOffset(sampleStart, sampleEnd, i / samples.toFloat()), capZ)
            }
            val screenStart = project(worldSamples.first())
            val screenEnd = project(worldSamples.last())
            val axis = screenEnd - screenStart
            val axisLengthSquared = (axis.x * axis.x + axis.y * axis.y).coerceAtLeast(1e-3f)
            val stops = worldSamples.map { sample ->
                val s = project(sample) - screenStart
                val fraction = ((s.x * axis.x + s.y * axis.y) / axisLengthSquared).coerceIn(0f, 1f)
                fraction to shade(sample, normal, colors)
            }.toTypedArray()

            drawPath(
                path = path,
                brush = Brush.linearGradient(*stops, start = screenStart, end = screenEnd),
            )
            drawPath(
                path = path,
                color = colors.edge,
                style = Stroke(width = 1.5.dp.toPx(), join = StrokeJoin.Round),
            )
        }
        if (!capsPainted && strokeProgress > 0f) {
            val visiblePath = if (strokeProgress >= 1f) path else {
                val measure = PathMeasure()
                measure.setPath(path, forceClosed = true)
                Path().also { measure.getSegment(0f, measure.length * strokeProgress, it, true) }
            }
            drawPath(
                path = visiblePath,
                color = colors.wireframe,
                style = Stroke(width = 2.dp.toPx(), join = StrokeJoin.Round),
            )
        }
    }

    caps.filter { (_, _, visible) -> !visible }.forEach { (capZ, normal) -> drawCap(capZ, normal) }

    panels.sortedBy { it.depth }.forEach { panel ->
        val reveal = panelReveal(panel.index, panelProgress)
        if (reveal <= 0f) return@forEach
        val paint = if (panelPaintProgress * count > panel.index) 1f else 0f
        if (paint > 0f) {
            drawPath(path = panel.path, brush = panel.brush, alpha = paint * reveal)
            drawPath(path = panel.path, brush = panel.brush, alpha = paint * reveal, style = Stroke(width = 1f, join = StrokeJoin.Round))
        }
        if (paint < 1f) {
            drawPath(
                path = panel.path,
                color = colors.wireframe.copy(alpha = .5f),
                alpha = reveal * (1f - paint),
                style = wireStroke,
            )
        }
    }

    caps.filter { (_, _, visible) -> visible }.forEach { (capZ, normal) -> drawCap(capZ, normal) }

    if (normalsProgress > 0f) {
        val normalWidth = 2.dp.toPx()

        fun drawNormal(from: Vec3, normal: Vec3, length: Float, facing: Boolean) {
            if (length <= 0f) return
            drawLine(
                color = if (facing) colors.normalFacing else colors.normalAway,
                start = project(from),
                end = project(from + normal * length),
                strokeWidth = normalWidth,
                cap = StrokeCap.Round,
            )
        }

        val capNormals = caps.map { (capZ, normal, visible) ->
            Triple(world(Offset(0f, -0.5f), capZ), normal, visible)
        }
        listOf(false, true).forEach { pass ->
            panels.filter { it.facing == pass }.forEach { panel ->
                val length = PanelNormalLength * panelReveal(panel.index, normalsProgress)
                drawNormal(panel.center, panel.normal, length, panel.facing)
            }
            capNormals.filter { it.third == pass }.forEach { (capCenter, normal, visible) ->
                drawNormal(capCenter, normal, CapNormalLength * normalsProgress, visible)
            }
        }
    }
}

private fun Path.moveTo(p: Offset) = moveTo(p.x, p.y)
private fun Path.lineTo(p: Offset) = lineTo(p.x, p.y)
