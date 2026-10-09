package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.sinasamaki.chromadecks._talks.ui_delight.components.DroidconLogo3D
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

data class DroidconLogoCodeSlideState(
    val step: Int = 0,
    val strokeProgress: Float = 0f,
    val thickness: Float = 0f,
    val panelProgress: Float = 0f,
    val showNormals: Boolean = false,
    val useNormals: Boolean = false,
    val paintFaces: Boolean = false,
)

class DroidconLogoCodeSlide : ListSlideAdvanced<DroidconLogoCodeSlideState>() {

    override val initialState: DroidconLogoCodeSlideState
        get() = DroidconLogoCodeSlideState()

    override val stateMutations: List<DroidconLogoCodeSlideState.() -> DroidconLogoCodeSlideState>
        get() = listOf(
            { copy(step = 1, strokeProgress = 1f) },
            { copy(step = 2, thickness = .3f) },
            { copy(step = 3, panelProgress = 1f) },
            { copy(step = 4, showNormals = true) },
            { copy(step = 5, useNormals = true) },
            { copy(step = 6, showNormals = false) },
            { copy(step = 7, paintFaces = true) },
        )

    @Composable
    override fun content(state: DroidconLogoCodeSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "Outline.kt" to OutlineCode,
                "Extrude.kt" to ExtrudeCode,
                "Panels.kt" to if (state.useNormals) PanelsCulledCode else PanelsCode,
                "Shading.kt" to ShadingCode,
            ),
            tabIndex = when (state.step) {
                0, 1 -> 0
                2 -> 1
                3, 4, 5, 6 -> 2
                else -> 3
            },
        ) {
            var rotation by remember { mutableFloatStateOf(0f) }
            var tilt by remember { mutableFloatStateOf(0f) }

            val strokeProgress by animateFloatAsState(
                targetValue = state.strokeProgress,
                animationSpec = tween(durationMillis = 3_000, easing = FastOutSlowInEasing),
            )
            val thickness by animateFloatAsState(
                targetValue = state.thickness,
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                ),
            )
            val panelProgress by animateFloatAsState(
                targetValue = state.panelProgress,
                animationSpec = tween(durationMillis = 4_500, easing = FastOutSlowInEasing),
            )
            val panelPaintProgress by animateFloatAsState(
                targetValue = if (state.paintFaces) 1f else 0f,
                animationSpec = tween(durationMillis = 3_500, easing = LinearEasing),
            )

            DroidconLogo3D(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            rotation += dragAmount.x * 0.4f
                            tilt = (tilt - dragAmount.y * 0.2f).coerceIn(-18f, 18f)
                        }
                    },
                rotation = rotation,
                tilt = tilt,
                thickness = thickness,
                paintFaces = state.paintFaces,
                useNormals = state.useNormals,
                strokeProgress = strokeProgress,
                panelProgress = panelProgress,
                panelPaintProgress = panelPaintProgress,
                normalsProgress = if (state.showNormals) 1f else 0f,
            )
        }
    }
}

private val OutlineCode = """
    val edges = buildList {
        var start = 180f
        for (angle in listOf(225f, 315f)) {
            val (left, right) = antennaSides(angle)
            addAll(arc(outer, start, left.angle))
            add(Edge(left.base, left.tip))
            add(Edge(left.tip, right.tip))
            add(Edge(right.tip, right.base))
            start = right.angle
        }
        addAll(arc(outer, start, 360f))
        add(Edge(outerEnd, innerEnd))
        addAll(arc(inner, 360f, 180f))
        add(Edge(innerStart, outerStart))
    }

    Canvas(modifier) {
        val face = edges.toPath(z = 0f)
        drawPath(face, White, style = Stroke())
    }
""".trimIndent()

private val ExtrudeCode = """
    Canvas(modifier) {
        val front = edges.toPath(z = depth / 2)
        val back = edges.toPath(z = -depth / 2)

        drawPath(front, White, style = Stroke())
        drawPath(back, White, style = Stroke())
    }
""".trimIndent()

private val PanelsCode = """
    edges.forEach { edge ->
        val panel = Path().apply {
            moveTo(edge.frontA)
            lineTo(edge.frontB)
            lineTo(edge.backB)
            lineTo(edge.backA)
            close()
        }

        drawPath(panel, White, style = Stroke())
    }
""".trimIndent()

private val PanelsCulledCode = """
    edges.forEach { edge ->
        val normal = rotate(edge.normal)
        val toCamera = camera - edge.center
        if (normal.dot(toCamera) < 0f) {
            return@forEach
        }

        val panel = Path().apply {
            moveTo(edge.frontA)
            lineTo(edge.frontB)
            lineTo(edge.backB)
            lineTo(edge.backA)
            close()
        }

        drawPath(panel, White, style = Stroke())
    }
""".trimIndent()

private val ShadingCode = """
    fun shade(position: Vec3, normal: Vec3): Color {
        val toLight = (light - position).normalized()
        val diffuse = max(normal.dot(toLight), 0f)
        return lerp(ShadowGreen, BaseGreen, diffuse)
    }

    drawPath(
        path = panel,
        brush = Brush.linearGradient(
            colors = listOf(
                shade(edge.a, normal),
                shade(edge.b, normal),
            ),
            start = edge.middleA,
            end = edge.middleB,
        ),
    )
""".trimIndent()
