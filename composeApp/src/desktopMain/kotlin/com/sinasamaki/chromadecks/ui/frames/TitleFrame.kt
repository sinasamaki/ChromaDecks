package com.sinasamaki.chromadecks.ui.frames

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
import org.jetbrains.compose.resources.painterResource

@Composable
fun TitleFrame(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    hint: String,
    bookNumber: Int,
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
                modifier = Modifier
                    .align(Alignment.Center),
                title = title,
            )

            VersionTag(
                modifier = Modifier
                    .align(Alignment.TopStart)
            )

            Description(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                description = description,
                bookNumber = bookNumber,
            )

            HintTag(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                hint = hint,
            )

            TechLogos(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )

        }
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier, title: String) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(0.dp) }

    Column(
        modifier = modifier.onSizeChanged { width = with(density) { it.width.toDp() } },
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 96.sp
            )
        )

        Box(
            Modifier
                .padding(top = 32.dp, bottom = 16.dp)
                .width(width)
                .height(2.dp)
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
private fun VersionTag(modifier: Modifier = Modifier) {
    Text(
        text = "v1.7.0",
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
    )
}

@Composable
private fun Description(modifier: Modifier = Modifier, bookNumber: Int, description: String) {

    Column(
        modifier = modifier.width(300.dp)
    ) {

        Text(
            text = bookNumber.toString().padStart(3, '0'),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp
            )
        )
    }
}


@Composable
private fun HintTag(modifier: Modifier = Modifier, hint: String) {
    Text(
        text = hint,
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
        modifier = modifier.blendMode(BlendMode.Darken),
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