package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.EditorObject
import io.github.wapuniverse.editor.EditorObjectComponent
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.EventHandlingStatus
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.SceneView
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import java.util.logging.Logger


private val mainInputHandlerPriority = 0

class MainInputHandler(
        private val root: Node,
        private val editorObjectComponent: EditorObjectComponent,
        private val sceneView: SceneView) : InputHandler {

    private val logger = Logger.getLogger(javaClass.simpleName)

    var dragOffset: Vec2d? = Vec2d()

    var selectedObject: EditorObject? = null

    private fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    override fun onMousePressed(ev: MouseEvent): EventHandlingStatus {
        val wv = invTr(ev.x, ev.y)
        if (selectedObject != null && selectedObject!!.boundingBox.contains(wv.x, wv.y)) {
            dragOffset = wv - selectedObject!!.position.toVec2d()
        }
        return EVENT_HANDLED
    }

    override fun onMouseReleased(ev: MouseEvent): EventHandlingStatus {
        if (ev.isStillSincePress) {
            selectNextObject(invTr(ev.x, ev.y))
        }
        dragOffset = null
        return EVENT_HANDLED
    }

    override fun onMouseMoved(ev: MouseEvent): EventHandlingStatus {
        editorObjectComponent.objects.forEach { unhoverObject(it) }
        if (!ev.isPrimaryButtonDown) {
            val s = selectableObjectsAt(invTr(ev.x, ev.y))
            s.forEach { hoverObject(it) }
        }

        val wv = invTr(ev.x, ev.y)
        if (selectedObject?.boundingBox?.contains(wv.x, wv.y) == true) {
            root.cursor = Cursor.MOVE
        } else {
            root.cursor = Cursor.DEFAULT
        }

        return EVENT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        if (selectedObject != null && dragOffset != null) {
            val wv = invTr(ev.x, ev.y)
            val p = wv - dragOffset!!
            selectedObject!!.position = p.toVec2i()
        }
        return EVENT_HANDLED
    }

    override val priority: Int
        get() = mainInputHandlerPriority

    private fun selectNextObject(wv: Vec2d) {
        val s = selectableObjectsAt(wv)
        if (s.isNotEmpty()) {
            val i = s.indexOf(selectedObject)
            if (i > 0) {
                selectObject(s[i - 1])
            } else {
                selectObject(s.last())
            }
        }
    }

    private fun selectableObjectsAt(wv: Vec2d): List<EditorObject> {
        return editorObjectComponent.selectableObjectsAt(wv.x.toInt(), wv.y.toInt())
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
