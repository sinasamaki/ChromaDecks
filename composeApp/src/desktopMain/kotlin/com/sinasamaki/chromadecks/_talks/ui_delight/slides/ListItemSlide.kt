package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks._talks.ui_delight.components.ListItems
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class ListItemSlideState
class ListItemSlide: ListSlideAdvanced<ListItemSlideState>() {

    override val initialState: ListItemSlideState
        get() = ListItemSlideState()

    @Composable
    override fun content(state: ListItemSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

//            ListItem_02(Modifier.width(400.dp))
            ListItems()

        }
    }
}