package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.extensions.moveItem
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Transparent
import dev.andrewbailey.diff.differenceOf
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun CodeBlock(
    modifier: Modifier = Modifier,
    code: String,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    highlightAnimation: Boolean = true,
    useIndexAsKey: Boolean = false
) {

    val states = remember { mutableStateListOf<LineState>() }

    LaunchedEffect(code) {
        val diff = differenceOf(
            states.map { it.text },
            code.split("\n"),
        )

        states.forEach { it.justAdded = false }

        var addedAt = 0
        diff.applyDiff(
            insert = { line, index ->
                states.add(
                    index, LineState(
                        text = line,
                        justAdded = highlightAnimation,
                        addedAt = addedAt++
                    )
                )
            },
            remove = { index ->
                states.removeAt(index)
            },
            move = { old, new ->
                states.moveItem(old, new)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMediumLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                )
            ),
        verticalArrangement = Arrangement.Top,
    ) {
        itemsIndexed(
            items = states,
            key = { index, state ->
                if (!useIndexAsKey) {
                    state.key
                } else {
                    index
                }
            }
        ) { _, line ->
            val delay = remember { line.addedAt * 50L }
            val color = remember { Animatable(Transparent) }
            LaunchedEffect(line.justAdded) {
                delay(delay)
                if (line.justAdded && line.text.trim() != ")") {
                    color.snapTo(Slate50.copy(alpha = .2f))
                    color.animateTo(
                        Slate50.copy(alpha = .1f),
                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                    )
                    color.animateTo(
                        Transparent,
                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                    )
                } else {
                    color.animateTo(Transparent)
                }
            }
            Text(
                line.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .border(
                        width = 1.dp,
                        color = color.value,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .background(
                        color = color.value,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .animateItem(
                        fadeInSpec = tween(delayMillis = delay.toInt()),
                        fadeOutSpec = spring(stiffness = Spring.StiffnessHigh),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                            dampingRatio = Spring.DampingRatioLowBouncy
                        )
                    ),
                style = style,
            )
        }
    }
}

private data class LineState(
    val text: String,
    val addedAt: Int,
    val key: String = "${Random.nextLong()}",
    var justAdded: Boolean = true,
)
