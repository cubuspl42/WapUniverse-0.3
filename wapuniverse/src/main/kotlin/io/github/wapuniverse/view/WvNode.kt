package io.github.wapuniverse.view

import javafx.scene.canvas.GraphicsContext


abstract class WvNode {
    var z = 0

    abstract fun draw(gc: GraphicsContext)
}
