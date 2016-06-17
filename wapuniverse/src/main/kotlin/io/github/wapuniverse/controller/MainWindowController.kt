package io.github.wapuniverse.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.canvas.Canvas
import javafx.scene.control.MenuItem
import javafx.scene.layout.Region
import java.net.URL
import java.util.*
import java.util.logging.Logger


private val wwdPath = "/home/kuba/Dropbox/temp/LEVEL.wwd" // HARDCODED: wwd path

class MainWindowController : Initializable {
    private val logger = Logger.getLogger(javaClass.simpleName)

    @FXML
    private lateinit var root: Region

    @FXML
    private lateinit var sceneCanvas: Canvas

    private lateinit var mainController: MainController

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        sceneCanvas.widthProperty().bind(root.widthProperty())
        sceneCanvas.heightProperty().bind(root.heightProperty())
        sceneCanvas.isFocusTraversable = true

        mainController = MainController(root, sceneCanvas)
    }

    @FXML
    private fun handleMenuOpen(ev: ActionEvent) {
        mainController.loadWorld(wwdPath)
    }

    @FXML
    private fun handleMenuSave(ev: ActionEvent) {
        mainController.saveWorld()
    }

    @FXML
    private fun handleNewLevel(ev: ActionEvent) {
        val menuItem = ev.source as MenuItem
        val levelIndex = (1..maxLevelIndex).first {
            menuItem.id == "level$it"
        }
        mainController.createNewWorld(levelIndex)
    }
}
