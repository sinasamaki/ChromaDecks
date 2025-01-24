package com.sinasamaki.chromadecks.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

abstract class ListSlide<T> {

    private var stateIndex = MutableStateFlow(0)
    protected abstract val states: List<T>

    fun next(): Boolean {
        if (stateIndex.value >= states.lastIndex) {
            return false
        }
        stateIndex.value++
        return true
    }

    fun previous(): Boolean {
        if (stateIndex.value <= 0) {
            return false
        }
        stateIndex.value--
        return true
    }

    @Composable
    fun currentContent() {
        val index by stateIndex.collectAsStateWithLifecycle()
        content(states[index])
    }


    @Composable
    abstract fun content(state: T)
}


abstract class ListSlideSimple : ListSlide<Unit>() {

    override val states: List<Unit>
        get() = listOf(Unit)

    @Composable
    override fun content(state: Unit) {
        TODO("Not yet implemented")
    }

    @Composable
    abstract fun content()
}

