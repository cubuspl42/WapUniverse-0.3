import javafx.stage.Stage
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene

private val fxmlFilename = "MainWindow.fxml"
private val stageTitle = "WapUniverse"

class MainWindowController(
        stage: Stage
) {
    private var fxml: MainWindowFxml

    init {
        val fxmlLoader = FXMLLoader(MyApplication::class.java.getResource(fxmlFilename))
        val root = fxmlLoader.load<Parent>()

        fxml = fxmlLoader.getController<MainWindowFxml>()!!.apply {
            controller = this@MainWindowController
        }

        stage.apply {
            title = stageTitle
            scene = Scene(root)
            show()
        }
    }

    fun createNewWorld() {
        val newWorldWindowController = NewWorldWindowController()
    }
}
