package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chroma.dial.drawArc
import com.sinasamaki.chroma.dial.drawEveryInterval
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.modifiers.grain
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.Cyan400
import com.sinasamaki.chromadecks.ui.theme.Emerald400
import com.sinasamaki.chromadecks.ui.theme.Fuchsia400
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Neutral300
import com.sinasamaki.chromadecks.ui.theme.Neutral400
import com.sinasamaki.chromadecks.ui.theme.Neutral500
import com.sinasamaki.chromadecks.ui.theme.Neutral600
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Neutral800
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.Neutral950
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Rose400
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate600
import com.sinasamaki.chromadecks.ui.theme.Slate700
import com.sinasamaki.chromadecks.ui.theme.Slate800
import com.sinasamaki.chromadecks.ui.theme.Stone200
import com.sinasamaki.chromadecks.ui.theme.Stone300
import com.sinasamaki.chromadecks.ui.theme.Stone400
import com.sinasamaki.chromadecks.ui.theme.Stone50
import com.sinasamaki.chromadecks.ui.theme.Stone500
import com.sinasamaki.chromadecks.ui.theme.Stone600
import com.sinasamaki.chromadecks.ui.theme.Stone700
import com.sinasamaki.chromadecks.ui.theme.Stone800
import com.sinasamaki.chromadecks.ui.theme.Stone900
import com.sinasamaki.chromadecks.ui.theme.Violet400
import com.sinasamaki.chromadecks.ui.theme.Violet500
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import javafx.scene.control.RadioButton

internal data class DialShowcasesState(val placeholder: Unit = Unit)

internal class DialShowcasesSlide : ListSlideAdvanced<DialShowcasesState>() {

    override val initialState get() = DialShowcasesState()

    @Composable
    override fun content(state: DialShowcasesState) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Stone200),
            contentAlignment = Alignment.Center,
        ) {
            val pagerState = rememberPagerState(pageCount = { 5 })

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxHeight(0.68f)
                        .aspectRatio(1f),
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (page) {
                            0 -> RadioShowcase()
                            1 -> NeonDialShowcase()
                            2 -> RainbowDialShowcase()
                            3 -> TickDialShowcase()
                            4 -> ConcentricDialShowcase()
                            else -> MinimalDialShowcase()
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pagerState.currentPage == index) 10.dp else 6.dp)
                                .background(
                                    if (pagerState.currentPage == index) Slate50 else Slate600,
                                    CircleShape,
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RadioShowcase(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize(.7f)
            .background(
                color = Neutral950,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
                .background(
                    color = Neutral500,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                    )
                )
        )


        var degree by remember { mutableStateOf(0f) }
        Dial(
            degree = degree,
            onDegreeChange = { degree = it },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxSize(.5f)
                .offset(24.dp, -24.dp),
            thumb = {
                Box(modifier = Modifier.fillMaxSize())
            },
            track = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            color = Neutral800,
                            shape = CircleShape,
                        )
                        .rotate(it.degree + it.overshootDegrees)
                        .drawBehind {
                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 360f,
                                spacing = 10f,
                                radius = size.width * .52f
                            ) {
                                rotate(
                                    degrees = it.rotationAngle,
                                    pivot = it.position,
                                ) {
                                    translate(
                                        left = it.position.x,
                                        top = it.position.y,
                                    ) {
                                        drawLine(
                                            color = Neutral800,
                                            start = Offset(0f, 10f),
                                            end = Offset(0f, 40f),
                                            strokeWidth = 22f,
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }

                            drawEveryInterval(
                                startDegrees = 0f,
                                sweepDegrees = 360f,
                                spacing = 10f,
                                radius = size.width / 2f
                            ) {
                                rotate(
                                    degrees = it.rotationAngle,
                                    pivot = it.position,
                                ) {
                                    translate(
                                        left = it.position.x,
                                        top = it.position.y,
                                    ) {
                                        drawLine(
                                            color = Orange400,
                                            start = Offset(0f, 10f),
                                            end = Offset(0f, 40f),
                                            strokeWidth = 6f,
                                            cap = StrokeCap.Round,
                                        )
                                    }
                                }
                            }
                        }
                )
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .drawBehind {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Neutral800.copy(alpha = 0f),
                                    Neutral600.copy(alpha = .3f),
                                ),
                            ),
                        )
                    }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        color = Neutral950
                    )
            )
            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp,
                        )
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(.35f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    imageVector = Icons.Rounded.SkipPrevious,
                    shape = RoundedCornerShape(
                        bottomStart = 24.dp,
                    ),
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(
                            color = Neutral950
                        )
                )
                RadioButton(
                    imageVector = Icons.Rounded.PlayArrow,
                    modifier = Modifier
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(
                            color = Neutral950
                        )
                )
                RadioButton(
                    imageVector = Icons.Rounded.SkipNext,
                    shape = RoundedCornerShape(
                        bottomEnd = 24.dp,
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun RowScope.RadioButton(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    imageVector: ImageVector,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 5.dp else 0.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    val shadow by animateFloatAsState(
        targetValue = if (isPressed) .8f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 5f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        )
    )
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = Orange400,
        modifier = modifier
            .clickable(
                interactionSource = interaction,
                indication = null,
            ) {

            }
            .graphicsLayer {
                transformOrigin = TransformOrigin(.5f, 0f)
                translationY = -pressOffset.toPx()
                rotationX = -rotation
            }
            .background(
                color = Neutral800,
                shape = shape,
            )
            .drawWithContent {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Neutral700.copy(alpha = .5f),
                            Neutral700.copy(alpha = 0f)
                        ),
                        endY = size.height * .8f
                    ),
                )
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Neutral950.copy(alpha = shadow),
                            Neutral700.copy(alpha = 0f)
                        ),
                        endY = size.height * .8f
                    ),
                )


            }
            .fillMaxHeight()
            .weight(1f)
            .padding(vertical = 24.dp)
    )
}

// Thumb: 24dp invisible → arc radius offset = 12dp
@Composable
private fun NeonDialShowcase() {
    var degree by remember { mutableFloatStateOf(135f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.size(250.dp),
        startDegrees = 135f,
        sweepDegrees = 270f,
        thumb = { _ -> Box(Modifier.size(24.dp)) },
        track = { dialState ->
            Canvas(Modifier.fillMaxSize()) {
                val r = dialState.radius - 12.dp.toPx()
                drawArc(
                    color = Slate700,
                    startAngle = dialState.startDegrees,
                    sweepAngle = 270f,
                    radius = r,
                    strokeWidth = 16.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = Cyan400.copy(alpha = 0.10f),
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r,
                    strokeWidth = 44.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = Cyan400.copy(alpha = 0.22f),
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r,
                    strokeWidth = 26.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = Cyan400,
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = White.copy(alpha = 0.65f),
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        },
    )
}

// Thumb: 18dp white dot → arc radius offset = 9dp
@Composable
private fun RainbowDialShowcase() {
    val colors = listOf(Rose500, Orange500, Amber400, Lime400, Cyan400, Violet500)
    var degree by remember { mutableFloatStateOf(180f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.size(250.dp),
        startDegrees = 135f,
        sweepDegrees = 270f,
        thumb = { _ ->
            Box(
                Modifier
                    .size(18.dp)
                    .background(White, CircleShape)
            )
        },
        track = { dialState ->
            Canvas(Modifier.fillMaxSize()) {
                val r = dialState.radius - 9.dp.toPx()
                drawArc(
                    color = Slate700,
                    startAngle = dialState.startDegrees,
                    sweepAngle = 270f,
                    radius = r,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
                val segmentSweep = 270f / colors.size
                colors.forEachIndexed { index, color ->
                    val segStart = dialState.startDegrees + index * segmentSweep
                    val sweep = (dialState.degree - index * segmentSweep).coerceIn(0f, segmentSweep)
                    if (sweep > 0f) {
                        drawArc(
                            color = color,
                            startAngle = segStart,
                            sweepAngle = sweep,
                            radius = r,
                            strokeWidth = 12.dp,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        },
    )
}

// Thumb: 24dp invisible → dots from drawEveryInterval align at dialState.radius
@Composable
private fun TickDialShowcase() {
    var degree by remember { mutableFloatStateOf(0f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.size(250.dp),
        startDegrees = 135f,
        sweepDegrees = 270f,
        thumb = { _ -> Box(Modifier.size(24.dp)) },
        track = { dialState ->
            Canvas(Modifier.fillMaxSize()) {
                drawEveryInterval(dialState = dialState, spacing = 9f) { data ->
                    drawCircle(
                        color = if (data.inActiveRange) Amber400 else Slate600,
                        radius = 3.dp.toPx(),
                        center = data.position,
                    )
                }
                drawCircle(color = Amber400.copy(alpha = 0.10f), radius = dialState.radius * 0.6f)
                drawCircle(color = Amber400, radius = 6.dp.toPx())
            }
        },
    )
}

// Thumb: 24dp invisible → arc radius offset = 12dp; concentric rings step in by 20dp each
@Composable
private fun ConcentricDialShowcase() {
    var degree by remember { mutableFloatStateOf(200f) }
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.size(250.dp),
        startDegrees = 135f,
        sweepDegrees = 270f,
        thumb = { _ -> Box(Modifier.size(24.dp)) },
        track = { dialState ->
            Canvas(Modifier.fillMaxSize()) {
                val base = dialState.radius - 12.dp.toPx()
                val ringColors = listOf(Violet400, Fuchsia400, Rose400, Orange400)
                ringColors.forEachIndexed { index, color ->
                    val r = base - index * 20.dp.toPx()
                    drawArc(
                        color = Slate700,
                        startAngle = dialState.startDegrees,
                        sweepAngle = 270f,
                        radius = r,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                    drawArc(
                        color = color,
                        startAngle = dialState.startDegrees,
                        sweepAngle = dialState.degree,
                        radius = r,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        },
    )
}

// Thumb: 14dp emerald dot → arc radius offset = 7dp
@Composable
private fun MinimalDialShowcase() {
    var degree by remember { mutableFloatStateOf(90f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()
    val thumbScale by animateFloatAsState(if (isDragging) 1.6f else 1f)
    Dial(
        degree = degree,
        onDegreeChange = { degree = it },
        modifier = Modifier.size(250.dp),
        startDegrees = 135f,
        sweepDegrees = 270f,
        overshootAnimationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy,
        ),
        interactionSource = interactionSource,
        thumb = { _ ->
            Box(
                Modifier
                    .size(14.dp)
                    .scale(thumbScale)
                    .background(Emerald400, CircleShape)
            )
        },
        track = { dialState ->
            Canvas(Modifier.fillMaxSize()) {
                val r = dialState.radius - 7.dp.toPx()
                drawArc(
                    color = Slate800,
                    startAngle = dialState.startDegrees,
                    sweepAngle = 270f,
                    radius = r,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = Emerald400.copy(alpha = 0.25f),
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r - 14.dp.toPx(),
                    strokeWidth = 14.dp,
                    strokeCap = StrokeCap.Round
                )
                drawArc(
                    color = Emerald400,
                    startAngle = dialState.startDegrees,
                    sweepAngle = dialState.degree,
                    radius = r,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        },
    )
}
