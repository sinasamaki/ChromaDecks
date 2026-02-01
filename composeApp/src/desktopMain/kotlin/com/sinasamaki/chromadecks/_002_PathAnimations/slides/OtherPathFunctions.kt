package com.sinasamaki.chromadecks._002_PathAnimations.slides

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addSvg
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinasamaki.chromadecks._002_PathAnimations.scalePath
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeBlock
import com.sinasamaki.chromadecks.ui.theme.Neutral700
import com.sinasamaki.chromadecks.ui.theme.Pink300
import com.sinasamaki.chromadecks.ui.theme.Sky500
import com.sinasamaki.chromadecks.ui.theme.Slate50
import com.sinasamaki.chromadecks.ui.theme.Slate950
import com.sinasamaki.chromadecks.ui.theme.Yellow300
import org.intellij.lang.annotations.Language


@Language("kotlin")
private val SAMPLE = """
    val path = Path()
    path.moveTo(50f, 50f)
""".trimIndent()

//@Language("kotlin")
private val SVG = """
path.addSvg(
    pathData = "
        M 50 10
        C 80 10, 90 40, 90 50
        C 90 60, 80 90, 50 90
        C 20 90, 10 60, 10 50
        C 10 40, 20 10, 50 10
        M 30 35
        L 40 35
        M 70 35
        C 65 30, 55 30, 50 35
        M 30 70
        C 40 80, 60 80, 70 70"
)
""".trimIndent()

@Language("kotlin")
private val RECT = """
path.addRect(
    rect = Rect(
        center = center,
        radius = 200f
    )
)
""".trimIndent()

@Language("kotlin")
private val OVAL = """
path.addOval(
    oval = Rect(
        topLeft = center - Offset(300f, 400f),
        bottomRight = center + Offset(300f, 400f)
    ),
)
""".trimIndent()

@Language("kotlin")
private val ARC = """
path.addArc(
    oval = Rect(
        center = center,
        radius = 200f
    ),
    startAngleDegrees = -10f,
    sweepAngleDegrees = 220f,
)
""".trimIndent()

data class CodeBlockData(
    val code: String,
    val implementation: @Composable () -> Unit,
)

data class OtherPathFunctionsState(
    val codeBlocks: List<CodeBlockData>,
    val currentBlock: Int,
)

private val blocks = listOf(
    CodeBlockData(
        code = ARC,
        implementation = {
            PathFunction {
                val path = Path()
                path.addArc(
                    oval = Rect(
                        center = center,
                        radius = 200f
                    ),
                    startAngleDegrees = -10f,
                    sweepAngleDegrees = 220f,
                )
                path
            }
        }
    ),
    CodeBlockData(
        code = RECT,
        implementation = {
            PathFunction {
                val path = Path()
                path.addRect(
                    rect = Rect(
                        center = center,
                        radius = 200f
                    )
                )

                path
            }
        }
    ),
    CodeBlockData(
        code = OVAL,
        implementation = {
            PathFunction {
                val path = Path()
                path.addOval(
                    oval = Rect(
                        topLeft = center - Offset(300f, 400f),
                        bottomRight = center + Offset(300f, 400f)
                    ),
                )

                path
            }
        }
    ),
    CodeBlockData(
        code = SVG,
        implementation = {
            PathFunction {
                val path = Path()
                path.addSvg(
                    pathData = """
                        M 50 10
                        C 80 10, 90 40, 90 50
                        C 90 60, 80 90, 50 90
                        C 20 90, 10 60, 10 50
                        C 10 40, 20 10, 50 10
                        M 30 35
                        L 40 35
                        M 70 35
                        C 65 30, 55 30, 50 35
                        M 30 70
                        C 40 80, 60 80, 70 70
                    """.trimIndent()
                )

                scalePath(path)
            }
        }
    ),

)

class OtherPathFunctions : ListSlideAdvanced<OtherPathFunctionsState>() {

    override val initialState: OtherPathFunctionsState
        get() = OtherPathFunctionsState(codeBlocks = blocks, currentBlock = 0)

    override val stateMutations: List<OtherPathFunctionsState.() -> OtherPathFunctionsState>
        get() = listOf(
            { copy(currentBlock = 1) },
            { copy(currentBlock = 2) },
            { copy(currentBlock = 3) },
        )

    @Composable
    override fun content(state: OtherPathFunctionsState) {

        CodeBlockCarousel(
            codeBlocks = state.codeBlocks,
            currentBlock = state.currentBlock,
        )
    }
}

@Composable
private fun PathFunction(
    modifier: Modifier = Modifier,
    path: DrawScope.() -> Path,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .drawCartesianGrid(
                color = Slate50,
                alpha = .4f
            )
            .drawWithContent {
                drawContent()
                drawPath(
                    path = path(this),
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Sky500,
                            Pink300,
                        )
                    ),
                    style = Stroke(
                        width = 20.dp.toPx()
                    )
                )
            }
    )
}

@Composable
fun CodeBlockCarousel(
    codeBlocks: List<CodeBlockData>,
    currentBlock: Int,
    modifier: Modifier = Modifier,
    codeChangeAnimations: Boolean = false,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 64.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(64.dp)
    ) {

        val pagerState = rememberPagerState {
            codeBlocks.size
        }

        LaunchedEffect(currentBlock) {
            pagerState.animateScrollToPage(
                page = currentBlock,
                animationSpec = spring(
                    stiffness = Spring.StiffnessVeryLow,
                )
            )
        }

        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .height(600.dp)
                .weight(1f),
            pageSize = PageSize.Fixed(600.dp),
        ) { it ->
            codeBlocks[it].let { block ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .border(
                            width = 1.dp,
                            color = Neutral700.copy(alpha = .8f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = Color(0xff090909),
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(16.dp)
                ) {
                    CodeBlock(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        code = block.code,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Slate950
                        ),
                        highlightAnimation = codeChangeAnimations,
                        enableAnimations = codeChangeAnimations,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(.8f)
//                .aspectRatio(1f)
            ,
        ) {

            BlurredAnimatedChange(
                targetState = pagerState.targetPage,
            ) { index ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    codeBlocks[index].implementation()
                }

            }
        }

    }


}


@Composable
fun <S> BlurredAnimatedChange(
    targetState: S,
    modifier: Modifier = Modifier,
    content: @Composable (AnimatedContentScope.(S) -> Unit)
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            fadeIn(
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ) + slideInVertically(
                initialOffsetY = { -it / 10 },
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ) togetherWith slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ) + scaleOut(
                targetScale = .9f,
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            ) using SizeTransform(clip = false)
        }
    ) { stateForContent ->

        val alpha by animateFloatAsState(
            targetValue = if (stateForContent == targetState) 1f else 0f,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )

        val blur by animateDpAsState(
            targetValue = if (stateForContent == targetState) 0.dp else 100.dp,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )

        Box(
            modifier = Modifier
                .blur(blur, BlurredEdgeTreatment.Unbounded)
                .alpha(alpha)
        ) {
            content(this@AnimatedContent, stateForContent)
        }

    }

}