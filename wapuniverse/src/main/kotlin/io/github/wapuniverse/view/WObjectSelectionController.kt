package io.github.wapuniverse.view

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.editor.WObject
import io.github.wapuniverse.editor.World

class WObjectSelectionController(
        world: World,
        worldScene: DScene,
        stPlane: StPlane,
        imageSetDatabase: ImageSetDatabase,
        imageMap: ImageMap
) {
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
                val (wObject, stNode) = it
                val worldBounds = wObjectBounds(wObject, CFG_LEVEL_INDEX, imageSetDatabase, imageMap)!!
                val viewBounds = worldScene.transform.map(worldBounds)
                stNode.bounds = viewBounds
            }
        }

        initPresentation(world)
    }

    private fun initPresentation(world: World) {

    }
}
