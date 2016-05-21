package io.github.wapuniverse.controller

import io.github.wapuniverse.view.SceneView
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import tornadofx.Controller


private val IMAGE_PATH = "file:/home/kuba/tmp/CLAW/CLAW/IMAGES/FRAME059.png"

class MainController(
        private val sceneCanvas: Canvas
) : Controller() {
    private val image = Image(IMAGE_PATH)
    private val sceneView = SceneView(sceneCanvas, image)

    init {
        sceneCanvas.setOnMouseDragged {
            sceneView.render()
        }
    }
}
