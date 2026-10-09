@file:JvmName("RibbonThumbnail")

package com.sinasamaki.chromadecks._005_RibbonModifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._005_RibbonModifier.components.ribbon
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Violet500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import org.jetbrains.skia.EncodedImageFormat
import java.io.File

private const val WIDTH = 1920
private const val HEIGHT = 1080

fun main(args: Array<String>) {
    val output = File(args.getOrElse(0) { "ribbon-modifier-thumbnail.png" })
    val font = File(args.getOrElse(1) { "src/commonMain/composeResources/font/jet_brains_mono_bold.ttf" })
    val numbers = args.getOrNull(2)?.split(":").orEmpty().map { it.toFloat() }
    val defaults = ThumbnailShape()
    val shape = ThumbnailShape(
        loops = numbers.getOrNull(0)?.toInt() ?: defaults.loops,
        height = numbers.getOrNull(1) ?: defaults.height,
        stroke = numbers.getOrNull(2) ?: defaults.stroke,
        boxWidth = numbers.getOrNull(3) ?: defaults.boxWidth,
        fontSize = numbers.getOrNull(4) ?: defaults.fontSize,
        buttonWidth = numbers.getOrNull(5) ?: defaults.buttonWidth,
        gap = numbers.getOrNull(6)?.toInt() ?: defaults.gap,
        textOffset = numbers.getOrNull(7) ?: defaults.textOffset,
        textOnTop = numbers.getOrNull(8)?.let { it != 0f } ?: defaults.textOnTop,
    )
    val mono = FontFamily(Font(file = font, weight = FontWeight.Bold))

    val scene = ImageComposeScene(width = WIDTH, height = HEIGHT, density = Density(1f)) {
        RibbonThumbnail(mono, shape)
    }
    val image = try {
        scene.render()
    } finally {
        scene.close()
    }

    output.writeBytes(requireNotNull(image.encodeToData(EncodedImageFormat.PNG)).bytes)
    println("Wrote ${output.absolutePath}")
}

@Composable
internal fun RibbonThumbnail(font: FontFamily, shape: ThumbnailShape) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Zinc50)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Violet500.copy(alpha = .10f),
                        Rose500.copy(alpha = .05f),
                        White.copy(alpha = 0f),
                    ),
                    center = Offset(WIDTH * .5f, HEIGHT * .45f),
                    radius = WIDTH * .7f,
                ),
            )
            .thumbnailGrid(),
        contentAlignment = Alignment.Center,
    ) {
        WrappedButton(font, shape)
    }
}

@Composable
private fun WrappedButton(font: FontFamily, shape: ThumbnailShape) {
    val text = "ribbon" + " ".repeat(shape.gap) + "modifier"
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .requiredSize(shape.boxWidth.dp, shape.height.dp)
                .ribbon(
                    colors = RIBBON_COLORS,
                    stroke = shape.stroke.dp,
                    loops = shape.loops,
                    progress = { 1f },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                font = font,
                width = shape.buttonWidth,
                height = shape.height,
                fontSize = shape.fontSize,
                text = if (shape.textOnTop) null else text,
                textOffset = shape.textOffset,
            )
        }

        if (shape.textOnTop) {
            Label(font, text, shape.fontSize, shape.textOffset, outline = shape.fontSize * .07f)
        }
    }
}

internal data class ThumbnailShape(
    val loops: Int = 3,
    val height: Float = 480f,
    val stroke: Float = 110f,
    val boxWidth: Float = 1920f,
    val fontSize: Float = 200f,
    val buttonWidth: Float = 1920f,
    val gap: Int = 1,
    val textOffset: Float = 0f,
    val textOnTop: Boolean = false,
)

@Composable
private fun Button(
    font: FontFamily,
    width: Float,
    height: Float,
    fontSize: Float,
    modifier: Modifier = Modifier,
    text: String? = "ribbon modifier",
    textOffset: Float = 0f,
) {
    Box(
        modifier = Modifier
            .requiredSize(width.dp, height.dp)
            .then(modifier)
            .shadow(elevation = 40.dp, shape = CircleShape, spotColor = Black.copy(alpha = .25f))
            .background(White, CircleShape)
            .border(3.dp, Zinc200, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (text != null) Label(font, text, fontSize, textOffset)
    }
}

@Composable
private fun Label(
    font: FontFamily,
    text: String,
    fontSize: Float,
    offset: Float,
    outline: Float = 0f,
) {
    val style = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize.sp,
        letterSpacing = (-4).sp,
    )
    Box(modifier = Modifier.offset(x = offset.dp)) {
        if (outline > 0f) {
            Text(
                text = text,
                style = style.copy(
                    color = White,
                    drawStyle = Stroke(width = outline * 2f, join = StrokeJoin.Round),
                ),
            )
        }
        Text(text = text, style = style.copy(color = Zinc900))
    }
}

private fun Modifier.thumbnailGrid(): Modifier = drawWithCache {
    val cell = 72f
    val line = 2f / cell
    val color = Zinc200.copy(alpha = .8f)
    val stops = arrayOf(
        0f to color,
        line to color,
        line to Color.Transparent,
        1f to Color.Transparent,
    )
    val shift = cell / 2f
    val rows = Brush.verticalGradient(
        colorStops = stops,
        startY = shift,
        endY = shift + cell,
        tileMode = TileMode.Repeated,
    )
    val columns = Brush.horizontalGradient(
        colorStops = stops,
        startX = shift,
        endX = shift + cell,
        tileMode = TileMode.Repeated,
    )
    onDrawBehind {
        drawRect(brush = rows, size = Size(size.width, size.height))
        drawRect(brush = columns, size = Size(size.width, size.height))
    }
}
