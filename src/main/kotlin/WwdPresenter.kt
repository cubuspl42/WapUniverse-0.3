import javafx.scene.Group
import wap32.Wwd

class WwdPresenter(
        root: Group,
        rezImageProvider: RezImageProvider
) {
    private val planePresenter = PlanePresenter(root, rezImageProvider)

    suspend fun presentWwd(wwd: Wwd) {
        val world = buildWorld(wwd)

        val actionPlane = world.planes[1]

        planePresenter.presentTiles(actionPlane)

        actionPlane.objects.forEach { wwdObject ->
            planePresenter.presentObject(wwdObject)
        }
    }
}