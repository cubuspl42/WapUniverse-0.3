import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.*
import javafx.stage.FileChooser
import javafx.stage.Stage


private val WINDOW_TITLE = "WapUniverse"

private fun buildRezImageProvider(classLoader: ClassLoader): RezImageProvider {
    val rezMetadataDao = YamlRezMetadataDao(classLoader.getResourceAsStream("rezMetadata.yaml"))
    val rezImageLoader = ClassLoaderRezImageLoader("CLAW")
    val rezImageProvider = CachingRezImageProvider(rezMetadataDao, rezImageLoader)
    return rezImageProvider
}

class MainWindow(stage: Stage) {
    private val classLoader = Thread.currentThread().contextClassLoader

    private val rezImageProvider = buildRezImageProvider(classLoader)

    private var editorController: EditorController? = null

    private val borderPane = BorderPane()

    init {
        initView(stage)

        stage.title = WINDOW_TITLE
    }

    private fun initView(stage: Stage) {
        val openItem = MenuItem("Open")

        openItem.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.title = "Open WWD File"
            fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("WWD", "*.wwd", "*.WWD"))
            fileChooser.showOpenDialog(stage)?.let { file ->
                openWwdFile(file.absolutePath)
            }
        }

        val fileMenu = Menu("File", null, openItem)
        val menuBar = MenuBar(fileMenu)

        borderPane.top = menuBar

        stage.scene = Scene(borderPane)

        stage.setOnCloseRequest {
            editorController?.close()
        }
    }

    private fun openWwdFile(filePath: String) {
        editorController?.close()
        editorController = EditorController(filePath, rezImageProvider, borderPane)
    }
}
