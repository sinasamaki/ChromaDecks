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
import com.sinasamaki.chromadecks.ui.theme.Orange400
import com.sinasamaki.chromadecks.ui.theme.Orange600
import com.sinasamaki.chromadecks.ui.theme.Rose500
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate400
import com.sinasamaki.chromadecks.ui.theme.Teal600
import com.sinasamaki.chromadecks.ui.theme.Violet500
import com.sinasamaki.chromadecks.ui.theme.Violet600

/** The deck runs light, so syntax colours sit in the 600s to hold up against white. */
internal val RibbonCodeColors = CodeColors(
    keyword = Indigo600,
    string = Emerald600,
    number = Orange600,
    function = Violet600,
    param = Teal600,
    comment = Slate400,
)

/** Two colours: enough to see a segment land part way towards the end colour. */
internal val WARM = listOf(Rose500, Amber400)

/** More colours, more steps along the same path. */
internal val SPECTRUM = listOf(Violet500, Fuchsia500, Rose500, Orange400, Amber400)

internal val COOL = listOf(Indigo500, Sky500, Emerald500)

internal val DUSK = listOf(Fuchsia500, Indigo500, Cyan600)

/**
 * Deliberately clashing, cycled one per half turn, for the beat that shows where the segments
 * actually are before any gradient is run along them.
 */
internal val CONTRAST = listOf(Indigo500, Emerald500, Orange400, Fuchsia500, Cyan600, Amber400)

/** The single flat colour used before the deck earns its gradients. */
internal val FLAT: List<Color> = listOf(Rose500)
