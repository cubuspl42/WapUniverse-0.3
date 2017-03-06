package io.github.wapuniverse.view

import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.loadImageSetDatabaseFromFile

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"

class EditorController(editor: Editor) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val world = editor.world

    val worldScene = DScene()

    private val stPlane = StPlane()

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    init {
        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        world.objects.forEach { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        world.objectAdded.connect { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        WObjectSelectionController(world, worldScene, stPlane, imageSetDatabase, imageMap)

        worldScene.children.add(stPlane)
    }

}

