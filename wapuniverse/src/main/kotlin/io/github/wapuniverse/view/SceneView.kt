package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.INVISIBLE_TILE_ID
import io.github.wapuniverse.editor.ImageSetDatabase
import io.github.wapuniverse.editor.TileLayer
import io.github.wapuniverse.editor.tileWidth
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.toVec2i
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import java.util.*


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

class TileImageCache(
        levelIndex: Int, tileImageSetName: String, imageSetDatabase: ImageSetDatabase, imageMap: ImageMap) {

    val cache: Array<Image?>

    init {
        val tileIndices = imageSetDatabase.listTiles(levelIndex, tileImageSetName)!!
        val min = 0
        val max = tileIndices.max()!!

        cache = Array(max + 1, { i ->
            imageMap.findTileImage(levelIndex, tileImageSetName, i)
        })
    }

    fun getTileImage(tileIndex: Int): Image? {
        if (tileIndex >= 0 && tileIndex < cache.size) {
            return cache[tileIndex]
        } else return null
    }
}

private val helpMessage = """
WapUniverse Alpha 0.3 (PREVIEW)

Press & drag Right Mouse Button to transform view
Click Left Mouse Button to select object
Drag & resize objects with Left Mouse Button
Press & drag Left Mouse Button to select multiple objects

Press `1` to spawn Block
Press `2` to spawn Ladder
Press `3` to spawn Spikes
Press `4` to spawn Column
Press `5` to spawn Platform
Press `Del` to delete selected objects

Choose File > Save to save world as .wwd
Drag .wwd file from File Explorer to load world
"""

class SceneView(
        levelIndex: Int,
        private val canvas: Canvas,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val tileLayer: TileLayer
) {
    private val items = HashSet<SceneItem>()

    private val tileImageCache = TileImageCache(levelIndex, tileLayer.imageSet, imageSetDatabase, imageMap)

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

        val v0 = invTransform.transform(0.0, 0.0).toVec2i()
        val v1 = invTransform.transform(canvas.width, canvas.height).toVec2i()
        val t0 = v0 / tileWidth
        val t1 = v1 / tileWidth

        gc.transform = transform

        for (i in t0.y - 1..t1.y) {
            for (j in t0.x - 1..t1.x) {
                val t = tileLayer.getTile(i, j)
                if (t != INVISIBLE_TILE_ID) {
                    // val img = imageMap.findTileImage(levelIndex, tileLayer.imageSet, t)
                    val img = tileImageCache.getTileImage(t) ?: continue
                    val x = j * tileWidth
                    val y = i * tileWidth
                    gc.drawImage(img, x, y)
                }
            }
        }

        items
                .sortedBy { it.z }
                .forEach { it.render(gc) }

        gc.transform = Affine()
        gc.fill = Color.BLACK
        gc.fillText(helpMessage, 16.0, 16.0)
    }
}
