package io.github.wapuniverse

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


private fun <T> loadFxml(javaClass: Class<T>, path: String): Parent {
    return FXMLLoader.load(javaClass.getResource(path)!!)
}

val defaultWidth = 640.0
val defaultHeight = 480.0

class WapUniverseApplication : Application() {
    override fun start(stage: Stage) {
        val root: Parent = loadFxml(javaClass, "MainView.fxml")
        val scene = Scene(root, defaultWidth, defaultHeight)
        stage.title = "WapUniverse Editor"
        stage.scene = scene
        stage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(WapUniverseApplication::class.java, *args)
}
