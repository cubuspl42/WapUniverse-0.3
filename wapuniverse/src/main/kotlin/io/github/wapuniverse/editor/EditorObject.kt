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

    val isHovered: Boolean
        get() = _isHovered

    open fun onSelected() {
    }

    open fun onUnselected() {
    }

    open fun onHover() {
    }

    open fun onUnhover() {
    }

    internal var _isSelected = false

    internal var _isHovered = false
}

class EditorObjectComponent {
    private val _objects = HashSet<EditorObject>()

    val objects: Set<EditorObject>
        get() = _objects

    fun addEditorObject(obj: EditorObject) {
        _objects.add(obj)
        objectAdded._emit(obj)
    }

    fun selectableObjectsAt(x: Int, y: Int): List<EditorObject> {
        return _objects.filter { it.boundingBox.contains(x, y) }
    }



    val objectAdded = Signal<EditorObject>()
}
