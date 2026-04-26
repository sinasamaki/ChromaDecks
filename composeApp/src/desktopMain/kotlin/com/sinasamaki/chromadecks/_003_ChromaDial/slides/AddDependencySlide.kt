package com.sinasamaki.chromadecks._003_ChromaDial.slides

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.data.ListSlideAdvanced
import com.sinasamaki.chromadecks.ui.components.CodeIDE

internal data class AddDependencySlideState(val placeholder: Unit = Unit)

internal class AddDependencySlide : ListSlideAdvanced<AddDependencySlideState>() {

    override val initialState get() = AddDependencySlideState()

    @Composable
    override fun content(state: AddDependencySlideState) {
        var selectedTab by remember { mutableIntStateOf(0) }

        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            contentAlignment = Alignment.Center,
        ) {
            CodeIDE(
                modifier = Modifier.widthIn(max = 800.dp),
                tabs = listOf(
                    "build.gradle.kts" to """
dependencies {
    implementation("com.sinasamaki:chroma-dial:1.0.0-Alpha5")
}
                    """.trimIndent(),
                    "settings.gradle.kts" to """
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
                    """.trimIndent(),
                ),
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it },
            )
        }
    }
}
