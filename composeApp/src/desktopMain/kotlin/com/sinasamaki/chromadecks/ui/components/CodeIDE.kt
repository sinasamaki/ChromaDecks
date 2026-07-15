package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Neutral900
import com.sinasamaki.chromadecks.ui.theme.Slate400
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate700
import com.sinasamaki.chromadecks.ui.theme.Slate800
import com.sinasamaki.chromadecks.ui.theme.Slate900
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc500
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.Zinc950

@Composable
fun CodeIDE(
    modifier: Modifier = Modifier,
    tabs: List<Pair<String, String>>,
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    style: TextStyle = MaterialTheme.typography.labelSmall,
    highlightAnimation: Boolean = true,
    useIndexAsKey: Boolean = false,
    enableAnimations: Boolean = true,
    fadeAnimations: Boolean = true,
    darkMode: Boolean = true,
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
//                color = Zinc500.copy(alpha = .3f),
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc500.copy(alpha = .3f),
                        Zinc500.copy(alpha = .05f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                Zinc700.copy(alpha = .3f),
                shape = RoundedCornerShape(24.dp),
            ),
    ) {
        val tabListState = rememberLazyListState()
        LaunchedEffect(selectedTab) {
            tabListState.animateScrollToItem(selectedTab)
        }
        LazyRow(
            state = tabListState,
            modifier = Modifier.clip(
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                )
            ),
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(tabs.size) { index ->
                val (fileName, _) = tabs[index]
                val isSelected = index == selectedTab
                Text(
                    text = fileName,
                    style = style,
                    color = if (isSelected) Slate50 else Slate400.copy(alpha = .2f),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) Zinc200.copy(alpha = .2f) else Transparent)
                        .clickable { onTabSelect(index) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }


        AnimatedContent(
            targetState = selectedTab,
            modifier = Modifier
                .fillMaxWidth(),
            transitionSpec = {
                val goingRight = targetState > initialState
                val enter =
                    slideInHorizontally(tween(300)) { width -> if (goingRight) (width * 0.3f).toInt() else -(width * 0.3f).toInt() } +
                            fadeIn(tween(300))
                val exit =
                    slideOutHorizontally(tween(300)) { width -> if (goingRight) -(width * 0.3f).toInt() else (width * 0.3f).toInt() } +
                            fadeOut(tween(300))
                enter togetherWith exit using SizeTransform(
                    sizeAnimationSpec = { _, _ ->
                        spring(
                            stiffness = Spring.StiffnessMediumLow,
//                            dampingRatio = Spring.DampingRatioLowBouncy,
                        )
                    }
                )
            },
        ) { tab ->
            CodeBlock(
                modifier = Modifier
                    .padding(2.dp)
                    .background(
                        color = Zinc950.copy(alpha = .7f),
                        shape = RoundedCornerShape(22.dp),
                    )
                    .padding(16.dp),
                code = tabs.getOrNull(tab)?.second ?: "",
                style = style,
                highlightAnimation = highlightAnimation,
                useIndexAsKey = useIndexAsKey,
                enableAnimations = enableAnimations,
                fadeAnimations = fadeAnimations,
                darkMode = darkMode,
            )
        }
    }
}
