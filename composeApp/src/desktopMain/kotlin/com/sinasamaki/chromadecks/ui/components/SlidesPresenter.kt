@file:OptIn(ExperimentalMaterial3Api::class)

package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.data.ListSlideSimple
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SlidesPresenter2(
    modifier: Modifier = Modifier,
    slides: List<ListSlideAdvanced<*>>,
) {

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 1)
    var size by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val requester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
    var currentSlideIndex by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        snapshotFlow { currentSlideIndex }.collect {
            scope.launch {
                listState.animateTo2(
                    index = currentSlideIndex + 1,
                    height = with(density) { size.height.roundToPx() + 32.dp.roundToPx() }
                )
            }
        }
    }
    Box(
        modifier
        .onPreviewKeyEvent {
            if (it.type == KeyEventType.KeyUp) {
                val currentSlide = slides[currentSlideIndex]
                if (it.key == Key.DirectionRight) {
                    if (!currentSlide.next())
                        currentSlideIndex++
                } else if (it.key == Key.DirectionLeft) {
                    if (!currentSlide.previous())
                        currentSlideIndex--
                }
                currentSlideIndex =
                    currentSlideIndex.coerceIn(slides.indices)
            }
            true
        }
        .focusRequester(requester)
        .focusable()
        .fillMaxSize()
        .onSizeChanged {
            size = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
        }
    ) {
        if (size != DpSize.Zero) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                userScrollEnabled = false,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item { Box(Modifier.size(size)) }
                itemsIndexed(slides) { index, slide ->
                    CompositionLocalProvider(
                        LocalSlideState provides SlideState(
                            currentIndex = currentSlideIndex,
                            slideIndex = index,
                        )
                    ) {
                        Box(Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(size)) {
                            when (slide) {
                                is ListSlideSimple -> {
                                    slide.content()
                                }

                                else -> {
                                    slide.currentContent()
                                }
                            }
                        }
                    }
                }
                item { Box(Modifier.size(size)) }
            }
        }
    }
}


@Composable
fun SlidesPresenter(
    modifier: Modifier = Modifier,
    slides: List<ListSlide<*>>,
) {

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 1)
    var size by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val requester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
    var currentSlideIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        snapshotFlow { currentSlideIndex }.collect {
            scope.launch {
                listState.animateTo2(
                    index = currentSlideIndex + 1,
                    height = with(density) { size.height.roundToPx() }
                )
            }
        }
    }
    Box(
        modifier
        .onPreviewKeyEvent {
            if (it.type == KeyEventType.KeyUp) {
                val currentSlide = slides[currentSlideIndex]
                if (it.key == Key.DirectionRight) {
                    if (!currentSlide.next())
                        currentSlideIndex++
                } else if (it.key == Key.DirectionLeft) {
                    if (!currentSlide.previous())
                        currentSlideIndex--
                }
                currentSlideIndex =
                    currentSlideIndex.coerceIn(slides.indices)
            }
            true
        }
        .focusRequester(requester)
        .focusable()
        .fillMaxSize()
        .onSizeChanged {
            size = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
        }
    ) {
        if (size != DpSize.Zero) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                userScrollEnabled = false,
            ) {
                item { Box(Modifier.size(size)) }
                items(slides) { slide ->
                    Box(Modifier.size(size)) {
                        when (slide) {
                            is ListSlideSimple -> {
                                slide.content()
                            }

                            else -> {
                                slide.currentContent()
                            }
                        }
                    }
                }
                item { Box(Modifier.size(size)) }
            }
        }
    }
}


private fun LazyListState.getProgress(): Float {

    if (layoutInfo.visibleItemsInfo.isEmpty()) return 0f
    return (firstVisibleItemIndex + ((firstVisibleItemScrollOffset * 1) / layoutInfo.visibleItemsInfo.first { it.index == firstVisibleItemIndex }.size.toFloat())) / (layoutInfo.totalItemsCount - 1)

}

private suspend fun LazyListState.animateTo2(
    index: Int,
    height: Int,
    animationSpec: AnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessVeryLow,
        dampingRatio = Spring.DampingRatioLowBouncy
    ),
) = coroutineScope {
    animateScrollBy(
        ((index - firstVisibleItemIndex) * height).toFloat() - firstVisibleItemScrollOffset,
        animationSpec = animationSpec,
    )
}

val LocalSlideState = compositionLocalOf<SlideState> { error("No Slide State available") }

data class SlideState(
    val currentIndex: Int,
    val slideIndex: Int,
)