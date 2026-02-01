package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Blue950
import com.sinasamaki.chromadecks.ui.theme.Lime400

class LayoutFundamentalsTitleCardState
class LayoutFundamentalsTitleCard: ListSlideAdvanced<LayoutFundamentalsTitleCardState>() {

    override val initialState: LayoutFundamentalsTitleCardState
        get() = LayoutFundamentalsTitleCardState()

    @Composable
    override fun content(state: LayoutFundamentalsTitleCardState) {
        TitleCardFrame(
            title = "Layout Fundamentals",
            description = "Box, Column, and Row are the three fundamental layout composables that form the foundation of Jetpack Compose's UI system, replacing the complex XML hierarchy and LayoutParams of traditional Android Views with an elegant, declarative model that makes positioning elements intuitive and powerful. Box operates as the simplest container, stacking its children along the z-axis on top of each other, making it indispensable for overlaying elements like badges on icons, floating action buttons over content, or creating custom layered compositions with precise alignment control through its contentAlignment parameter that can position children at any of the nine standard positions or with custom alignment logic. Column arranges its children vertically in a linear flow from top to bottom, serving as the backbone for scrollable lists, form layouts, and any vertical content arrangement—it offers sophisticated spacing control through arrangement parameters like Arrangement.SpaceBetween, Arrangement.SpaceEvenly, and Arrangement.spacedBy() that distribute children with mathematical precision, while alignment options control the horizontal positioning of each child independently or collectively. Row functions identically to Column but flows horizontally from start to end, automatically respecting right-to-left layouts for proper internationalization, making it essential for navigation bars, button groups, toolbars, and any horizontal content organization, complete with the same powerful arrangement and alignment capabilities that enable pixel-perfect layouts with minimal code. The true elegance of these primitives emerges through composition—nesting Columns within Rows within Boxes creates arbitrarily complex layouts through simple, readable code that mirrors the visual hierarchy, while the Modifier system allows chaining operations like padding, size constraints, backgrounds, borders, and interaction handlers in a declarative, order-dependent manner that's both intuitive and powerful. Each layout integrates seamlessly with Compose's intrinsic measurement system, intelligently calculating optimal sizes based on children's requirements and parent constraints, while supporting weighted space distribution through Modifier.weight() that enables children to claim proportional amounts of available space, perfect for responsive designs that adapt fluidly across screen sizes. Understanding how these three layouts handle constraint propagation, measure their children, position elements within their bounds, and interact with modifiers is fundamental to building performant, accessible, responsive interfaces that feel native across devices while maintaining the precise control needed for branded, standout experiences.",
            backgroundColor = Blue950,
            borderColor = Lime400,
        )
    }
}
