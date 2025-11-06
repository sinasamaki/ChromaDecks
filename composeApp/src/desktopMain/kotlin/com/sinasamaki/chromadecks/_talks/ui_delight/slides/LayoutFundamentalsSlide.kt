package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Red500

class LayoutFundamentalsSlideState
class LayoutFundamentalsSlide : ListSlideAdvanced<LayoutFundamentalsSlideState>() {

    override val initialState: LayoutFundamentalsSlideState
        get() = LayoutFundamentalsSlideState()

    @Composable
    override fun content(state: LayoutFundamentalsSlideState) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize(),
//            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier.weight(.5f),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                ) {
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Red500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Green500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Blue500
                    )
                }

                Row(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                ) {
                    Item(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        color = Red500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        color = Green500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        color = Blue500
                    )
                }


                Box(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Item(
                        modifier = Modifier
                            .fillMaxSize(1f),
                        color = Red500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxSize(.666f),
                        color = Green500
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxSize(.333f),
                        color = Blue500
                    )
                }


            }


            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.weight(.3f),
                horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally)
            ) {
                CodeBlock(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    code = """
                        Column {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()
                )
                CodeBlock(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    code = """
                        Row {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()
                )
                CodeBlock(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    code = """
                        Box {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()
                )
            }
        }
    }
}


@Composable
private fun Item(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Box(
        modifier
            .padding(8.dp)
            .background(
                color = color.copy(alpha = .2f),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 5.dp,
                color = color,
                shape = RoundedCornerShape(24.dp)
            )
    )
}
