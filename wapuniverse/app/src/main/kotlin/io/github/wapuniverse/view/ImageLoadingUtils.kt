package io.github.wapuniverse.view

import javafx.scene.image.Image
import java.io.File
import java.nio.file.Paths
import java.util.*


fun loadAllImagesRecursively(baseDirPath: String, imageExtension: String): Map<String, Image> {
    val imagesDict = HashMap<String, Image>()
    val basePath = Paths.get(baseDirPath)
    File(baseDirPath).walk()
            .filter { it.isFile && it.path.endsWith("" + imageExtension) }
            .forEach { file ->
                val relPath = basePath.relativize(Paths.get(file.path))
                file.inputStream().use {
                    imagesDict.put(relPath.toString(), Image(it))
                }
            }
    return imagesDict
}
