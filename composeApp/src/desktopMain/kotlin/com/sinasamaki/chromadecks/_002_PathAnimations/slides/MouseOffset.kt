package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.extensions.relativeLineTo
import com.sinasamaki.chromadecks.extensions.toIntOffset
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Slate200
import com.sinasamaki.chromadecks.ui.theme.Slate300
import com.sinasamaki.chromadecks.ui.theme.Slate800
import com.sinasamaki.chromadecks.ui.theme.Slate950
import org.jetbrains.skiko.currentNanoTime

class MouseOffsetState()
class MouseOffset : ListSlideAdvanced<MouseOffsetState>() {

    override val initialState: MouseOffsetState
        get() = MouseOffsetState()

    override val stateMutations: List<MouseOffsetState.() -> MouseOffsetState>
        get() = listOf()

    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun content(state: MouseOffsetState) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeBlock(
                modifier = Modifier
                    .weight(1f),
                code = """
                    val path = Path()
                """.trimIndent()
            )


            val pathDrawings = remember { mutableStateListOf<Path>() }

            val workingPath by remember { mutableStateOf(Path()) }
            var redraw by remember { mutableLongStateOf(0L) }

            var cursorPosition by remember { mutableStateOf(Offset.Unspecified) }

            val textMeasurer = rememberTextMeasurer()
            Box(
                Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(
                        Slate800
                    )
                    .onPointerEvent(
                        eventType = PointerEventType.Move
                    ) {
                        cursorPosition = it.changes.first().position
                    }
                    .onPointerEvent(
                        eventType = PointerEventType.Exit
                    ) {
                        cursorPosition = Offset.Unspecified
                    }
                    .pointerInput(Unit) {
//                        var workingPath = Path()
                        detectDragGestures(
                            onDragStart = { offset ->
//                                var workingPath = Path()
                                workingPath.reset()
                                workingPath.moveTo(offset)
                            },
                            onDragEnd = {
                                pathDrawings.add(workingPath.copy())
                            },
                            onDrag = { offset ->
                                workingPath.relativeLineTo(offset)
                                redraw = currentNanoTime()
                            }
                        )
                    }
                    .drawBehind {
                        redraw
                        pathDrawings.plus(workingPath).forEach {
                            drawPath(
                                path = it,
                                color = Red500,
                                style = Stroke(
                                    width = 10.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                )
                            )
                        }

                        drawLine(
                            color = Purple400,
                            start = Offset(cursorPosition.x, 0f),
                            end = Offset(cursorPosition.x, size.height),
                        )

                        drawLine(
                            color = Purple400,
                            start = Offset( 0f, cursorPosition.y,),
                            end = Offset( size.height, cursorPosition.y,),
                        )


                        drawText(
                            textMeasurer = textMeasurer,
                            text = "${((cursorPosition.x / size.width) * 100f).toInt()}",
                            style = TextStyle.Default.copy(
                                color = Purple400
                            ),
                            topLeft = Offset(cursorPosition.x - 25f, -50f),
                            size = Size(50f, 50f)
                        )

                        drawText(
                            textMeasurer = textMeasurer,
                            text = "${((cursorPosition.y / size.height) * 100f).toInt()}",
                            style = TextStyle.Default.copy(
                                color = Purple400
                            ),
                            topLeft = Offset(-60f, cursorPosition.y - 20f),
                            size = Size(50f, 50f)
                        )
                    }
            ) {
                if (cursorPosition.isSpecified)
                    Box(
                        Modifier
                            .size(20.dp)
                            .offset {
                                if (cursorPosition.isSpecified)
                                    cursorPosition.toIntOffset()
                                else IntOffset.Zero
                            }
                            .offset(-10.dp, -10.dp)
                            .border(
                                width = 3.dp,
                                color = Slate950,
                                shape = CircleShape,
                            )
                            .background(
                                color = Slate300,
                                shape = CircleShape,
                            )
                    )
            }

        }
    }
}