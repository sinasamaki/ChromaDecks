package com.sinasamaki.chromadecks._005_RibbonModifier

import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks.ui.theme.Amber400
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Cyan600
import com.sinasamaki.chromadecks.ui.theme.Emerald500
import com.sinasamaki.chromadecks.ui.theme.Emerald600
import com.sinasamaki.chromadecks.ui.theme.Fuchsia500
import com.sinasamaki.chromadecks.ui.theme.Indigo500
import com.sinasamaki.chromadecks.ui.theme.Indigo600
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate400
import com.sinasamaki.chromadecks.ui.theme.Teal600
import com.sinasamaki.chromadecks.ui.theme.Violet600

internal val RibbonCodeColors = CodeColors(
    keyword = Indigo600,
    string = Emerald600,
    number = Orange600,
    function = Violet600,
    param = Teal600,
    comment = Slate400,
)

internal val WARM = listOf(Rose500, Amber400)

internal val COOL = listOf(Indigo500, Sky500, Emerald500)

internal val DUSK = listOf(Fuchsia500, Indigo500, Cyan600)

internal val FLAT: List<Color> = listOf(Rose500)
