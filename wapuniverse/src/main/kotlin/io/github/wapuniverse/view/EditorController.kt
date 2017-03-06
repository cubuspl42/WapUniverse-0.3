package io.github.wapuniverse.view

import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import javafx.event.ActionEvent
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import java.beans.EventHandler

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"

class EditorController(private val editor: Editor, sceneVBox: VBox) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val world = editor.world

    private val menuBar = MenuBar()

    val worldScene = DScene()

    private val stPlane = StPlane()

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val objectSelectionController = WObjectSelectionController(
            editor, worldScene, stPlane, imageSetDatabase, imageMap)

    init {
        sceneVBox.children.add(menuBar)
        sceneVBox.children.add(worldScene)
        VBox.setVgrow(worldScene, Priority.ALWAYS)

        initMenu()

        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        world.objects.forEach { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        world.onObjectAdded.connect { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        worldScene.children.add(stPlane)

        worldScene.setOnKeyPressed { ev ->
            when {
                ev.code == KeyCode.DELETE -> objectSelectionController.destroySelectedObjects()
            }
        }

        worldScene.requestFocus()
    }

    private fun initMenu() {
        val menuFile = Menu("File")

        val menuEdit = Menu("Edit")

        val undoItem = MenuItem("Undo")
        undoItem.setOnAction { ev ->
            editor.undoStack.undo()
        }

        val redoItem = MenuItem("Redo")
        redoItem.setOnAction { ev ->
            editor.undoStack.redo()
        }

        menuEdit.items.addAll(undoItem, redoItem)

        menuBar.menus.addAll(menuFile, menuEdit)
    }
}
