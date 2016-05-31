package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.EditorObject
import io.github.wapuniverse.editor.EditorObjectComponent
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.SceneView
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseEvent


class SelectionController(
        private val root: Node,
        private val editorObjectComponent: EditorObjectComponent,
        private val sceneView: SceneView) {

    var selectedObject: EditorObject? = null

    var dragOffset: Vec2d? = Vec2d()

    fun onMousePressed(x: Double, y: Double) {
        val wv = sceneView.invTransform.transform(x, y).toVec2d()
        if (selectedObject != null && selectedObject!!.boundingBox.contains(wv.x, wv.y)) {
            dragOffset = wv - selectedObject!!.position.toVec2d()
        }
    }

    fun onMouseReleased(ev: MouseEvent) {
        if (ev.isStillSincePress) {
            selectNext(ev.x, ev.y)
        }
        dragOffset = null
    }

    fun onMouseDragged(ev: MouseEvent) {
        if (selectedObject != null && dragOffset != null) {
            val wv = sceneView.invTransform.transform(ev.x, ev.y).toVec2d()
            selectedObject!!.position = (wv - dragOffset!!).toVec2i()
        }
    }

    fun onMouseMoved(ev: MouseEvent) {
        editorObjectComponent.objects.forEach { unhoverObject(it)}
        if (!ev.isPrimaryButtonDown) {
            val s = selectableObjectsAt(ev.x, ev.y)
            s.forEach { hoverObject(it) }
        }

        val wv = sceneView.invTransform.transform(ev.x, ev.y).toVec2d()
        if (selectedObject?.boundingBox?.contains(wv.x, wv.y) == true) {
            root.cursor = Cursor.MOVE
        } else {
            root.cursor = Cursor.DEFAULT
        }
    }

    private fun selectNext(cx: Double, cy: Double) {
        val s = selectableObjectsAt(cx, cy)
        if (s.isNotEmpty()) {
            val i = s.indexOf(selectedObject)
            if (i > 0) {
                selectObject(s[i - 1])
            } else {
                selectObject(s.last())
            }
        }
    }

    private fun selectableObjectsAt(cx: Double, cy: Double): List<EditorObject> {
        val wv = sceneView.invTransform.transform(cx, cy).toVec2i()
        val s = editorObjectComponent.selectableObjectsAt(wv.x, wv.y)
        return s
    }

    private fun selectObject(obj: EditorObject) {
        unselectObject(selectedObject)
        selectedObject = obj
        obj._isSelected = true
        obj.onSelected()
    }

    private fun unselectObject(obj: EditorObject?) {
        obj?._isSelected = false
        obj?.onUnselected()
    }

    private fun hoverObject(obj: EditorObject) {
        obj._isHovered = true
        obj.onHover()
    }

    private fun unhoverObject(obj: EditorObject) {
        obj._isHovered = false
        obj.onUnhover()
    }
}
