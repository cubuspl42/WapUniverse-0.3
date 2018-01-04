import javafx.scene.Group
import javafx.scene.image.ImageView

class PlanePresenter(
        private val root: Group,
        private val rezImageProvider: RezImageProvider
) {
    suspend fun presentPlane(plane: Plane) {
        presentTiles(plane)

        plane.objects.forEach { wwdObject ->
            presentObject(wwdObject)
        }
    }

    suspend fun presentTiles(plane: Plane) {
        val tiles = plane.tiles
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
                    root.children.add(imageView)
                }
            }
        }
        println()
    }

    suspend fun presentObject(wwdObject: WObject) {
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

            root.children.add(imageView)
        }
    }

}