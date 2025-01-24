package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.animation.core.*
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.ui.theme.Blue700
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.ui.theme.Purple300
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Slate900
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.theme.Rose800
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Yellow500
import com.sinasamaki.chromadecks.ui.theme.Zinc900

internal data class TitleSlideState(val revealed: Boolean = false)
internal class TitleSlide : ListSlide<TitleSlideState>() {

    override val states: List<TitleSlideState>
        get() = listOf(
            TitleSlideState(false),
            TitleSlideState(true),
            )

    @Composable
    override fun content(state: TitleSlideState) {

//        val infinite = rememberInfiniteTransition()
//        val t by infinite.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                animation = tween(4_500),
//                repeatMode = RepeatMode.Restart,
//            )
//        )
        val t by animateFloatAsState(
            targetValue = if (state.revealed) 1f else 0f,
            animationSpec = tween(durationMillis = 3000)
        )

        val dark = Zinc900
        CompositionLocalProvider(
            LocalContentColor provides dark
        ) {
            TitleFrame(
                modifier = Modifier
//                    .background(Color.White)
                    .clip(RectangleShape)
                    .meshGradient(
                        showPoints = false,
                        points = listOf(
                            listOf(
                                Offset(-.3f, -10f) to dark,
                                Offset(.5f, -10f) to dark,
                                Offset(1.3f, -10f) to dark,
                            ),


                            listOf(
                                Offset(-.3f, lerp(1.5f, -2.5f, t)) to dark,
                                Offset(.5f, lerp(1.1f, -6.7f, t)) to dark,
                                Offset(1.3f, lerp(1.5f, -2.5f, t)) to dark,
                            ),
                            listOf(
                                Offset(-.3f, lerp(2.5f, -1.8f, t)) to Rose800,
                                Offset(.35f, lerp(1.2f, -2.9f, t)) to Rose800,
                                Offset(1.3f, lerp(2.5f, -1.8f, t)) to Rose800,
                            ),
                            listOf(
                                Offset(-.3f, lerp(3.5f, -1.2f, t)) to Yellow500,
                                Offset(.5f, lerp(2.8f, -2.2f, t)) to Yellow500,
                                Offset(1.3f, lerp(3.5f, -1.2f, t)) to Yellow500,
                            ),
                            listOf(
                                Offset(-.3f, lerp(5f, 0f, t)) to Slate50,
                                Offset(.3f, lerp(2.9f, 0f, t)) to Slate50,
                                Offset(1.3f, lerp(5f, 0f, t)) to Slate50,
                            ),


                            listOf(
                                Offset(-.3f, 1f) to Slate50,
                                Offset(.5f, 1.1f) to Slate50,
                                Offset(1.3f, 1f) to Slate50,
                            ),
                        ),
//                        showPoints = true,
                        resolutionX = 24,
                        resolutionY = 24,
                    )
            )
        }

        return
        Box(
            Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "_001_MeshGradients",
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFeatureSettings = "liga 0"
                )
            )
            Text(
                text = "Modifier",
                modifier = Modifier.align(Alignment.TopEnd).graphicsLayer {
                    transformOrigin = TransformOrigin(1f, 1f)
                    rotationZ = -90f
                },
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFeatureSettings = "liga 0"
                )
            )
            Text(
                text = "sinasamaki",
                modifier = Modifier.align(Alignment.BottomCenter),
                style = MaterialTheme.typography.displaySmall,
            )

            val color1 = Rose500
            val color2 = Purple300
            val color3 = Blue700

            Row(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    Modifier
                        .size(64.dp)
                        .background(
                            color = color1,
                            shape = CircleShape
                        )
                )

                Box(
                    Modifier
                        .size(64.dp)
                        .background(
                            color = color2,
                            shape = CircleShape
                        )
                )

                Box(
                    Modifier
                        .size(64.dp)
                        .background(
                            color = color3,
                            shape = CircleShape
                        )
                )
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(64.dp))
                    .meshGradient(
                        points = listOf(
                            listOf(
                                Offset(0f, 0f) to color1,
                                Offset(.3f, 0f) to color1,
                                Offset(.6f, 0f) to color1,
                                Offset(1f, 0f) to color1,
                            ),
                            listOf(
                                Offset(0f, .5f) to color2,
                                Offset(.4f, .2f) to color2,
                                Offset(.5f, .8f) to color2,
                                Offset(1f, .5f) to color2,
                            ),
                            listOf(
                                Offset(0f, 1f) to color3,
                                Offset(.3f, 1f) to color3,
                                Offset(.6f, 1f) to color3,
                                Offset(1f, 1f) to color3,
                            ),
                        ),
                        resolutionX = 12,
                        resolutionY = 12,
                    )
                    .padding(horizontal = 128.dp, vertical = 256.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    "Mesh Gradients",
                    modifier = Modifier.scale(scaleY = 1.0f, scaleX = 1f),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Slate900,
                )
//                Text(
//                    "Gradients",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp,
//                    letterSpacing = 24.sp
//                )
            }

        }
    }
}