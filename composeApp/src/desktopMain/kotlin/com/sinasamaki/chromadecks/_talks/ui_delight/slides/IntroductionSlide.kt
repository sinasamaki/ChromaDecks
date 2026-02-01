package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc900

class IntroductionSlideState
class IntroductionSlide: ListSlideAdvanced<IntroductionSlideState>() {

    override val initialState: IntroductionSlideState
        get() = IntroductionSlideState()

    @Composable
    override fun content(state: IntroductionSlideState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCardFrame(
                title = "Delightful UI",
                description = "Delightful design is characterized by its ability to anticipate user needs, respond intuitively to input, and provide feedback that feels natural and satisfying. It's the difference between an interface that merely functions and one that users genuinely enjoy using. Jetpack Compose's declarative nature allows developers to think in terms of state and composition, making it significantly easier to implement the kinds of fluid, responsive designs that create these memorable experiences. The framework's integration with Material Design 3 principles provides a robust foundation, but the true magic happens when developers push beyond defaults to create unique, branded experiences that stand out in a crowded app marketplace.",
                backgroundColor = Zinc900,
                borderColor = Red600,
            )

            Text(
                text = "by sinasamaki",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 52.dp),
                style = MaterialTheme.typography.displaySmall.copy(
                    color = White,
                )
            )
        }
    }
}
