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

    private fun findImageSet(expandedImageSetId: String): YamlDb.ImageSet? {
        val imageSet = root.imageSets[expandedImageSetId]
        return imageSet
    }

    private fun findImageMetadata(imageSet: YamlDb.ImageSet, frameIndex: Int): ImageMetadata? {
        val frameName = imageSet.frames[frameIndex]
        if (frameName != null) {
            val imageMetadata = imageSet.sprites[frameName]!!
            val x = imageMetadata.offset[0].toDouble()
            val y = imageMetadata.offset[1].toDouble()
            val path = imageMetadata.path
            return ImageMetadata(Vec2d(x, y), path)
        }
        return null
    }

    private fun findImageMetadata(expandedImageSetId: String, frameIndex: Int): ImageMetadata? {
        val imageSet = findImageSet(expandedImageSetId) ?: return null
        return findImageMetadata(imageSet, frameIndex)
    }

    fun findObjectImageMetadata(levelIndex: Int, imageSetId: String, frameIndex: Int): ImageMetadata? {
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

    fun readAllImageMetadata(): List<ImageMetadata> {
        val imageMetadataList = mutableListOf<ImageMetadata>()
        root.imageSets.values.forEach { imst ->
            imst.frames.keys.forEach { i ->
                findImageMetadata(imst, i)?.let {
                    imageMetadataList.add(it)
                }
            }
        }
        return imageMetadataList
    }

    private fun findTileImageSet(levelIndex: Int, tileImageSetName: String): YamlDb.ImageSet? {
        val expandedImageSetId = "LEVEL${levelIndex}_TILES_" + tileImageSetName
        val imageSet = findImageSet(expandedImageSetId)
        return imageSet
    }

    fun findTileMetadata(levelIndex: Int, tileImageSetName: String, tileIndex: Int): ImageMetadata? {
        val imageSet = findTileImageSet(levelIndex, tileImageSetName) ?: return null
        return findImageMetadata(imageSet, tileIndex)
    }

    fun listTiles(levelIndex: Int, tileImageSetName: String) : Set<Int>? {
        val imageSet = findTileImageSet(levelIndex, tileImageSetName) ?: return null
        return imageSet.frames.keys
    }
}

fun loadImageSetDatabaseFromFile(path: String): ImageSetDatabase {
    getResourceAsStream(path).use {
        return ImageSetDatabase(it)
    }
}
