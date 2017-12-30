import BaseLevel.BATTLEMENTS
import BaseLevel.FOOTPATH
import BaseLevel.LA_ROCA
import com.google.common.collect.HashBiMap
import javafx.collections.FXCollections.observableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.stage.Stage
import javafx.util.StringConverter
import java.net.URL
import java.util.ResourceBundle

private val fxmlFilename = "NewWorldWindow.fxml"
private val stageTitle = "New World"

private val baseLevelToString = HashBiMap.create(mapOf(
        LA_ROCA to "La Roca",
        BATTLEMENTS to "Battlements",
        FOOTPATH to "Footpath"
))

private val stringToBaseLevel = baseLevelToString.inverse()

private object BaseLevelConverter : StringConverter<BaseLevel>() {
    override fun toString(baseLevel: BaseLevel) =
            baseLevelToString.getOrDefault(baseLevel, baseLevel.toString())

    override fun fromString(baseLevelString: String) =
            stringToBaseLevel[baseLevelString]!!
}

class NewWorldWindowController(
        private val newWorldWindow: NewWorldWindow
) : Initializable {
    @FXML
    lateinit var baseLevelComboBox: ComboBox<BaseLevel>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        baseLevelComboBox.apply {
            items = observableList(BaseLevel.values().asList())
            converter = BaseLevelConverter
            selectionModel.selectFirst()
        }
    }

    fun onCreatePressed(actionEvent: ActionEvent) {
        newWorldWindow.create()
    }

    fun onCancelPressed(actionEvent: ActionEvent) {
        newWorldWindow.cancel()
    }
}

class NewWorldWindow {
    private val window: Stage

    init {
        val fxmlLoader = FXMLLoader(MyApplication::class.java.getResource(fxmlFilename)).apply {
            setControllerFactory { NewWorldWindowController(this@NewWorldWindow) }
        }
        val root = fxmlLoader.load<Parent>()

        window = Stage().apply {
            title = stageTitle
            scene = Scene(root)
            show()
        }
    }

    internal fun create() {
        window.close()
    }

    internal fun cancel() {
        window.close()
    }
}
