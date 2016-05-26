package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.util.logging.Logger

class RubberBand(var offset: Vec2d, var width: Double, var height: Double) : SceneItem() {
    constructor(rect: Rectangle2D) : this(Vec2d(rect.minX, rect.minY), rect.width, rect.height)

    private val logger = Logger.getLogger(javaClass.simpleName)

    val dragged = Signal<Rectangle2D>()

    override val boundingBox: Rectangle2D
        get() = Rectangle2D(offset.x, offset.y, width, height)

    var isDragged = false

    private var dragOffset = Vec2d()

    override fun onMousePressed(x: Double, y: Double): EventHandlingStatus {
        isDragged = true
        dragOffset = Vec2d(x, y) - offset
        scene!!.addMouseReleasedHandler(this)
        scene!!.addMouseDraggedHandler(this)
        return EVENT_HANDLED
    }

    override fun onMouseReleased(x: Double, y: Double) {
        isDragged = false
        scene!!.removeMouseReleasedHandler(this)
        scene!!.removeMouseDraggedHandler(this)
    }

    override fun onMouseDragged(x: Double, y: Double) {
        val newOffset = Vec2d(x, y) - dragOffset
        dragged._emit(Rectangle2D(newOffset.x, newOffset.y, width, height))
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color.RED
        gc.strokeRect(offset.x, offset.y, width, height)
    }
}
