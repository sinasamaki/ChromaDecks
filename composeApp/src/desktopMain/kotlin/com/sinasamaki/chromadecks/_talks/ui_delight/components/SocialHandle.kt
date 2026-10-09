package com.sinasamaki.chromadecks._talks.ui_delight.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.bluesky_logo
import chromadecks.composeapp.generated.resources.twitter_logo
import com.sinasamaki.chromadecks.ui.components.Space
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class Social(val icon: DrawableResource, val handle: String) {
    Twitter(Res.drawable.twitter_logo, "@sinasamaki"),
    Bluesky(Res.drawable.bluesky_logo, "@sinasamaki.com"),
}

@Composable
fun SocialHandle(
    social: Social,
    color: Color,
    fontSize: TextUnit,
    iconSize: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(social.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(iconSize),
        )
        Space(iconSize * .5f)
        Text(
            text = social.handle,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = fontSize),
            color = color,
        )
    }
}
