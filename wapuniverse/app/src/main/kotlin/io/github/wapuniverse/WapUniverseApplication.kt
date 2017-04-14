package io.github.wapuniverse

import io.github.wapuniverse.view.ApplicationController
import javafx.application.Application
import javafx.stage.Stage


class WapUniverseApplication : Application() {
    override fun start(stage: Stage) {
        ApplicationController(stage)
    }
}

fun main(args: Array<String>) {
    Application.launch(WapUniverseApplication::class.java, *args)
}
