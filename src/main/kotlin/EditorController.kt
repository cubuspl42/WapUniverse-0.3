import javafx.scene.layout.BorderPane
import javafx.scene.text.Text
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.javafx.JavaFx as UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run
import wap32.Wwd
import wap32.loadWwd
import java.io.FileInputStream

private suspend fun loadWwdFromPath(filePath: String): Wwd {
    return run(CommonPool) {
        FileInputStream(filePath).use { loadWwd(it) }
    }
}

class EditorController(
        filePath: String,
        contentPane: BorderPane
) {
    private val masterJob = Job()

    init {
        contentPane.center = Text("...")

        launch(masterJob + UI) {
            val wwd = loadWwdFromPath(filePath)
            contentPane.center = Text(wwd.header.levelName)
        }
    }

    fun close() {
        masterJob.cancel()
    }
}
