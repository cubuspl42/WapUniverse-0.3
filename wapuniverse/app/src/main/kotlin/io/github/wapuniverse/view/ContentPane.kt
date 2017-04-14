package io.github.wapuniverse.view

import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

class ContentPane : StackPane() {
    init {
        val clipRect = Rectangle()
        clipRect.widthProperty().bind(widthProperty())
        clipRect.heightProperty().bind(heightProperty())
        clip = clipRect
    }
}
