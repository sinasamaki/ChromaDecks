package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.zIndex

@Composable
fun Cube(
    modifier: Modifier = Modifier,
    angleX: Float = 0f,
    angleY: Float = 0f,
    angleZ: Float = 0f,
    faceContent: @Composable (CubeFace) -> Unit,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        CubeFace.entries.forEachIndexed { index, face ->
            val graphicsLayer = rememberGraphicsLayer()
            val zIndex: Float by remember(angleX, angleY, angleZ) {
                derivedStateOf {
                    calculateZIndex(
                        angleX,
                        angleY,
                        angleZ,
                        face
                    )
                }
            }
            Box(
                modifier = Modifier
                    .zIndex(zIndex)
                    .fillMaxSize()
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        cubeTransform(
                            angleX,
                            angleY,
                            angleZ,
                            face,
                        ) {
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                faceContent(face)
            }
        }
    }

}


private fun DrawScope.cubeTransform(
    angleX: Float,
    angleY: Float,
    angleZ: Float,
    face: CubeFace,
    drawBlock: DrawScope.() -> Unit
) {
    withTransform(
        transformBlock = {

            val angleXOffset = when (face) {
                CubeFace.TOP -> 90f
                CubeFace.BOTTOM -> -90f
                else -> 0f
            }
            val angleYOffset = when (face) {
                CubeFace.LEFT -> -90f
                CubeFace.RIGHT -> 90f
                CubeFace.BACK -> 180f
                else -> 0f
            }


            var pix = size.width

            rotate(angleZ)
            translate(pix * 0.5f, pix * 0.5f)

            val offset = Matrix().apply {
                if (angleXOffset != 0f) {
                    translate(
                        y = when {
                            angleXOffset > 0f -> -pix
                            else -> 0f
                        },
                        z = when {
                            angleXOffset < 0f -> pix
                            else -> 0f
                        },
                    )
                    rotateX(angleXOffset)
                }
            }
            val rotate = Matrix().apply {
                translate(
                    x = -pix * .5f,
                    y = -pix * .5f,
                    z = pix * .5f,
                )
                rotateY(angleY + angleYOffset)
                rotateX(angleX)
            }

            val matrix = Matrix().apply {
                timesAssign(offset)
                timesAssign(rotate)
            }
            transform(matrix)
        },
        drawBlock = drawBlock,
    )
}


private fun calculateZIndex(
    angleX: Float,
    angleY: Float,
    angleZ: Float,
    face: CubeFace
): Float {
    return when (face) {
        CubeFace.FRONT -> {
            val tangent = Offset.Zero
            listOf(
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateX(angleX)
                },
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateY(angleY)
                }
            ).map {
                it.map(tangent)
            }.let { (x, y) ->
                if (x.y > 0f) {
                    if (y.x > 0f) 1f else -1f
                } else {
                    if (y.x > 0f) -1f else 1f
                }
            }
        }

        CubeFace.BACK -> {

            val tangent = Offset.Zero
            listOf(
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateX(angleX)
                },
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateY(angleY)
                }
            ).map { matrix ->
                matrix.map(tangent)
            }.let { (x, y) ->
                if (x.y > 0f) {
                    if (y.x > 0f) -1f else 1f
                } else {
                    if (y.x > 0f) 1f else -1f
                }
            }
        }

        CubeFace.LEFT -> {

            val tangent = Offset.Zero
            listOf(
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateX(angleX)
                },
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateY(angleY - 90f)
                }
            ).map {
                it.map(tangent)
            }.let { (x, y) ->
                if (x.y > 0f) {
                    if (y.x > 0f) 1f else -1f
                } else {
                    if (y.x > 0f) -1f else 1f
                }
            }
        }

        CubeFace.RIGHT -> {

            val tangent = Offset.Zero
            listOf(
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateX(angleX)
                },
                Matrix().apply {
                    translate(1f, 1f, 0f)
                    rotateY(angleY - 90f)
                }
            ).map {
                it.map(tangent)
            }.let { (x, y) ->
                if (x.y > 0f) {
                    if (y.x > 0f) -1f else 11f
                } else {
                    if (y.x > 0f) 1f else -1f
                }
            }
        }

        CubeFace.TOP -> {
            val tangent = Offset(0f, 0f)
            val matrix = Matrix().apply {
                translate(0f, 1f, 0f)
                rotateX(angleX + 90f)
                rotateY(angleY)
                rotateZ(angleZ)
            }

            matrix.map(tangent).let {
                if (it.y > 0f) .5f else -1f
            }
        }

        CubeFace.BOTTOM -> {
            val tangent = Offset(0f, 0f)
            val matrix = Matrix().apply {
                translate(0f, 1f, 0f)
                rotateX(angleX + 90f)
                rotateY(angleY)
                rotateZ(angleZ)
            }

            matrix.map(tangent).let {
                if (it.y > 0f) -1f else 1f
            }
        }
    }
}


enum class CubeFace {
    FRONT, LEFT, RIGHT, BACK, TOP, BOTTOM
}