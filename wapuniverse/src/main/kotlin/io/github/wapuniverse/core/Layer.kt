package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Rectangle2Di
import io.github.wapuniverse.utils.Signal

interface Layer {
    val entities: Collection<Entity>
    val onEntityAdded: Signal<Entity>
    val onEntityRemoved: Signal<Entity>
    val imageSet: String
    fun getTile(i: Int, j: Int): Int
    fun calculateBounds(): Rectangle2Di
    fun getEntitiesByArea(rect: Rectangle2Di): List<Entity>
}
