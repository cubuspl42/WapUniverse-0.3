package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import io.github.wapuniverse.util.*
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.loadWwd
import io.github.wapuniverse.world.WObject
import io.github.wapuniverse.world.World
import io.github.wapuniverse.world.makeWorld
import javafx.scene.Scene
import javafx.stage.Stage

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"
private val WWD_PATH = "RETAIL0$CFG_LEVEL_INDEX.WWD"
private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0
private val SELECTION_NODE_Z = 1000000

class ApplicationController(stage: Stage) {

    private val world = makeWorld()

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val worldScene = DScene()

    init {
        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        val selectionNode = DSelectionNode()
        selectionNode.z = SELECTION_NODE_Z
        worldScene.addNode(selectionNode)

        world.objectAdded.on { wObject ->
            val snItem = selectionNode.addItem()
            WObjectController(wObject, worldScene, snItem, imageMap, imageSetDatabase)
        }

        loadWorld(world)

        stage.title = INITIAL_TITLE
        stage.scene = Scene(worldScene, INITIAL_WIDTH, INITIAL_HEIGHT)
        stage.show()
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

