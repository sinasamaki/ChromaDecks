package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Red500
import com.sinasamaki.chromadecks.ui.theme.Sky300
import com.sinasamaki.chromadecks.ui.theme.Yellow400

class AnimationsTitleCardState
class AnimationsTitleCard: ListSlideAdvanced<AnimationsTitleCardState>() {

    override val initialState: AnimationsTitleCardState
        get() = AnimationsTitleCardState()

    @Composable
    override fun content(state: AnimationsTitleCardState) {
        TitleCardFrame(
            title = "Animations",
            description = "Animation system that makes creating smooth, delightful motion effortless through a suite of powerful yet simple APIs that handle the complexity of timing, interpolation, and state synchronization automatically. The animate*AsState family of functions—including animateFloatAsState, animateColorAsState, animateDpAsState, and animateOffsetAsState—provides the most straightforward approach to animating values, automatically transitioning from the current value to any new target value whenever state changes, with customizable animation specs like spring physics for natural motion, tween for duration-based transitions, or keyframes for complex multi-stage animations. AnimatedVisibility is a high-level composable that handles the entire lifecycle of showing and hiding content with built-in enter and exit transitions like fade, slide, expand, and shrink that can be combined for sophisticated effects, automatically managing composition state and providing proper accessibility announcements while supporting custom transitions and children-specific animation behavior through AnimatedVisibilityScope. For more complex scenarios, updateTransition creates a Transition object that orchestrates multiple animated values in sync, perfect for state machines where multiple properties need to animate together cohesively, while animateContentSize automatically animates size changes of composables when their content grows or shrinks, eliminating jarring layout jumps. The rememberInfiniteTransition API enables continuous, looping animations ideal for loading indicators and attention-grabbing effects, while AnimatedContent provides sophisticated crossfade and slide transitions when swapping between entirely different composables based on state. All these APIs integrate seamlessly with Compose's recomposition system, use spring physics by default for natural, interruptible motion that feels responsive rather than robotic, and support custom animation specifications through AnimationSpec parameters that control duration, easing curves, delay, and repetition behavior, making it trivial to add polish and personality to interfaces that transform functional interactions into memorable, delightful experiences.",
            backgroundColor = Red500,
            borderColor = Sky300,
        )
    }
}
