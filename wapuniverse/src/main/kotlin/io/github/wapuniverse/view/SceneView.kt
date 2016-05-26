package io.github.wapuniverse.view

import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_HANDLED
import javafx.event.EventType
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
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

    init {
        eventHandlers[MouseEvent.MOUSE_RELEASED] = mutableListOf()
        eventHandlers[MouseEvent.MOUSE_MOVED] = mutableListOf()
        eventHandlers[MouseEvent.MOUSE_DRAGGED] = mutableListOf()

        canvas.setOnMousePressed { ev ->
            onMousePressed(ev.x, ev.y)
        }

        canvas.setOnMouseReleased { ev ->
            onMouseEvent(MouseEvent.MOUSE_RELEASED, ev.x, ev.y, SceneItem::onMouseReleased)
        }

        canvas.setOnMouseMoved { ev ->
            onMouseEvent(MouseEvent.MOUSE_MOVED, ev.x, ev.y, SceneItem::onMouseMoved)
        }

        canvas.setOnMouseDragged { ev ->
            onMouseEvent(MouseEvent.MOUSE_DRAGGED, ev.x, ev.y, SceneItem::onMouseDragged)
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
        return items
                .filter { Point2D(x, y) in it.boundingBox }
                .sortedBy { -it.z }
    }

    private fun onMousePressed(x: Double, y: Double) {
        for (item in itemsAt(x, y)) {
            if (item.onMousePressed(x, y) == EVENT_HANDLED) {
                return
            } else continue
        }
    }

    private fun onMouseEvent(
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
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

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
