package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.utils.*
import java.util.*


abstract class EditorObject {
    abstract var position: Vec2i

    abstract val boundingBox: Rectangle2Di

    val isSelected: Boolean
        get() = _isSelected

    open fun onSelected() {
    }

    open fun onUnselected() {
    }

    internal var _isSelected = false
}

class EditorObjectComponent {
    private val objects = HashSet<EditorObject>()

    fun addEditorObject(obj: EditorObject) {
        objects.add(obj)
        objectAdded._emit(obj)
    }

    fun selectableObjectsAt(x: Int, y: Int): List<EditorObject> {
        return objects.filter { it.boundingBox.contains(x, y) }
    }

    val objectAdded = Signal<EditorObject>()
}
