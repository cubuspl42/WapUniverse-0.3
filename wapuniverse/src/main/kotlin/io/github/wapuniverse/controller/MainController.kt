package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.loadImageSetDatabaseFromFile
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import java.net.URL
import java.util.*


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

class MainController : Initializable {
    @FXML
    private lateinit var root: Region

    @FXML
    private lateinit var sceneCanvas: Canvas

    private lateinit var sceneView: SceneView

    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromPath(imageSetDatabase, IMAGE_BASE_DIR_PATH)

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        sceneCanvas.widthProperty().bind(root.widthProperty())
        sceneCanvas.heightProperty().bind(root.heightProperty())
        sceneCanvas.isFocusTraversable = true

        val image = imageMap.findObjectImage(1, "LEVEL_OFFICER", -1)!!
        sceneView = SceneView(sceneCanvas, image)
        sceneView.render()

        val gc = sceneCanvas.graphicsContext2D
        gc.fill = Color.BLACK
        gc.fillRect(50.0, 50.0, 100.0, 100.0)

        sceneCanvas.setOnMouseDragged {
            sceneView.render()
        }
    }
}
