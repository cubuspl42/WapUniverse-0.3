package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

abstract class ResizableCanvas : Canvas() {
    init {
        widthProperty().addListener({ evt -> draw(graphicsContext2D, width, height) })
        heightProperty().addListener({ evt -> draw(graphicsContext2D, width, height) })
    }

    override fun isResizable() = true

    override fun prefWidth(height: Double): Double {
        return width
    }

    override fun prefHeight(width: Double): Double {
        return height
    }

    abstract fun draw(gc: GraphicsContext, width: Double, height: Double)
}
