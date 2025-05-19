package com.sinasamaki.chromadecks.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chromadecks.composeapp.generated.resources.*
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.league_spartan_bold
import chromadecks.composeapp.generated.resources.league_spartan_regular
import chromadecks.composeapp.generated.resources.space_mono_regular
import org.jetbrains.compose.resources.Font

@Composable
fun ChromaTheme(
    colors: ColorScheme = darkColorScheme(),
    content: @Composable () -> Unit,
) {

    val defaultFontFamily = FontFamily(
        Font(
            resource = Res.font.league_spartan_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.league_spartan_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        )
    )

    val monoFontFamily = FontFamily(
        Font(
            resource = Res.font.space_mono_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
    )

    val displayFontFamily = FontFamily(
        Font(
            resource = Res.font.prata_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            displayLarge = TextStyle(
                fontSize = 96.sp,
                fontFamily = displayFontFamily,
            ),
            displayMedium = TextStyle(
                fontSize = 72.sp,
                fontFamily = displayFontFamily,
            ),
            displaySmall = TextStyle(
                fontSize = 56.sp,
                fontFamily = displayFontFamily,
            ),
            headlineLarge = TextStyle(
                fontSize = 72.sp,
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.Bold
            ),
            bodyLarge = TextStyle(
                fontSize = 32.sp,
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W400,
            ),
            labelLarge = TextStyle(
                fontSize = 24.sp,
                fontFamily = monoFontFamily
            ),
            labelMedium = TextStyle(
                fontSize = 20.sp,
                fontFamily = monoFontFamily
            ),
            labelSmall = TextStyle(
                fontSize = 14.sp,
                fontFamily = monoFontFamily
            ),
        ),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        ) {
            content()
        }
    }
}
