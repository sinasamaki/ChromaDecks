package com.sinasamaki.chromadecks._001_MeshGradients.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sinasamaki.chromadecks.data.ListSlide
import com.sinasamaki.chromadecks.data.ListSlideSimple
import com.sinasamaki.chromadecks.ui.components.HeartLogo
import com.sinasamaki.chromadecks.ui.theme.Zinc950

class EndSlide : ListSlideSimple() {

    @Composable
    override fun content() {
        Box(Modifier.fillMaxSize().background(color = Zinc950)) {
            HeartLogo(Modifier.align(Alignment.Center))
        }
    }

}