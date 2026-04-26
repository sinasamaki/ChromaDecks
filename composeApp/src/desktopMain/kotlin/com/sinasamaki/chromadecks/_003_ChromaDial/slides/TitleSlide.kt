package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.toPx
import com.sinasamaki.chromadecks.ui.frames.TitleFrame
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Indigo300
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate200
import com.sinasamaki.chromadecks.ui.theme.Slate300
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate800
import com.sinasamaki.chromadecks.ui.theme.Slate900
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc500
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.Zinc950

internal data class TitleSlideState(val revealed: Boolean = false)

internal class TitleSlide : ListSlideAdvanced<TitleSlideState>() {

    override val initialState: TitleSlideState
        get() = TitleSlideState()

    override val stateMutations: List<TitleSlideState.() -> TitleSlideState>
        get() = listOf()

    @Composable
    override fun content(state: TitleSlideState) {

        val t by animateFloatAsState(
            targetValue = if (state.revealed) 1f else 0f,
            animationSpec = tween(durationMillis = 3000)
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(Zinc950)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {

            Box(
                modifier = Modifier
                    .height(600.dp)
                    .aspectRatio(.5f)
            ) {
                val buttonSize = 3.dp
                Column(
                    modifier = Modifier
                        .offset(x = (-buttonSize), y = 110.dp)
                        .width(buttonSize),
                ) {
                    DeviceButton(
                        buttonSize = buttonSize
                    )
                    Spacer(Modifier.height(20.dp))
                    DeviceButton(
                        buttonSize = buttonSize
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (buttonSize), y = 110.dp)
                        .width(buttonSize)
                        .scale(scaleX = -1f, scaleY = 1f),
                ) {
                    Spacer(Modifier.height(30.dp))
                    DeviceButton(
                        buttonSize = buttonSize
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Neutral900,
                            shape = RoundedCornerShape(32.dp)
                        )
                        .border(
                            width = 0.dp,
                            shape = RoundedCornerShape(32.dp),
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Zinc300.copy(alpha = .3f),
                                    Zinc300.copy(alpha = .2f),
                                )
                            )
                        )
                        .padding(12.dp)
                        .background(
                            color = Zinc500,
                            shape = RoundedCornerShape(20.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            shape = RoundedCornerShape(32.dp)
                        )
                        .blur(
                            radius = 6.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded,
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(32.dp),
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Zinc300.copy(alpha = .8f),
                                    Zinc300.copy(alpha = .2f),
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            shape = RoundedCornerShape(32.dp)
                        )
                        .background(
                            shape = RoundedCornerShape(32.dp),
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Transparent,
                                    Zinc200.copy(alpha = .1f),
                                    Transparent,
                                )
                            )
                        )
                )
            }

//            TitleFrame(
//                modifier = Modifier.fillMaxSize(),
//                title = "chroma \ndial",
//                description = "custom dial controls with \n" +
//                        "smooth animations and haptic feedback",
//                hint = "rotationGestures()",
//                bookNumber = 3,
//                contentColor = Zinc900
//            )
        }
    }
}


@Composable
fun DeviceButton(
    buttonSize: Dp = 12.dp
) {
    Box(
        modifier = Modifier
            .width(buttonSize)
            .height(60.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Zinc800,
                        Zinc950,
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = buttonSize,
                    bottomStart = buttonSize
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Zinc200.copy(alpha = .3f),
                        Transparent,
                    ),
                    endX = 2.dp.toPx
                ),
                shape = RoundedCornerShape(
                    topStart = buttonSize,
                    bottomStart = buttonSize
                )
            )
    )
}