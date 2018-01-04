import javafx.scene.Group
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Affine
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import wap32.Wwd

class PlaneView(wwd: Wwd, rezImageProvider: RezImageProvider) : Pane() {
    var scale = 1.0

    var dragConstraint: Vec2d? = null

    private val root = Group()

    private val presenter = WwdPresenter(root, rezImageProvider)

    init {
        children.add(root)

        val clipRect = Rectangle()
        clipRect.widthProperty().bind(widthProperty())
        clipRect.heightProperty().bind(heightProperty())
        clip = clipRect

        addEventFilter(MouseEvent.MOUSE_PRESSED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                dragConstraint = root.parentToLocal(ev.position)
                ev.consume()
            }
        })

        addEventFilter(MouseEvent.MOUSE_DRAGGED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                transformSpace(ev.position, dragConstraint!!)
                ev.consume()
            }
        })

        addEventFilter(MouseEvent.MOUSE_RELEASED, { ev ->
            if (ev.button == MouseButton.SECONDARY) {
                ev.consume()
            }
        })

        addEventFilter(ScrollEvent.SCROLL, { ev ->
            val scaleMultiplier = when {
                ev.deltaY > 0 -> 2.0
                else -> 0.5
            }

            scale *= scaleMultiplier

            transformSpace(ev.position, root.parentToLocal(ev.position))
            ev.consume()
        })

        launch(JavaFx) {
            presenter.presentWwd(wwd)
        }
    }

    private fun transformSpace(viewConstraint: Vec2d, worldConstraint: Vec2d) {
        val translate = -(worldConstraint - (viewConstraint / scale))

        val affine = Affine()
        affine.appendScale(scale, scale)
        affine.appendTranslation(translate.x, translate.y)

        root.transforms.setAll(affine)
    }
}
