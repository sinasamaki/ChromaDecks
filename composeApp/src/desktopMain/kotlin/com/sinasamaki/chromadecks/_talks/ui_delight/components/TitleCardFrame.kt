package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.RobotoFlex
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Indigo300
import com.sinasamaki.chromadecks.ui.theme.Indigo400
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Pink400
import com.sinasamaki.chromadecks.ui.theme.Pink500
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Yellow400
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import org.jetbrains.compose.resources.Font

@Composable
fun TitleCardFrame(
    title: String,
    description: String,
    backgroundColor: Color,
    borderColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                }
                .padding(32.dp)
                .border(
                    width = 1.dp,
                    color = borderColor,
                )
                .padding(4.dp)
                .border(
                    width = 4.dp,
                    color = borderColor,
                )
        )
        val y by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing)
            )
        )

        Cube(
            modifier = Modifier.size(300.dp),
            angleX = -20f,
            angleY = y,
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        drawRect(
                            color = Black.copy(alpha = .8f)
                        )
                        drawCheckerboard(
                            primary = Indigo300,
                            secondary = Indigo400,
                            squareLength = 200f
                        )
                    }
                    .border(
                        width = 2.dp,
                        color = Pink500,
                    )
            )
        }

//            Box(
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .width(300.dp)
//                    .height(100.dp)
//                    .drawBehind {
//                        drawStripes(
//                            color = Yellow500,
//                            stripeLength = 60f,
//                        )
//                    }
//            )

        Box(
            modifier = Modifier
                .size(300.dp)
                .drawBehind {
                    rotate(
                        degrees = 90f
                    ) {

//                            translate(
//                                left = -250f,
//                                top = -250f,
//                            ) {
//                                drawRect(
//                                    color = Slate200.copy(alpha = .1f)
//                                )
//                                drawRect(
//                                    brush = Brush.horizontalGradient(
//                                        colors = listOf(
//                                            Purple100,
//                                            Purple400,
//                                        )
//                                    ),
//                                    style = Stroke(width = 2f)
//                                )
//                            }
                    }
                }
        )

        val xCheck by rememberInfiniteTransition().animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )
        val yCheck by rememberInfiniteTransition().animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (xCheck.toInt() * 60.dp.toPx()).toInt(),
                        y = (yCheck.toInt() * 60.dp.toPx()).toInt(),
//                            y = 0,
                    )
                }
                .size(300.dp)
                .drawBehind {
                    drawCheckerboard(
                        primary = Yellow400,
                        secondary = Lime400,
                        squareLength = 60.dp.toPx(),
                    )
                }
        )

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = -(xCheck.toInt() * 60.dp.toPx()).toInt(),
                        y = (yCheck.toInt() * 60.dp.toPx()).toInt(),
//                            y = 0,
                    )
                }
                .size(300.dp)
                .drawBehind {
                    drawCheckerboard(
                        primary = Pink400,
                        secondary = Orange600,
                        squareLength = 60.dp.toPx(),
                    )
                }
        )


        val fontFamily = FontFamily(
            Font(
                resource = Res.font.RobotoFlex,
                variationSettings = FontVariation.Settings(
                    FontVariation.Setting("wght", 400f),
                    FontVariation.Setting("wdth", 25f),
                    FontVariation.Setting("YTUC", 760f),
                    FontVariation.Setting("YOPQ", 25f),
                    FontVariation.Setting("XOPQ", 175f),
                    FontVariation.Setting("XTRA", 300f),
                ),
            )
        )

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = fontFamily,
                    fontSize = 280.sp,
                    letterSpacing = -10.sp,
                    shadow = Shadow(
                        blurRadius = 50f,
                        color = Black.copy(alpha = .3f)
                    )
                ),

                color = Zinc50,
            )
            Text(
                description,
                color = Zinc50
            )
        }
    }

}


private fun DrawScope.drawStripes(
    color: Color,
    stripeLength: Float = 40f
) {
    layer {
        rotate(
            degrees = 45f
        ) {
            drawCircle(
                brush = Brush.verticalGradient(
                    0f to color,
                    0.5f to color,
                    0.5f to Transparent,
                    1f to Transparent,
                    tileMode = TileMode.Repeated,
                    startY = 0f,
                    endY = stripeLength,
                ),
                radius = size.width * size.height
            )
        }
    }
}


private fun DrawScope.drawCheckerboard(
    primary: Color,
    secondary: Color,
    squareLength: Float = 40f
) {
    layer {
        drawRect(
            brush = Brush.verticalGradient(
                0f to primary,
                0.5f to secondary,
                0.5f to Transparent,
                1f to Transparent,
                tileMode = TileMode.Repeated,
                startY = 0f,
                endY = squareLength * 2,
            )
        )
        drawRect(
            brush = Brush.horizontalGradient(
                0f to primary,
                0.5f to secondary,
                0.5f to Transparent,
                1f to Transparent,
                tileMode = TileMode.Repeated,
                startX = 0f,
                endX = squareLength * 2,
            ),
            blendMode = BlendMode.Xor
        )
    }

}

private fun DrawScope.layer(
    bounds: Rect = size.toRect(),
    block: DrawScope.() -> Unit
) =
    drawIntoCanvas { canvas ->
        canvas.withSaveLayer(
            bounds = bounds,
            paint = Paint(),
        ) { block() }
    }