package io.github.wapuniverse.view

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.wapuniverse.editor.WObject
import io.github.wapuniverse.editor.World

class WObjectSelectionController(world: World, worldScene: DScene, stPlane: StPlane) {
    private val mvMap: BiMap<WObject, StNode> = HashBiMap.create();

    private val vmMap = mvMap.inverse()

    init {
        world.objectAdded.connect { wObject ->
            val stNode = StNode()
            stPlane.addNode(stNode)
            mvMap.put(wObject, stNode)
        }

        worldScene.onTransformChanged.connect {
            mvMap.forEach {
                val viewRect = worldScene.transform.transform

            }
        }
    }
}
