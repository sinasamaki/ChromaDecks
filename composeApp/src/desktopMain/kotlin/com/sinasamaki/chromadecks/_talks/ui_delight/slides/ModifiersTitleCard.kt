package com.sinasamaki.chromadecks._talks.ui_delight.slides

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sinasamaki.chromadecks._talks.ui_delight.components.TitleCardFrame
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.theme.Lime400
import com.sinasamaki.chromadecks.ui.theme.Orange300
import com.sinasamaki.chromadecks.ui.theme.Orange500
import com.sinasamaki.chromadecks.ui.theme.Teal500

class ModifiersTitleCardState
class ModifiersTitleCard: ListSlideAdvanced<ModifiersTitleCardState>() {

    override val initialState: ModifiersTitleCardState
        get() = ModifiersTitleCardState()

    @Composable
    override fun content(state: ModifiersTitleCardState) {
        TitleCardFrame(
            title = "Modifiers",
            description = "Modifiers in Jetpack Compose are the key mechanism for decorating, configuring, and augmenting composables, functioning as an elegant chain of transformations that fundamentally shapes how UI elements appear and behave. The most critical concept to understand about modifiers is that order absolutely matters—each modifier wraps the previous one, creating a chain where the sequence directly impacts the final result in ways that can be surprising if you don't understand the underlying model. When you write Modifier.padding(16.dp).background(Color.Blue).padding(8.dp), you're creating distinctly different behavior than Modifier.background(Color.Blue).padding(24.dp): the first adds outer padding, then applies a blue background, then adds inner padding between the background and content, while the second would only show padding outside the blue background. This ordering principle extends to all modifiers—size constraints applied before padding behave differently than those applied after, clickable regions defined before or after padding determine whether the padding area is interactive, and background colors interact with borders and shapes based on their position in the chain. Modifiers handle a vast array of concerns: layout modifications like size, padding, and offset; drawing operations like background, border, and clip; interaction handling through clickable, draggable, and pointerInput; accessibility with semantics; and animation with animateContentSize. The Modifier.then() function allows conditional composition of modifier chains, while remember and derivedStateOf enable creating modifiers that respond to state changes efficiently. Understanding that each modifier creates a new Modifier instance—they're immutable—helps explain why order matters and why you can safely reuse and compose modifier chains. Common patterns include applying size modifiers early to establish boundaries, adding padding to create internal spacing, applying backgrounds and borders for visual styling, then finishing with interaction and semantic modifiers that define behavior and accessibility, though this isn't a rigid rule and creative ordering can achieve sophisticated effects like nested borders, layered backgrounds, or complex gesture handling zones that would be cumbersome with traditional Views.",
            backgroundColor = Teal500,
            borderColor = Orange300,
        )
    }
}
