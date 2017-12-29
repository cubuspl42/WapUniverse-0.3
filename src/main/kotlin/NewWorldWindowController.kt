import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

private val fxmlFilename = "NewWorldWindow.fxml"
private val stageTitle = "New World"

class NewWorldWindowController {
    private var fxml: NewWorldWindowFxml

    init {
        val fxmlLoader = FXMLLoader(MyApplication::class.java.getResource(fxmlFilename))
        val root = fxmlLoader.load<Parent>()

        fxml = fxmlLoader.getController<NewWorldWindowFxml>()!!.apply {
            controller = this@NewWorldWindowController
        }

        Stage().apply {
            title = stageTitle
            scene = Scene(root)
            show()
        }
    }
}
