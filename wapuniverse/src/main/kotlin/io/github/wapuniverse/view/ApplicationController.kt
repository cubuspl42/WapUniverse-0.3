package io.github.wapuniverse.view

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import io.github.wapuniverse.util.*
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
private val NO_DRAW_ALPHA = 0.5

class ApplicationController(stage: Stage) {

    private val wwd = loadWwd()

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val sceneView = SceneView(wwd, imageMap)

    init {
        updateSceneView()

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

    private fun updateSceneView() {
        val actionPlane = wwd.planes[1]
        val tileMatrixNode = WvTileMatrixNode(actionPlane, imageMap)
        sceneView.scene.addNode(tileMatrixNode)

        val levelIndex = 1 // FIXME

        val objects = actionPlane.objects
        for (obj in objects) {
            imageMap.findObjectImage(levelIndex, obj.imageSet, obj.i)?.let { objImg ->
                // FIXME: imageMap/imageSetDatabase
                val imgMd = imageSetDatabase.findObjectImageMetadata(levelIndex, obj.imageSet, obj.i)!!
                val halfSize = Vec2d(objImg.width, objImg.height) / 2.0
                val anchor = halfSize - imgMd.offset
                val spriteNode = WvSpriteNode(objImg, anchor)
                spriteNode.position = Vec2i(obj.x, obj.y).toVec2d()
                spriteNode.alpha = if(obj.drawFlags.noDraw) NO_DRAW_ALPHA else 1.0
                spriteNode.scale.x = if(obj.drawFlags.mirror) -1.0 else 1.0
                spriteNode.scale.y = if(obj.drawFlags.invert) -1.0 else 1.0
                sceneView.scene.addNode(spriteNode)
            }
        }
    }
}
