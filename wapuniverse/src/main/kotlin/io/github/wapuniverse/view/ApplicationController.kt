package io.github.wapuniverse.view

import io.github.wapuniverse.loadImageSetDatabaseFromFile
import io.github.wapuniverse.util.getResourceAsStream
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.loadWwd
import javafx.scene.Scene
import javafx.stage.Stage

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"
private val WWD_PATH = "RETAIL01.WWD"
private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0


class ApplicationController(stage: Stage) {

    private val wwd = loadWwd()

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val sceneView = SceneView(wwd, imageMap)

    init {
        stage.title = INITIAL_TITLE
        stage.scene = Scene(sceneView, INITIAL_WIDTH, INITIAL_HEIGHT)
        stage.show()
    }

    private fun loadWwd(): Wwd {
        getResourceAsStream(WWD_PATH).use {
            val wwd = loadWwd(it)
            return wwd
        }
    }
}
