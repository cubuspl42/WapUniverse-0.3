import javafx.event.ActionEvent

class MainWindowFxml {
    lateinit var controller: MainWindowController

    fun onFileNew(actionEvent: ActionEvent) {
        controller.createNewWorld()
    }
}
