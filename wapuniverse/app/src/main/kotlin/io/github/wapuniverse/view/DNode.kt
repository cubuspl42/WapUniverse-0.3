package io.github.wapuniverse.view

import javafx.scene.canvas.GraphicsContext


abstract class DNode {
    var z = 0

    abstract fun draw(gc: GraphicsContext)
}
