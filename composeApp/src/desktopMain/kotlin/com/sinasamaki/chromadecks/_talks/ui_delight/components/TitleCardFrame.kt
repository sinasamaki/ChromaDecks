package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.RobotoFlex
import com.sinasamaki.chromadecks._003_ChromaDial.slides.CaratDisplay
import com.sinasamaki.chromadecks._003_ChromaDial.slides.InfluenceCircle
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Neutral200
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Pink50
import com.sinasamaki.chromadecks.ui.theme.Pink500
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.Font
import kotlin.random.Random

@Composable
fun TitleCardFrame(
    title: String,
    description: String,
    backgroundColor: Color,
    borderColor: Color,
) {
    val backgroundColor = Black
    val borderColor = Orange400
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

//        Box(
//            Modifier
//                .fillMaxSize()
//                .drawWithContent {
//                    drawContent()
//                }
//                .padding(32.dp)
//                .border(
//                    width = 0.dp,
//                    color = borderColor,
//                )
//                .padding(4.dp)
//                .border(
//                    width = 0.dp,
//                    color = borderColor,
//                )
//        )
        val y by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing)
            )
        )


//        AnimatedCheckerBoard()
//        AnimatedCheckerBoard()

        data class LineData(val quadrant: Int, val startXFraction: Float, val endYFraction: Float)

        val lines = remember {
            (0..20).map {
                LineData(
                    quadrant = Random.nextInt(4),
                    startXFraction = Random.nextFloat() * .25f,
                    endYFraction = Random.nextFloat() * .25f,
                )
            }
        }

        Cube(
            modifier = Modifier.size(300.dp),
            angleX = -20f,
            angleY = y,
        ) { face ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                        drawRect(
                            color = backgroundColor.copy(alpha = .7f)
                        )
//                        drawCheckerboard(
//                            primary = borderColor,
//                            secondary = borderColor,
//                            squareNumber = 3
//                        )
                        for (line in lines) {
                            rotate(degrees = 90f * line.quadrant) {
                                drawLine(
//                                    color = borderColor,
                                    brush = Brush.linearGradient(
                                        colors = listOf(Orange400, Purple400, Pink500)
                                    ),
                                    start = Offset(x = line.startXFraction * size.width, y = 0f),
                                    end = Offset(x = 0f, y = line.endYFraction * size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                        }
                    }
                    .border(
                        width = 2.dp,
//                        color = borderColor,
                        brush = Brush.linearGradient(
                            colors = listOf(Orange400, Purple400, Pink500)
                        ),
                        shape = RectangleShape
                    )
                    .innerShadow(
                        shape = RectangleShape
                    ) {
//                        color = borderColor
                        brush = Brush.linearGradient(
                            colors = listOf(Orange400, Purple400, Pink500)
                        )
                        radius = 120f
                        alpha = .2f
                    }
            )
        }


//        val titleReveal by rememberInfiniteTransition().animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(6000, easing = LinearEasing)
//            )
//        )

        val titleReveal = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            titleReveal.snapTo(0f)
            delay(200)
            titleReveal.animateTo(
                targetValue = 1f,
                animationSpec =  tween(6000, easing = LinearEasing)
            )
        }

        CaratDisplay(
            modifier = Modifier
                .fillMaxSize(),
            colors = listOf(Orange400, Purple400, Orange500),
            circles = {
                listOf(
                    InfluenceCircle(
                        center = it.center,
                        radius = it.center.x + lerp(-200f, 900f, titleReveal.value),
                    )
                )
            }
        )

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
            MaxText(
                text = title,
                modifier = Modifier
                    .offset(
                        y = androidx.compose.ui.unit.lerp(60.dp, 0.dp, titleReveal.value)
                    )
                    .drawWithContent {
                        layer {
                            this@drawWithContent.drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(White, Transparent),
                                    startY = lerp(
                                        -100f,
                                        size.height,
                                        (titleReveal.value / .2f).coerceIn(0f..1f)
                                    ),
                                    endY = lerp(
                                        0f,
                                        size.height * 1.1f,
                                        (titleReveal.value / .2f).coerceIn(0f..1f)
                                    ),
                                ),
                                blendMode = BlendMode.DstIn
                            )
                        }
                    }
                    .padding(horizontal = 32.dp),
                maxFont = 280.sp,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = fontFamily,
                    fontSize = 280.sp,
                    letterSpacing = -10.sp,
//                    shadow = Shadow(
//                        blurRadius = 50f,
//                        color = Black.copy(alpha = .3f)
//                    ),
                    shadow = Shadow(
                        blurRadius = 1f,
                        color = White,
                    ),
//                    color = backgroundColor,
                    brush = Brush.verticalGradient(
                        colors = listOf(Zinc50, backgroundColor),
                        startY = lerp(-1000f, 900f, titleReveal.value),
                        endY = lerp(0f, 2900f, titleReveal.value),
                    )
                ),
            )
            Text(
                description,
                modifier = Modifier.fillMaxWidth(.5f)
                    .align(Alignment.CenterHorizontally),
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                color = Zinc50.copy(alpha = .2f)
            )
        }
    }

}


@Composable
fun AnimatedCheckerBoard(modifier: Modifier = Modifier) {
    var xCheck by remember { mutableStateOf(0) }
    var yCheck by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
//            val to = Random.nextInt(-8, 8)
//            val x = if (Random.nextBoolean()) 1 else -1
//            val y = if (Random.nextBoolean()) 1 else -1
//            Animatable(xCheck.toFloat())
//                .animateTo(
//                    to.toFloat(),
//                    animationSpec = tween(
//                        durationMillis = 2000,
//                        easing = LinearEasing,
//                    )
//                ) {
//                    xCheck = this.value.toInt() * x
//                    yCheck = this.value.toInt() * y
//                }
//            delay(300)

            xCheck = Random.nextInt(-8, 8)
            yCheck = Random.nextInt(-8, 8)
            delay(500)
        }
    }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (xCheck.toInt() * 60.dp.toPx()).toInt(),
                    y = (yCheck.toInt() * 60.dp.toPx()).toInt(),
                )
            }
            .alpha(.1f)
//            .fillMaxSize(.5f)
//            .aspectRatio(1f)
            .size(500.dp)
            .drawBehind {
                drawCheckerboard(
                    primary = Neutral200,
                    secondary = Neutral200,
                    squareNumber = 5,
                )
            }
    )
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
    squareNumber: Int = 5,
) {
    val width = size.width / squareNumber
    val height = size.width / squareNumber
    layer {
        drawRect(
            brush = Brush.verticalGradient(
                0f to primary,
                0.5f to secondary,
                0.5f to Transparent,
                1f to Transparent,
                tileMode = TileMode.Repeated,
                startY = 0f,
                endY = width * 2,
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
                endX = height * 2,
            ),
            blendMode = BlendMode.Xor
        )
    }

}
