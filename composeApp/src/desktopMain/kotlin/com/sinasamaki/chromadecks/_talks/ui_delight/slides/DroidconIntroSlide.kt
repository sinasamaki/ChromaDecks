package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import chromadecks.composeapp.generated.resources.myheart
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chromadecks.composeapp.generated.resources.Res
import coil3.compose.AsyncImage
import com.sinasamaki.chromadecks._talks.ui_delight.components.DroidconLogo3D
import com.sinasamaki.chromadecks._talks.ui_delight.components.DroidconLogoColors
import com.sinasamaki.chromadecks._talks.ui_delight.components.Social
import com.sinasamaki.chromadecks._talks.ui_delight.components.SocialHandle
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.Space
import com.sinasamaki.chromadecks.ui.theme.Emerald200
import com.sinasamaki.chromadecks.ui.theme.Green600
import com.sinasamaki.chromadecks.ui.theme.Sky950
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

class DroidconIntroSlideState
class DroidconIntroSlide : ListSlideAdvanced<DroidconIntroSlideState>() {

    override val initialState: DroidconIntroSlideState
        get() = DroidconIntroSlideState()

    @Composable
    override fun content(state: DroidconIntroSlideState) {
        var spin by remember { mutableFloatStateOf(0f) }
        var tilt by remember { mutableFloatStateOf(-18f) }


        val leftAntennaDirection = remember { Animatable(0f) }
        val rightAntennaDirection = remember { Animatable(0f) }
        val rotationZ = remember { Animatable(0f) }
        val rotationY = remember { Animatable(0f) }
        val translationY = remember { Animatable(0f) }
        val translationX = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                // Antenna hi
                launch {
                    rotationZ.animateTo(
                        -5f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )
                }
                launch {
                    translationX.animateTo(
                        70f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioHighBouncy
                        )
                    )
                }
                (0..7).forEach { i ->
                    rightAntennaDirection.animateTo(
                        -10f,
                        animationSpec = tween(120, easing = EaseInOutSine)
                    )
                    rightAntennaDirection.animateTo(
                        10f,
                        animationSpec = tween(120, easing = EaseInOutSine)
                    )
                }
                launch {
                    rotationZ.animateTo(
                        0f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    )
                }
                launch {
                    translationX.animateTo(
                        0f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioHighBouncy
                        )
                    )
                }
                // Antenna hi

                delay(500)

                // Jump
                launch {
                    translationY.animateTo(
                        -360f,
                        animationSpec = tween(durationMillis = 450, easing = EaseInOutSine)
                    )
                    translationY.animateTo(
                        0f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioHighBouncy
                        )
                    )
                }
                rotationY.animateTo(
                    360f,
                    animationSpec = tween(durationMillis = 900, easing = EaseInOutSine)
                )
                rotationY.snapTo(0f)
                // Jump


                delay(1200)

                // Wiggle
                launch {
                    translationY.animateTo(
                        -100f,
                        animationSpec = tween(durationMillis = 1500, easing = EaseInOutSine)
                    )
                    translationY.animateTo(
                        0f,
                        animationSpec = spring(
                            stiffness = Spring.StiffnessVeryLow,
                            dampingRatio = Spring.DampingRatioHighBouncy
                        )
                    )
                }
                (0..10).forEach { i ->
                    launch {
                        rightAntennaDirection.animateTo(
                            -10f,
                            animationSpec = tween(70, easing = EaseInOutSine)
                        )
                        rightAntennaDirection.animateTo(
                            10f,
                            animationSpec = tween(70, easing = EaseInOutSine)
                        )
                    }
                    leftAntennaDirection.animateTo(
                        -10f,
                        animationSpec = tween(70, easing = EaseInOutSine)
                    )
                    leftAntennaDirection.animateTo(
                        10f,
                        animationSpec = tween(70, easing = EaseInOutSine)
                    )
                }
                // Wiggle

                delay(1800)


            }
        }

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF101321),
                            Color(0xFF0B2068),
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f),
                    )
                )
        ) {
            DroidconLogo3D(
                rotation = rotationY.value,
                tilt = tilt,
                colors = DroidconLogoColors(
                    base = Green600,
                    shadow = Sky950,
                    highlight = Emerald200,
                ),
                leftAntennaDirectionOffset = leftAntennaDirection.value,
                rightAntennaDirectionOffset = rightAntennaDirection.value,
                rotationZ = rotationZ.value,
                translationY = translationY.value,
                translationX = translationX.value,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(.5f)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            spin += dragAmount.x * 0.4f
                            tilt = (tilt - dragAmount.y * 0.2f).coerceIn(-18f, 18f)
                        }
                    }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(32.dp)
                    .fillMaxWidth(.5f)
            ){
                Text(
                    text = "Crafting delightful apps your users will love",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 84.sp
                    ),
                    color = Zinc200,
                )

                Space(16.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = Zinc200.copy(alpha = .4f),
                            shape = CircleShape,
                        )
                )

                Space(64.dp)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.myheart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                    Space(32.dp)
                    Column {
                        Text(
                            text = "sinasamaki",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 56.sp
                            ),
                            color = Zinc200,
                        )
                        Space(8.dp)
                        Social.entries.forEach { social ->
                            SocialHandle(
                                social = social,
                                color = Zinc200,
                                fontSize = 24.sp,
                                iconSize = 24.dp,
                            )
                            Space(4.dp)
                        }
                    }
                }
            }
        }
    }
}
