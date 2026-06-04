package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock

class CodeHighlightSlideState
class CodeHighlightSlide : ListSlideAdvanced<CodeHighlightSlideState>() {

    override val initialState: CodeHighlightSlideState
        get() = CodeHighlightSlideState()

    @Composable
    override fun content(state: CodeHighlightSlideState) {
        ListItemDisplay(
            tabs = listOf(
                "CodeHighlight.kt" to """
                    .clickable(
                        indication = null,
                        interactionSource = remember { 
                            MutableInteractionSource()
                        },
                    ) {
                        scope.launch {
                            color.snapTo(highlightColor.copy(alpha = .3f))
                            color.animateTo(
                                highlightColor.copy(alpha = .1f),
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessVeryLow
                                )
                            )
                            color.animateTo(
                                Transparent,
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessVeryLow
                                )
                            )
                        }
                    }
                """.trimIndent()
            )
        ) {
            CodeBlock(
                code = """
                    Box {
                        ClickMe()
                    }
                """.trimIndent(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 32.sp,
                ),
            )
        }
    }
}
