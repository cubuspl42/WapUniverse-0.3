package io.github.wapuniverse.view

import javafx.scene.canvas.Canvas
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.get


class MainView : View() {
    override val root: BorderPane by fxml()
    val sceneCanvas: Canvas by fxid()

    init {
        sceneCanvas.widthProperty().bind(root.widthProperty())
        sceneCanvas.heightProperty().bind(root.heightProperty())
        sceneCanvas.isFocusTraversable = true;

        title = messages["title"]
    }
}
