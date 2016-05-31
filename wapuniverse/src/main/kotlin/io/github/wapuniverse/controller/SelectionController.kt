package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.EditorObject
import io.github.wapuniverse.editor.EditorObjectComponent
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.SceneItem
import io.github.wapuniverse.view.SceneView
import javafx.scene.input.MouseEvent


class SelectionController(
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

    private fun selectNext(cx: Double, cy: Double) {
        val wv = sceneView.invTransform.transform(cx, cy).toVec2i()
        val s = editorObjectComponent.selectableObjectsAt(wv.x, wv.y)
        if (s.isNotEmpty()) {
            val i = s.indexOf(selectedObject)
            if (i > 0) {
                selectObject(s[i - 1])
            } else {
                selectObject(s.last())
            }
        }
    }

    private fun selectObject(obj: EditorObject) {
        selectedObject?.onUnselected()
        selectedObject = obj
        obj.onSelected()
    }
}
