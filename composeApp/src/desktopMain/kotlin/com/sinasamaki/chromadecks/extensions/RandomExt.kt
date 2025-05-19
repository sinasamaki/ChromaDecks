package com.sinasamaki.chromadecks.extensions

import kotlin.random.Random

fun Random.nextFloat(start: Float, end: Float) : Float {
    return start + ((end - start) * Random.nextFloat())
}