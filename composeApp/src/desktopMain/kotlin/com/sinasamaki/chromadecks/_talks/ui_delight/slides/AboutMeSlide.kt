package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import chromadecks.composeapp.generated.resources.Res
import coil3.compose.AsyncImage
import com.sinasamaki.chromadecks._002_PathAnimations.slides.BlurredAnimatedChange
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.frames.blendMode
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc50
import com.sinasamaki.chromadecks.ui.theme.Zinc800
import com.sinasamaki.chromadecks.ui.theme.Zinc950

data class AboutMeSlideState(
    val img: String = "imgly.png",
    val profile: String = "leshan.jpeg",
    val name: String = "Ian Leshan",
    val color: Color = Zinc300,
)

class AboutMeSlide : ListSlideAdvanced<AboutMeSlideState>() {

    override val initialState: AboutMeSlideState
        get() = AboutMeSlideState()

    override val stateMutations: List<AboutMeSlideState.() -> AboutMeSlideState>
        get() = listOf(
            {
              copy(name = "Leshan")
            },
            {
                copy(img = "sinasamaki.png", profile = "myheart.png", name = "sinasamaki", color = Zinc950)
            }
        )


    @Composable
    override fun content(state: AboutMeSlideState) {

        val backgroundColor by animateColorAsState(
            targetValue = state.color,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )
        Row(
            modifier = Modifier
                .background(
                    backgroundColor,
                )
                .fillMaxSize().padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = Res.getUri("drawable/${state.profile}"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = state.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = Zinc50,
                    modifier = Modifier.blendMode(BlendMode.Difference)
                )
            }
            Spacer(
                Modifier
                    .width(32.dp)
            )
            BlurredAnimatedChange(
                targetState = state.img,
                modifier = Modifier.weight(1f, false)
            ) { img ->
                AsyncImage(
                    model = Res.getUri("drawable/${img}"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(.7f)
//                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(24.dp))
                )
            }

        }

    }

}