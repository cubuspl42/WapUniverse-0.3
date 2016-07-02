package io.github.wapuniverse.controller

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.core.*
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.utils.div
import io.github.wapuniverse.utils.minus
import io.github.wapuniverse.utils.toVec2i
import io.github.wapuniverse.view.*
import javafx.scene.Node

class WapObjectVo(
        private val sceneView: SceneView,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val levelIndex: Int,
        private val wapObject: WapObject
) {
    init {
        val wwdObject = wapObject.wwdObject
        val imageMd = imageSetDatabase.findObjectImageMetadata(levelIndex, wwdObject.imageSet, wwdObject.i)
        val image = imageMap.findObjectImage(levelIndex, wwdObject.imageSet, wwdObject.i)

        if (imageMd != null && image != null) {
            val imageSize = Vec2d(image.width, image.height).toVec2i()
            val offset: Vec2i = wapObject.position - imageSize / 2.0 + imageMd.offset.toVec2i()
            sceneView.addItem(SceneImage(offset.toVec2d(), image))
        }
    }
}

class WapObjectPresenter(
        private val entityComponent: EntityComponent,
        private val sceneView: SceneView,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap,
        private val levelIndex: Int) {

    init {
        entityComponent.entities.forEach { ent ->
            presentEntity(ent)
        }

        entityComponent.entityAdded.on { ent ->
            presentEntity(ent)
        }
    }

    private fun presentEntity(ent: Entity) {
        if (ent is WapObject) {
            WapObjectVo(sceneView, imageSetDatabase, imageMap, levelIndex, ent)
        }
    }
}
