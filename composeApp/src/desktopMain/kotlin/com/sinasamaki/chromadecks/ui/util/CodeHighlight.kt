package com.sinasamaki.chromadecks.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

@Composable
fun highlightLine(
    line: String,
    darkMode: Boolean,
): AnnotatedString {
    return remember(line) {
        buildAnnotatedString {
            append(line)
            Highlights.Builder()
                .language(SyntaxLanguage.KOTLIN)
                .code(line)
                .theme(SyntaxThemes.atom(darkMode))
                .build()
                .apply {
                    getHighlights().forEach {
                        when (it) {
                            is ColorHighlight -> {
                                this@buildAnnotatedString.addStyle(
                                    style = SpanStyle(
                                        color = Color(0xff000000 + it.rgb)
                                    ),
                                    start = it.location.start,
                                    end = it.location.end,
                                )
                            }

                            is BoldHighlight -> {
                                this@buildAnnotatedString.addStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    start = it.location.start,
                                    end = it.location.end,
                                )
                            }
                        }
                    }
                }
        }
    }
}
