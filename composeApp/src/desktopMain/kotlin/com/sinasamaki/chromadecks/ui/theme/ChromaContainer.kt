package com.sinasamaki.chromadecks.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ChromaContainer(
    content: @Composable () -> Unit,
) {
    ChromaTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Black),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                Modifier
                    .padding(16.dp)
                    .aspectRatio(4 / 3f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                content()
            }
        }
    }

}