package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Blue950
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.Transparent
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc400
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc500
import com.sinasamaki.chromadecks.ui.theme.Zinc600
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val title = "Droidcon Conference"
const val subtitle = "Droidcon Conference"
const val time = "00:00"

@Composable
fun ListItems(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .width(350.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ListItem_01()
        SmallClickArea()
        BigClickArea()
        JustRightClickArea()
        CustomClickArea()
        ListItem_02()
        AnimatedListItem()
    }
}

@Composable
fun ListItem_01(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = Zinc800,
                )
        )
        Column {
            Text(
                text = title,
                color = Zinc200,
            )
            Text(
                text = subtitle,
                color = Zinc400,
            )
        }
        Text(
            text = time,
            color = Zinc500,
        )
    }
}

@Composable
fun ListItem_02(modifier: Modifier = Modifier) {

    val state = rememberSwipeToDismissBoxState()

    val scope = rememberCoroutineScope()
    val willTrigger by remember {
        derivedStateOf {
            state.targetValue != SwipeToDismissBoxValue.Settled
        }
    }

    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        backgroundContent = {
            val color = when (state.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Blue500
                SwipeToDismissBoxValue.EndToStart -> Red600
                SwipeToDismissBoxValue.Settled -> Transparent
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = color,
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) .2f else 0f
                    }
                    .background(
                        color = Blue500.copy(
                            alpha = if (willTrigger) .1f else .06f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) 1f else .2f
                    }
            ) {
                Icon(
                    imageVector = when (state.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Icons.Rounded.Archive
                        SwipeToDismissBoxValue.EndToStart -> Icons.Rounded.Delete
                        SwipeToDismissBoxValue.Settled -> Icons.Rounded.Egg
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            when (state.dismissDirection) {
                                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                SwipeToDismissBoxValue.Settled -> Alignment.Center
                            }
                        )
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .padding(28.dp),
                    tint = Zinc50,
                )
            }
        },
        onDismiss = {
            scope.launch {
                delay(1000)
                state.reset()
            }
        },
        content = {
            CustomClickArea()
        }
    )
}


@Composable
fun SmallClickArea(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f),
                        Zinc100.copy(alpha = .1f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Zinc900,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Blue950.copy(alpha = if (selected) .4f else 0f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
            .clickable { selected = !selected },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) Blue950 else Zinc600,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    selected = !selected
                }
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Zinc200
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Zinc400
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = time,
            color = Zinc500
        )
    }

}

@Composable
fun BigClickArea(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { selected = !selected }
            .padding(4.dp)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f),
                        Zinc100.copy(alpha = .1f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Zinc900,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Blue950.copy(alpha = if (selected) .4f else 0f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) Blue950 else Zinc600,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    selected = !selected
                }
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Zinc200
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Zinc400
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = time,
            color = Zinc500
        )
    }

}

@Composable
fun JustRightClickArea(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f),
                        Zinc100.copy(alpha = .1f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Zinc900,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Blue950.copy(alpha = if (selected) .4f else 0f),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable { selected = !selected }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) Blue950 else Zinc600,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    selected = !selected
                }
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Zinc200
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Zinc400
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = time,
            color = Zinc500
        )
    }

}


@Composable
fun CustomClickArea(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val isHovered by interaction.collectIsHoveredAsState()
    val isPressed by interaction.collectIsPressedAsState()
    Row(
        modifier = modifier
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
                selected = !selected
            }
            .fillMaxWidth()
            .background(
                color = Zinc900,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Blue950.copy(alpha = if (selected) .4f else 0f),
                shape = RoundedCornerShape(24.dp)
            )
            .innerShadow(
                shape = RoundedCornerShape(24.dp)
            ) {
                color = Blue500
                radius = when {
                    isPressed -> 50f
                    isHovered -> 30f
                    else -> 0f
                }
                alpha = when {
                    isPressed -> .8f
                    isHovered -> .4f
                    else -> 0f
                }
            }
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f),
                        Zinc100.copy(alpha = .1f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) Blue950 else Zinc600,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    selected = !selected
                }
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Zinc200
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Zinc400
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = time,
            color = Zinc500
        )
    }

}


@Composable
fun AnimatedListItem(modifier: Modifier = Modifier) {

    val state = rememberSwipeToDismissBoxState()

    val scope = rememberCoroutineScope()
    val willTrigger by remember {
        derivedStateOf {
            state.targetValue != SwipeToDismissBoxValue.Settled
        }
    }

    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        backgroundContent = {
            val color = when (state.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Blue500
                SwipeToDismissBoxValue.EndToStart -> Red600
                SwipeToDismissBoxValue.Settled -> Transparent
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = color,
                    )
                    .dropShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) .2f else 0f
                    }
                    .background(
                        color = Blue500.copy(
                            alpha = if (willTrigger) .1f else .06f
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .innerShadow(
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        this.color = color
                        radius = 40f
                        alpha = if (willTrigger) 1f else .2f
                    }
            ) {
                Icon(
                    imageVector = when (state.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Icons.Rounded.Archive
                        SwipeToDismissBoxValue.EndToStart -> Icons.Rounded.Delete
                        SwipeToDismissBoxValue.Settled -> Icons.Rounded.Egg
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .align(
                            when (state.dismissDirection) {
                                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                                SwipeToDismissBoxValue.Settled -> Alignment.Center
                            }
                        )
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .padding(28.dp),
                    tint = Zinc50,
                )
            }
        },
        onDismiss = {
            scope.launch {
                delay(1000)
                state.reset()
            }
        },
        content = {
            AnimatedCustomClickArea()
        }
    )
}


@Composable
fun AnimatedCustomClickArea(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val isHovered by interaction.collectIsHoveredAsState()
    val isPressed by interaction.collectIsPressedAsState()

    val selectedAnimation by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val focusAnimation by animateFloatAsState(
        targetValue = when {
            isPressed -> 1f
            isHovered -> .5f
            else -> 0f
        }
    )
    Row(
        modifier = modifier
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null
            ) {
                selected = !selected
            }
            .fillMaxWidth()
            .background(
                color = Zinc900,
                shape = RoundedCornerShape(24.dp)
            )
            .background(
                color = Blue950.copy(alpha = lerp(0f, .4f, selectedAnimation)),
                shape = RoundedCornerShape(24.dp)
            )
            .innerShadow(
                shape = RoundedCornerShape(24.dp)
            ) {
                color = Blue500
                radius = lerp(0f, 40f, focusAnimation)
                alpha = lerp(0f, 1f, focusAnimation)
            }
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Zinc100.copy(alpha = .3f),
                        Zinc100.copy(alpha = .1f),
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (selected) Blue950 else Zinc600,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    selected = !selected
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AnimatedVisibility(
                visible = selected,
                enter = scaleIn(
                    initialScale = .1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                ) + fadeIn(),
                exit = scaleOut(targetScale = .1f) + fadeOut(),
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier
                        .rotate(lerp(30f, 0f, selectedAnimation))
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Zinc200
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Zinc400
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = time,
            color = Zinc500
        )
    }

}