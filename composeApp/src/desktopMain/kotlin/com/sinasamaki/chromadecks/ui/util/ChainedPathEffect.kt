package com.sinasamaki.chromadecks.ui.util

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle

class ChainedPathEffect {

    private val effectList = mutableListOf<PathEffect>()

    val effect: PathEffect?
        get() = chainEffect(effectList)

    private fun chainEffect(
        effects: List<PathEffect>,
        index: Int = 0,
    ): PathEffect? = when {
        effects.isEmpty() -> null
        index == effectList.lastIndex -> effects.last()
        else -> PathEffect.Companion.chainPathEffect(
            chainEffect(effects, index + 1)!!,
            effects[index]
        )
    }

    fun dashPathEffect(intervals: FloatArray, phase: Float = 0f): ChainedPathEffect {
        effectList.add(PathEffect.Companion.dashPathEffect(intervals, phase))
        return this
    }

    fun stampedPathEffect(
        shape: Path,
        advance: Float,
        phase: Float,
        style: StampedPathEffectStyle
    ): ChainedPathEffect {
        effectList.add(PathEffect.Companion.stampedPathEffect(shape, advance, phase, style))
        return this
    }

    fun cornerPathEffect(radius: Float): ChainedPathEffect {
        effectList.add(PathEffect.Companion.cornerPathEffect(radius))
        return this
    }
}