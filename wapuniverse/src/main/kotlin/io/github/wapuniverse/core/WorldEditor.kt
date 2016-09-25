package io.github.wapuniverse.core

import com.sun.javafx.geom.Vec2d
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i

class WorldEditor(
        val world: World
) {
    private var _selectedEntities: List<Entity> = listOf<Entity>()

    val selectedEntities: List<Entity>
        get() = _selectedEntities

    fun selectEntities(entities: List<Entity>) {
        _selectedEntities = entities
        System.err.println(_selectedEntities.size)
    }

    private var _entitySelection: EntitySelection? = null

    val entitySelection: EntitySelection?
        get() = _entitySelection

    fun _clearEntitySelection() {
        _entitySelection = null
    }

    var cameraOffset = Vec2d(0.0, 0.0)

    var cameraZoom = 1.0

    val onSelectionStarted = Signal<EntitySelection>()

    fun startSelection(wv: Vec2i): EntitySelection {
        assert(_entitySelection == null)
        val es = EntitySelectionImpl(this, wv)
        _entitySelection = es
        onSelectionStarted._emit(es)
        return es
    }
}
