package com.sinasamaki.chromadecks.ui.frames

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.img
import com.sinasamaki.chromadecks.ui.components.AccordionText
import com.sinasamaki.chromadecks.ui.util.StepsEasing
import com.sinasamaki.chromadecks.ui.util.progress
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

private val versionRange    = 0.0f..0.5f
private val hintRange       = 0.0f..0.7f
private val bookNumberRange  = 0.2f..0.5f
private val descriptionRange = 0.45f..0.9f
private val dividerRange     = 0.6f..1.0f

private val scrambleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

private val scrambleDigits = "0123456789"

private fun resolveText(text: String, progress: Float, charset: String = scrambleChars): String {
    if (progress >= 1f) return text
    return buildString {
        text.forEachIndexed { index, char ->
            val settleAt = (index + 1).toFloat() / text.length
            if (progress >= settleAt || char == ' ' || char !in charset) {
                append(char)
            } else {
                val step = (progress * 30).toInt()
                val i = ((step + 1) * 17 + index * 31).let { if (it < 0) -it else it } % charset.length
                append(charset[i])
            }
        }
    }
}

private fun scrambleText(text: String, progress: Float): String {
    if (progress >= 1f) return text
    if (progress <= 0f) return ""
    val scrambleSteps = 6
    val scrambleDuration = 0.06f
    return buildString {
        text.forEachIndexed { index, char ->
            val revealAt = index.toFloat() / text.length
            val settleAt = revealAt + scrambleDuration
            when {
                progress < revealAt -> {}
                progress >= settleAt || char == ' ' -> append(char)
                else -> {
                    val localP = (progress - revealAt) / scrambleDuration
                    val step = (localP * scrambleSteps).toInt()
                    val i = ((step + 1) * 17 + index * 31).let { if (it < 0) -it else it } % scrambleChars.length
                    append(scrambleChars[i])
                }
            }
        }
    }
}

@Composable
fun TitleFrame(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    hint: String,
    bookNumber: Int,
    animationProgress: Float = 1f,
    contentColor: Color = LocalContentColor.current
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Box(
            modifier
                .fillMaxSize()
                .padding(36.dp)
        ) {
            Title(
                modifier = Modifier.align(Alignment.Center),
                title = title,
                animationProgress = animationProgress,
            )

            VersionTag(
                modifier = Modifier.align(Alignment.TopStart),
                animationProgress = animationProgress,
            )

            Description(
                modifier = Modifier.align(Alignment.BottomStart),
                description = description,
                bookNumber = bookNumber,
                animationProgress = animationProgress,
            )

            HintTag(
                modifier = Modifier.align(Alignment.TopEnd),
                hint = hint,
                animationProgress = animationProgress,
            )

            TechLogos(
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    title: String,
    animationProgress: Float,
) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(0.dp) }

    Column(
        modifier = modifier.onSizeChanged { width = with(density) { it.width.toDp() } },
    ) {
        AccordionText(
            text = title,
            animationProgress = animationProgress,
        )

        Box(
            Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .width(width)
                .height(2.dp)
                .graphicsLayer {
                    scaleX = LinearOutSlowInEasing.transform(
                        dividerRange.progress(animationProgress)
                    )
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .background(LocalContentColor.current)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "by ",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
            )
            Text(
                "sinasamaki",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 28.sp,
                )
            )
        }
    }
}

@Composable
private fun VersionTag(modifier: Modifier = Modifier, animationProgress: Float) {
    Text(
        text = resolveText("v1.10.2", versionRange.progress(animationProgress), scrambleDigits),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
    )
}

@Composable
private fun Description(
    modifier: Modifier = Modifier,
    bookNumber: Int,
    description: String,
    animationProgress: Float,
) {
    val animatedBookNumber = if (bookNumber == 0) 0 else
        (StepsEasing(bookNumber).transform(bookNumberRange.progress(animationProgress)) * bookNumber).roundToInt()

    Column(
        modifier = modifier.width(300.dp)
    ) {
        Text(
            text = animatedBookNumber.toString().padStart(3, '0'),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = scrambleText(description, descriptionRange.progress(animationProgress)),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            )
        )
    }
}


@Composable
private fun HintTag(modifier: Modifier = Modifier, hint: String, animationProgress: Float) {
    Text(
        text = resolveText(hint, hintRange.progress(animationProgress)),
        modifier = modifier
            .graphicsLayer {
                val pivot = (size.width - (size.height / 2)) / size.width
                transformOrigin = TransformOrigin(
                    pivotFractionX = pivot,
                    pivotFractionY = .5f,
                )
                rotationZ = -90f
            },
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
    )
}


@Composable
fun TechLogos(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.img),
        contentDescription = null,
        modifier = modifier.blendMode(BlendMode.Lighten),
    )
}

fun Modifier.blendMode(blendMode: BlendMode) =
    drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()

        graphicsLayer.apply {
            record { drawContent() }
            this.blendMode = blendMode
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
