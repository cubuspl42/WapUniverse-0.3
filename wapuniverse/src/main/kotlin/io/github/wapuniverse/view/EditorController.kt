package io.github.wapuniverse.view

import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import javafx.scene.input.KeyCode

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"

class EditorController(private val editor: Editor) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val world = editor.world

    val worldScene = DScene()

    private val stPlane = StPlane()

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val objectSelectionController = WObjectSelectionController(
            editor, worldScene, stPlane, imageSetDatabase, imageMap)

    init {
        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        world.objects.forEach { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        world.onObjectAdded.connect { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        worldScene.children.add(stPlane)

        worldScene.setOnKeyPressed { ev ->
            when {
                ev.code == KeyCode.DELETE -> objectSelectionController.destroySelectedObjects()
            }
        }

        worldScene.requestFocus()
    }
}
