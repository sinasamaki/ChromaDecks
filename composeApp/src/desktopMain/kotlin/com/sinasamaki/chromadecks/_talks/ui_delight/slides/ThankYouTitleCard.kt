package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import chromadecks.composeapp.generated.resources.myheart
import chromadecks.composeapp.generated.resources.thank_you_qrcode
import com.sinasamaki.chromadecks._talks.ui_delight.components.Social
import com.sinasamaki.chromadecks._talks.ui_delight.components.SocialHandle
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.Space
import com.sinasamaki.chromadecks.ui.theme.Zinc300
import com.sinasamaki.chromadecks.ui.theme.Zinc700
import com.sinasamaki.chromadecks.ui.theme.Zinc950
import org.jetbrains.compose.resources.painterResource

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
            Image(
                painter = painterResource(Res.drawable.thank_you_qrcode),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp)
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(Res.drawable.myheart),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(152.dp)
                        .clip(CircleShape)
                )
                Space(32.dp)
                Column {
                    Text(
                        text = "sinasamaki.com",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 48.sp),
                        color = Zinc300,
                    )
                    Space(8.dp)
                    Social.entries.forEach { social ->
                        SocialHandle(
                            social = social,
                            color = Zinc300,
                            fontSize = 32.sp,
                            iconSize = 32.dp,
                        )
                        Space(4.dp)
                    }
                }
            }
        }

    }
}
