package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._002_PathAnimations.lineTo
import com.sinasamaki.chromadecks._002_PathAnimations.moveTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarLineTo
import com.sinasamaki.chromadecks._002_PathAnimations.polarToCart
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Fuchsia300
import com.sinasamaki.chromadecks.ui.theme.Fuchsia400
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import com.sinasamaki.chromadecks.ui.theme.Indigo400
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Sky400
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate950
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

internal data class CartVsPolarState(
    val cartX: Int = 0,
    val cartY: Int = 0,
    val polarDegrees: Int = 0,
    val polarDistance: Int = 0,
    val showCartGrid: Boolean = true,
    val showPolarGrid: Boolean = false,
    val showCode: Boolean = false,
    val showRelativePolarCode: Boolean = false,
)

internal class CartVsPolar : ListSlideAdvanced<CartVsPolarState>() {

    override val initialState: CartVsPolarState
        get() = CartVsPolarState()

    override val stateMutations: List<CartVsPolarState.() -> CartVsPolarState>
        get() = listOf(
            { copy(cartX = 50) },
            { copy(cartY = 50) },
            { copy(showPolarGrid = true) },
            { copy(polarDegrees = 240) },
            { copy(polarDistance = 40) },
            { copy(polarDegrees = 60) },
            { copy(polarDistance = 50) },
            { copy(cartX = polarToCart(60f, 50f, origin = Offset(50f, 50f)).x.roundToInt()) },
            { copy(cartY = polarToCart(60f, 45f, origin = Offset(50f, 50f)).y.roundToInt()) },
            { copy(showCartGrid = false) },
            { copy(showCode = true) },
            { copy(showRelativePolarCode = true) },
        )

    @Composable
    override fun content(state: CartVsPolarState) {

        val spring = remember {
            spring<Float>(
                stiffness = Spring.StiffnessVeryLow,
                visibilityThreshold = .00001f
            )
        }
        val cartX by animateFloatAsState(
            targetValue = state.cartX.toFloat(),
            animationSpec = spring
        )
        val cartY by animateFloatAsState(
            targetValue = state.cartY.toFloat(),
            animationSpec = spring
        )
        val polarDegrees by animateFloatAsState(
            targetValue = state.polarDegrees.toFloat(),
            animationSpec = spring
        )
        val polarDistance by animateFloatAsState(
            targetValue = state.polarDistance.toFloat(),
            animationSpec = spring
        )

        val textMeasure = rememberTextMeasurer()
        val textStyle = MaterialTheme.typography.labelMedium

        Row(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally)
        ) {

            AnimatedVisibility(
                visible = state.showCartGrid,
                enter = slideInHorizontally(initialOffsetX = { -it / 2 }) + fadeIn() + expandHorizontally(),
                exit = slideOutHorizontally(targetOffsetX = { -it / 2 }) + fadeOut() + shrinkHorizontally(),
            ) {
                Box(
                    modifier = Modifier
                        .width(600.dp)
                        .aspectRatio(1f)
                        .padding(1.dp)
                        .background(
                            color = Neutral950
                        )
                        .drawCartesianGrid(
                            color = Slate50,
                            alpha = .5f
                        )
                        .drawCartesianGrid(
                            color = Slate50,
                            range = 100,
                            alpha = .1f,
                        )
                        .drawBehind {
                            val xPath = Path()
                            xPath.lineTo(size.width, 0f)

                            val yPath = Path()
                            yPath.lineTo(0f, size.height)

                            drawAxis(
                                path = xPath,
                                cartX / 100f,
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Sky400,
                                        Indigo400,
                                        Fuchsia300,
                                    )
                                ),
                                textMeasure = textMeasure,
                                textStyle = textStyle,
                                text = cartX.toString().take(5)
                            )

                            drawAxis(
                                path = yPath,
                                cartY / 100f,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Fuchsia400,
                                        Sky500,
                                    )
                                ),
                                textMeasure = textMeasure,
                                textStyle = textStyle,
                                text = cartY.toString().take(5)
                            )
                        }

                )
            }
            Spacer(Modifier.width(64.dp))
            AnimatedVisibility(
                visible = state.showPolarGrid,
                enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn() + expandHorizontally(
                    expandFrom = Alignment.Start
                ),
                exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut() + shrinkHorizontally(
                    shrinkTowards = Alignment.Start
                ),
            ) {
                Box(
                    modifier = Modifier
                        .width(600.dp)
                        .aspectRatio(1f)
                        .padding(1.dp)
                        .background(
                            color = Neutral950
                        )
                        .drawPolarGrid(
                            color = Slate50
                        )
                        .drawBehind {
                            val degreePath = Path()

                            degreePath.addArc(
                                oval = Rect(
                                    center = center,
                                    radius = size.width / 8
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = -polarDegrees.toFloat()
                            )

                            drawPath(
                                path = degreePath.copy().apply { lineTo(center) },
                                alpha = .2f,
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Indigo400,
                                        Fuchsia300,
                                        Sky400,
                                    )
                                ),
                            )
                            if (polarDegrees.absoluteValue > 0.1f) {
                                drawAxis(
                                    path = degreePath,
                                    progress = .999f,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Indigo400,
                                            Fuchsia300,
                                            Sky400,
                                        )
                                    ),
                                    textMeasure = textMeasure,
                                    textStyle = textStyle,
                                    text = "${polarDegrees.toInt()}º"
                                )
                            }

                            drawAxis(
                                path = Path().apply {
                                    moveTo(center)
                                    polarLineTo(
                                        polarDegrees.toFloat(),
                                        (polarDistance / 100f) * size.width,
                                        center
                                    )
                                },
                                progress = polarDistance / 50f,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Fuchsia500,
                                        Sky400,
                                        Indigo400,
                                    )
                                ),
                                textMeasure = textMeasure,
                                textStyle = textStyle,
                                text = "${polarDistance.toInt()}"
                            )
                        }

                )
            }

            AnimatedVisibility(
                visible = state.showCode,
                enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn() + expandHorizontally(
                    expandFrom = Alignment.Start
                ),
                exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut() + shrinkHorizontally(
                    shrinkTowards = Alignment.Start
                ),
            ) {
                Box(
                    Modifier
                        .padding(start = 64.dp)
                        .fillMaxHeight()
                        .width(600.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CodeBlock(
                        code = """
                            fun Path.polarLineTo(
                                degrees: Float,
                                distance: Float,
                                origin: Offset = Offset.Zero
                            ) {
                                lineTo(
                                    polarToCart(degrees, distance, origin)
                                )
                            }
                            ${
                            if (state.showRelativePolarCode) """
                            fun Path.relativePolarLineTo(
                                degrees: Float,
                                distance: Float
                            ) {
                                val actualDegrees = -((180f - degrees)
                                    + getLastAngle)
                                val delta = polarToCart(
                                    actualDegrees, 
                                    distance
                                )
                                relativeLineTo(delta)
                            }
                                """
                            else ""
                        }
                        """.trimIndent(),
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawAxis(
    path: Path,
    progress: Float,
    brush: Brush,
    textMeasure: TextMeasurer,
    textStyle: TextStyle,
    text: String,
) {

    val arrow = Path()
//    arrow.moveTo(
//        0f,
//        -12.dp.toPx(),
//    )
//    arrow.lineTo(
//        12.dp.toPx(),
//        0f
//    )
//    arrow.lineTo(
//        0f,
//        12.dp.toPx(),
//    )

    arrow.moveTo(
        -12.dp.toPx(),
        -12.dp.toPx(),
    )
    arrow.lineTo(
        0f,
        0f
    )
    arrow.lineTo(
        -12.dp.toPx(),
        12.dp.toPx(),
    )

    if (progress > .02f) {
        val measure = PathMeasure()
        measure.setPath(path, false)

        drawPath(
            path = path,
            brush = brush,
            style = Stroke(
                pathEffect = PathEffect.stampedPathEffect(
                    shape = arrow,
                    advance = measure.length,
                    phase = -(measure.length - .001f) * progress,
                    style = StampedPathEffectStyle.Rotate
                )
            )
        )

        drawPath(
            path = path,
            brush = brush,
            style = Stroke(
                width = 5.dp.toPx(),
                cap = StrokeCap.Butt,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        (measure.length * progress.coerceAtLeast(.03f) - arrow.getBounds().width).coerceAtLeast(
                            .01f
                        ),
                        measure.length
                    )
                )
            )
        )

        val textDistance = (measure.length * progress) - 130f
        val position = measure.getPosition(textDistance)
        val tangent = measure.getTangent(textDistance)
        translate(
            left = position.x,
            top = position.y,
        ) {
            rotate(
                degrees = 0f,//getDegrees(tangent),
                pivot = Offset.Zero,
            ) {

                val textSize = textMeasure.measure(text, style = textStyle).size

                val textRect = Rect(
                    offset = Offset.Zero,
                    size = Size(textSize.width.toFloat(), textSize.height.toFloat()),
                ).inflate(5f).translate(-2.5f, 4f)

                drawRoundRect(
                    color = Slate950,
                    alpha = .8f,
                    size = Size(
                        textRect.width,
                        textRect.height
                    ),
                    topLeft = textRect.topLeft,
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawText(
                    textMeasurer = textMeasure,
                    text = text,
                    style = textStyle.copy(
                        brush = brush
                    )
                )
            }
        }
    }
}


