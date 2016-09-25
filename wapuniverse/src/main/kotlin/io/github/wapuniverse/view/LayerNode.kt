package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.Layer
import io.github.wapuniverse.core.tileWidth
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.toVec2i
import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine

class LayerNode(
        private val layer: Layer,
        private val tileImageCache: TileImageCache
) {
    private val INVISIBLE_TILE_ID = -1

    private val imageNodes = hashSetOf<ImageNode>()
    private val rectNodes = hashSetOf<RectNode>()

    val z = 0

    fun drawTile(gc: GraphicsContext, i: Int, j: Int) {
        val t = layer.getTile(i, j)
        if (t != INVISIBLE_TILE_ID) {
            tileImageCache.getTileImage(t)?.let { image ->
                val y = i * tileWidth
                val x = j * tileWidth
                gc.drawImage(image, x, y)
            }
        }
    }

    private fun drawTiles(gc: GraphicsContext, invTransform: Affine, viewport: Vec2i) {
        val v0 = invTransform.transform(0.0, 0.0).toVec2i()
        val v1 = invTransform.transform(viewport.width.toDouble(), viewport.height.toDouble()).toVec2i()
        val t0 = v0 / tileWidth - Vec2i.UNIT
        val t1 = v1 / tileWidth

        for (i in t0.y..t1.y) {
            for (j in t0.x..t1.x) {
                drawTile(gc, i, j)
            }
        }
    }

    fun draw(gc: GraphicsContext, camera: Vec2d, scale: Double, viewport: Vec2i) {
        val ap = makeTransforms(camera, scale)
        val imageNodesByZ = imageNodes.sortedBy { it.z }
        gc.transform = ap.transform
        imageNodesByZ.filter { it.z < 0 }.forEach { it.draw(gc) }
        drawTiles(gc, ap.invTransform, viewport)
        imageNodesByZ.filter { it.z >= 0 }.forEach { it.draw(gc) }
    }

    fun drawOverlay(gc: GraphicsContext, camera: Vec2d, scale: Double, viewport: Vec2i) {
        val ap = makeTransforms(camera, scale)
        gc.transform = Affine()
        rectNodes.forEach { it.draw(gc, ap.transform) }
    }
}
