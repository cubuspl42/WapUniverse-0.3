package io.github.wapuniverse.view

import io.github.wapuniverse.editor.TileLayer
import javafx.scene.canvas.Canvas


private val levelIndex = 1

private val width = 4
private val height = 4
private val tileWidth = 64.0

class SceneView(
        private val canvas: Canvas,
        private val imageMap: ImageMap,
        private val tileLayer: TileLayer
) {
    fun render() {
        for (i in 0..height-1) {
            for (j in 0..width-1) {
                val t = tileLayer.getTile(i, j)
                val img = imageMap.findTileImage(levelIndex, tileLayer.imageSet, t)
                val x = j * tileWidth
                val y = i * tileWidth
                canvas.graphicsContext2D.drawImage(img, x, y)
            }
        }
    }
}
