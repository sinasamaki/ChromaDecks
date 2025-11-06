package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Amber600
import com.sinasamaki.chromadecks.ui.theme.Lime200
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Red300
import com.sinasamaki.chromadecks.ui.theme.Red700
import com.sinasamaki.chromadecks.ui.theme.Red800

class GesturesTitleCardState
class GesturesTitleCard: ListSlideAdvanced<GesturesTitleCardState>() {

    override val initialState: GesturesTitleCardState
        get() = GesturesTitleCardState()

    @Composable
    override fun content(state: GesturesTitleCardState) {
        TitleCardFrame(
            title = "Gestures",
            description = "Jetpack Compose provides a comprehensive gesture system that ranges from simple, high-level interactions like clickable to sophisticated custom gesture detection, enabling developers to create intuitive, responsive interfaces that feel natural and polished. The clickable modifier is the most fundamental interaction primitive, handling tap events with built-in ripple effects, press indication, and accessibility semantics automatically, while also supporting enabled states, role descriptions for screen readers, and custom indication effects for branded experiences beyond Material's default ripple. For more nuanced control, combinedClickable extends basic clicking with long-press detection and double-tap handling, perfect for context menus or alternative actions without cluttering the interface with additional buttons. The pointerInput modifier unlocks the full power of Compose's gesture system through coroutine-based APIs like detectTapGestures, detectDragGestures, and detectTransformGestures, allowing you to implement custom swipes, pinch-to-zoom, rotation, and complex multi-touch interactions with complete control over gesture lifecycle, velocity tracking, and cancellation handling. SwipeToDismissBox represents a higher-level gesture component that encapsulates the common pattern of swiping items to reveal actions or remove them from lists, handling the physics of the swipe animation, threshold detection, directional constraints, and background content revelation with minimal code while remaining fully customizable through state management and custom dismissal logic. Draggable and scrollable modifiers provide one-dimensional drag and scroll detection with built-in fling physics, while transformable handles multi-touch pan, zoom, and rotation simultaneously for image viewers and map-like interfaces. The Modifier.pointerInput scope provides access to AwaitPointerEventScope, where you can implement completely custom gesture recognizers by consuming or ignoring pointer events, tracking multiple fingers independently, calculating velocities, and composing complex gesture sequences like \"tap-then-drag\" or \"long-press-then-swipe\" that feel sophisticated and purposeful, all while maintaining proper accessibility support through semantic actions and state descriptions that ensure gesture-based interactions remain usable for everyone regardless of their input method or assistive technology.",
            backgroundColor = Amber600,
            borderColor = Red700,
        )
    }
}
