package io.github.wapuniverse.view

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.editor.WObject
import io.github.wapuniverse.editor.World

class WObjectSelectionController(
        private val editor: Editor,
        worldScene: DScene,
        private val stPlane: StPlane,
        imageSetDatabase: ImageSetDatabase,
        imageMap: ImageMap
) {
    private val mvMap: BiMap<WObject, StNode> = HashBiMap.create();

    private val vmMap = mvMap.inverse()

    init {
        val world = editor.world

        world.objects.forEach { presentWObject(stPlane, it) }

        world.onObjectAdded.connect { presentWObject(stPlane, it) }

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

    private fun presentWObject(stPlane: StPlane, wObject: WObject) {
        val stNode = StNode()
        stPlane.addNode(stNode)
        mvMap.put(wObject, stNode)

        wObject.preRemoved.connect {
            stPlane.removeNode(stNode)
            mvMap.remove(wObject)
        }
    }

    private fun initPresentation(world: World) {

    }

    fun destroySelectedObjects() {
        stPlane.selectedNodes.forEach {
            val wObject = vmMap[it]!!
            editor.destroyObject(wObject)
        }
    }
}
