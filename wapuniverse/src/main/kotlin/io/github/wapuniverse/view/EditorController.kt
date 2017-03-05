package io.github.wapuniverse.view

import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.common.util.Vec2i
import io.github.wapuniverse.common.util.getResourceAsStream
import io.github.wapuniverse.common.wap32.loadWwd
import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.editor.World
import io.github.wapuniverse.loadImageSetDatabaseFromFile

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"
private val WWD_PATH = "RETAIL0${CFG_LEVEL_INDEX}.WWD"
private val SELECTION_NODE_Z = 1000000

class EditorController(editor: Editor) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val world = editor.world

    val worldScene = DScene()

    private val stPlane = StPlane()

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val wObjectSelectionController = WObjectSelectionController(world, worldScene, stPlane)

    init {
        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        world.objectAdded.connect { wObject ->
            WObjectController(wObject, worldScene, stPlane, imageMap, imageSetDatabase)
        }

        loadWorld(world)

        worldScene.children.add(stPlane)
    }

    private fun loadWorld(world: World) {
        getResourceAsStream(WWD_PATH).use {
            val wwd = loadWwd(it)
            val actionPlane = wwd.planes[1]

            for (i in (0..actionPlane.tilesHigh - 1)) {
                for (j in (0..actionPlane.tilesWide - 1)) {
                    val tileIdx = actionPlane.getTile(i, j)
                    if (tileIdx > 0) {
                        world.setTile(Vec2i(j, i), tileIdx)
                    }
                }
            }

            val objects = actionPlane.objects
            for (wwdObject in objects) {
                val wObject = world.addObject()
                wObject.wwdObject = wwdObject
            }
        }
    }
}

