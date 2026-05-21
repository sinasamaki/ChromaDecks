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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE
import com.sinasamaki.chromadecks.ui.theme.Blue500
import com.sinasamaki.chromadecks.ui.theme.Green500
import com.sinasamaki.chromadecks.ui.theme.Purple400
import com.sinasamaki.chromadecks.ui.theme.Purple500
import com.sinasamaki.chromadecks.ui.theme.Purple600
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.kotlinconf.logo.brush

class LayoutFundamentalsSlideState
class LayoutFundamentalsSlide : ListSlideAdvanced<LayoutFundamentalsSlideState>() {

    override val initialState: LayoutFundamentalsSlideState
        get() = LayoutFundamentalsSlideState()

    @Composable
    override fun content(state: LayoutFundamentalsSlideState) {
        Column(
            modifier = Modifier
                .padding(64.dp)
                .fillMaxSize(),
//            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier.weight(.5f),
                horizontalArrangement = Arrangement.spacedBy(128.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                ) {
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Purple400
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Purple400.copy(alpha = .5f)
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = Purple400.copy(alpha = .25f)
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
                        color = Purple400.copy(alpha = 1f)
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        color = Purple400.copy(alpha = .5f)
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        color = Purple400.copy(alpha = .25f)
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
                        color = Purple400.copy(alpha = 1f)
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxSize(.666f),
                        color = Purple400.copy(alpha = .5f)
                    )
                    Item(
                        modifier = Modifier
                            .fillMaxSize(.333f),
                        color = Purple400.copy(alpha = .25f)
                    )
                }


            }


            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.weight(.3f),
                horizontalArrangement = Arrangement.spacedBy(128.dp, Alignment.CenterHorizontally)
            ) {
                CodeIDE(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    tabs = listOf("Column.kt" to """
                        Column {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()),
                    selectedTab = 0,
                    onTabSelect = {},
                )
                CodeIDE(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    tabs = listOf("Row.kt" to """
                        Row {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()),
                    selectedTab = 0,
                    onTabSelect = {},
                )
                CodeIDE(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    tabs = listOf("Box.kt" to """
                        Box {
                            Item()
                            Item()
                            Item()
                        }
                    """.trimIndent()),
                    selectedTab = 0,
                    onTabSelect = {},
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
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = .1f),
                        color.copy(alpha = .02f),
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(32.dp)
            )
    )
}
