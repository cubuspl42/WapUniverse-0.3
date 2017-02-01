package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import javafx.animation.AnimationTimer
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.layout.BorderPane
import java.net.URL
import java.util.*
import java.util.logging.Logger

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"

class MainWindowController : Initializable {
    private val log = Logger.getLogger(this.javaClass.name)

    @FXML
    private lateinit var borderPane: BorderPane

    private lateinit var canvas: ResizableCanvas

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val viewportVn = ViewportVn()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        canvas = makeResizableCanvas()
        borderPane.children.add(canvas)

        canvas.widthProperty().bind(borderPane.widthProperty())
        canvas.heightProperty().bind(borderPane.heightProperty())

        canvas.setOnMouseMoved { ev -> viewportVn.onMouseMoved(ev) }
        canvas.setOnMouseDragged { ev -> viewportVn.onMouseDragged(ev) }
        canvas.setOnMousePressed { ev -> viewportVn.onMousePressed(ev) }
        canvas.setOnMouseReleased { ev -> viewportVn.onMouseReleased(ev) }
        canvas.setOnScroll { ev -> viewportVn.onScroll(ev) }

        (object : AnimationTimer() {
            override fun handle(t: Long) {
                viewportVn.draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
            }
        }).start()
    }

    private fun makeResizableCanvas() = object : ResizableCanvas() {
        override fun draw() {
            viewportVn.draw(canvas.graphicsContext2D, Vec2d(canvas.width, canvas.height))
        }
    }

    @FXML
    private fun handleNewLevel(ev: ActionEvent) {

    }
}
