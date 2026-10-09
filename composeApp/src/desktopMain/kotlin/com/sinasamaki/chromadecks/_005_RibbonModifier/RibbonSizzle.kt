@file:JvmName("RibbonSizzle")

package com.sinasamaki.chromadecks._005_RibbonModifier

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._005_RibbonModifier.components.HabitRow
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonDiagram
import com.sinasamaki.chromadecks._005_RibbonModifier.components.RibbonExploded
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.GradientRibbon
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.INJECTION_END
import com.sinasamaki.chromadecks._005_RibbonModifier.slides.STAGE_ALONG_PATH
import com.sinasamaki.chromadecks.ui.theme.ChromaTheme
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Violet500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import java.io.File

private const val SIDE = 1080

private const val CANVAS = 720f

private class Scene(val seconds: Float, val content: @Composable (time: Float) -> Unit)

private val SCENES = listOf(
    Scene(seconds = .9f) { CubicScene(it) },
    Scene(seconds = .9f) { ExplodedScene(it) },
    Scene(seconds = 2f) { InjectionScene(it) },
)

fun main(args: Array<String>) {
    val output = File(args.getOrElse(0) { "ribbon-modifier-sizzle.mp4" })
    val fps = args.getOrNull(1)?.toInt() ?: 60

    var sceneIndex by mutableIntStateOf(0)
    var time by mutableFloatStateOf(0f)

    val scene = ImageComposeScene(width = SIDE, height = SIDE, density = Density(SIDE / CANVAS)) {
        ChromaTheme(colors = lightColorScheme()) {
            Box(Modifier.fillMaxSize().sizzleBackground(), contentAlignment = Alignment.Center) {
                SCENES[sceneIndex].content(time)
            }
        }
    }

    val ffmpeg = ProcessBuilder(
        "ffmpeg", "-y", "-loglevel", "error",
        "-f", "rawvideo", "-pix_fmt", "rgba", "-s", "${SIDE}x$SIDE", "-r", "$fps", "-i", "-",
        "-c:v", "libx264", "-preset", "slow", "-crf", "16", "-pix_fmt", "yuv420p",
        "-movflags", "+faststart",
        output.absolutePath,
    ).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start()

    val info = ImageInfo(SIDE, SIDE, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
    val bitmap = Bitmap().apply { allocPixels(info) }
    var frameNanos = 0L

    try {
        ffmpeg.outputStream.buffered(SIDE * SIDE * 4).use { pipe ->
            repeat(3) { scene.render(frameNanos); frameNanos += 1_000_000_000L / fps }

            SCENES.forEachIndexed { index, sizzle ->
                sceneIndex = index
                val frames = (sizzle.seconds * fps).toInt()
                repeat(frames) { frame ->
                    time = frame / fps.toFloat()
                    val image = scene.render(frameNanos)
                    frameNanos += 1_000_000_000L / fps
                    image.readPixels(bitmap)
                    pipe.write(requireNotNull(bitmap.readPixels()))
                    image.close()
                }
            }
        }
    } finally {
        scene.close()
    }

    check(ffmpeg.waitFor() == 0) { "ffmpeg failed" }
    println("Wrote ${output.absolutePath}")
}

private fun AnimationSpec<Float>.at(seconds: Float): Float {
    if (seconds <= 0f) return 0f
    return TargetBasedAnimation(this, Float.VectorConverter, 0f, 1f)
        .getValueFromNanos((seconds * 1_000_000_000L).toLong())
}

@Composable
private fun CubicScene(time: Float) {
    val loops = 3f
    val handleScale = spring<Float>(stiffness = Spring.StiffnessVeryLow, visibilityThreshold = .0001f)
        .at(time - .1f)

    RibbonDiagram(
        modifier = Modifier.width(660.dp).height(360.dp),
        sweep = 360f * loops,
        loops = loops,
        centerTravel = 1f,
        handleScale = handleScale,
        showRadius = false,
        showSamples = true,
        showHandles = true,
        trailToSamples = true,
    )
}

@Composable
private fun ExplodedScene(time: Float) {
    val start = time - .1f
    val open = spring<Float>(stiffness = Spring.StiffnessLow).at(start)
    val spread = spring<Float>(stiffness = Spring.StiffnessVeryLow).at(start)
    val planes = tween<Float>(durationMillis = 700).at(start)

    RibbonExploded(
        rotationX = 58f * open,
        spacing = 104.dp * spread,
        width = 460.dp,
        height = 124.dp,
        colors = FLAT,
        stroke = 20.dp,
        loops = 3,
        planes = planes,
        points = planes,
    ) {
        HabitRow(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun InjectionScene(time: Float) {
    val injected = tween<Float>(durationMillis = 1800, easing = LinearEasing).at(time - .1f) * INJECTION_END

    GradientRibbon(
        modifier = Modifier.offset(y = (-56).dp),
        stage = STAGE_ALONG_PATH,
        progress = { 1f },
        points = { Float.MAX_VALUE },
        ramp = { 1f },
        injected = { injected },
    )
}

private fun Modifier.sizzleBackground(): Modifier = this
    .background(Zinc50)
    .background(
        Brush.radialGradient(
            colors = listOf(
                Violet500.copy(alpha = .07f),
                Rose500.copy(alpha = .04f),
                White.copy(alpha = 0f),
            ),
            center = Offset(SIDE * .5f, SIDE * .35f),
            radius = SIDE * .9f,
        ),
    )
