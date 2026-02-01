package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun MaxText(
    text: String,
    modifier: Modifier = Modifier,
    maxFont: TextUnit = 96.sp,
    minFont: TextUnit = 6.sp,
    style: TextStyle = LocalTextStyle.current,
) {

    BasicText(
        text = text,
        modifier = modifier,
        autoSize = TextAutoSize.StepBased(
            maxFontSize = maxFont,
            minFontSize = minFont,
        ),
        maxLines = 1,
        style = style,
    )

}