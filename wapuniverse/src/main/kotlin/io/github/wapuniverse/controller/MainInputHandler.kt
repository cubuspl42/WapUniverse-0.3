package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.Entity
import io.github.wapuniverse.editor.EntityComponent
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.*
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import javafx.geometry.Rectangle2D
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import java.util.logging.Logger


private val mainInputHandlerPriority = 0

class MainInputHandler(
        private val root: Node,
        private val sBoxComponent: SBoxComponent,
        private val sceneView: SceneView) : InputHandler {

    private val logger = Logger.getLogger(javaClass.simpleName)

    var dragRays: List<Pair<Entity, Vec2d>>? = null

    var selectionArea: Rectangle2D? = null

    private fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    init {
        sceneView.addItem(object : SceneItem() {
            init {
                z = 100000.0
            }

            override val boundingBox: Rectangle2D
                get() = selectionArea ?: Rectangle2D(0.0, 0.0, 0.0, 0.0)

            override fun render(gc: GraphicsContext) {
                val sa = selectionArea ?: return
                val c = Color(0.6, 0.0, 0.6, 0.5)
                gc.fill = c
                gc.fillRect(sa.minX, sa.minY, sa.width, sa.height)
            }
        })
    }

    override fun onMousePressed(ev: MouseEvent): EventHandlingStatus {
        val wv = invTr(ev.x, ev.y)
        val sBoxes = sBoxComponent.query(wv)
        if (sBoxes.any { it.isSelected }) {
            val selectedEntities = sBoxComponent.sBoxes
                    .filter { it.isSelected }
                    .map { it.entity }
            dragRays = selectedEntities
                    .map { Pair(it, wv - it.position.toVec2d()) }
        } else {
            sBoxComponent.unselectAll()
            selectionArea = Rectangle2D(wv.x, wv.y, 1.0, 1.0)
        }
        return EVENT_HANDLED
    }

    override fun onMouseReleased(ev: MouseEvent): EventHandlingStatus {
        if (selectionArea != null) {
            sBoxComponent.select(selectionArea!!)
        } else if (ev.isStillSincePress) {
            selectNextObject(invTr(ev.x, ev.y))
        }
        selectionArea = null
        dragRays = null
        return EVENT_HANDLED
    }

    override fun onMouseMoved(ev: MouseEvent): EventHandlingStatus {
        if (!ev.isPrimaryButtonDown) {
            sBoxComponent.hover(invTr(ev.x, ev.y))
        }

        val wv = invTr(ev.x, ev.y)
        val sBoxes = sBoxComponent.query(wv)
        if (sBoxes.any { it.isSelected && it.boundingRect.contains(wv.toPoint2D()) }) {
            root.cursor = Cursor.MOVE
        } else {
            root.cursor = Cursor.DEFAULT
        }


        return EVENT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        val wv = invTr(ev.x, ev.y)
        if (selectionArea != null) {
            val minX = selectionArea!!.minX
            val minY = selectionArea!!.minY
            val w = Math.max(wv.x - minX, 0.0)
            val h = Math.max(wv.y - minY, 0.0)
            selectionArea = Rectangle2D(minX, minY, w, h)
        } else if (dragRays != null) {
            dragRays!!.forEach {
                val (e, r) = it
                val p = wv - r
                e.position = p.toVec2i()
            }
        }
        return EVENT_HANDLED
    }

    override val priority: Int
        get() = mainInputHandlerPriority

    private fun selectNextObject(wv: Vec2d) {
        val sBoxes = sBoxComponent.query(wv)
        if (sBoxes.isNotEmpty()) {
            val i = sBoxes.indexOfFirst { it.isSelected }
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
    }

    private fun unselect(sBox: SBox?) {
        sBox?.isSelected = false
    }

    private fun hover(sBox: SBox) {
        sBox.isHovered = true
    }

    private fun unhover(sBox: SBox) {
        sBox.isHovered = false
    }
}
