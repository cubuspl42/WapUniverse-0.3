import javafx.scene.Group
import javafx.scene.layout.BorderPane
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
        borderPane: BorderPane
) {
    private val masterJob = Job()

    init {
        borderPane.center = Text("...")

        launch(masterJob + UI) {
            val wwd = loadWwdFromPath(filePath)
            val layerSpace = Group(Text("<${wwd.header.levelName}>"))
            val viewport = Viewport(layerSpace)

            borderPane.center = viewport
        }
    }

    fun close() {
        masterJob.cancel()
    }
}
