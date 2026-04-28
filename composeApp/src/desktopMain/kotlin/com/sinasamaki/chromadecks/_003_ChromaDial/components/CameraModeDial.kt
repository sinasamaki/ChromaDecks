package com.sinasamaki.chromadecks._003_ChromaDial.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.DialInterval
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.White

@Composable
fun CameraModeDial() {
    var degree by remember { mutableFloatStateOf(90f) }
    val animatedDegree by animateFloatAsState(
        targetValue = degree,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioLowBouncy,
        )
    )
    Dial(
        degree = animatedDegree,
        onDegreeChange = { degree = it },
        modifier = Modifier.fillMaxSize(),
            startDegrees = -90f,
            sweepDegrees = 220f,
            interval = 20f,
            thumb = {
                Box(Modifier.fillMaxSize())
            },
            track = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawEveryInterval(
                                startDegrees = -180f,
                                sweepDegrees = 180f,
                                spacing = 4f,
                                radius = it.radius,
                            ) { data ->
                                rotate(
                                    data.rotationAngle,
                                    pivot = data.position
                                ) {
                                    drawLine(
                                        color = White,
                                        start = data.position,
                                        end = data.position + Offset(
                                            0f,
                                            if (data.rotationAngle in (-91f)..(-89f)) 30f else 10f
                                        )
                                    )
                                }
                            }
                        }
                )
                DialInterval(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = it.absoluteDegree + it.overshootDegrees
                        }
                        .fillMaxSize()
                        .padding(20.dp),
                    startDegrees = -220f,
                    sweepDegrees = 220f,
                    spacing = 20f,
                ) { data ->
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(0f, .5f)
                            }
                            .background(
                                color = if (data.index == 4) Red500 else Transparent,
                            )
                            .border(
                                width = 1.dp,
                                color = when (data.index) {
                                    0 -> Green500
                                    4 -> Red400
                                    else -> White
                                },
                            )
                            .padding(2.dp)
                    ) {
                        when (data.index) {
                            0 -> Text(
                                text = "Auto",
                                fontSize = 8.sp,
                                color = Green500,
                                fontFamily = FontFamily.Monospace,
                            )
                            1 -> Text(
                                text = "P",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            2 -> Text(
                                text = "A",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            3 -> Text(
                                text = "S",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            4 -> Text(
                                text = "M",
                                fontSize = 10.sp,
                                color = Black,
                                fontFamily = FontFamily.Monospace,
                            )
                            5 -> Icon(
                                imageVector = Icons.Default.Api,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(12.dp)
                            )
                            6 -> Icon(
                                imageVector = Icons.Default.BrowseGallery,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(12.dp)
                            )
                            7 -> Text(
                                text = "B",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            8 -> Text(
                                text = "C",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            9 -> Icon(
                                imageVector = Icons.Default.ModeNight,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(12.dp)
                            )
                            10 -> Text(
                                text = "Video",
                                fontSize = 8.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                            11 -> Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(12.dp)
                            )
                            else -> Text(
                                text = "${data.index}",
                                fontSize = 10.sp,
                                color = White,
                                fontFamily = FontFamily.Monospace,
                            )
                        }
                    }
                }
            }
        )
}
