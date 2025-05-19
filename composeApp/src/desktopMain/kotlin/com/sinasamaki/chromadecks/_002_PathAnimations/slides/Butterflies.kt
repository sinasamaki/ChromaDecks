package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks.data.ListSlideAdvanced

class ButterfliesState()
class Butterflies : ListSlideAdvanced<ButterfliesState>() {

    override val initialState: ButterfliesState
        get() = ButterfliesState()

    override val stateMutations: List<ButterfliesState.() -> ButterfliesState>
        get() = listOf()

    @Composable
    override fun content(state: ButterfliesState) {
        Text(
            text = "Butterflies Slide",
            style = MaterialTheme.typography.labelLarge
        )
    }
}