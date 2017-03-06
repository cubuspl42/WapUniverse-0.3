package io.github.wapuniverse.editor

import io.github.wapuniverse.common.wap32.Wwd

internal class EditorImpl(wwd: Wwd) : Editor {
    private val worldImpl = WorldImpl(wwd)

    private val undoStackImpl = UndoStackImpl()

    override val world: World = worldImpl

    override val undoStack: UndoStack = undoStackImpl

    override fun destroyObject(obj: WObject) {
        class DestroyObject : UndoFrame {
            private val wwdObject = obj.wwdObject.copy()
            private var wObject = obj

            override fun undo() {
                wObject = worldImpl.addObject()
                wObject.wwdObject = wwdObject
            }

            override fun redo() {
                worldImpl.removeObject(wObject)
            }

        }
        undoStackImpl.push(DestroyObject())
    }
}
