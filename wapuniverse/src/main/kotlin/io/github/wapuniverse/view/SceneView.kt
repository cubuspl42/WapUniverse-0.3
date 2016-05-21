package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image


class SceneView(
        private val canvas: Canvas,
        private val image: Image
) {
    fun render() {
        canvas.graphicsContext2D.drawImage(image, 0.0, 0.0)
    }
}
