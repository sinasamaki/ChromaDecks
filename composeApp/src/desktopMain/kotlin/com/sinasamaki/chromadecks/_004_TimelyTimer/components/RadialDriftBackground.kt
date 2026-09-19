package com.sinasamaki.chromadecks._004_TimelyTimer.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private enum class ShapeKind { Circle, Line }

private data class FloatingShape(
    val kind: ShapeKind,
    val baseAngle: Float,
    val distFrac: Float,
    val size: Float,
    val stroke: Float,
    val parallax: Float,
    val speed: Int,
    val color: Color,
    val wanderAmp: Float,
    val wanderFx: Int,
    val wanderFy: Int,
    val wanderPhaseX: Float,
    val wanderPhaseY: Float,
)

/** The single wavy path every particle in a blast hugs before deviating. */
private data class BlastPath(val amp: Float, val waves: Int, val phase: Float)

/** Re-rolled each restart: where the blast launches, its curve, and the rotation
 *  to treat as "upright" (so a fresh blast looks like index 0). */
private data class BlastState(
    val originXFrac: Float,
    val phase: Float,
    val resetRotation: Float,
)

private data class Particle(
    val spread: Float,
    val amp: Float,
    val waves: Int,
    val phase: Float,
    val duration: Float,
    val startDepth: Float,
    val maxRadiusDp: Float,
    val parallax: Float,
    val colorIndex: Int,
)

private const val BURST_PERIOD_MS = 30_000
private const val BLAST_FRACTION = 0.955f

private const val PARTICLE_COUNT = 200
private const val PARTICLE_MIN_RADIUS_DP = .3f
private const val PARTICLE_MAX_RADIUS_DP = 6f
private const val PARTICLE_SEED_RADIUS_PX = 1.5f
private const val PARTICLE_SPREAD_PX = 250f
private const val PARTICLE_WOBBLE_MIN_PX = 50f
private const val PARTICLE_WOBBLE_MAX_PX = 200f
private const val PARTICLE_SPLIT_AT = -.7f
private const val PARTICLE_MIN_DURATION = 0.3f
private const val PARTICLE_MAX_DURATION = 1.0f
private const val PARTICLE_MIN_PARALLAX = 0.3f
private const val PARTICLE_MAX_PARALLAX = 0.9f
private const val PARTICLE_LAUNCH_OVERSHOOT = 1.05f
private const val PARTICLE_START_DEPTH_MAX = 0.5f
private const val PARTICLE_EXIT_MARGIN_PX = 60f
private const val PARTICLE_FADE_OUT_AT = 0.8f
private const val PARTICLE_ORIGIN_MIN = 0.1f
private const val PARTICLE_ORIGIN_MAX = 0.9f

private const val PARTICLE_SLOW_AT = 0.3f
private const val PARTICLE_SLOW_STRENGTH = 0.3f

private const val BLAST_PATH_AMP_MIN_PX = 120f
private const val BLAST_PATH_AMP_MAX_PX = 240f

/**
 * Animated background: a radial gradient ([centerColor] -> [midColor] -> [edgeColor], centred at
 * the bottom), overlaid with blurred circles, radial lines and a stream of rising particles, all
 * in a polar system around a centre far below the bottom edge. As [index] rotates (15° per step,
 * eased with a slow low-stiffness spring), shapes and particles swing around it, each with its own
 * parallax. Colour changes animate smoothly.
 */
@Composable
fun RadialDriftBackground(
    centerColor: Color,
    midColor: Color,
    edgeColor: Color,
    index: Int,
    modifier: Modifier = Modifier,
) {
    val animatedCenter by animateColorAsState(centerColor, tween(800), label = "center")
    val animatedMid by animateColorAsState(midColor, tween(800), label = "mid")
    val animatedEdge by animateColorAsState(edgeColor, tween(800), label = "edge")

    val rotation by animateFloatAsState(
        targetValue = index * 15f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = 4f,
        ),
        label = "rotation",
    )

    val twoPi = (PI * 2).toFloat()

    val shapes = remember {
        val random = Random(0x71E)
        val palette = listOf(
            Color.White.copy(alpha = .28f),
            Color.White.copy(alpha = .20f),
            Color.White.copy(alpha = .14f),
        )
        buildList {
            repeat(44) {
                val isLine = it % 2 == 0
                add(
                    FloatingShape(
                        kind = if (isLine) ShapeKind.Line else ShapeKind.Circle,
                        baseAngle = random.nextFloat() * twoPi,
                        distFrac = random.nextFloat(),
                        size = if (isLine) random.nextFloat() * 240f + 160f
                        else random.nextFloat() * 110f + 16f,
                        stroke = random.nextFloat() * 10f + 6f,
                        parallax = random.nextFloat() * 1.2f + 0.5f,
                        speed = random.nextInt(1, 5),
                        color = palette[random.nextInt(palette.size)],
                        wanderAmp = random.nextFloat() * 180f + 80f,
                        wanderFx = random.nextInt(1, 3),
                        wanderFy = random.nextInt(1, 3),
                        wanderPhaseX = random.nextFloat() * twoPi,
                        wanderPhaseY = random.nextFloat() * twoPi,
                    )
                )
            }
        }
    }

    val blastPath = remember {
        val random = Random(0x5B)
        BlastPath(
            amp = random.nextFloat() * (BLAST_PATH_AMP_MAX_PX - BLAST_PATH_AMP_MIN_PX) + BLAST_PATH_AMP_MIN_PX,
            waves = 1,
            phase = random.nextFloat() * twoPi,
        )
    }

    val particles = remember {
        val random = Random(0x9A2)
        List(PARTICLE_COUNT) {
            Particle(
                spread = (random.nextFloat() * 2f - 1f) * PARTICLE_SPREAD_PX,
                amp = random.nextFloat() * (PARTICLE_WOBBLE_MAX_PX - PARTICLE_WOBBLE_MIN_PX) + PARTICLE_WOBBLE_MIN_PX,
                waves = 1,
                phase = random.nextFloat() * twoPi,
                duration = random.nextFloat() * (PARTICLE_MAX_DURATION - PARTICLE_MIN_DURATION) + PARTICLE_MIN_DURATION,
                startDepth = random.nextFloat() * PARTICLE_START_DEPTH_MAX,
                maxRadiusDp = random.nextFloat() * (PARTICLE_MAX_RADIUS_DP - PARTICLE_MIN_RADIUS_DP) + PARTICLE_MIN_RADIUS_DP,
                parallax = random.nextFloat() * (PARTICLE_MAX_PARALLAX - PARTICLE_MIN_PARALLAX) + PARTICLE_MIN_PARALLAX,
                colorIndex = random.nextInt(0, 2),
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "drift")
    val outward by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "outward",
    )
    val wanderT by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 120_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "wander",
    )
    val burst = remember { Animatable(0f) }
    var blastState by remember {
        mutableStateOf(BlastState(originXFrac = 0.5f, phase = blastPath.phase, resetRotation = 0f))
    }
    val blastRandom = remember { Random(0xB1A5) }
    LaunchedEffect(Unit) {
        while (true) {
            blastState = BlastState(
                originXFrac = blastRandom.nextFloat() * (PARTICLE_ORIGIN_MAX - PARTICLE_ORIGIN_MIN) + PARTICLE_ORIGIN_MIN,
                phase = blastRandom.nextFloat() * twoPi,
                resetRotation = rotation,
            )
            burst.snapTo(0f)
            burst.animateTo(1f, animationSpec = tween(BURST_PERIOD_MS, easing = LinearEasing))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(animatedCenter, animatedMid, animatedEdge),
                            center = Offset(size.width / 2f, size.height),
                            radius = maxOf(size.width, size.height) * 1.15f,
                        )
                    )
                }
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp)
        ) {
            val center = Offset(size.width / 2f, size.height + size.height * 0.35f)
            val rotationRad = rotation * (PI.toFloat() / 180f)

            val maxDist = hypot(size.width / 2f, center.y) + 250f
            val bottomDist = center.y - size.height

            val wanderAngle = wanderT * twoPi

            shapes.forEach { shape ->
                when (shape.kind) {
                    ShapeKind.Circle -> {
                        val dist = sqrt(
                            lerp(bottomDist * bottomDist, maxDist * maxDist, shape.distFrac)
                        )
                        val baseVec = Offset(cos(shape.baseAngle), sin(shape.baseAngle)) * dist
                        val wander = Offset(
                            sin(wanderAngle * shape.wanderFx + shape.wanderPhaseX),
                            cos(wanderAngle * shape.wanderFy + shape.wanderPhaseY),
                        ) * shape.wanderAmp
                        val a = rotationRad * shape.parallax
                        val pos = center + rotateVec(baseVec + wander, cos(a), sin(a))
                        drawCircle(
                            color = shape.color.copy(alpha = .3f),
                            radius = shape.size,
                            center = pos,
                        )
                        drawCircle(
                            color = shape.color,
                            radius = shape.size,
                            center = pos,
                            style = Stroke(width = 30f),
                        )
                    }

                    ShapeKind.Line -> {
                        val angle = shape.baseAngle + rotationRad * shape.parallax
                        val dir = Offset(cos(angle), sin(angle))
                        val frac = (shape.distFrac + outward * shape.speed).mod(1f)
                        val dist = frac * maxDist
                        val pos = center + dir * dist
                        val length = shape.size
                        val thickness = shape.stroke * 1.8f
                        rotate(
                            degrees = angle * 180f / PI.toFloat(),
                            pivot = pos,
                        ) {
                            drawOval(
                                color = shape.color,
                                topLeft = Offset(
                                    x = pos.x - length / 2f,
                                    y = pos.y - thickness / 2f,
                                ),
                                size = Size(length, thickness),
                            )
                        }
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(1.dp)
        ) {
            val center = Offset(size.width / 2f, size.height + size.height * 0.35f)
            val rotationRad = (rotation - blastState.resetRotation) * (PI.toFloat() / 180f)

            val palette = listOf(animatedMid, animatedEdge)
            val blast = burst.value / BLAST_FRACTION
            particles.forEach { p ->
                val t = blast / p.duration
                if (t >= 1f) return@forEach

                val m = PARTICLE_SLOW_AT
                val k = PARTICLE_SLOW_STRENGTH
                val piece = if (t < m) {
                    val u = t / m
                    m * (1f - (1f - u) * (1f - u))
                } else {
                    val u = (t - m) / (1f - m)
                    m + (1f - m) * (u * u)
                }
                val h = (1f - k) * t + k * piece

                val startY = size.height * (PARTICLE_LAUNCH_OVERSHOOT + p.startDepth)
                val endY = -PARTICLE_EXIT_MARGIN_PX
                val y = lerp(startY, endY, h)
                val heightFrac = (size.height - y) / size.height
                val pathX = blastState.originXFrac * size.width +
                        sin(heightFrac * blastPath.waves * twoPi + blastState.phase) * blastPath.amp
                val split = ((heightFrac - PARTICLE_SPLIT_AT) / (1f - PARTICLE_SPLIT_AT))
                    .coerceIn(0f, 1f)
                val dev = split * split * split
                val x = pathX +
                        p.spread * dev +
                        sin(h * p.waves * twoPi + p.phase) * p.amp * dev
                val a = rotationRad * p.parallax
                val pos = center + rotateVec(Offset(x, y) - center, cos(a), sin(a))

                val grow = 1f - (1f - h) * (1f - h)
                val radius = lerp(PARTICLE_SEED_RADIUS_PX, p.maxRadiusDp.dp.toPx(), grow)
                val enterH = (startY - size.height) / (startY - endY)
                val alpha = when {
                    h < enterH -> h / enterH
                    h > PARTICLE_FADE_OUT_AT -> (1f - h) / (1f - PARTICLE_FADE_OUT_AT)
                    else -> 1f
                }.coerceIn(0f, 1f)

                val color = palette[p.colorIndex]
                drawCircle(color.copy(alpha = alpha), radius, pos)
            }
        }
    }
}

/** Rotate [v] by the angle whose cosine/sine are [cos]/[sin]. */
private fun rotateVec(v: Offset, cos: Float, sin: Float): Offset =
    Offset(v.x * cos - v.y * sin, v.x * sin + v.y * cos)
