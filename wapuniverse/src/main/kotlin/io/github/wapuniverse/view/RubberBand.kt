package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.controller.InputHandler
import io.github.wapuniverse.controller.InputHandlerImpl
import io.github.wapuniverse.core.SmartObject
import io.github.wapuniverse.utils.*
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_NOT_HANDLED
import javafx.geometry.Rectangle2D
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import java.util.logging.Logger


private val resizePadding = 8.0

class RubberBand(
        var offset: Vec2d,
        var width: Double,
        var height: Double,
        private val root: Node) : SceneItem(), InputHandler by InputHandlerImpl() {

    constructor(rect: Rectangle2D, root: Node) :
        this(Vec2d(rect.minX, rect.minY), rect.width, rect.height, root)

    private val logger = Logger.getLogger(javaClass.simpleName)

    val resized = Signal<Rectangle2D>()

    var color = Color.RED

    override val boundingBox: Rectangle2D
        get() = Rectangle2D(offset.x, offset.y, width, height)

    private var isResized = false

    private var resizeDir = Vec2i()

    private fun handleResize(r: Vec2i) {
        isResized = true
        resizeDir = r
    }

    private fun invTr(x: Double, y: Double) = scene!!.invTransform.transform(x, y).toVec2d()

    private fun classifyArea(wv: Vec2d): Vec2i {
        val p = (wv - offset).toPoint2D()
        val pd = resizePadding
        val w = boundingBox.width
        val h = boundingBox.height

        val rx =
                if (p in Rectangle2D(w - pd, 0.0, pd, h)) 1
                else if (p in Rectangle2D(0.0, 0.0, pd, h)) -1
                else 0

        val ry =
                if (p in Rectangle2D(0.0, h - pd, w, pd)) 1
                else if (p in Rectangle2D(0.0, 0.0, w, pd)) -1
                else 0

        return Vec2i(rx, ry)
    }

    override fun onMousePressed(ev: MouseEvent): EventHandlingStatus {
        val r = classifyArea(invTr(ev.x, ev.y))
        if (r.x == 0 && r.y == 0) {
            return EVENT_NOT_HANDLED
        } else {
            handleResize(r)
            return EVENT_HANDLED
        }
    }

    override fun onMouseReleased(ev: MouseEvent): EventHandlingStatus {
        if (isResized) {
            isResized = false
            return EVENT_HANDLED
        } else return EVENT_NOT_HANDLED
    }

    override fun onMouseMoved(ev: MouseEvent): EventHandlingStatus {
        val r = classifyArea(invTr(ev.x, ev.y))
        val cursor = when {
            r.x == -1 && r.y == -1 -> Cursor.NW_RESIZE
            r.x == 0 && r.y == -1 -> Cursor.N_RESIZE
            r.x == 1 && r.y == -1 -> Cursor.NE_RESIZE
            r.x == 1 && r.y == 0 -> Cursor.E_RESIZE
            r.x == 1 && r.y == 1 -> Cursor.SE_RESIZE
            r.x == 0 && r.y == 1 -> Cursor.S_RESIZE
            r.x == -1 && r.y == 1 -> Cursor.SW_RESIZE
            r.x == -1 && r.y == 0 -> Cursor.W_RESIZE
            else -> return EVENT_NOT_HANDLED
        }
        root.cursor = cursor
        return EVENT_HANDLED
    }

    override fun onMouseDragged(ev: MouseEvent): EventHandlingStatus {
        if (isResized) {
            val wv = invTr(ev.x, ev.y)
            val wx = wv.x
            val wy = wv.y
            val bb = boundingBox
            val minX = if (resizeDir.x < 0) Math.min(wx, bb.maxX) else bb.minX
            val minY = if (resizeDir.y < 0) Math.min(wy, bb.maxY) else bb.minY
            val maxX = if (resizeDir.x > 0) Math.max(wx, bb.minX) else bb.maxX
            val maxY = if (resizeDir.y > 0) Math.max(wy, bb.minY) else bb.maxY
            resized._emit(Rectangle2D(minX, minY, maxX - minX, maxY - minY))
            return EVENT_HANDLED
        } else return EVENT_NOT_HANDLED
    }

    override val priority = 1000

    override fun render(gc: GraphicsContext) {
        gc.stroke = color
        gc.strokeRect(offset.x, offset.y, width, height)
    }
}
