package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas

class ResizableCanvas : Canvas() {
    override fun isResizable() = true

    override fun prefWidth(height: Double): Double {
        return getWidth()
    }

    override fun prefHeight(width: Double): Double {
        return getHeight()
    }
}