package io.github.wapuniverse.controller

import io.github.wapuniverse.core.loadImageSetDatabaseFromFile
import io.github.wapuniverse.utils.getResourceAsStream
import io.github.wapuniverse.view.loadImageMapFromPath
import io.github.wapuniverse.view.loadImageMapFromResources
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdPlane
import io.github.wapuniverse.wap32.loadWwd
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.TransferMode
import java.io.FileInputStream


private val IMAGE_BASE_DIR_PATH = "/home/kuba/tmp/CLAW/"

private val wwdExt = "wwd"

class MainController(private val rootNode: Node, private val sceneCanvas: Canvas) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile("imageSetDatabase.yaml")

    private val imageMap = loadImageMapFromResources(imageSetDatabase, "CLAW/")

    private var worldController: WorldController? = null

    private fun makeWorldController(wwd: Wwd, wwdPath: String?) =
            WorldController(rootNode, wwdPath, imageSetDatabase, imageMap, sceneCanvas, wwd)

    init {
        sceneCanvas.setOnDragOver {
            val db = it.dragboard
            if (db.hasFiles()) {
                val file = db.files[0]
                if (file.extension.toLowerCase() == wwdExt) {
                    it.acceptTransferModes(TransferMode.COPY);
                }
            } else {
                it.consume()
            }
        }

        sceneCanvas.setOnDragDropped {
            val db = it.dragboard
            if (db.hasFiles()) {
                val file = db.files[0]
                loadWorld(file.absolutePath)
            }
        }

        createNewWorld(1) // TEMPORARY
    }

    fun loadWorld(wwdPath: String) {
        FileInputStream(wwdPath).use {
            val wwd = loadWwd(it)
            worldController = makeWorldController(wwd, wwdPath)
        }
    }

    fun createNewWorld(levelIndex: Int) {
        val retailWwd = "RETAIL%02d.WWD".format(levelIndex)
        getResourceAsStream(retailWwd).use {
            val wwd = loadWwd(it)
            wwd.mainPlane!!.objects.clear()
            worldController = makeWorldController(wwd, null)
        }
    }

    fun saveWorld() {
        worldController?.save()
    }
}
