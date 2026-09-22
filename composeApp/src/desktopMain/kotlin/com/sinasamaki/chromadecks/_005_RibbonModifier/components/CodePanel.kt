package com.sinasamaki.chromadecks._005_RibbonModifier.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.White
import com.sinasamaki.chromadecks.ui.theme.Zinc200

/** A light-mode sheet for on-slide code. The deck runs light, so the code does too. */
@Composable
fun CodePanel(
    code: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelSmall,
) {
    Box(
        modifier = modifier
            .background(White, RoundedCornerShape(28.dp))
            .border(1.dp, Zinc200, RoundedCornerShape(28.dp))
            .padding(horizontal = 40.dp, vertical = 36.dp),
    ) {
        CodeBlock(
            code = code,
            style = style,
            darkMode = false,
            bouncy = false,
        )
    }
}
