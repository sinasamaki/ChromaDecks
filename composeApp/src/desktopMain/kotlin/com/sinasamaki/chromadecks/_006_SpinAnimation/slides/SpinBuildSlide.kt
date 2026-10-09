package com.sinasamaki.chromadecks._006_SpinAnimation.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinCodeText
import com.sinasamaki.chromadecks._006_SpinAnimation.components.BackStyle
import com.sinasamaki.chromadecks._006_SpinAnimation.components.DemoCaption
import com.sinasamaki.chromadecks._006_SpinAnimation.components.DemoHeight
import com.sinasamaki.chromadecks._006_SpinAnimation.components.DemoThickness
import com.sinasamaki.chromadecks._006_SpinAnimation.components.DemoWidth
import com.sinasamaki.chromadecks._006_SpinAnimation.components.FAR_CAMERA
import com.sinasamaki.chromadecks._006_SpinAnimation.components.HabitLabel
import com.sinasamaki.chromadecks._006_SpinAnimation.components.NEAR_CAMERA
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinBody
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinMotion
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinStage
import com.sinasamaki.chromadecks._006_SpinAnimation.components.animateSpinStage
import com.sinasamaki.chromadecks._006_SpinAnimation.components.rememberSpinAngle
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.slideanimations.blurOut
import com.sinasamaki.chromadecks.ui.slideanimations.fadeOut
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Red300
import com.sinasamaki.chromadecks.ui.theme.Red700
import com.sinasamaki.chromadecks.ui.theme.Red900

private const val THIN = .12f

private const val SLOW = 60f

private const val TIPPED = 50f

private const val BUTTON_TAB = "Button.kt"
private const val SHADING_TAB = "Shading.kt"
private const val SIDE_TAB = "Side.kt"
private const val THICKNESS_TAB = "Thickness.kt"
private const val BACK_TAB = "Back.kt"

internal enum class PathStep { Rest, Linear, Eased }

internal enum class DrawStep { Thin, Thick, Clipped, Gradient, Shadow }

internal enum class BackStep { Check, Darken, Swap, Tint }

internal data class BuildCode(
    val border: Boolean = false,
    val rotate: Boolean = false,
    val camera: Boolean = false,
    val shade: Boolean = false,
    val hop: Boolean = false,
    val path: PathStep? = null,
    val draw: DrawStep? = null,
    val back: BackStep? = null,
)

internal data class SpinBuildState(
    val stage: SpinStage,
    val motion: SpinMotion,
    val code: BuildCode,
    val tab: String,
    val caption: String = "",
)

internal class SpinBuildSlide : ListSlideAdvanced<SpinBuildState>() {

    override val initialState: SpinBuildState
        get() = SpinBuildState(
            stage = SpinStage.Flat.copy(border = 0f, cameraDistance = NEAR_CAMERA),
            motion = SpinMotion.Park(0f),
            code = BuildCode(),
            tab = BUTTON_TAB,
        )

    override val stateMutations: List<SpinBuildState.() -> SpinBuildState>
        get() = listOf(
            { copy(stage = stage.copy(border = 1f), code = code.copy(border = true)) },
            { copy(motion = SpinMotion.Spin(), code = code.copy(rotate = true)) },
            {
                copy(
                    stage = stage.copy(cameraDistance = FAR_CAMERA),
                    code = code.copy(camera = true),
                )
            },
            { copy(stage = stage.copy(faceShade = 1f), code = code.copy(shade = true)) },
            { copy(tab = SHADING_TAB) },
            { copy(motion = SpinMotion.Park(90f), tab = BUTTON_TAB, caption = "90°") },
            {
                copy(
                    stage = stage.copy(thickness = 1f, strokeScale = THIN, faceAlpha = .25f),
                    motion = SpinMotion.Park(0f),
                    code = code.copy(path = PathStep.Rest, draw = DrawStep.Thin),
                    tab = SIDE_TAB,
                    caption = "",
                )
            },
            {
                copy(
                    stage = stage.copy(strokeScale = 1f),
                    code = code.copy(draw = DrawStep.Thick),
                    tab = THICKNESS_TAB,
                )
            },
            {
                copy(
                    stage = stage.copy(faceAlpha = 1f),
                    motion = SpinMotion.Park(90f),
                    code = code.copy(path = PathStep.Linear),
                    tab = SIDE_TAB,
                    caption = "90°",
                )
            },
            { copy(motion = SpinMotion.Spin(SLOW), caption = "linear") },
            {
                copy(
                    stage = stage.copy(eased = 1f),
                    code = code.copy(path = PathStep.Eased),
                    caption = "EaseInOutSine",
                )
            },
            {
                copy(
                    stage = stage.copy(clip = true),
                    code = code.copy(draw = DrawStep.Clipped),
                    tab = THICKNESS_TAB,
                    caption = "",
                )
            },
            {
                copy(
                    motion = SpinMotion.Park(TIPPED),
                    tab = BUTTON_TAB,
                    caption = "${TIPPED.toInt()}°",
                )
            },
            { copy(stage = stage.copy(hop = 1f), code = code.copy(hop = true)) },
            { copy(motion = SpinMotion.Spin(SLOW), caption = "") },
            {
                copy(
                    stage = stage.copy(sideGradient = 1f),
                    code = code.copy(draw = DrawStep.Gradient),
                    tab = THICKNESS_TAB,
                )
            },
            {
                copy(
                    stage = stage.copy(sideShade = 1f),
                    code = code.copy(draw = DrawStep.Shadow),
                )
            },
            {
                copy(
                    motion = SpinMotion.Park(180f),
                    code = code.copy(back = BackStep.Check),
                    tab = BACK_TAB,
                    caption = "180°",
                )
            },
            {
                copy(
                    stage = stage.copy(back = BackStyle.Darkened),
                    motion = SpinMotion.Flip(),
                    code = code.copy(back = BackStep.Darken),
                    caption = "darker overlay",
                )
            },
            {
                copy(
                    stage = stage.copy(back = BackStyle.Swapped),
                    code = code.copy(back = BackStep.Swap),
                    caption = "different content",
                )
            },
            {
                copy(
                    stage = stage.copy(back = BackStyle.Tinted),
                    code = code.copy(back = BackStep.Tint),
                    caption = "SubAtomic",
                )
            },
        )

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().blurOut().fadeOut()) { content() }
        }

    @Composable
    override fun content(state: SpinBuildState) {
        val stage = animateSpinStage(state.stage)
        val angle = rememberSpinAngle(state.motion)
        val tabs = state.code.tabs()

        Row(
            modifier = Modifier.fillMaxSize().padding(56.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(56.dp),
            ) {
                SpinBody(
                    angle = { angle.value },
                    modifier = Modifier.width(DemoWidth).height(DemoHeight),
                    stage = stage,
                    thickness = DemoThickness,
                    backContent = { HabitLabel(text = "Done") },
                ) {
                    HabitLabel()
                }
                DemoCaption(
                    text = state.caption,
                    visible = if (state.caption.isEmpty()) 0f else 1f,
                )
            }

            CodeIDE(
                modifier = Modifier.zIndex(10f).weight(1.4f),
                tabs = tabs,
                selectedTab = tabs.indexOfFirst { it.first == state.tab }.coerceAtLeast(0),
                onTabSelect = {},
                textColor = SpinCodeText,
                tabTextColor = SpinCodeText,
                chromeColor = Red300,
                frameColor = Red700,
                panelColor = Red900,
                codeBackground = Black,
            )
        }
    }
}

private fun BuildCode.tabs(): List<Pair<String, String>> = listOf(
    BUTTON_TAB to buttonCode(),
    SHADING_TAB to if (shade) shadingCode() else "",
    SIDE_TAB to (path?.let(::sideCode) ?: ""),
    THICKNESS_TAB to (draw?.let(::thicknessCode) ?: ""),
    BACK_TAB to (back?.let(::backCode) ?: ""),
)

private fun BuildCode.buttonCode(): String {
    val modifiers = buildList {
        if (rotate) {
            add(".graphicsLayer {")
            if (hop) {
                add("    val hop = strokeWidth / 2f")
                add("    translationY = if (t < 1f) {")
                add("        lerp(0f, -hop, t)")
                add("    } else {")
                add("        lerp(hop, 0f, t - 1f)")
                add("    }")
            }
            add("    rotationX = angle")
            if (camera) add("    cameraDistance = 200f")
            add("}")
        }
        add(".size(width = 400.dp, height = 116.dp)")
        if (shade) {
            add(".clip(CircleShape)")
            add(".background(Red600)")
        } else {
            add(".background(Red600, CircleShape)")
        }
        if (border) add(".border(3.dp, rim, CircleShape)")
        if (shade) {
            add(".drawWithContent {")
            add("    drawContent()")
            add("    drawRect(Color.Black.copy(alpha = shade))")
            add("}")
        }
    }

    return buildString {
        if (border) {
            appendLine("val rim = Brush.sweepGradient(")
            appendLine("    listOf(Red500, Red800, Red500)")
            appendLine(")")
            appendLine()
        }
        appendLine("Box(")
        appendLine("    modifier = Modifier")
        modifiers.forEachIndexed { index, line ->
            val comma = if (index == modifiers.lastIndex) "," else ""
            appendLine("        $line$comma")
        }
        appendLine("    contentAlignment = Alignment.Center,")
        appendLine(") {")
        appendLine("    Text(\"Morning run\")")
        append("}")
    }
}

private fun BuildCode.shadingCode(): String = buildString {
    appendLine("val t = angle.mod(180f) / 90f")
    appendLine()
    appendLine("val shade = if (t < 1f) {")
    appendLine("    lerp(0f, .8f, EaseInSine.transform(t))")
    appendLine("} else {")
    appendLine("    lerp(.8f, 0f, EaseOutSine.transform(t - 1f))")
    append("}")
    if (draw != null && draw >= DrawStep.Shadow) {
        appendLine()
        appendLine()
        appendLine("val sideShade = if (t < 1f) {")
        appendLine("    lerp(.4f, 0f, EaseInSine.transform(t))")
        appendLine("} else {")
        appendLine("    lerp(0f, .4f, EaseOutSine.transform(t - 1f))")
        appendLine("}")
        append("val shadow = Color.Black.copy(alpha = sideShade)")
    }
}

private fun sideCode(path: PathStep): String = buildString {
    appendLine("fun DrawScope.sidePath(angle: Float): Path {")
    appendLine("    val mid = size.height / 2")
    appendLine("    val right = size.width")
    when (path) {
        PathStep.Rest -> appendLine("    val bottom = size.height")
        PathStep.Linear -> {
            appendLine("    val turn = angle.mod(180f) / 180f")
            appendLine("    val bottom = size.height * (1f - turn)")
        }

        PathStep.Eased -> {
            appendLine("    val turn = angle.mod(180f) / 180f")
            appendLine("    val progress = EaseInOutSine.transform(turn)")
            appendLine("    val bottom = size.height * (1f - progress)")
        }
    }
    appendLine()
    appendLine("    return Path().apply {")
    appendLine("        moveTo(0f, mid)")
    appendLine("        cubicTo(0f, bottom, mid, bottom, mid, bottom)")
    appendLine("        lineTo(right - mid, bottom)")
    appendLine("        cubicTo(right, bottom, right, mid, right, mid)")
    appendLine("    }")
    append("}")
}

private fun thicknessCode(draw: DrawStep): String = buildString {
    appendLine("Box(")
    appendLine("    modifier = Modifier")
    appendLine("        .matchParentSize()")
    if (draw >= DrawStep.Clipped) appendLine("        .clip(CircleShape)")
    appendLine("        .drawBehind {")
    appendLine("            val path = sidePath(angle)")
    if (draw == DrawStep.Thin) {
        appendLine("            val stroke = Stroke(width = 3.dp.toPx())")
    } else {
        appendLine("            val stroke = Stroke(")
        appendLine("                width = 24.dp.toPx(),")
        appendLine("                cap = StrokeCap.Round,")
        appendLine("            )")
    }
    if (draw >= DrawStep.Gradient) {
        appendLine("            val edge = (size.height / 2) / size.width")
        appendLine("            drawPath(")
        appendLine("                path = path,")
        appendLine("                brush = Brush.horizontalGradient(")
        appendLine("                    0f to Red950,")
        appendLine("                    edge to Red700,")
        appendLine("                    1f - edge to Red700,")
        appendLine("                    1f to Red950,")
        appendLine("                ),")
        appendLine("                style = stroke,")
        appendLine("            )")
    } else {
        appendLine("            drawPath(")
        appendLine("                path = path,")
        appendLine("                color = Red700,")
        appendLine("                style = stroke,")
        appendLine("            )")
    }
    if (draw >= DrawStep.Shadow) {
        appendLine("            drawPath(")
        appendLine("                path = path,")
        appendLine("                color = shadow,")
        appendLine("                style = stroke,")
        appendLine("            )")
    }
    appendLine("        }")
    append(")")
}

private fun backCode(back: BackStep): String = when (back) {
    BackStep.Check -> SHOWS_BACK

    BackStep.Darken -> """
$SHOWS_BACK

Box(
    modifier = Modifier
        .clip(CircleShape)
        .background(Red600)
        .drawWithContent {
            drawContent()
            if (showsBack) {
                drawRect(Color.Black.copy(alpha = .5f))
            }
        }
)
""".trim()

    BackStep.Swap -> """
$SHOWS_BACK

if (showsBack) {
    Text(
        text = "Done",
        modifier = Modifier.graphicsLayer {
            rotationX = 180f
        },
    )
} else {
    Text("Morning run")
}
""".trim()

    BackStep.Tint -> """
$SHOWS_BACK
val faceColor = if (showsBack) Red800 else Red600
val textAlpha = if (showsBack) .1f else 1f

Box(
    modifier = Modifier.background(faceColor, CircleShape)
) {
    Text(
        text = "Morning run",
        modifier = Modifier.alpha(textAlpha),
    )
}
""".trim()
}

private const val SHOWS_BACK = "val showsBack = angle.mod(360f) in 90f..270f"
