package com.sinasamaki.chromadecks._006_SpinAnimation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

internal sealed interface SpinMotion {
    data class Park(val angle: Float) : SpinMotion

    data class Spin(val degreesPerSecond: Float = 90f) : SpinMotion

    data class Flip(val restMillis: Long = 900) : SpinMotion
}

private val Settle = spring<Float>(dampingRatio = .6f, stiffness = Spring.StiffnessVeryLow)

@Composable
internal fun rememberSpinAngle(
    motion: SpinMotion,
    initial: Float = 0f,
): Animatable<Float, AnimationVector1D> {
    val angle = remember { Animatable(initial) }
    LaunchedEffect(motion) {
        when (motion) {
            is SpinMotion.Park -> {
                val offset = (motion.angle - angle.value).mod(360f).let {
                    if (it > 180f) it - 360f else it
                }
                angle.animateTo(
                    targetValue = angle.value + offset,
                    animationSpec = spring(
                        dampingRatio = .8f,
                        stiffness = Spring.StiffnessVeryLow,
                    ),
                )
            }

            is SpinMotion.Spin -> {
                val turnMillis = (360f / motion.degreesPerSecond * 1000f).roundToInt()
                while (true) {
                    angle.animateTo(
                        targetValue = angle.value + 360f,
                        animationSpec = tween(durationMillis = turnMillis, easing = LinearEasing),
                    )
                }
            }

            is SpinMotion.Flip -> {
                while (true) {
                    val face = (angle.value / 180f).roundToInt() * 180f
                    angle.animateTo(targetValue = face + 180f, animationSpec = Settle)
                    delay(motion.restMillis)
                }
            }
        }
    }
    return angle
}
