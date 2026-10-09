package com.sinasamaki.chromadecks._006_SpinAnimation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._006_SpinAnimation.RedLook
import com.sinasamaki.chromadecks._006_SpinAnimation.SpinLook
import kotlin.math.roundToInt

private const val DragToSpin = .6f

private const val DragToLean = .04f
private const val MaxLean = 5f

private val Settle = spring<Float>(
    dampingRatio = .6f,
    stiffness = Spring.StiffnessVeryLow,
)

private fun restingAngle(angle: Float): Float = (angle / 180f).roundToInt() * 180f

@Composable
internal fun SpinButton(
    modifier: Modifier = Modifier,
    look: SpinLook = RedLook,
    thickness: Dp = 30.dp,
    content: @Composable () -> Unit,
) {
    var spinTarget by remember { mutableStateOf(0f) }
    var leanTarget by remember { mutableStateOf(0f) }

    val rotation = animateFloatAsState(targetValue = spinTarget, animationSpec = Settle)
    val lean = animateFloatAsState(targetValue = leanTarget, animationSpec = Settle)

    fun release() {
        spinTarget = restingAngle(spinTarget)
        leanTarget = 0f
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = ::release,
                    onDragCancel = ::release,
                ) { change, drag ->
                    change.consume()
                    spinTarget -= drag.y * DragToSpin
                    leanTarget = (leanTarget + drag.x * DragToLean).coerceIn(-MaxLean, MaxLean)
                }
            }
            .graphicsLayer { rotationZ = lean.value },
    ) {
        SpinBody(
            angle = { rotation.value },
            modifier = Modifier.matchParentSize(),
            look = look,
            thickness = thickness,
            content = content,
        )
    }
}
