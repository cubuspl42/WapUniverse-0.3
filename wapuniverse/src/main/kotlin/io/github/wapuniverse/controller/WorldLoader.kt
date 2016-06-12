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
        val tileLayer: TileLayer,
        val entityComponent: EntityComponent
)

private fun makeAlphaTileMapper(levelIndex: Int): AlphaTileMapper {
    val fomulaGroup = formulaGroupMap[levelIndex]!!
    return AlphaTileMapper(fomulaGroup)
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

        mainPlane.objects.forEach {
            loadObject(levelIndex, it, tileLayer, entityComponent)
        }

        return World(levelIndex, tileLayer, entityComponent)
    }

    private fun loadObject(levelIndex: Int, obj: WwdObject, tileLayer: TileLayer, entityComponent: EntityComponent) {
        when (obj.logic) {
            "_WU_SmartObject" -> loadSmartObject(levelIndex, obj, tileLayer, entityComponent)
        }
    }

    private fun loadSmartObject(levelIndex: Int, obj: WwdObject, tileLayer: TileLayer, entityComponent: EntityComponent) {
        val scriptMap = scriptMetaMap[levelIndex]!!
        val script = scriptMap[obj.name]!!
        val entity = SmartObject(tileLayer, script)
        entity.position = Vec2i(obj.x, obj.y)
        entity.resize(obj.width, obj.height)
        entityComponent.addEntity(entity)
    }
}
