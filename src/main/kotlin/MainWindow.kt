import javafx.stage.Stage
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit.SECONDS


private val WINDOW_TITLE_LOADING = "WapUniverse (loading...)"
private val WINDOW_TITLE = "WapUniverse"

class MainWindow(stage: Stage) {
    init {
        val rootJob = Job()
        val rootContext = rootJob + JavaFx

        stage.title = WINDOW_TITLE_LOADING

        launch(rootContext) {
            delay(2, SECONDS)
            stage.title = WINDOW_TITLE
        }

        stage.setOnCloseRequest {
            rootJob.cancel()
        }
    }
}
