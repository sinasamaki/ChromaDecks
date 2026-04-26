package com.sinasamaki.chromadecks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                color = Zinc500.copy(alpha = .3f),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                Black.copy(alpha = .3f),
                shape = RoundedCornerShape(24.dp),
            ),
    ) {
        Row {
            Row(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp,
                    ),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                tabs.forEachIndexed { index, (fileName, _) ->
                    val isSelected = index == selectedTab
                    Text(
                        text = fileName,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) Slate50 else Slate400,
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
//                                    bottomStart = 2.dp,
//                                    bottomEnd = 2.dp,
                                )
                            )
                            .background(if (isSelected) Zinc200.copy(alpha = .1f) else Transparent)
                            .clickable { onTabSelect(index) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }
        }

//        Space(1.dp)

        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 8.dp)
                .background(
                    color = Zinc500.copy(alpha = .3f),
                )
        )

        CodeBlock(
            modifier = Modifier.padding(16.dp),
            code = tabs.getOrNull(selectedTab)?.second ?: "",
            style = style,
            highlightAnimation = highlightAnimation,
            useIndexAsKey = useIndexAsKey,
            enableAnimations = enableAnimations,
            fadeAnimations = fadeAnimations,
            darkMode = darkMode,
        )
    }
}
