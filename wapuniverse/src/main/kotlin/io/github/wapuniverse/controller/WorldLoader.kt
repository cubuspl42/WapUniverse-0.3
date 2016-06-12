package io.github.wapuniverse.controller

import io.github.wapuniverse.editor.*
import io.github.wapuniverse.lsd.level1.blockScript
import io.github.wapuniverse.lsd.level1.ladderScript
import io.github.wapuniverse.lsd.level1.level1FormulaGroup
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd
import io.github.wapuniverse.wap32.WwdObject

val maxLevelIndex = 14

data class World(
        val tileLayer: TileLayer,
        val entityComponent: EntityComponent
)

private fun makeAlphaTileMapper(levelIndex: Int): AlphaTileMapper {
    return AlphaTileMapper(when (levelIndex) {
        1 -> level1FormulaGroup
        else -> throw IllegalArgumentException()
    })
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
            loadObject(it, tileLayer, entityComponent)
        }

        return World(tileLayer, entityComponent)
    }

    private fun loadObject(obj: WwdObject, tileLayer: TileLayer, entityComponent: EntityComponent) {
        when (obj.logic) {
            "_WU_SmartObject" -> loadSmartObject(obj, tileLayer, entityComponent)
        }
    }

    private fun loadSmartObject(obj: WwdObject, tileLayer: TileLayer, entityComponent: EntityComponent) {
        val entity = SmartObject(tileLayer, when(obj.name) {
            "Block" -> blockScript
            "Ladder" -> ladderScript
            else -> throw IllegalArgumentException()
        })
        entity.position = Vec2i(obj.x, obj.y)
        entityComponent.addEntity(entity)
    }
}
