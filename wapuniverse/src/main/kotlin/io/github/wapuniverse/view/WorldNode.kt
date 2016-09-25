package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.World
import io.github.wapuniverse.utils.Vec2i
import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine

class WorldNode(
        private val world: World
) {
    private val layerNodes = hashSetOf<LayerNode>()

    fun draw(gc: GraphicsContext, camera: Vec2d, scale: Double, viewport: Vec2i) {
        val layerNodesByZ = layerNodes.sortedBy { it.z }
        gc.transform = Affine()
        gc.clearRect(0.0, 0.0, viewport.width.toDouble(), viewport.height.toDouble())
        layerNodesByZ.forEach { it.draw(gc, camera, scale, viewport) }
        layerNodesByZ.forEach { it.drawOverlay(gc, camera, scale, viewport) }
    }

    fun addLayerNode(layerNode: LayerNode) = layerNodes.add(layerNode)

    fun removeLayerNode(layerNode: LayerNode) = layerNodes.remove(layerNode)
}
