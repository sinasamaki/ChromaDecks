package com.sinasamaki.chromadecks.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.sinasamaki.chromadecks.ui.theme.LocalCodeColors

private val KOTLIN_KEYWORDS = setOf(
    "fun", "val", "var", "class", "data", "object", "interface", "enum",
    "sealed", "abstract", "override", "open", "private", "public", "protected",
    "internal", "return", "if", "else", "when", "for", "while", "do", "in",
    "is", "as", "null", "true", "false", "this", "super", "import", "package",
    "by", "companion", "init", "constructor", "get", "set", "typealias",
    "suspend", "inline", "reified", "crossinline", "noinline", "out",
    "where", "expect", "actual", "external", "lateinit", "const",
    "operator", "infix", "tailrec", "annotation", "throw", "try",
    "catch", "finally", "continue", "break", "it", "to",
)

// Patterns applied in priority order — first match wins for any character range.
private val COMMENT_RE  = Regex("""//.*$""")
private val STRING_RE   = Regex(""""(?:[^"\\]|\\.)*"""")
private val NUMBER_RE   = Regex("""\b\d+\.?\d*[fFdDlL]?\b""")
// Named parameter: word followed by optional space and `=` but not `==`
private val PARAM_RE    = Regex("""\b([a-zA-Z_]\w*)\s*=(?!=)""")
// Capture group 1 = the function name only (excludes the opening paren)
private val FUN_CALL_RE = Regex("""\b([a-zA-Z_]\w*)\s*\(""")
private val KEYWORD_RE  = Regex("""\b(${KOTLIN_KEYWORDS.joinToString("|")})\b""")

@Composable
fun highlightLine(line: String, darkMode: Boolean = true): AnnotatedString {
    val colors = LocalCodeColors.current
    return remember(line, colors) {
        data class Token(val start: Int, val end: Int, val color: androidx.compose.ui.graphics.Color)

        val tokens = mutableListOf<Token>()

        fun tryAdd(start: Int, end: Int, color: androidx.compose.ui.graphics.Color) {
            if (tokens.none { it.start < end && it.end > start }) {
                tokens.add(Token(start, end, color))
            }
        }

        // 1. Comments — highest priority
        COMMENT_RE.find(line)?.let {
            tryAdd(it.range.first, it.range.last + 1, colors.comment)
        }

        // 2. Strings
        STRING_RE.findAll(line).forEach {
            tryAdd(it.range.first, it.range.last + 1, colors.string)
        }

        // 3. Numbers
        NUMBER_RE.findAll(line).forEach {
            tryAdd(it.range.first, it.range.last + 1, colors.number)
        }

        // 4. Named parameters — color only the name, not the `=`
        PARAM_RE.findAll(line).forEach { m ->
            m.groups[1]?.let { g ->
                if (line.substring(g.range) !in KOTLIN_KEYWORDS) {
                    tryAdd(g.range.first, g.range.last + 1, colors.param)
                }
            }
        }

        // 5. Function names (declarations and calls) — color only the name, not the paren
        FUN_CALL_RE.findAll(line).forEach { m ->
            m.groups[1]?.let { g ->
                if (line.substring(g.range) !in KOTLIN_KEYWORDS) {
                    tryAdd(g.range.first, g.range.last + 1, colors.function)
                }
            }
        }

        // 6. Keywords — lowest priority
        KEYWORD_RE.findAll(line).forEach {
            tryAdd(it.range.first, it.range.last + 1, colors.keyword)
        }

        buildAnnotatedString {
            append(line)
            tokens.forEach { addStyle(SpanStyle(color = it.color), it.start, it.end) }
        }
    }
}
