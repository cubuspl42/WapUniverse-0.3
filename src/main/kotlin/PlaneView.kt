import javafx.scene.Group
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Affine
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import wap32.Wwd

private suspend fun presentWwd(root: Group, wwd: Wwd, rezImageProvider: RezImageProvider) {
    val world = buildWorld(wwd)

    val actionPlane = world.planes[1]

    presentTiles(actionPlane, root, rezImageProvider)

    actionPlane.objects.forEach { wwdObject ->
        presentObject(wwdObject, root, rezImageProvider)
    }
}

private suspend fun presentTiles(actionPlane: Plane, planeNode: Group, rezImageProvider: RezImageProvider) {
    val tiles = actionPlane.tiles
    val size = tiles.size
    for (i in 0 until size.height) {
        for (j in 0 until size.width) {
            println("$i, $j")
            val tileId = tiles[i, j]
            if (tileId >= 0) {
                val tileRezImage = rezImageProvider.provideImage("LEVEL1_TILES_ACTION", tileId)!!
                val imageView = ImageView(tileRezImage.image)
                imageView.translateX = (j * 64).toDouble()
                imageView.translateY = (i * 64).toDouble()
                planeNode.children.add(imageView)
            }
        }
    }
    println()
}

private suspend fun presentObject(wwdObject: WObject, planeNode: Group, rezImageProvider: RezImageProvider) {
    val imageSetId = wwdObject.imageSet
            .replace("GAME_", "GAME_IMAGES_")
            .replace("LEVEL_", "LEVEL1_IMAGES_")
    rezImageProvider.provideImage(imageSetId, -1)?.let { rezImage ->
        val image = rezImage.image
        val imageView = ImageView(image)
        imageView.x = rezImage.offset.x.toDouble() - image.width / 2
        imageView.y = rezImage.offset.y.toDouble() - image.height / 2

        imageView.translateX = wwdObject.x.toDouble()
        imageView.translateY = wwdObject.y.toDouble()

        planeNode.children.add(imageView)
    }
}

class PlaneView(wwd: Wwd, rezImageProvider: RezImageProvider) : Pane() {
    var scale = 1.0

    var dragConstraint: Vec2d? = null

    private val root = Group()

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
            presentWwd(root, wwd, rezImageProvider)
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
