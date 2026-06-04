package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chromadecks.composeapp.generated.resources.Res
import coil3.compose.AsyncImage
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.Space
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc950

class ThankYouTitleCardState
class ThankYouTitleCard: ListSlideAdvanced<ThankYouTitleCardState>() {

    override val initialState: ThankYouTitleCardState
        get() = ThankYouTitleCardState()

    @Composable
    override fun content(state: ThankYouTitleCardState) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TitleCardFrame(
                title = "Thank you!",
                description = "",
                backgroundColor = Zinc950,
                borderColor = Zinc700,
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = Res.getUri("drawable/myheart.png"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Space(32.dp)
                Column {
                    Text(
                        text = "sinasamaki.com",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 48.sp),
                        color = Zinc300,
                    )
                    Space(2.dp)
                    Text(
                        text = "@sinasamaki",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 32.sp),
                        color = Zinc300,
                    )
                }
            }
        }

    }
}
