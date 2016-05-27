package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.makeMatrix
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
import io.github.wapuniverse.view.RubberBand
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import java.net.URL
import java.util.*


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

private fun addAlphaTileMatrix(tileLayer: TileLayer, m: Int, n: Int, x: Int, y: Int, vararg tiles: AlphaTile) {
    val mx = makeMatrix(m, n, tiles.asList())
    val a = AlphaTileMatrix()
    a.offset = Vec2i(x, y)
    a.alphaTiles = mx
    tileLayer.addMatrix(a)
}

class MainController : Initializable {
    @FXML
    private lateinit var root: Region

    @FXML
    private lateinit var sceneCanvas: Canvas

    private lateinit var sceneView: SceneView

    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromPath(imageSetDatabase, IMAGE_BASE_DIR_PATH)

    private val alphaTileMapper = AlphaTileMapper(level1FormulaGroup)

    private val tileLayer = TileLayer(alphaTileMapper, "ACTION")

    private val smartObjectComponent = SmartObjectComponent()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        sceneCanvas.widthProperty().bind(root.widthProperty())
        sceneCanvas.heightProperty().bind(root.heightProperty())
        sceneCanvas.isFocusTraversable = true

        sceneView = SceneView(sceneCanvas, imageMap, tileLayer)
        sceneView.render()

        val gc = sceneCanvas.graphicsContext2D
        gc.fill = Color.BLACK
        gc.fillRect(50.0, 50.0, 100.0, 100.0)

        SmartObjectPresenter(smartObjectComponent, sceneView)

        sceneCanvas.setOnKeyPressed {
            smartObjectComponent.addSmartObject(SmartObject(tileLayer, 6, 6))
        }
    }
}
