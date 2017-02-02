package io.github.wapuniverse.view

import io.github.wapuniverse.wap32.WwdPlane
import javafx.scene.canvas.GraphicsContext

private val T = 64.0

class WvTileMatrixNode(val plane: WwdPlane, private val imageMap: ImageMap) : WvNode() {
    override fun draw(gc: GraphicsContext) {
        val levelIndex = 1 // FIXME
        val tileImageSet = "ACTION" // FIXME
        for (i in (0..plane.tilesHigh - 1)) {
            for (j in (0..plane.tilesWide - 1)) {
                val tileIdx = plane.getTile(i, j)
                if (tileIdx > 0) {
                    val tImg = imageMap.findTileImage(levelIndex, tileImageSet, tileIdx)
                    gc.drawImage(tImg, j * T, i * T, T, T)
                }
            }
        }
    }
}
