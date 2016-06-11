package io.github.wapuniverse.view

import io.github.wapuniverse.editor.Entity
import io.github.wapuniverse.utils.Signal
import javafx.geometry.Rectangle2D
import kotlin.properties.Delegates.observable
import kotlin.properties.ReadWriteProperty


private fun _observable(s: SBox, ts: Signal<SBox>, fs: Signal<SBox>): ReadWriteProperty<SBox, Boolean> {
    return observable(false, { p, o, n ->
        if (n != o) {
            if (n) ts._emit(s)
            else fs._emit(s)
        }
    })
}

class SBox(val entity: Entity, var boundingRect: Rectangle2D) {
    val hovered = Signal<SBox>()

    val unhovered = Signal<SBox>()

    var isHovered: Boolean by _observable(this, hovered, unhovered)

    val selected = Signal<SBox>()

    val unselected = Signal<SBox>()

    var isSelected: Boolean by _observable(this, selected, unselected)
}
