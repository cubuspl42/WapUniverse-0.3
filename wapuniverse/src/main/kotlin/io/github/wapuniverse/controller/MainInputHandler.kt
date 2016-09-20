package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.Entity
import io.github.wapuniverse.core.WorldEditor
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
        private val node: Node,
        private val sceneView: SceneView,
        private val worldEditor: WorldEditor) : InputHandler {

    private val logger = Logger.getLogger(javaClass.simpleName)

    var dragRays: List<Pair<Entity, Vec2d>>? = null

    var selectionHandler: SelectionInputHandler? = null

    private fun invTr(x: Double, y: Double) = sceneView.invTransform.transform(x, y).toVec2d()

    init {
        sceneView.addItem(object : SceneItem() {
            init {
                z = 100000.0
            }

            private val selectionArea: Rectangle2D?
                get() = selectionHandler?.selection?.area?.toRectangle2D()

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
        selectionHandler = SelectionInputHandler(sceneView, worldEditor, wv.toVec2i())
        selectionHandler!!.onDispose.on {
            selectionHandler = null
        }
        return EVENT_HANDLED
    }

    override fun onMouseReleased(ev: MouseEvent): EventHandlingStatus {
        selectionHandler?.onMouseReleased(ev)
        dragRays = null
        return EVENT_HANDLED
    }

    override fun onMouseMoved(ev: MouseEvent): EventHandlingStatus {
        return EVENT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        val wv = invTr(ev.x, ev.y)
        selectionHandler?.onMouseDragged(ev)
        return EVENT_HANDLED
    }

    override val priority: Int
        get() = mainInputHandlerPriority

    private fun selectNextObject(wv: Vec2d) {
    }
}
