package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal

interface EntitySelection {
    val area: Rectangle2Di
    fun updateArea(area: Rectangle2Di)
    fun abort()
    fun commit()
    val onCommit: Signal<EntitySelection>
}
