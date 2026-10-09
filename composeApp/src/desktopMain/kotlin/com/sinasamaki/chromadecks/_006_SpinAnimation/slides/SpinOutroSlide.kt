package com.sinasamaki.chromadecks._006_SpinAnimation.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._006_SpinAnimation.components.DemoCaption
import com.sinasamaki.chromadecks._006_SpinAnimation.components.HabitLabel
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinBody
import com.sinasamaki.chromadecks._006_SpinAnimation.components.SpinMotion
import com.sinasamaki.chromadecks._006_SpinAnimation.components.rememberSpinAngle
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.slideanimations.fadeIn
import com.sinasamaki.chromadecks.ui.slideanimations.parallax
import com.sinasamaki.chromadecks.ui.slideanimations.translateInX

internal class SpinOutroSlide : ListSlideAdvanced<Unit>() {

    override val initialState get() = Unit

    override val animator: (@Composable (@Composable () -> Unit) -> Unit)?
        get() = { content ->
            Box(Modifier.parallax(1f).translateInX().fadeIn()) { content() }
        }

    @Composable
    override fun content(state: Unit) {
        val angle = rememberSpinAngle(SpinMotion.Flip(restMillis = 1200))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(72.dp),
            ) {
                SpinBody(
                    angle = { angle.value },
                    modifier = Modifier.width(640.dp).height(180.dp),
                    thickness = 32.dp,
                ) {
                    HabitLabel(text = "SubAtomic", fontSize = 72.sp)
                }
                DemoCaption(text = "thanks for watching")
            }
        }
    }
}
