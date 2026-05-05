package com.sinasamaki.chromadecks.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.util.StepsEasing
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun AccordionText(
    text: String,
    animationProgress: Float,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge.copy(
        fontSize = 96.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 96.sp,
        letterSpacing = (-1).sp,
    ),
    shadowMaxOffset: Float = 30f,
    shadowStagger: Float = 0.3f,
    shadowStartColor: Color = Lime400,
    shadowEndColor: Color = Green500,
) {
    val n = text.length
    Row(modifier = modifier) {
        text.forEachIndexed { index, char ->
            val layer = rememberGraphicsLayer()
            val localStart = if (n > 1) index.toFloat() / (n - 1) * shadowStagger else 0f
            val localProgress = ((animationProgress - localStart) / (1f - shadowStagger).coerceAtLeast(0.001f))
                .coerceIn(0f, 1f)
                .let { StepsEasing(6).transform(it) }
            val offset = shadowMaxOffset * sin(localProgress * PI.toFloat()).coerceAtLeast(0f)

            Text(
                text = char.toString(),
                style = style,
                modifier = Modifier.drawWithContent {
                    if (localProgress <= 0f) return@drawWithContent
                    layer.record { this@drawWithContent.drawContent() }
                    for (i in 0..20) {
                        val x = i / 20f
                        translate(offset * x, -offset * x) {
                            layer {
                                drawLayer(layer)
                                drawRect(
                                    color = lerp(shadowStartColor, shadowEndColor, x),
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                        }
                    }
                    translate(offset, -offset) {
                        drawLayer(layer)
                    }
                },
            )
        }
    }
}
