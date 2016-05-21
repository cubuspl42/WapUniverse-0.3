package io.github.wapuniverse

import javafx.application.Application
import tornadofx.App
import tornadofx.importStylesheet
import io.github.wapuniverse.view.MainView

class MyApp: App() {
    override val primaryView = MainView::class

    init {
        importStylesheet("/app.css")
    }
}

fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
}