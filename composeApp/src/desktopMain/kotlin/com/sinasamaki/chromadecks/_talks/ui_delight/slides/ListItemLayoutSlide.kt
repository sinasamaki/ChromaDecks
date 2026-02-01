package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._talks.ui_delight.components.BasicListItem
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItemDisplay
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

private val codeBlockCode = """
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(color = Zinc800)
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
""".trimIndent()

class ListItemLayoutSlideState
class ListItemLayoutSlide : ListSlideAdvanced<ListItemLayoutSlideState>() {

    override val initialState: ListItemLayoutSlideState
        get() = ListItemLayoutSlideState()

    @Composable
    override fun content(state: ListItemLayoutSlideState) {
        ListItemDisplay(
            code = codeBlockCode
        ) {
            BasicListItem()
        }
    }
}
