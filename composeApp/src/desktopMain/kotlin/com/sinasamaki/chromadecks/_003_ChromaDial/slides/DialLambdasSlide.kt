package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE

internal data class DialLambdasState(
    val showDialState: Boolean = false,
)

internal class DialLambdasSlide : ListSlideAdvanced<DialLambdasState>() {

    override val initialState get() = DialLambdasState()

    override val stateMutations get() = listOf<DialLambdasState.() -> DialLambdasState>(
        { copy(showDialState = true) },
    )

    @Composable
    override fun content(state: DialLambdasState) {
        val param = if (state.showDialState) " dialState ->" else ""
        val code = """
            Dial(
                degree = degree,
                onDegreeChange = { degree = it },
                track = {$param
                    ...
                },
                thumb = {$param
                    ...
                },
            )
        """.trimIndent()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            CodeIDE(
                modifier = Modifier.width(480.dp),
                tabs = listOf("DialCustomization.kt" to code),
                selectedTab = 0,
                onTabSelect = {},
                highlightAnimation = true,
            )
        }
    }
}
