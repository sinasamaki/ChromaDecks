package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.VertexMode
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._001_MeshGradients.components.PointHandle
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Amber500
import com.sinasamaki.chromadecks.ui.theme.Blue700
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Rose700
import com.sinasamaki.chromadecks.ui.theme.Slate50

private val initialCode = """
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = listOf(),
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

private val withOffsets = """
    val points: List<Offset> = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(1f, 1f),
    )
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = listOf(),
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

private val withOffsetsAndColors = """
    val points: List<Offset> = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(1f, 1f),
    )
    val colors: List<Color> = listOf(
        Red500,
        Yellow500,
        Teal500,
        Emerald500,
    )
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = listOf(),
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

private val withIndices = """
    val points: List<Offset> = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(1f, 1f),
    )
    val colors: List<Color> = listOf(
        Red500,
        Yellow500,
        Teal500,
        Emerald500,
    )
    val indices: List<Int> = listOf(
        0, 2, 3,
    )
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = indices,
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

private val withMoreIndices = """
    val points: List<Offset> = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(1f, 1f),
    )
    val colors: List<Color> = listOf(
        Red500,
        Yellow500,
        Teal500,
        Emerald500,
    )
    val indices: List<Int> = listOf(
        0, 2, 3,
        2, 1, 0,
    )
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = indices,
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

private val indicesAsSquare = """
    val points: List<Offset> = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(0f, 1f),
        Offset(1f, 1f),
    )
    val colors: List<Color> = listOf(
        Red500,
        Yellow500,
        Teal500,
        Emerald500,
    )
    val indices: List<Int> = listOf(
        0, 1, 3,
        0, 2, 3,
    )
    canvas.drawVertices(
        vertices = Vertices(
            vertexMode = VertexMode.Triangles,
            positions = points,
            textureCoordinates = points,
            colors = colors,
            indices = indices,
        ),
        blendMode = BlendMode.Dst,
        paint = Paint(),
    )
    """.trimIndent()

data class CodeSlideState(
    val code: String,
    val showGradient: Boolean = false,
    val showPoints: Boolean,
    val showColor: Boolean,
    val indices: List<Int> = listOf()
)

class CodeSlide : ListSlide<CodeSlideState>() {

    override val states: List<CodeSlideState>
        get() = listOf(
            CodeSlideState(
                code = initialCode,
                showPoints = false,
                showColor = false,
            ),
            CodeSlideState(
                code = withOffsets,
                showPoints = true,
                showColor = false,
            ),
            CodeSlideState(
                code = withOffsetsAndColors,
                showPoints = true,
                showColor = true,
            ),
            CodeSlideState(
                code = withOffsetsAndColors,
                showPoints = true,
                showColor = true,
                showGradient = true,
            ),
            CodeSlideState(
                code = withOffsetsAndColors,
                showPoints = true,
                showColor = true,
                showGradient = false,
            ),
            CodeSlideState(
                code = withIndices,
                showPoints = true,
                showColor = true,
                showGradient = true,
                indices = listOf(0, 2, 3)
            ),
            CodeSlideState(
                code = withMoreIndices,
                showPoints = true,
                showColor = true,
                showGradient = true,
                indices = listOf(0, 2, 3, 2, 1, 0)
            ),
            CodeSlideState(
                code = withOffsetsAndColors,
                showPoints = true,
                showColor = true,
                showGradient = false,
            ),
            CodeSlideState(
                code = indicesAsSquare,
                showPoints = true,
                showColor = true,
                showGradient = true,
                indices = listOf(0, 1, 3, 0, 2, 3)
            ),
        )

    @Composable
    override fun content(state: CodeSlideState) {


        Row(
            modifier = Modifier.padding(64.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopStart
            ) {
                CodeBlock(
                    modifier = Modifier.fillMaxHeight(),
                    code = state.code
                )
            }

            Sample(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                state = state,
            )

        }

    }

}

@Composable
fun Sample(
    modifier: Modifier = Modifier,
    state: CodeSlideState,
) {

    val points = remember {
        listOf(
            Offset(0f, 0f) to Rose700,
            Offset(1f, 0f) to Amber500,
            Offset(0f, 1f) to Fuchsia500,
            Offset(1f, 1f) to Blue700,
        )
    }
    BoxWithConstraints(
        modifier = modifier
            .padding(64.dp)
            .aspectRatio(1f)
            .drawBehind {
                if (state.showGradient)
                    drawIntoCanvas {
                        scale(
                            scaleX = size.width,
                            scaleY = size.height,
                            pivot = Offset.Zero
                        ) {
                            it.drawVertices(
                                vertices = Vertices(
                                    vertexMode = VertexMode.Triangles,
                                    positions = points.map { it.first },
                                    textureCoordinates = points.map { it.first },
                                    colors = points.map { it.second },
                                    indices = state.indices,
                                ),
                                blendMode = BlendMode.Dst,
                                paint = Paint(),
                            )
                        }
                    }
            }
    ) {

        points.forEach { (offset, color) ->
            PointHandle(
                color = color,
                offset = offset,
                showColors = state.showColor,
                showPoints = state.showPoints,
                contentColor = Slate50
            )
        }
    }
}

private fun code(
    showOffset: Boolean = false,

): String {
    return """
${
    if (showOffset){
        """
val points: List<Offset> = listOf(
    Offset(0f, 0f),
    Offset(1f, 0f),
    Offset(0f, 1f),
    Offset(1f, 1f),
)      
            """.trimIndent()
        } else ""
    }
val colors: List<Color> = listOf(
)
val indices: List<Int> = listOf(
)
canvas.drawVertices(
    vertices = Vertices(
        vertexMode = VertexMode.Triangles,
        positions = points,
        textureCoordinates = points,
        colors = colors,
        indices = indices,
    ),
    blendMode = BlendMode.Dst,
    paint = Paint(),
)
""".trimIndent()
}


//val points: List<Offset> = listOf(
//    Offset(0f, 0f),
//    Offset(1f, 0f),
//    Offset(0f, 1f),
//    Offset(1f, 1f),
//)
//val colors: List<Color> = listOf(
//    Red500,
//    Yellow500,
//    Teal500,
//    Emerald500,
//)
//it.drawVertices(
//    vertices = Vertices(
//        vertexMode = VertexMode.Triangles,
//        positions = points,
//        textureCoordinates = points,
//        colors = colors,
//        indices = listOf(),
//    ),
//    blendMode = BlendMode.Dst,
//    paint = Paint(),
//)