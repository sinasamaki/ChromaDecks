package com.sinasamaki.chromadecks.ui.util

import androidx.compose.animation.core.Easing

fun StepsEasing(steps: Int) = Easing { fraction ->
    (fraction * steps).toInt() / steps.toFloat()
}


fun ClosedFloatingPointRange<Float>.progress(global: Float) =
    ((global - start) / (endInclusive - start)).coerceIn(0f, 1f)