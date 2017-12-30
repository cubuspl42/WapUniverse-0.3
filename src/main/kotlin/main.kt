import javafx.application.Application
import javafx.stage.Stage

class MyApplication : Application() {
    override fun start(primaryStage: Stage) {
        MainWindow()
    }
}

fun main(args: Array<String>) {
    Application.launch(MyApplication::class.java, *args)
}
