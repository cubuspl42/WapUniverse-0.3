package io.github.wapuniverse.view

import io.github.wapuniverse.editor.makeEditor
import javafx.scene.Scene
import javafx.stage.Stage


private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0

class ApplicationController(stage: Stage) {
    private val editor = makeEditor()

    private val editorController = EditorController(editor)

    init {
        stage.title = INITIAL_TITLE
        stage.scene = Scene(editorController.worldScene, INITIAL_WIDTH, INITIAL_HEIGHT)
        stage.show()
    }
}
