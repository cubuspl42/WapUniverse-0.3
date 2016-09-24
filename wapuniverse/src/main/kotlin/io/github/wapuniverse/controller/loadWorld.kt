package io.github.wapuniverse.controller

import io.github.wapuniverse.core.*
import io.github.wapuniverse.lsd.formulaMetaMap
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdObject

val maxLevelIndex = 14

fun makeEntityLoaders(): List<EntityLoader> {
    return listOf(AdaptiveEntity.Loader())
}

private fun determineLevelIndex(wwd: Wwd): Int {
    return (1..maxLevelIndex).first { i ->
        wwd.header.levelName == "Claw - Level $i"
    }
}

private fun loadObject(
        levelIndex: Int, wwdObject: WwdObject, entityLoaders: List<EntityLoader>, layer: MutableLayer) {
    val loader = entityLoaders.firstOrNull { loader ->
        wwdObject.logic == loader.logicName
    }

    if (loader != null) {
        val repr = loader.load(levelIndex, wwdObject)
        layer.addEntity(repr)
    } else {
        layer.addEntity(WapObjectRepr(wwdObject))
    }
}

fun loadWorld(wwd: Wwd): World {
    val levelIndex = determineLevelIndex(wwd)
    val mainPlane = wwd.mainPlane!!

    val formulaLevelMap = formulaMetaMap[levelIndex]!!
    val scriptMap = scriptMetaMap[levelIndex]!!
    val world = World(levelIndex, wwd, formulaLevelMap, scriptMap, mainPlane.imageSets.first())
    val entityLoaders = makeEntityLoaders()

    mainPlane.objects.forEach {
        loadObject(levelIndex, it, entityLoaders, world.primaryLayer)
    }

    return world
}
