package io.github.wapuniverse.controller

import io.github.wapuniverse.core.*
import io.github.wapuniverse.lsd.formulaGroupMap
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdObject

val maxLevelIndex = 14

private fun makeAlphaTileMapper(levelIndex: Int): AlphaTileMapper {
    val fomulaGroup = formulaGroupMap[levelIndex]!!
    return AlphaTileMapper(fomulaGroup)
}

fun makeEntityLoaders(layer: LayerImpl): List<EntityLoader> {
    return listOf(AdaptiveEntity.Loader(layer))
}

class WorldLoader() {
    private fun determineLevelIndex(wwd: Wwd): Int {
        return (1..maxLevelIndex).first { i ->
            wwd.header.levelName == "Claw - Level $i"
        }
    }

    fun loadWorld(wwd: Wwd): World {
        val levelIndex = determineLevelIndex(wwd)
        val mainPlane = wwd.mainPlane!!

        val alphaTileMapper = makeAlphaTileMapper(levelIndex)

        val world = World(levelIndex, wwd, alphaTileMapper, mainPlane.imageSets.first())
        val entityLoaders = makeEntityLoaders(world.primaryLayerImpl)

        mainPlane.objects.forEach {
            loadObject(levelIndex, it, entityLoaders, world.primaryLayer)
        }

        return world
    }

    private fun loadObject(
            levelIndex: Int, obj: WwdObject, entityLoaders: List<EntityLoader>, layer: MutableLayer) {
        val loader = entityLoaders.firstOrNull { loader ->
            obj.logic == loader.logicName
        }

        val entity = if (loader != null) {
            loader.load(levelIndex, obj)
        } else {
            WapObject(obj)
        }

        layer.addEntity(entity)
    }
}
