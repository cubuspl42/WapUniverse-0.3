package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.times
import io.github.wapuniverse.utils.toVec2d
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import javafx.event.EventType
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Affine
import java.util.*


private val levelIndex = 1

private val width = 32
private val height = 32

enum class EventHandlingStatus {
    EVENT_HANDLED,
    EVENT_NOT_HANDLED
}

abstract class SceneItem {
    internal var scene: SceneView? = null

    abstract val boundingBox: Rectangle2D

    var z = 0.0
        protected set

    open fun onMouseMoved(x: Double, y: Double) {
    }

    open fun onMousePressed(x: Double, y: Double): EventHandlingStatus {
        return EventHandlingStatus.EVENT_NOT_HANDLED
    }

    open fun onMouseDragged(x: Double, y: Double) {
    }

    open fun onMouseReleased(x: Double, y: Double) {
    }

    open fun render(gc: GraphicsContext) {
    }
}

class SceneView(
        private val canvas: Canvas,
        private val imageMap: ImageMap,
        private val tileLayer: TileLayer
) {
    private val items = HashSet<SceneItem>()

    private val eventHandlers = HashMap<EventType<MouseEvent>, MutableList<SceneItem>>()

    private var isDragged = false

    private var dragAnchor = Vec2d(0.0, 0.0)

    var transform = Affine()
        private set

    var invTransform = Affine()
        private set

    private fun recalculateTransform() {
        transform = Affine()
        transform.appendTranslation(-cameraOffset.x, -cameraOffset.y)
        transform.appendScale(scale, scale)
        invTransform = transform.createInverse()
    }

    var cameraOffset = Vec2d()
        set(value) {
            field = value
            recalculateTransform()
        }

    var scale = 1.0
        set(value) {
            field = value
            recalculateTransform()
        }

    init {
        eventHandlers[MouseEvent.MOUSE_RELEASED] = mutableListOf()
        eventHandlers[MouseEvent.MOUSE_MOVED] = mutableListOf()
        eventHandlers[MouseEvent.MOUSE_DRAGGED] = mutableListOf()

        canvas.setOnMousePressed { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                propagateMousePressed(ev.x, ev.y)
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = true
                dragAnchor = invTransform.transform(ev.x, ev.y).toVec2d()
            }
        }

        canvas.setOnMouseReleased { ev ->
            if (ev.button == MouseButton.PRIMARY) {
                propagateMouseEvent(MouseEvent.MOUSE_RELEASED, ev.x, ev.y, SceneItem::onMouseReleased)
            } else if (ev.button == MouseButton.SECONDARY) {
                isDragged = false
            }
        }

        canvas.setOnMouseMoved { ev ->
            propagateMouseEvent(MouseEvent.MOUSE_MOVED, ev.x, ev.y, SceneItem::onMouseMoved)
        }

        canvas.setOnMouseDragged { ev ->
            if (ev.isSecondaryButtonDown) {
                val mv = Vec2d(ev.x, ev.y)
                cameraOffset = dragAnchor - (mv * scale)
            } else if (ev.isPrimaryButtonDown) {
                propagateMouseEvent(MouseEvent.MOUSE_DRAGGED, ev.x, ev.y, SceneItem::onMouseDragged)
            }
            render()
        }
    }

    fun addItem(item: SceneItem) {
        assert(item.scene == null)
        item.scene = this
        items.add(item)
    }

    fun removeItem(item: SceneItem) {
        assert(item.scene!! == this)
        item.scene = null
        items.remove(item)
    }

    private fun itemsAt(x: Double, y: Double): List<SceneItem> {
        val wp = invTransform.transform(x, y)
        return items
                .filter { wp in it.boundingBox }
                .sortedBy { -it.z }
    }

    private fun propagateMousePressed(x: Double, y: Double) {
        for (item in itemsAt(x, y)) {
            if (item.onMousePressed(x, y) == EVENT_HANDLED) {
                return
            } else continue
        }
    }

    private fun propagateMouseEvent(
            type: EventType<MouseEvent>, x: Double, y: Double, f: (SceneItem, Double, Double) -> Unit) {
        eventHandlers[type]!!.toList().forEach { f(it, x, y) }
    }

    internal fun addMouseReleasedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_RELEASED]!!.add(handler)
    }

    internal fun removeMouseReleasedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_RELEASED]!!.remove(handler)
    }

    internal fun addMouseMovedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_MOVED]!!.add(handler)
    }

    internal fun removeMouseMovedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_RELEASED]!!.remove(handler)
    }

    internal fun addMouseDraggedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_DRAGGED]!!.add(handler)
    }

    internal fun removeMouseDraggedHandler(handler: SceneItem) {
        eventHandlers[MouseEvent.MOUSE_DRAGGED]!!.remove(handler)
    }

    fun render() {
        val gc = canvas.graphicsContext2D
        gc.transform = Affine()

        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        gc.transform = transform

        for (i in 0..height - 1) {
            for (j in 0..width - 1) {
                val t = tileLayer.getTile(i, j)
                val img = imageMap.findTileImage(levelIndex, tileLayer.imageSet, t)
                val x = j * tileWidth
                val y = i * tileWidth
                gc.drawImage(img, x, y)
            }
        }

        items
                .sortedBy { it.z }
                .forEach { it.render(gc) }
    }
}
