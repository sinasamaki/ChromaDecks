package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.allDrawableResources
import com.sinasamaki.chromadecks._talks.ui_delight.components.MaxText
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

private val postThumbnails: List<DrawableResource> by lazy {
    Res.allDrawableResources
        .filterKeys { it.startsWith("sinasamaki_post_") }
        .toSortedMap()
        .values
        .toList()
}

private data class BentoCell(val col: Int, val row: Int, val colSpan: Int = 1, val rowSpan: Int = 1)

private const val BENTO_COLUMNS = 8
private const val BENTO_ROWS = 6

private val centerCell = BentoCell(col = 2, row = 2, colSpan = 4, rowSpan = 2)

private val imageCells = listOf(
    BentoCell(col = 0, row = 0, colSpan = 3, rowSpan = 2),
    BentoCell(col = 3, row = 0),
    BentoCell(col = 4, row = 0, colSpan = 2),
    BentoCell(col = 6, row = 0),
    BentoCell(col = 7, row = 0),
    BentoCell(col = 3, row = 1, colSpan = 2),
    BentoCell(col = 5, row = 1),
    BentoCell(col = 6, row = 1, colSpan = 2),
    BentoCell(col = 0, row = 2, colSpan = 2, rowSpan = 2),
    BentoCell(col = 6, row = 2),
    BentoCell(col = 7, row = 2),
    BentoCell(col = 6, row = 3, colSpan = 2),
    BentoCell(col = 0, row = 4),
    BentoCell(col = 1, row = 4, colSpan = 2),
    BentoCell(col = 3, row = 4),
    BentoCell(col = 4, row = 4),
    BentoCell(col = 5, row = 4, colSpan = 3, rowSpan = 2),
    BentoCell(col = 0, row = 5, colSpan = 2),
    BentoCell(col = 2, row = 5),
    BentoCell(col = 3, row = 5, colSpan = 2),
)

private val tileShape = RoundedCornerShape(20.dp)

class SinasamakiPromoSlideState
class SinasamakiPromoSlide : ListSlideAdvanced<SinasamakiPromoSlideState>() {

    override val initialState: SinasamakiPromoSlideState
        get() = SinasamakiPromoSlideState()

    @Composable
    override fun content(state: SinasamakiPromoSlideState) {
        val shown = remember {
            mutableStateListOf<DrawableResource?>().apply { repeat(imageCells.size) { add(null) } }
        }

        LaunchedEffect(Unit) {
            val queue = ArrayDeque(postThumbnails.shuffled())

            delay(600)
            imageCells.indices.shuffled().forEach { tile ->
                shown[tile] = queue.removeFirst()
                delay(300)
            }

            var order = imageCells.indices.shuffled()
            var step = 0
            while (true) {
                delay(800)
                if (step == order.size) {
                    val last = order.last()
                    order = imageCells.indices.shuffled().let { if (it.first() == last) it.reversed() else it }
                    step = 0
                }
                val tile = order[step++]
                shown[tile]?.let { queue.addLast(it) }
                shown[tile] = queue.removeFirst()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(24.dp)
        ) {
            BentoGrid(
                cells = imageCells + centerCell,
                gap = 12.dp,
            ) {
                imageCells.indices.forEach { index ->
                    BlurCrossfadeImage(
                        image = shown[index],
                        modifier = Modifier.clip(tileShape),
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(tileShape)
                        .background(Zinc900)
                        .border(1.dp, Zinc800, tileShape)
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    MaxText(
                        text = "sinasamaki.com",
                        modifier = Modifier.fillMaxWidth(),
                        maxFont = MaterialTheme.typography.displayLarge.fontSize,
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = Zinc50,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun BentoGrid(
    cells: List<BentoCell>,
    gap: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val gapPx = gap.roundToPx()
        val cellWidth = (constraints.maxWidth - gapPx * (BENTO_COLUMNS - 1)) / BENTO_COLUMNS.toFloat()
        val cellHeight = (constraints.maxHeight - gapPx * (BENTO_ROWS - 1)) / BENTO_ROWS.toFloat()

        val placeables = measurables.zip(cells).map { (measurable, cell) ->
            val width = (cellWidth * cell.colSpan + gapPx * (cell.colSpan - 1)).toInt()
            val height = (cellHeight * cell.rowSpan + gapPx * (cell.rowSpan - 1)).toInt()
            measurable.measure(Constraints.fixed(width, height)) to cell
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { (placeable, cell) ->
                placeable.place(
                    x = (cell.col * (cellWidth + gapPx)).toInt(),
                    y = (cell.row * (cellHeight + gapPx)).toInt(),
                )
            }
        }
    }
}

@Composable
private fun BlurCrossfadeImage(
    image: DrawableResource?,
    modifier: Modifier = Modifier,
    maxBlur: Dp = 32.dp,
) {
    var current by remember { mutableStateOf<DrawableResource?>(null) }
    var previous by remember { mutableStateOf<DrawableResource?>(null) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(image) {
        if (image == current) return@LaunchedEffect
        previous = current
        current = image
        progress.snapTo(0f)
        progress.animateTo(1f, tween(durationMillis = 1600, easing = FastOutSlowInEasing))
        previous = null
    }

    Box(modifier = modifier) {
        listOfNotNull(previous, current).forEach { layer ->
            key(layer) {
                val isCurrent = layer == current
                PanningImage(
                    image = layer,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val visibility = if (isCurrent) progress.value else 1f - progress.value
                            val radius = maxBlur.toPx() * (1f - visibility)
                            alpha = visibility
                            renderEffect = if (radius > .5f) BlurEffect(radius, radius) else null
                        }
                )
            }
        }
    }
}

@Composable
private fun PanningImage(
    image: DrawableResource,
    modifier: Modifier = Modifier,
    durationMillis: Int = 24_000,
) {
    val pan = remember { Animatable(0f) }
    val horizontalDirection = remember { if (Random.nextBoolean()) 1f else -1f }
    val verticalDirection = remember { if (Random.nextBoolean()) 1f else -1f }

    LaunchedEffect(Unit) {
        pan.animateTo(1f, tween(durationMillis = durationMillis, easing = LinearEasing))
    }

    val alignment = remember {
        Alignment { size, space, layoutDirection ->
            BiasAlignment(
                horizontalBias = lerp(-horizontalDirection, horizontalDirection, pan.value),
                verticalBias = lerp(-verticalDirection, verticalDirection, pan.value),
            ).align(size, space, layoutDirection)
        }
    }

    Image(
        painter = painterResource(image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = alignment,
        modifier = modifier,
    )
}
