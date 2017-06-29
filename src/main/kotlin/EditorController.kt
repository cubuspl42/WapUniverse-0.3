import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run
import wap32.Wwd
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
        val planeNode = Group()

        val actionPlane = wwd.planes[1]
        actionPlane.objects.forEach { wwdObject ->
            val id = wwdObject.imageSet
                    .replace("GAME_", "GAME_IMAGES_")
                    .replace("LEVEL_", "LEVEL1_IMAGES_")
            rezImageProvider.provideImage(id, -1)?.let { rezImage ->
                val image = rezImage.image
                val imageView = ImageView(image)
                imageView.x = -(image.width / 2 + rezImage.offset.x.toDouble())
                imageView.y = -(image.height / 2 + rezImage.offset.y.toDouble())

                imageView.translateX = wwdObject.x.toDouble()
                imageView.translateY = wwdObject.y.toDouble()

                planeNode.children.add(imageView)
            }
            val point = Circle(wwdObject.x.toDouble(), wwdObject.y.toDouble(), 8.0)
            planeNode.children.add(point)
        }

        val viewport = Viewport(planeNode)
        borderPane.center = viewport
    }
}
