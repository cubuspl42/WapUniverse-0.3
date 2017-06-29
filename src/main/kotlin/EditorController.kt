import javafx.scene.Group
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run
import wap32.Wwd
import wap32.WwdObject
import wap32.WwdPlane
import wap32.loadWwd
import java.io.FileInputStream
import kotlinx.coroutines.experimental.javafx.JavaFx as UI

private suspend fun loadWwdFromPath(filePath: String): Wwd {
    return run(CommonPool) {
        FileInputStream(filePath).use { loadWwd(it) }
    }
}

class EditorController(
        filePath: String,
        private val rezImageProvider: RezImageProvider,
        private val borderPane: BorderPane
) {
    private val masterJob = Job()

    init {
        borderPane.center = Text("...")

        launch(masterJob + UI) {
            val wwd = loadWwdFromPath(filePath)
            presentWwd(wwd)
        }
    }

    fun close() {
        masterJob.cancel()
    }

    private suspend fun presentWwd(wwd: Wwd) {
        val actionPlane = wwd.planes[1]
        val planeNode = Group()

        presentTiles(actionPlane, planeNode)

        actionPlane.objects.forEach { wwdObject ->
            presentObject(wwdObject, planeNode)
        }

        val viewport = Viewport(planeNode)
        borderPane.center = viewport
    }

    private suspend fun presentTiles(actionPlane: WwdPlane, planeNode: Group) {
        for (i in 0 until actionPlane.tilesHigh) {
            for (j in 0 until actionPlane.tilesWide) {
                val tileId = actionPlane.getTile(i, j)
                if (tileId >= 0) {
                    val tileRezImage = rezImageProvider.provideImage("LEVEL1_TILES_ACTION", tileId)!!
                    val imageView = ImageView(tileRezImage.image)
                    imageView.translateX = (j * 64).toDouble()
                    imageView.translateY = (i * 64).toDouble()
                    planeNode.children.add(imageView)
                }
            }
        }
    }

    private suspend fun presentObject(wwdObject: WwdObject, planeNode: Group) {
        val imageSetId = wwdObject.imageSet
                .replace("GAME_", "GAME_IMAGES_")
                .replace("LEVEL_", "LEVEL1_IMAGES_")
        rezImageProvider.provideImage(imageSetId, -1)?.let { rezImage ->
            val image = rezImage.image
            val imageView = ImageView(image)
            imageView.x = rezImage.offset.x.toDouble() - image.width / 2
            imageView.y = rezImage.offset.y.toDouble() - image.height / 2

            imageView.translateX = wwdObject.x.toDouble()
            imageView.translateY = wwdObject.y.toDouble()

            planeNode.children.add(imageView)
        }
    }
}
