import BaseLevel.BATTLEMENTS
import BaseLevel.DARK_WOODS
import BaseLevel.FOOTPATH
import BaseLevel.LA_ROCA
import com.google.common.collect.HashBiMap
import javafx.collections.FXCollections.observableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import javafx.util.StringConverter
import java.net.URL
import java.util.ResourceBundle

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

class NewWorldWindowFxml : Initializable {
    lateinit var controller: NewWorldWindowController

    @FXML
    lateinit var baseLevelComboBox: ComboBox<BaseLevel>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        baseLevelComboBox.apply {
            items = observableList(BaseLevel.values().toList())
            converter = BaseLevelConverter
            selectionModel.selectFirst()
        }
    }
}
