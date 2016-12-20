package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

abstract class ResizableCanvas : Canvas() {
    init {
        widthProperty().addListener({ evt -> draw() })
        heightProperty().addListener({ evt -> draw() })
    }

    override fun isResizable() = true

    override fun prefWidth(height: Double): Double {
        return width
    }

    override fun prefHeight(width: Double): Double {
        return height
    }

    abstract fun draw()
}
