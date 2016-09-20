package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.Wwd

class EntitySelectionImpl(
        private val worldEditor: WorldEditor, wv: Vec2i
) : EntitySelection {
    override var area = Rectangle2Di(wv.x, wv.y, 0, 0)

    override fun updateArea(area: Rectangle2Di) {
        this.area = area
        val entities = worldEditor.world.primaryLayer.getEntitiesByArea(area)
        worldEditor.selectEntities(entities)
    }

    override fun abort() {
        // TODO: Revert previous selection (when ESC is pressed)
    }

    override fun commit() {
        worldEditor._clearEntitySelection()
        onCommit._emit(this)
    }

    override val onCommit = Signal<EntitySelection>()
}
