import javafx.application.Application
import javafx.stage.Stage

fun main(args: Array<String>) {
    class MyApplication : Application() {
        override fun start(primaryStage: Stage) {
            MainWindow(primaryStage)
            primaryStage.show()
        }
    }

    Application.launch(MyApplication::class.java, *args)
}
