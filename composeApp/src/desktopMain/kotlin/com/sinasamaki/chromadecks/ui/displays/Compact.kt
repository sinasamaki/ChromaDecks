package com.sinasamaki.chromadecks.ui.displays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc500

@Composable
fun CompactDevice(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 300.dp)
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
                .clip(
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            content()
        }
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

}