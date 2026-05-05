package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.DialColors
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Amber300
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Amber500
import com.sinasamaki.chromadecks.ui.theme.Lime300
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Rose300
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky300
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Violet300
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Violet500
import kotlinx.coroutines.delay

private data class DialColorScheme(
    val name: String,
    val activeTrack: Color,
    val thumbStroke: Color,
    val activeTick: Color,
)

private val colorSchemes = listOf(
    DialColorScheme("Lime",   Lime500,   Lime400,   Lime300),
    DialColorScheme("Sky",    Sky500,    Sky400,    Sky300),
    DialColorScheme("Rose",   Rose500,   Rose400,   Rose300),
    DialColorScheme("Amber",  Amber500,  Amber400,  Amber300),
    DialColorScheme("Violet", Violet500, Violet400, Violet300),
)

internal data class DefaultDialState(
    val showStartDegrees: Boolean = false,
    val targetStartDegrees: Float = 0f,
    val showSweepDegrees: Boolean = false,
    val targetSweepDegrees: Float = 360f,
    val showInterval: Boolean = false,
    val showColors: Boolean = false,
)

internal class DefaultDialSlide : ListSlideAdvanced<DefaultDialState>() {

    override val initialState get() = DefaultDialState()

    override val stateMutations get() = listOf<DefaultDialState.() -> DefaultDialState>(
        { copy(showStartDegrees = true, targetStartDegrees = 225f) },
        { copy(showSweepDegrees = true, targetSweepDegrees = 270f) },
        { copy(targetStartDegrees = 0f, targetSweepDegrees = 1440f) },
        { copy(showInterval = true) },
        { copy(showColors = true) },
    )

    @Composable
    override fun content(state: DefaultDialState) {
        var degree by remember { mutableFloatStateOf(0f) }
        var colorIndex by remember { mutableIntStateOf(0) }

        LaunchedEffect(state.showColors) {
            if (!state.showColors) {
                colorIndex = 0
                return@LaunchedEffect
            }
            while (true) {
                delay(800)
                colorIndex = (colorIndex + 1) % colorSchemes.size
            }
        }

        val animatedStart by animateFloatAsState(state.targetStartDegrees)
        val animatedSweep by animateFloatAsState(state.targetSweepDegrees)

        val scheme = colorSchemes[colorIndex]
        val activeTrackColor by animateColorAsState(scheme.activeTrack)
        val thumbStrokeColor by animateColorAsState(scheme.thumbStroke)
        val activeTickColor by animateColorAsState(scheme.activeTick)

        val code = buildString {
            appendLine("var degree by remember {")
            appendLine("    mutableFloatStateOf(0f)")
            appendLine("}")
            appendLine("")
            appendLine("Dial(")
            appendLine("    degree = degree,")
            appendLine("    onDegreeChange = { degree = it },")
            append("    modifier = Modifier.size(280.dp),")
            if (state.showStartDegrees) {
                appendLine()
                append("    startDegrees = ${state.targetStartDegrees.toInt()}f,")
            }
            if (state.showSweepDegrees) {
                appendLine()
                append("    sweepDegrees = ${state.targetSweepDegrees.toInt()}f,")
            }
            if (state.showInterval) {
                appendLine()
                append("    interval = 10f,")
            }
            if (state.showColors) {
                appendLine()
                appendLine("    colors = DialColors.default(")
                appendLine("        activeTrackColor = ${scheme.name}500,")
                append("        thumbStrokeColor = ${scheme.name}400,")
                appendLine()
                append("    ),")
            }
            appendLine()
            append(")")
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier.weight(1f),
                tabs = listOf("DialUsage.kt" to code),
                selectedTab = 0,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Dial(
                    degree = degree,
                    onDegreeChange = { degree = it },
                    modifier = Modifier.size(280.dp),
                    startDegrees = animatedStart,
                    sweepDegrees = animatedSweep,
                    interval = if (state.showInterval) 10f else 0f,
                    colors = DialColors.default(
                        activeTrackColor = activeTrackColor,
                        thumbStrokeColor = thumbStrokeColor,
                        activeTickColor = activeTickColor,
                    ),
                )
            }
        }
    }
}
