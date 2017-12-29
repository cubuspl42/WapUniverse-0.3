import javafx.application.Application
import javafx.stage.Stage

class MyApplication : Application() {
    override fun start(primaryStage: Stage) {
        MainWindowController(primaryStage)
    }
}

fun main(args: Array<String>) {
    Application.launch(MyApplication::class.java, *args)
}
