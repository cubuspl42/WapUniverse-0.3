package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.minus
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

    private fun handleDrag(x: Double, y: Double) {
        isDragged = true
        dragOffset = Vec2d(x, y) - offset
    }

    override fun onMousePressed(x: Double, y: Double): EventHandlingStatus {
        val p = Point2D(x, y) - offset
        val pd = resizePadding
        val w = boundingBox.width
        val h = boundingBox.height

        when {
            p in Rectangle2D(w - pd, 0.0, pd, h) -> handleResize(1, 0)
            p in Rectangle2D(0.0, h - pd, w, pd) -> handleResize(0, 1)
            p in Rectangle2D(0.0, 0.0, pd, h) -> handleResize(-1, 0)
            p in Rectangle2D(0.0, 0.0, w, pd) -> handleResize(0, -1)
            else -> handleDrag(x, y)
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
        if (isDragged) {
            val newOffset = Vec2d(x, y) - dragOffset
            dragged._emit(Rectangle2D(newOffset.x, newOffset.y, width, height))
        } else if (isResized) {
            val bb = boundingBox
            val minX = if (resizeDir.x < 0) x else bb.minX
            val minY = if (resizeDir.y < 0) y else bb.minY
            val maxX = if (resizeDir.x > 0) x else bb.maxX
            val maxY = if (resizeDir.y > 0) y else bb.maxY
            val w = maxX - minX
            val h = maxY - minY

            if (w > 0 && h > 0) {
                resized._emit(Rectangle2D(minX, minY, maxX - minX, maxY - minY))
            }
        }
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color.RED
        gc.strokeRect(offset.x, offset.y, width, height)
    }
}
