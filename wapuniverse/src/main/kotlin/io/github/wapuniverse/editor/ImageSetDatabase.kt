package io.github.wapuniverse.editor

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.getResourceAsStream
import org.yaml.snakeyaml.TypeDescription
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.io.InputStream
import java.util.*
import kotlin.properties.Delegates.vetoable


class ImageSetDatabase(yamlDocumentStream: InputStream) {
    class ImageNotFound(expandedImageSetId: String, frameIndex: Int) :
            Exception("Image $expandedImageSetId:$frameIndex not found")

    private class YamlDb {
        class Image {
            var offset: List<Int> by vetoable(listOf(0, 0), { prop, old, new ->
                if (new.size != 2) throw IllegalArgumentException()
                else true
            })
            var path: String = ""
        }

        class ImageSet {
            var sprites: Map<String, Image> = HashMap() // frame name -> image
            var frames: Map<Int, String> = HashMap() // frame index -> frame name
        }

        class Root {
            var imageSets: Map<String, ImageSet> = HashMap() // long imageset id -> imageset
        }
    }

    /**
     * @property offset Image's offset retrieved from PID file
     * @property path Path to the PID file inside REZ
     */
    data class ImageMetadata(val offset: Vec2d, val path: String)

    private var root: YamlDb.Root = YamlDb.Root()

    init {
        val constructor = Constructor(YamlDb.Root::class.java)
        with(constructor) {
            addTypeDescription(TypeDescription(YamlDb.Image::class.java))
            addTypeDescription(TypeDescription(YamlDb.ImageSet::class.java))
        }
        yamlDocumentStream.use {
            val yaml = Yaml(constructor)
            root = yaml.load(it) as YamlDb.Root
        }
    }

    private fun findImageMetadata(expandedImageSetId: String, frameIndex: Int): ImageMetadata {
        try {
            val imageSet = root.imageSets[expandedImageSetId]!!
            val frameName = imageSet.frames[frameIndex]
            val imageMetadata = imageSet.sprites[frameName]!!
            val x = imageMetadata.offset[0].toDouble()
            val y = imageMetadata.offset[1].toDouble()
            val path = imageMetadata.path
            return ImageMetadata(Vec2d(x, y), path)
        } catch(e: KotlinNullPointerException) {
            throw ImageNotFound(expandedImageSetId, frameIndex)
        }
    }

    fun findObjectImageMetadata(levelIndex: Int, imageSetId: String, frameIndex: Int): ImageMetadata {
        when {
            imageSetId.startsWith("LEVEL_") -> {
                val expandedImageSetId = imageSetId.replaceFirst("LEVEL_", "LEVEL${levelIndex}_IMAGES_")
                return findImageMetadata(expandedImageSetId, frameIndex)
            }
            imageSetId.startsWith("GAME_") -> {
                val expandedImageSetId = imageSetId.replaceFirst("GAME_", "GAME_IMAGES_")
                return findImageMetadata(expandedImageSetId, frameIndex)
            }
            else -> throw IllegalArgumentException(imageSetId)
        }
    }

    fun findTileMetadata(levelIndex: Int, tileImageSetName: String, tileIndex: Int): ImageMetadata {
        val expandedImageSetId = "LEVEL${levelIndex}_TILES_" + tileImageSetName
        return findImageMetadata(expandedImageSetId, tileIndex)
    }
}

fun loadImageSetDatabaseFromFile(path: String): ImageSetDatabase {
    getResourceAsStream(path).use {
        return ImageSetDatabase(it)
    }
}
