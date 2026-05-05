package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.sinasamaki.chromadecks._003_ChromaDial.components.BraunFMRadio
import com.sinasamaki.chromadecks._003_ChromaDial.components.CameraFocusDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.CameraModeDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.ColorPickerDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.EQDials
import com.sinasamaki.chromadecks._003_ChromaDial.components.GradientDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.MaterialDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.MonthDurationPicker
import com.sinasamaki.chromadecks._003_ChromaDial.components.RadioShowcase
import com.sinasamaki.chromadecks._003_ChromaDial.components.RotaryDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.TemperatureDial
import com.sinasamaki.chromadecks._003_ChromaDial.components.Type3
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Zinc950

internal data class DialShowcasesState(val showcaseIndex: Int = 0)

internal class DialShowcasesSlide : ListSlideAdvanced<DialShowcasesState>() {

    override val initialState get() = DialShowcasesState()

    override val stateMutations
        get() = listOf<DialShowcasesState.() -> DialShowcasesState>(
            { copy(showcaseIndex = 1) },
            { copy(showcaseIndex = 2) },
            { copy(showcaseIndex = 3) },
            { copy(showcaseIndex = 4) },
            { copy(showcaseIndex = 5) },
            { copy(showcaseIndex = 6) },
            { copy(showcaseIndex = 7) },
            { copy(showcaseIndex = 8) },
            { copy(showcaseIndex = 9) },
            { copy(showcaseIndex = 10) },
            { copy(showcaseIndex = 11) },
        )

    @Composable
    override fun content(state: DialShowcasesState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Zinc950),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .aspectRatio(1f)
                    .scale(2.8f),
                contentAlignment = Alignment.Center,
            ) {
                when (state.showcaseIndex) {
                    0  -> RadioShowcase()
                    1  -> Type3()
                    2  -> MonthDurationPicker()
                    3  -> GradientDial()
                    4  -> MaterialDial()
                    5  -> TemperatureDial()
                    6  -> CameraModeDial()
                    7  -> BraunFMRadio()
                    8  -> CameraFocusDial()
                    9  -> EQDials()
                    10 -> ColorPickerDial()
                    11 -> RotaryDial()
                }
            }
        }
    }
}
