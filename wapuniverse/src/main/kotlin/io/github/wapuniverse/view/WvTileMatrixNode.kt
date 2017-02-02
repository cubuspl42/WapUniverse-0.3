package io.github.wapuniverse.view

import io.github.wapuniverse.util.Vec2i
import io.github.wapuniverse.world.World
import javafx.scene.canvas.GraphicsContext

private val T = 64.0

class WvTileMatrixNode(val world: World, private val imageMap: ImageMap) : WvNode() {
    override fun draw(gc: GraphicsContext) {
        val levelIndex = 1 // FIXME
        val tileImageSet = "ACTION" // FIXME
        val bounds = world.tilesBounds
        for (i in (bounds.minY..bounds.maxY - 1)) {
            for (j in (bounds.minX..bounds.maxX - 1)) {
                val tileIdx = world.getTile(Vec2i(j, i))
                if (tileIdx > 0) {
                    val tImg = imageMap.findTileImage(levelIndex, tileImageSet, tileIdx)
                    gc.drawImage(tImg, j * T, i * T, T, T)
                }
            }
        }
    }
}
