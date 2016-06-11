package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.Entity
import io.github.wapuniverse.editor.EntityComponent
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.EventHandlingStatus
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.SBox
import io.github.wapuniverse.view.SBoxComponent
import io.github.wapuniverse.view.SceneView
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import java.util.logging.Logger


private val mainInputHandlerPriority = 0

class MainInputHandler(
        private val root: Node,
        private val sBoxComponent: SBoxComponent,
        private val entityComponent: EntityComponent,
        private val sceneView: SceneView) : InputHandler {

    private val logger = Logger.getLogger(javaClass.simpleName)

    var dragOffset: Vec2d? = Vec2d()

    var selectedSBox: SBox? = null

    private fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    override fun onMousePressed(ev: MouseEvent): EventHandlingStatus {
        val wv = invTr(ev.x, ev.y)
        if (selectedSBox != null && selectedSBox!!.boundingRect.contains(wv.x, wv.y)) {
            val selectedEntity = selectedSBox!!.entity
            dragOffset = wv - selectedEntity.position.toVec2d()
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
        sBoxComponent
        if (!ev.isPrimaryButtonDown) {
            val sBoxes = sBoxComponent.query(invTr(ev.x, ev.y))
            sBoxes.forEach { hover(it) }
        }

        val wv = invTr(ev.x, ev.y)
        if (selectedSBox?.boundingRect?.contains(wv.x, wv.y) == true) {
            root.cursor = Cursor.MOVE
        } else {
            root.cursor = Cursor.DEFAULT
        }

        return EVENT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        if (selectedSBox != null && dragOffset != null) {
            val wv = invTr(ev.x, ev.y)
            val p = wv - dragOffset!!
            val selectedEntity = selectedSBox!!.entity
            selectedEntity.position = p.toVec2i()
        }
        return EVENT_HANDLED
    }

    override val priority: Int
        get() = mainInputHandlerPriority

    private fun selectNextObject(wv: Vec2d) {
        val sBoxes = sBoxComponent.query(wv)
        if (sBoxes.isNotEmpty()) {
            val i = sBoxes.indexOf(selectedSBox)
            if (i > 0) {
                select(sBoxes[i - 1])
            } else {
                select(sBoxes.last())
            }
        }
    }

    private fun select(sBox: SBox) {
        sBoxComponent.unselectAll()
        sBox.isSelected = true
        selectedSBox = sBox
    }

    private fun unselect(sBox: SBox?) {
        sBox?.isSelected = false
        selectedSBox = null
    }

    private fun hover(sBox: SBox) {
        sBox.isHovered = true
    }

    private fun unhover(sBox: SBox) {
        sBox.isHovered = false
    }
}
