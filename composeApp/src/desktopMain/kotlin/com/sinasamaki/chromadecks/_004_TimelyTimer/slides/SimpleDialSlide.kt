package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chroma.dial.Dial
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE

internal class SimpleDialSlide : ListSlideAdvanced<Unit>() {

    override val initialState get() = Unit

    @Composable
    override fun content(state: Unit) {
        var degree by remember { mutableFloatStateOf(0f) }

        val code = """
            var degree by remember {
                mutableFloatStateOf(0f)
            }

            Dial(
                degree = degree,
                onDegreeChange = { degree = it },
                sweepDegrees = 99 * 360f,
                modifier = Modifier.size(400.dp),
            )
        """.trimIndent()

        Row(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeIDE(
                modifier = Modifier.weight(1f),
                tabs = listOf("TimelyDial.kt" to code),
                selectedTab = 0,
                onTabSelect = {},
            )

            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Dial(
                    degree = degree,
                    onDegreeChange = { degree = it },
                    sweepDegrees = 99 * 360f,
                    modifier = Modifier.size(400.dp),
                )
            }
        }
    }
}
