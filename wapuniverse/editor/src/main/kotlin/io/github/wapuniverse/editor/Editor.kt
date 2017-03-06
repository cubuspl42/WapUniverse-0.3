package io.github.wapuniverse.editor

import io.github.wapuniverse.common.wap32.Wwd

interface Editor {
    companion object {
        fun create(wwd: Wwd): Editor {
            return EditorImpl(wwd)
        }
    }

    val world: World

    val undoStack: UndoStack

    fun destroyObject(obj: WObject)
}

