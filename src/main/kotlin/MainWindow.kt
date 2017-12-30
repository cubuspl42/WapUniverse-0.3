import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.run
import wap32.Wwd
import wap32.loadWwd
import java.io.FileInputStream
import java.net.URL
import java.util.ResourceBundle
import kotlinx.coroutines.experimental.javafx.JavaFx as UI

private val fxmlFilename = "MainWindow.fxml"
private val stageTitle = "WapUniverse"

private fun buildRezImageProvider(classLoader: ClassLoader): RezImageProvider {
    val rezMetadataDao = YamlRezMetadataDao(classLoader.getResourceAsStream("rezMetadata.yaml"))
    val rezImageLoader = ClassLoaderRezImageLoader("CLAW")
    val rezImageProvider = CachingRezImageProvider(rezMetadataDao, rezImageLoader)
    return rezImageProvider
}

private suspend fun loadWwdFromPath(filePath: String): Wwd {
    return run(CommonPool) {
        FileInputStream(filePath).use { loadWwd(it) }
    }
}

class MainWindowController(
        private val mainWindow: MainWindow
) : Initializable {
    private val classLoader = Thread.currentThread().contextClassLoader

    private val rezImageProvider = buildRezImageProvider(classLoader)

    @FXML
    lateinit var borderPane: BorderPane

    @FXML
    fun onFileNewPressed(actionEvent: ActionEvent) {
        mainWindow.createNewWorld()
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        launch(UI) {
            val wwdPath = "D:\\WORLD.WWD"
            val wwd = loadWwdFromPath(wwdPath)
            borderPane.center = PlaneView(wwd, rezImageProvider)
        }
    }
}

class MainWindow {
    init {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(MyApplication::class.java.getResource(fxmlFilename)).apply {
            setControllerFactory { MainWindowController(this@MainWindow) }
        }
        val root = fxmlLoader.load<Parent>()

        stage.apply {
            title = stageTitle
            scene = Scene(root)
            show()
        }
    }

    internal fun createNewWorld() {
//        save()
        NewWorldWindow()
    }

    private fun save() {
        // if no save path, get save path
        // save to save path
    }
}
