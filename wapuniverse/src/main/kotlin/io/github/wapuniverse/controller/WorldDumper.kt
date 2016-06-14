package io.github.wapuniverse.controller

import io.github.wapuniverse.wap32.Wwd


private val wapUniverseLogicPrefix = "_WU_"

class WorldDumper() {
    fun dumpWorld(world: World): Wwd {
        val wwd = world.wwd.clone()
        val mainPlane = wwd.mainPlane!!

        mainPlane.objects = mainPlane.objects.filter {
            !it.logic.startsWith(wapUniverseLogicPrefix)
        }.toMutableList()

        var nextObjId = mainPlane.objects.map { it.id }.max() ?: 0 + 1
        world.entityComponent.entities.forEach {
            val wwdObject = it.dump()
            wwdObject.id = nextObjId
            ++nextObjId
            mainPlane.objects.add(wwdObject)
        }

        val bounds = world.tileLayer.calculateBounds()
        mainPlane.tilesWide = bounds.maxX + 1
        mainPlane.tilesHigh = bounds.maxY + 1
        mainPlane.tiles.clear()

        for (i in 0..bounds.maxY) {
            for (j in 0..bounds.maxX) {
                val t = world.tileLayer.getTile(i, j)
                mainPlane.tiles.add(t)
            }
        }

        return wwd
    }
}
