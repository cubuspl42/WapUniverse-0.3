package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd
import java.util.logging.Logger

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

    val onSelectionStarted = Signal<EntitySelection>()

    fun startSelection(wv: Vec2i): EntitySelection {
        assert(_entitySelection == null)
        val es = EntitySelectionImpl(this, wv)
        _entitySelection = es
        onSelectionStarted._emit(es)
        return es
    }
}
