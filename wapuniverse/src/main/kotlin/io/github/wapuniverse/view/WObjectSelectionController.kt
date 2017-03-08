package io.github.wapuniverse.view

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import io.github.wapuniverse.CFG_LEVEL_INDEX
import io.github.wapuniverse.ImageSetDatabase
import io.github.wapuniverse.editor.Editor
import io.github.wapuniverse.editor.WObject

class WObjectSelectionController(
        private val editor: Editor,
        private val stPane: StPane,
        private val imageSetDatabase: ImageSetDatabase,
        private val imageMap: ImageMap
) {
    private val mvMap: BiMap<WObject, StNode> = HashBiMap.create();

    private val vmMap = mvMap.inverse()

    init {
        val world = editor.world

        world.objects.forEach { presentWObject(stPane, it) }

        world.onObjectAdded.connect { presentWObject(stPane, it) }
    }

    private fun presentWObject(stPane: StPane, wObject: WObject) {
        val stNode = StNode()
        stPane.addNode(stNode)
        mvMap.put(wObject, stNode)

        wObjectBounds(wObject, CFG_LEVEL_INDEX, imageSetDatabase, imageMap)?.let { stNode.bounds = it }

        wObject.onWwdObjectChanged.connect {
            stNode.bounds = wObjectBounds(wObject, CFG_LEVEL_INDEX, imageSetDatabase, imageMap)!!
        }

        wObject.preRemoved.connect {
            stPane.removeNode(stNode)
            mvMap.remove(wObject)
        }
    }


    fun destroySelectedObjects() {
        stPane.selectedNodes.forEach {
            val wObject = vmMap[it]!!
            editor.destroyObject(wObject)
        }
    }
}
