package com.sinasamaki.kotlinconf.utils

import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.cos

fun Float.oscillateToOne(peaks: Int): Float {
    return (1f - cos((2 * peaks - 1) * PI.toFloat() * this)) / 2f
}

fun Float.oscillateToZero(peaks: Int): Float {
    return (1f - cos(2 * peaks * PI.toFloat() * this)) / 2f
}