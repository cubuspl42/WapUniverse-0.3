package io.github.wapuniverse.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import java.net.URL
import java.util.*
import java.util.logging.Logger

class MainWindowController : Initializable {
    private val log = Logger.getLogger(this.javaClass.name)

    @FXML
    private lateinit var borderPane: BorderPane

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val canvas = makeResizableCanvas()
        borderPane.children.add(canvas)
        canvas.widthProperty().bind(borderPane.widthProperty())
        canvas.heightProperty().bind(borderPane.heightProperty())
    }

    private fun makeResizableCanvas() = object : ResizableCanvas() {
        override fun draw(gc: GraphicsContext, width: Double, height: Double) {
            log.info("($width, $height)")

            gc.clearRect(0.0, 0.0, width, height)

            gc.stroke = Color.RED
            gc.strokeLine(0.0, 0.0, width, height)
            gc.strokeLine(0.0, height, width, 0.0)
        }

    }

    @FXML
    private fun handleNewLevel(ev: ActionEvent) {

    }
}
