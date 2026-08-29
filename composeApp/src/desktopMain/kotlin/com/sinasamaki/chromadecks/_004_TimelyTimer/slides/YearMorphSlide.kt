package com.sinasamaki.chromadecks._004_TimelyTimer.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks._004_TimelyTimer.components.TimelyNumber
import com.sinasamaki.chromadecks._004_TimelyTimer.timelySwatch
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.LocalSlideState
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.Orange
import com.sinasamaki.chromadecks.ui.theme.Teal
import com.sinasamaki.chromadecks.ui.theme.Zinc200
import com.sinasamaki.chromadecks.ui.theme.Zinc900
import com.sinasamaki.chromadecks.ui.theme.Zinc950

internal data class YearMorphState(val year: Int)

internal class YearMorphSlide : ListSlideAdvanced<YearMorphState>() {

    override val initialState get() = YearMorphState(year = 2026)

    override val stateMutations: List<YearMorphState.() -> YearMorphState>
        get() = listOf(
            { copy(year = 2013) },
        )

    @Composable
    override fun content(state: YearMorphState) {
        val swatch = timelySwatch(LocalSlideState.current.slideIndex)
        val digits = "%04d".format(state.year).map { it - '0' }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
//                .background(color = Black)
//                .padding(64.dp)
                .background(
                    color = Zinc200,
                )
            ,
            contentAlignment = Alignment.Center,
        ) {
            val digitHeight = maxHeight * 0.4f
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                digits.forEach { digit ->
                    TimelyNumber(
                        digit = digit,
                        height = digitHeight,
                        color = Teal.v500,
                        strokeWidth = 20.dp
                    )
                }
            }
        }
    }
}
