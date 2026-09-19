package com.sinasamaki.chromadecks._004_TimelyTimer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.sinasamaki.chromadecks._004_TimelyTimer.components.RadialDriftBackground
import com.sinasamaki.chromadecks._004_TimelyTimer.components.TimelyDial
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.ChromaTheme
import com.sinasamaki.chromadecks.ui.theme.Red
import com.sinasamaki.chromadecks.ui.theme.Swatch
import com.sinasamaki.chromadecks.ui.theme.chromaticRingSize
import com.sinasamaki.chromadecks.ui.theme.plus

fun main() = application {
    Window(
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        title = "Timely Dial",
        onCloseRequest = ::exitApplication,
        content = {
            ChromaTheme {
                TimelyDialShowcase()
            }
        },
    )
}

/**
 * The [TimelyDial] on its own — no slides — over a full-screen [RadialDriftBackground] whose
 * palette is driven by the dial. Each full turn of the dial walks the whole chromatic ring:
 * Red at 0°, back to Red at 360°.
 */
@Composable
fun TimelyDialShowcase() {
    var degree by remember { mutableFloatStateOf(0f) }

    val swatchStep by remember {
        derivedStateOf {
            ((degree.mod(360f) / 360f) * chromaticRingSize).toInt()
        }
    }
    val step by remember {
        derivedStateOf {
            ((degree / 360f) * chromaticRingSize).toInt()
        }
    }
    val swatch: Swatch = Red + swatchStep

    Box(
        modifier = Modifier
            .background(Black)
            .fillMaxSize()
            .aspectRatio(16/9f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(1920.dp, 1080.dp),
            contentAlignment = Alignment.Center,
        ) {
            RadialDriftBackground(
                modifier = Modifier.fillMaxSize(),
                centerColor = swatch.v400,
                midColor = (swatch + 3).v400,
                edgeColor = (swatch + 6).v200,
                index = step,
            )
            TimelyDial(
                swatch = swatch,
                onDegree = { degree = it },
            )
        }
    }
}
