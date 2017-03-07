package io.github.wapuniverse.view

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.editor.WObject

class WObjectSelectionController(
        private val editor: Editor,
        private val stPlane: StPlane,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap
) {
    private val mvMap: BiMap<WObject, StNode> = HashBiMap.create();

    private val vmMap = mvMap.inverse()

    init {
        val world = editor.world

        world.objects.forEach { presentWObject(stPlane, it) }

        world.onObjectAdded.connect { presentWObject(stPlane, it) }
    }

    private fun presentWObject(stPlane: StPlane, wObject: WObject) {
        val stNode = StNode()
        stPlane.addNode(stNode)
        stNode.bounds = wObjectBounds(wObject, CFG_LEVEL_INDEX, imageSetDatabase, imageMap)!!
        mvMap.put(wObject, stNode)

        wObject.preRemoved.connect {
            stPlane.removeNode(stNode)
            mvMap.remove(wObject)
        }
    }


    fun destroySelectedObjects() {
        stPlane.selectedNodes.forEach {
            val wObject = vmMap[it]!!
            editor.destroyObject(wObject)
        }
    }
}
