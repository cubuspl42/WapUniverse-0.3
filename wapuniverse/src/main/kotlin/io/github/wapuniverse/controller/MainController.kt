package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.makeMatrix
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import io.github.wapuniverse.lsd.level1.Level1AlphaTile.*
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

    private fun initWorld() {
        addAlphaTileMatrix(tileLayer, 3, 5, 0, 0,
                BLOCK_TL,   BLOCK_T,    BLOCK_T,    BLOCK_TR_1,     BLOCK_TR_2,
                BLOCK_L,    BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R,
                BLOCK_BL,   BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R
        )
        addAlphaTileMatrix(tileLayer, 3, 5, 3, 1,
                BLOCK_TL,   BLOCK_T,    BLOCK_T,    BLOCK_TR_1,     BLOCK_TR_2,
                BLOCK_L,    BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R,
                BLOCK_BL,   BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R
        )
        addAlphaTileMatrix(tileLayer, 3, 7, 6, 0,
                BLOCK_TL,   BLOCK_T,    BLOCK_T,    BLOCK_T,    BLOCK_T,    BLOCK_TR_1,     BLOCK_TR_2,
                BLOCK_L,    BLOCK_M,    BLOCK_M,    BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R,
                BLOCK_BL,   BLOCK_M,    BLOCK_M,    BLOCK_M,    BLOCK_M,    BLOCK_M,        BLOCK_R
        )
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        sceneCanvas.widthProperty().bind(root.widthProperty())
        sceneCanvas.heightProperty().bind(root.heightProperty())
        sceneCanvas.isFocusTraversable = true

        sceneView = SceneView(sceneCanvas, imageMap, tileLayer)
        sceneView.render()

        val gc = sceneCanvas.graphicsContext2D
        gc.fill = Color.BLACK
        gc.fillRect(50.0, 50.0, 100.0, 100.0)

        sceneCanvas.setOnMouseDragged {
            sceneView.render()
        }

        initWorld()
    }
}
