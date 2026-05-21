package com.sinasamaki.kotlinconf.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density

fun DrawScope.expandPathCubic(
    source: Path,
    padding: Float,
    cornerRadius: RoundedCornerShape = RoundedCornerShape(percent = 50),
    density: Density = this,
    steps: Int = 64,
): Path {
    val size = Size(padding * 2, padding * 2)
    val endOuterCr = cornerRadius.topStart.toPx(size, density).coerceIn(0f, padding)
    val endInnerCr = cornerRadius.topEnd.toPx(size, density).coerceIn(0f, padding)
    val startInnerCr = cornerRadius.bottomEnd.toPx(size, density).coerceIn(0f, padding)
    val startOuterCr = cornerRadius.bottomStart.toPx(size, density).coerceIn(0f, padding)
    val k = 0.5523f

    val measure = PathMeasure()
    measure.setPath(source, false)
    val length = measure.length
    if (length == 0f) return Path()

    val right = mutableListOf<Offset>()
    val left = mutableListOf<Offset>()

    for (i in 0..steps) {
        val distance = (i / steps.toFloat()) * length
        val pos = measure.getPosition(distance)
        val tan = measure.getTangent(distance)
        val perp = Offset(tan.y, -tan.x)
        right.add(pos + perp * padding)
        left.add(pos - perp * padding)
    }

    val startPos = measure.getPosition(0f)
    val startTan = measure.getTangent(0f)
    val startPerp = Offset(startTan.y, -startTan.x)

    val endPos = measure.getPosition(length)
    val endTan = measure.getTangent(length)
    val endPerp = Offset(endTan.y, -endTan.x)

    val endArc1End = endPos + endTan * endOuterCr + endPerp * (padding - endOuterCr)
    val endArc2Start = endPos + endTan * endInnerCr - endPerp * (padding - endInnerCr)

    val startArc1End = startPos - startTan * startInnerCr - startPerp * (padding - startInnerCr)
    val startArc2Start = startPos - startTan * startOuterCr + startPerp * (padding - startOuterCr)

    return Path().apply {
        moveTo(right.first().x, right.first().y)
        right.drop(1).forEach { lineTo(it.x, it.y) }

        val ec1 = right.last() + endTan * (endOuterCr * k)
        val ec2 = endArc1End + endPerp * (endOuterCr * k)
        cubicTo(ec1.x, ec1.y, ec2.x, ec2.y, endArc1End.x, endArc1End.y)
        lineTo(endArc2Start.x, endArc2Start.y)
        val ec3 = endArc2Start - endPerp * (endInnerCr * k)
        val ec4 = left.last() + endTan * (endInnerCr * k)
        cubicTo(ec3.x, ec3.y, ec4.x, ec4.y, left.last().x, left.last().y)

        left.reversed().drop(1).forEach { lineTo(it.x, it.y) }

        val sc1 = left.first() - startTan * (startInnerCr * k)
        val sc2 = startArc1End - startPerp * (startInnerCr * k)
        cubicTo(sc1.x, sc1.y, sc2.x, sc2.y, startArc1End.x, startArc1End.y)
        lineTo(startArc2Start.x, startArc2Start.y)
        val sc3 = startArc2Start + startPerp * (startOuterCr * k)
        val sc4 = right.first() - startTan * (startOuterCr * k)
        cubicTo(sc3.x, sc3.y, sc4.x, sc4.y, right.first().x, right.first().y)

        close()
    }
}
