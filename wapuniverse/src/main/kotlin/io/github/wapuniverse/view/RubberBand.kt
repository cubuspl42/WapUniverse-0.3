package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toPoint2D
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.util.logging.Logger


private val resizePadding = 8.0

class RubberBand(var offset: Vec2d, var width: Double, var height: Double) : SceneItem() {
    constructor(rect: Rectangle2D) : this(Vec2d(rect.minX, rect.minY), rect.width, rect.height)

    private val logger = Logger.getLogger(javaClass.simpleName)

    val dragged = Signal<Rectangle2D>()

    val resized = Signal<Rectangle2D>()

    override val boundingBox: Rectangle2D
        get() = Rectangle2D(offset.x, offset.y, width, height)

    private var isDragged = false

    private var dragOffset = Vec2d()

    private var isResized = false

    private var resizeDir = Vec2d()

    private fun handleResize(dx: Int, dy: Int) {
        isResized = true
        resizeDir = Vec2d(dx.toDouble(), dy.toDouble())
    }

    private fun handleDrag(wv: Vec2d) {
        isDragged = true
        dragOffset = wv - offset
    }

    private fun invTr(x: Double, y: Double): Vec2d {
        return scene!!.invTransform.transform(x, y).toVec2d()
    }

    override fun onMousePressed(x: Double, y: Double): EventHandlingStatus {
        val wv = invTr(x, y)
        val wx = wv.x
        val wy = wv.y
        val p = (wv - offset).toPoint2D()
        val pd = resizePadding
        val w = boundingBox.width
        val h = boundingBox.height

        logger.info(wv.toString() + offset.toString() + p.toString())

        val rx =
                if (p in Rectangle2D(w - pd, 0.0, pd, h)) 1
                else if (p in Rectangle2D(0.0, 0.0, pd, h)) -1
                else 0

        val ry =
                if (p in Rectangle2D(0.0, h - pd, w, pd)) 1
                else if (p in Rectangle2D(0.0, 0.0, w, pd)) -1
                else 0

        if (rx == 0 && ry == 0) {
            handleDrag(wv)
        } else {
            handleResize(rx, ry)
        }

        scene!!.addMouseReleasedHandler(this)
        scene!!.addMouseDraggedHandler(this)
        return EVENT_HANDLED
    }

    override fun onMouseReleased(x: Double, y: Double) {
        isDragged = false
        isResized = false
        scene!!.removeMouseReleasedHandler(this)
        scene!!.removeMouseDraggedHandler(this)
    }

    override fun onMouseDragged(x: Double, y: Double) {
        val wv = invTr(x, y)
        val wx = wv.x
        val wy = wv.y
        if (isDragged) {
            val newOffset = wv - dragOffset
            dragged._emit(Rectangle2D(newOffset.x, newOffset.y, width, height))
        } else if (isResized) {
            val bb = boundingBox
            val minX = if (resizeDir.x < 0) Math.min(wx, bb.maxX) else bb.minX
            val minY = if (resizeDir.y < 0) Math.min(wy, bb.maxY) else bb.minY
            val maxX = if (resizeDir.x > 0) Math.max(wx, bb.minX) else bb.maxX
            val maxY = if (resizeDir.y > 0) Math.max(wy, bb.minY) else bb.maxY
            val w = maxX - minX
            val h = maxY - minY
            resized._emit(Rectangle2D(minX, minY, maxX - minX, maxY - minY))
        }
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color.RED
        gc.strokeRect(offset.x, offset.y, width, height)
    }
}
