package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import chromadecks.composeapp.generated.resources.Res
import chromadecks.composeapp.generated.resources.titleSlide
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.modifiers.layer
import com.sinasamaki.kotlinconf.logo.KotlinLogo
import org.jetbrains.compose.resources.painterResource

class KotlinConfIntroSlideState
class KotlinConfIntroSlide : ListSlideAdvanced<KotlinConfIntroSlideState>() {

    override val initialState: KotlinConfIntroSlideState
        get() = KotlinConfIntroSlideState()

    @Composable
    override fun content(state: KotlinConfIntroSlideState) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.titleSlide),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight())
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    KotlinLogo(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .zIndex(1f)
                            .padding(24.dp)
                            .drawWithContent {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF3E0850),
                                            Color(0x323E0850),
//                                    Color(0xFF120317),
                                        ),
                                        center = Offset(size.width, size.height * .5f)
                                    ),
                                    radius = size.width * 2
                                )

                                val inflatedBounds = size.toRect().inflate(100f)
                                layer(
                                    inflatedBounds
                                ) {
                                    this@drawWithContent.drawContent()
                                    drawRect(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFFF3421C),
                                                Color(0xFFB935EF),
                                            ),
                                            start = Offset(0f, size.height),
                                            end = Offset(size.width, 0f),
                                        ),
                                        topLeft = inflatedBounds.topLeft,
                                        size = inflatedBounds.size,
                                        blendMode = BlendMode.SrcIn,
                                    )
                                }
                            }

                    )
                }
            }
        }
    }
}
