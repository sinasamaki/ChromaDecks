package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._001_MeshGradients.meshGradient
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.ui.components.ChromaSlider
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Sky600
import com.sinasamaki.chromadecks.ui.theme.Sky700
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.White
import kotlin.math.roundToInt

private val yellow = Color(0xFF0C0C0C)
private val red = Color(0xFFEC2D59)
private val blue = Color(0xFF2F6AE1)

class FirstSlide : ListSlideSimple() {

    @Composable
    override fun content() {
        Row {


            var stepsX by rememberSaveable { mutableStateOf(1f) }
            var stepsY by rememberSaveable { mutableStateOf(1f) }
            var showPoints by rememberSaveable { mutableStateOf(false) }

            Column(
                Modifier.weight(1f).fillMaxHeight().padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ChromaSlider(
                    value = stepsX,
                    onValueChange = { stepsX = it },
                    valueRange = 1f..32f,
                    displayValue = { "${it.toInt()}" },
                )

                Spacer(Modifier.height(16.dp))
                ChromaSlider(
                    value = stepsY,
                    onValueChange = { stepsY = it },
                    valueRange = 1f..32f,
                    displayValue = { "${it.toInt()}" },
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            showPoints = !showPoints
                        }
                        .padding(16.dp)
                ) {
                    Text("Show points")
                    Spacer(Modifier.width(16.dp))
                    Switch(
                        checked = showPoints,
                        onCheckedChange = null,
                    )
                }
            }

            var size by remember { mutableStateOf(IntSize.Zero) }
            var offset1 by rememberSaveable { mutableStateOf(Offset(.3f, .25f)) }
            var offset2 by rememberSaveable { mutableStateOf(Offset(.6f, .25f)) }
            var offset3 by rememberSaveable { mutableStateOf(Offset(.3f, .75f)) }
            var offset4 by rememberSaveable { mutableStateOf(Offset(.6f, .75f)) }
            Box(
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(3/4f)
                    .align(Alignment.CenterVertically)
                    .padding(64.dp)
                    .clip(
                        RoundedCornerShape(64.dp)
                    )
                    .onSizeChanged {
                        size = it
                    }
                    .meshGradient(
                        resolutionX = stepsX.toInt(),
                        resolutionY = stepsY.toInt(),
                        showPoints = showPoints,
//                        points = listOf(
//                            listOf(
//                                Offset(0f, 0f) to Rose500,
//                                Offset(.33f, 0f) to Rose500,
//                                Offset(.66f, 0f) to Rose500,
//                                Offset(1f, 0f) to Rose500,
//                            ),
//                            listOf(
//                                Offset(0f, .25f) to Rose500,
//                                Offset(offset1.x, offset1.y) to Color.Transparent,
//                                Offset(offset2.x, offset2.y) to Color.Transparent,
//                                Offset(1f, .25f) to Rose500,
//                            ),
//                            listOf(
//                                Offset(0f, .75f) to Rose500,
//                                Offset(offset3.x, offset3.y) to Color.Transparent,
//                                Offset(offset4.x, offset4.y) to Color.Transparent,
//                                Offset(1f, .75f) to Rose500,
//                            ),
//                            listOf(
//                                Offset(0f, 1f) to Rose500,
//                                Offset(.33f, 1f) to Rose500,
//                                Offset(.66f, 1f) to Rose500,
//                                Offset(1f, 1f) to Rose500,
//                            ),
//                        )
                        points = listOf(
                            listOf(
                                Offset(0f, 0f) to Slate950,
                                Offset(.33f, 0f) to Slate950,
                                Offset(.66f, 0f) to Slate950,
                                Offset(1f, 0f) to Slate950,
                            ),
                            listOf(
                                Offset(0f, .25f) to Amber400,
                                Offset(offset1.x, offset1.y) to Amber400,
                                Offset(offset2.x, offset2.y) to Amber400,
                                Offset(1f, .25f) to Amber400,
                            ),
                            listOf(
                                Offset(0f, .75f) to red,
                                Offset(offset3.x, offset3.y) to red,
                                Offset(offset4.x, offset4.y) to red,
                                Offset(1f, .75f) to red,
                            ),
                            listOf(
                                Offset(0f, 1f) to Sky600,
                                Offset(.33f, 1f) to Sky600,
                                Offset(.66f, 1f) to Sky700,
                                Offset(1f, 1f) to Sky700,
                            ),
                        )
                    )
            ) {
                Handle(
                    offset = offset1,
                    size = size,
                    setOffset = {
                        offset1 = it
                    }
                )

                Handle(
                    offset = offset2,
                    size = size,
                    setOffset = {
                        offset2 = it
                    }
                )

                Handle(
                    offset = offset3,
                    size = size,
                    setOffset = {
                        offset3 = it
                    }
                )

                Handle(
                    offset = offset4,
                    size = size,
                    setOffset = {
                        offset4 = it
                    }
                )
            }
        }
    }
}

@Composable
fun Handle(modifier: Modifier = Modifier, offset: Offset, size: IntSize, setOffset: (Offset) -> Unit) {
    val offsetState by rememberUpdatedState(offset)
    Box(
        modifier
            .size(32.dp)
            .offset {
                offsetState * size
            }
            .offset(x = (-16).dp, y = (-16).dp)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
            )
            .background(
                color = White,
                shape = CircleShape,
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    setOffset(offsetState + (dragAmount / size))
                }
            }
    )
}

private operator fun Offset.times(size: IntSize): IntOffset {
    return IntOffset((x * size.width).roundToInt(), (y * size.height).roundToInt())
}

private operator fun Offset.div(size: IntSize): Offset {
    return Offset(x / size.width, y / size.height)
}