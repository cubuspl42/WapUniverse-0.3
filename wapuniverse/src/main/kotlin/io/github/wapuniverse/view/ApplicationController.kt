package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
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
private val WWD_PATH = "RETAIL01.WWD"
private val INITIAL_TITLE = "WapUniverse Editor"
private val INITIAL_WIDTH = 640.0
private val INITIAL_HEIGHT = 480.0


class ApplicationController(stage: Stage) {

    private val world = makeWorld()

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val sceneView = SceneView()

    private val scene = sceneView.scene

    init {
        val tileMatrixNode = WvTileMatrixNode(world, imageMap)
        scene.addNode(tileMatrixNode)

        world.objectAdded.on { wObject ->
            WObjectController(wObject, scene, imageMap, imageSetDatabase)
        }

        loadWorld(world)

        stage.title = INITIAL_TITLE
        stage.scene = Scene(sceneView, INITIAL_WIDTH, INITIAL_HEIGHT)
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

