import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage


private val WINDOW_TITLE = "WapUniverse"

class MainWindow(stage: Stage) {
    private var editorController: EditorController? = null

    private val contentPane = BorderPane()

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

        stage.scene = Scene(VBox(menuBar, contentPane))

        stage.setOnCloseRequest {
            editorController?.close()
        }
    }

    private fun openWwdFile(filePath: String) {
        editorController?.close()
        editorController = EditorController(filePath, contentPane)
    }
}
