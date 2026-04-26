package com.sinasamaki.chromadecks.ui.slideanimations

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.ui.components.LocalSlideProgress
import kotlin.math.absoluteValue

// progress: 1f = offscreen below, 0f = in position, -1f = offscreen above
// startAt: 0f = animation spans full range [1f→0f], 0.3f = kicks in at progress 0.7f

private fun enterFraction(progress: Float, startAt: Float): Float {
    if (progress <= 0f) return 1f
    val threshold = (1f - startAt.absoluteValue).coerceIn(0f, 1f)
    if (threshold == 0f) return 0f
    if (progress >= threshold) return 0f
    return (1f - progress / threshold).coerceIn(0f, 1f)
}

private fun exitFraction(progress: Float, startAt: Float): Float {
    if (progress >= 0f) return 1f
    val threshold = (1f - startAt.absoluteValue).coerceIn(0f, 1f)
    val t = (-progress).coerceIn(0f, 1f)
    if (threshold == 0f) return 0f
    if (t >= threshold) return 0f
    return (1f - t / threshold).coerceIn(0f, 1f)
}

@Composable
fun Modifier.fadeIn(
    startAt: Float = 0f,
    initial: Float = 0f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        alpha = lerp(initial, 1f, enterFraction(progress, startAt))
    }
}

@Composable
fun Modifier.fadeOut(
    startAt: Float = 0f,
    initial: Float = 0f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        alpha = lerp(initial, 1f, exitFraction(progress, startAt))
    }
}

@Composable
fun Modifier.blurIn(
    startAt: Float = 0f,
    initial: Float = 20f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    val blur = lerp(initial, 0f, enterFraction(progress, startAt))
    return this.graphicsLayer {
        renderEffect = if (blur > 0f)
            androidx.compose.ui.graphics.BlurEffect(blur, blur)
        else null
    }
}

@Composable
fun Modifier.blurOut(
    startAt: Float = 0f,
    initial: Float = 20f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    val blur = lerp(initial, 0f, exitFraction(progress, startAt))
    return this.graphicsLayer {
        renderEffect = if (blur > 0f)
            androidx.compose.ui.graphics.BlurEffect(blur, blur)
        else null
    }
}

@Composable
fun Modifier.scaleIn(
    startAt: Float = 0f,
    initial: Float = 0.8f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        val s = lerp(initial, 1f, enterFraction(progress, startAt))
        scaleX = s
        scaleY = s
    }
}

@Composable
fun Modifier.scaleOut(
    startAt: Float = 0f,
    initial: Float = 0.8f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        val s = lerp(initial, 1f, exitFraction(progress, startAt))
        scaleX = s
        scaleY = s
    }
}

@Composable
fun Modifier.translateInX(
    startAt: Float = 0f,
    initial: Float = 100f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        translationX = lerp(initial, 0f, enterFraction(progress, startAt))
    }
}

@Composable
fun Modifier.translateOutX(
    startAt: Float = 0f,
    initial: Float = 100f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        translationX = lerp(initial, 0f, exitFraction(progress, startAt))
    }
}

@Composable
fun Modifier.translateInY(
    startAt: Float = 0f,
    initial: Float = 100f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        translationY = lerp(initial, 0f, enterFraction(progress, startAt))
    }
}

@Composable
fun Modifier.translateOutY(
    startAt: Float = 0f,
    initial: Float = 100f,
): Modifier {
    val progress = LocalSlideProgress.current.progress
    return this.graphicsLayer {
        translationY = lerp(initial, 0f, exitFraction(progress, startAt))
    }
}

@Composable
fun Modifier.parallax(
    factor: Float = .5f
): Modifier {
    val progress = LocalSlideProgress.current.progress

    return this
        .graphicsLayer {
            translationY = progress * -size.height * factor
        }
}
