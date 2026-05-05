package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Emerald500
import com.sinasamaki.chromadecks.ui.theme.Lime500
import com.sinasamaki.chromadecks.ui.theme.Neutral800
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val hueColors = List(13) { i -> Color.hsl(i * 30f, 1f, 0.5f) }
//private val hueColors = listOf(
//    Lime500,
//    Sky500,
//    Blue500,
//    Emerald500,
//    Lime500,
//)

@Composable
fun ColorPickerDial(modifier: Modifier = Modifier) {
    var degree by remember { mutableFloatStateOf(30f) }

    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = modifier.fillMaxSize(),
        thumb = {
            Box(
                Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .dropShadow(
                        shape = CircleShape
                    ) {
                        radius = 10f
                        alpha = .4f
                    }
                    .border(
                        width = 4.dp,
                        color = White,
                        shape = CircleShape,
                    )
//                    .background(
//                        color = Zinc800.copy(alpha = .3f),
//                        shape = CircleShape,
//                    )
            )
        },
        track = { state ->
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawArc(
                            brush = Brush.sweepGradient(hueColors, center = center),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            radius = center.x - 48.dp.toPx() / 2,
                            strokeWidth = 48.dp
                        )
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .innerShadow(shape = CircleShape) {
                        radius = 20f
                    }
                    .border(
                        width = 1.dp,
                        shape = CircleShape,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc50.copy(alpha = .3f),
                                Zinc300.copy(alpha = .3f),
                            )
                        )
                    )
                    .padding(48.dp)
                    .dropShadow(shape = CircleShape) {
                        radius = 20f
                    }
                    .border(
                        width = 1.dp,
                        shape = CircleShape,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Zinc50.copy(alpha = .3f),
                                Zinc300.copy(alpha = .3f),
                            )
                        )
                    )
            )
        }
    )
}
