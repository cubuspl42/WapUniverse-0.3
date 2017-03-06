package io.github.wapuniverse.view

import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.common.util.getResourceAsStream
import io.github.wapuniverse.common.wap32.loadWwd
import io.github.wapuniverse.editor.Editor
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage


private val WWD_PATH = "RETAIL0${CFG_LEVEL_INDEX}.WWD"
private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0

class ApplicationController(stage: Stage) {
    private val editor = makeEditor()

    private val sceneVBox = VBox()

    private val editorController = EditorController(editor, sceneVBox)

    init {
        stage.title = INITIAL_TITLE
        val scene = Scene(sceneVBox, INITIAL_WIDTH, INITIAL_HEIGHT)
        stage.scene = scene
        stage.show()

        editorController.worldScene.requestFocus()
    }

    private fun makeEditor(): Editor {
        getResourceAsStream(WWD_PATH).use {
            val wwd = loadWwd(it)
            return Editor.create(wwd)
        }
    }
}
