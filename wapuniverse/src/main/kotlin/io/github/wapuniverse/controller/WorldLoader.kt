package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.formulaGroupMap
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.lsd.scriptMetaMap
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdObject

val maxLevelIndex = 14

data class World(
        val levelIndex: Int,
        val wwd: Wwd,
        val tileLayer: TileLayer,
        val entityComponent: EntityComponent
)

private fun makeAlphaTileMapper(levelIndex: Int): AlphaTileMapper {
    val fomulaGroup = formulaGroupMap[levelIndex]!!
    return AlphaTileMapper(fomulaGroup)
}

fun makeEntityLoaders(tileLayer: TileLayer): List<EntityLoader> {
    return listOf(SmartObject.Loader(tileLayer))
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
        val tileLayer = TileLayer(alphaTileMapper, mainPlane.imageSets.first())
        val entityComponent = EntityComponent()

        val entityLoaders = makeEntityLoaders(tileLayer)
        mainPlane.objects.forEach {
            loadObject(levelIndex, it, entityLoaders, entityComponent)
        }

        return World(levelIndex, wwd, tileLayer, entityComponent)
    }

    private fun loadObject(
            levelIndex: Int, obj: WwdObject, entityLoaders: List<EntityLoader>, entityComponent: EntityComponent) {
        val loader = entityLoaders.firstOrNull { loader ->
            obj.logic == loader.logicName
        }

        val entity = if (loader != null) {
            loader.load(levelIndex, obj)
        } else {
            WapObject(obj)
        }

        entityComponent.addEntity(entity)
    }
}
