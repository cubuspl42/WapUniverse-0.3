package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.SmartObject
import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.view.EventHandlingStatus.EVENT_NOT_HANDLED
import javafx.event.EventType
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Affine
import java.util.*


private val levelIndex = 1

private val renderRadius = 24

enum class EventHandlingStatus {
    EVENT_HANDLED,
    EVENT_NOT_HANDLED
}

abstract class SceneItem() {
    internal var scene: SceneView? = null

    abstract val boundingBox: Rectangle2D

    var z = 0.0

    open fun render(gc: GraphicsContext) {
    }
}

class SceneView(
        private val canvas: Canvas,
        private val imageMap: ImageMap,
        private val tileLayer: TileLayer
) {
    private val items = HashSet<SceneItem>()

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

    /**
     * Items at (x, y), from front to back.
     */
    fun itemsAt(x: Double, y: Double): List<SceneItem> {
        val wp = invTransform.transform(x, y)
        return items
                .filter { wp in it.boundingBox }
                .sortedBy { -it.z }
    }

    fun render() {
        val gc = canvas.graphicsContext2D
        gc.transform = Affine()

        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        gc.transform = transform

        for (i in -renderRadius..renderRadius) {
            for (j in -renderRadius..renderRadius) {
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
