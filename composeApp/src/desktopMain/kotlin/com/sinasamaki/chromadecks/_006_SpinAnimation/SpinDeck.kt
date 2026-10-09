package com.sinasamaki.chromadecks._006_SpinAnimation

import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks.ui.theme.Black
import com.sinasamaki.chromadecks.ui.theme.CodeColors
import com.sinasamaki.chromadecks.ui.theme.Red100
import com.sinasamaki.chromadecks.ui.theme.Red200
import com.sinasamaki.chromadecks.ui.theme.Red300
import com.sinasamaki.chromadecks.ui.theme.Red400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Red600
import com.sinasamaki.chromadecks.ui.theme.Red700
import com.sinasamaki.chromadecks.ui.theme.Red800
import com.sinasamaki.chromadecks.ui.theme.Red900
import com.sinasamaki.chromadecks.ui.theme.Red950

internal val SpinRed: Color = Red600

internal val SpinRedMuted: Color = Red800

internal val SpinRedDim: Color = Red700

internal val SpinGround: Color = Black

internal val SpinCodeColors = CodeColors(
    keyword = Red600,
    string = Red300,
    number = Red300,
    function = Red400,
    param = Red200,
    comment = Red900,
)

internal val SpinCodeText: Color = Red100

internal val SpinCodePanelFill: Color = Red950.copy(alpha = .35f)
internal val SpinCodePanelBorder: Color = Red900

internal data class SpinLook(
    val front: Color,
    val back: Color,
    val edge: List<Color>,
    val side: Color,
    val sideEnds: Color,
    val ink: Color,
)

internal val RedLook = SpinLook(
    front = Red600,
    back = Red800,
    edge = listOf(Red500, Red800, Red500),
    side = Red700,
    sideEnds = Red950,
    ink = Black,
)
