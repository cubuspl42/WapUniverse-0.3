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

        val rezImage = rezImageProvider.provideImage("GAME_IMAGES_BOSSWARP", -1)
        planeNode.children.add(ImageView(rezImage!!.image))

        val actionPlane = wwd.planes[1]
        actionPlane.objects.forEach {
            val point = Circle(it.x.toDouble(), it.y.toDouble(), 8.0)
            planeNode.children.add(point)
        }

        val viewport = Viewport(planeNode)
        borderPane.center = viewport
    }
}
