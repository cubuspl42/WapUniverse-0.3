package io.github.wapuniverse.view

import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.loadImageSetDatabaseFromFile
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCode

private val IMAGE_SET_DATABASE_PATH = "imageSetDatabase.yaml"
private val CLAW_PREFIX = "CLAW/"

class EditorController(private val editor: Editor, menuBar: MenuBar, contentPane: ContentPane) {

    private val imageSetDatabase = loadImageSetDatabaseFromFile(IMAGE_SET_DATABASE_PATH)

    private val world = editor.world

    val worldScene = DScene()

    private val stPlane = StPlane()

    private val imageMap = loadImageMapFromResources(imageSetDatabase, CLAW_PREFIX)

    private val objectSelectionController = WObjectSelectionController(
            editor, stPlane, imageSetDatabase, imageMap)

    init {
        initMenu(menuBar)

        contentPane.children.add(worldScene)
        worldScene.children.add(stPlane)

        val editorOverlay = EditorOverlay(worldScene)
        contentPane.children.add(editorOverlay)

        val tileMatrixNode = DTileMatrixNode(world, imageMap)
        worldScene.addNode(tileMatrixNode)

        world.objects.forEach { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        world.onObjectAdded.connect { WObjectController(it, worldScene, imageMap, imageSetDatabase) }

        worldScene.onTransformChanged.connect {
            stPlane.transforms.setAll(worldScene.transform.toAffine())
        }

        worldScene.setOnKeyPressed { ev ->
            when {
                ev.code == KeyCode.DELETE -> objectSelectionController.destroySelectedObjects()
            }
        }

        worldScene.requestFocus()
    }

    private fun initMenu(menuBar: MenuBar) {
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
