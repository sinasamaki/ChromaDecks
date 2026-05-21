package com.sinasamaki.kotlinconf.utils

import androidx.compose.ui.graphics.Path

fun Path.moveTo(x: Int, y: Int) = moveTo(x.toFloat(), y.toFloat())
fun Path.lineTo(x: Int, y: Int) = lineTo(x.toFloat(), y.toFloat())
