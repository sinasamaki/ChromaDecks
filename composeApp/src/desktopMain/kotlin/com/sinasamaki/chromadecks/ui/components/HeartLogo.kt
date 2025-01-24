package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks.ui.theme.Blue700
import com.sinasamaki.chromadecks.ui.theme.Gray900
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate900
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


val logo = arrayOf(
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 1, 1),
    arrayOf(1, 1, 1, 1, 0, 1),
    arrayOf(1, 0, 1, 0, 1, 1),
    arrayOf(0, 0, 1, 0, 0, 0)
)

//val a1 = 0.0f
//val a2 = 0.05f
//val a3 = 0.1f
//val a4 = 0.2f
val a1 = 0.0f
val a2 = 0.1f
val a3 = 0.2f
val a4 = 0.35f

val logoShading = arrayOf(
    arrayOf(a1, a1, a1, a1, a1, a1),
    arrayOf(a2, a1, a1, a2, a2, a1),
    arrayOf(a2, a2, a2, a2, a2, a2),
    arrayOf(a3, a3, a2, a4, a2, a2),
    arrayOf(a4, a3, a3, a4, a3, a3),
    arrayOf(a4, a4, a3, a4, a4, a4),
    arrayOf(a4, a4, a4, a4, a4, a4),
    arrayOf(a4, a4, a4, a4, a4, a4),
    arrayOf(a4, a4, a4, a4, a4, a4)
)


@Composable
fun HeartLogo(modifier: Modifier = Modifier) {

    val value = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        while (true) {
            value.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
            delay(2000)
            value.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
            delay(1000)
        }
    }
    Box(modifier = modifier
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        ) {
            scope.launch {
                value.animateTo(
                    if (value.targetValue == 0f) 1f else 0f,
                    animationSpec = tween(durationMillis = 3000)
                )
            }
        }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (i in 0..logo.lastIndex) {
                Row {
                    for (j in 0..logo[i].lastIndex) {
                        val isSolid = logo[i][j] == 1
                        val randomizer =
                            remember {
                                ((Random.nextInt(0, i + 1)
                                    .toFloat() + Random.nextFloat()) / (logo.lastIndex * 1.5f))

//                                ((i + 1 + j).toFloat() + (Random.nextFloat() * 2f)) / (logo.lastIndex + logo[0].lastIndex + 2)
                            }

                        val start = value.value > randomizer

                        val localValue = remember { Animatable(0f) }

                        LaunchedEffect(start) {
                            localValue.animateTo(
                                targetValue = if (start) 1f else 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }


//                        val shape = getShape(value.value + (randomizer * (value.value * 1.8f)))
                        val shape = getShape(localValue.value)
                        Box(
                            Modifier
                                .size(28.dp)
                                .padding(if (shape != RectangleShape) 2.dp else 0.dp)
//                                .padding(
//                                    if (shape != RectangleShape) lerp(
//                                        18f,
//                                        0f,
//                                        localValue.value
//                                    ).dp else 0.dp
//                                )
                                .border(
                                    width = 4.dp,
                                    shape = shape,
                                    color = if (getColor(localValue.value) == Slate50) Slate900 else Transparent
                                )
                                .background(
                                    color = if (isSolid)
                                        if (shape != RectangleShape) getColor(localValue.value) else
                                            lerp(
                                                getColor(localValue.value),
                                                Gray900,
                                                logoShading[i][j]
                                            )
                                    else
                                        Transparent,
                                    shape = shape
                                )

                        )
                    }
                }
            }

            Spacer(Modifier.height(64.dp))
            val color: Color by remember {
                derivedStateOf {
                    lerp(Red500, Slate50, value.value)
                }
            }
            Text(
                text = "sinasamaki.com",
                modifier = Modifier.alpha(value.value),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
                color = color
            )
        }

    }

}

fun getShape(value: Float): Shape {
    return when {
//        value < .1f -> SmallCircle
//        value < .3f -> TriangleShape
//        value < .5f -> CircleShape
//        value < .7f -> RoundedCornerShape(8.dp)
//        value < .8f -> UnTriangleShape
//        value < .9f -> CutCornerShape(100)
        value < .9f -> CircleShape
        else -> RectangleShape
    }
}

fun getColor(value: Float): Color {
//    return when {
//        value < .1f -> Purple600
//        value < .3f -> Orange600
//        value < .5f -> Teal600
//        value < .7f -> Red400
//        value < .8f -> Fuchsia600
//        value < .9f -> Red700
//        else -> Red500
//    }
    return when {
        value < .1f -> Transparent
//        value < .1f -> Slate900
//        value < .3f -> Slate900
//        value < .5f -> Amber500
//        value < .7f -> Slate50
//        value < .8f -> Red900
//        value < .9f -> Red900
//        else -> Red500
        value < .3f -> Blue700
        value < .6f -> Sky600
        value < .9f -> Yellow300
        else -> Red600
    }
}


object TriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(size.width / 2f, 0f)
        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        path.close()
        return Outline.Generic(path = path)
    }

}

object UnTriangleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        path.moveTo(size.width / 2f, size.height)
        path.lineTo(0f, 0f)
        path.lineTo(size.width, 0f)
        path.close()
        return Outline.Generic(path = path)
    }

}

object SmallCircle : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                rect = Rect(
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width / 10
                ),
                cornerRadius = CornerRadius(size.width)
            )
        )
    }

}



@Composable
fun SimpleLogo(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for (i in 0..logo.lastIndex) {
            Row {
                for (j in 0..logo[i].lastIndex) {
                    val isSolid = logo[i][j] == 1
                    Box(
                        Modifier
                            .size(28.dp)
                            .background(
                                color = if (isSolid) Red500 else Transparent,
                            )

                    )
                }
            }
        }
    }
}

object HeartShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(createHeartPath(size))
    }

}

fun createHeartPath(size: Size): Path {
    return Path().apply {
        moveTo(0f, 0f)
        val width = size.width / logo[0].size
        val height = size.height / logo.size
        for (y in 0..logo.lastIndex) {
            for (x in 0..logo[y].lastIndex) {
                val isSolid = logo[y][x] == 1
                if (isSolid) {
                    moveTo(x * width, y * height)
                    addRect(
                        rect = Rect(
                            offset = Offset(x * width, y * height),
                            size = Size(width, height)
                        )
                    )
                }
            }
        }
    }
}

fun DrawScope.renderShading(width: Float, height: Float) {
    val width = width / logoShading[0].size
    val height = height / logoShading.size
    for (y in 0..logoShading.lastIndex) {
        for (x in 0..logoShading[y].lastIndex) {
            val alpha = logoShading[y][x]
            drawRect(
                color = Zinc900.copy(alpha),
                topLeft = Offset(x * width, y * height),
                size = Size(width, height)
            )
        }
    }
}

fun Modifier.heartRatio(): Modifier = aspectRatio(2 / 3f)