package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.loadImageSetDatabaseFromFile
import io.github.wapuniverse.utils.getResourceAsStream
import io.github.wapuniverse.view.loadImageMapFromPath
import io.github.wapuniverse.view.loadImageMapFromResources
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdPlane
import io.github.wapuniverse.wap32.loadWwd
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import java.io.FileInputStream


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

class MainController(private val rootNode: Node, private val sceneCanvas: Canvas) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromResources(imageSetDatabase, "CLAW/")

    private var worldController: WorldController? = null

    private fun makeWorldController(wwd: Wwd) =
            WorldController(rootNode, imageSetDatabase, imageMap, sceneCanvas, wwd)

    fun loadWorld(wwdPath: String) {
        FileInputStream(wwdPath).use {
            val wwd = loadWwd(it)
            worldController = makeWorldController(wwd)
        }
    }

    fun createNewWorld(levelIndex: Int) {
        val retailWwd = "RETAIL%02d.WWD".format(levelIndex)
        getResourceAsStream(retailWwd).use {
            val wwd = loadWwd(it)
            wwd.mainPlane!!.objects.clear()
            worldController = makeWorldController(wwd)
        }
    }
}
