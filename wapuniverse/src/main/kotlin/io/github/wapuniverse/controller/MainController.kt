package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.makeMatrix
import io.github.wapuniverse.view.SceneView
import io.github.wapuniverse.view.loadImageMapFromPath
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

private fun addAlphaTileMatrix(tileLayer: TileLayer, m: Int, n: Int, x: Int, y: Int, vararg tiles: AlphaTile) {
    val mx = makeMatrix(m, n, tiles.asList())
    val a = AlphaTileMatrix()
    a.offset = Vec2i(x, y)
    a.alphaTiles = mx
    tileLayer.addMatrix(a)
}

class MainController(private val sceneCanvas: Canvas) {
    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromPath(imageSetDatabase, IMAGE_BASE_DIR_PATH)

    private val alphaTileMapper = AlphaTileMapper(level1FormulaGroup)

    private val tileLayer = TileLayer(alphaTileMapper, "ACTION")

    private val sceneView = SceneView(sceneCanvas, imageMap, tileLayer)

    private val smartObjectComponent = SmartObjectComponent()

    init {

        sceneView.render()

        val gc = sceneCanvas.graphicsContext2D
        gc.fill = Color.BLACK
        gc.fillRect(50.0, 50.0, 100.0, 100.0)

        SmartObjectPresenter(smartObjectComponent, sceneView)

        sceneCanvas.setOnKeyPressed { ev ->
            val script = when (ev.text) {
                "1" -> blockScript
                "2" -> ladderScript
                "3" -> spikesScript
                "4" -> columnScript
                "5" -> platformScript
                else -> null
            }
            if (script != null) {
                smartObjectComponent.addSmartObject(SmartObject(tileLayer, script))
            }
        }
    }
}
