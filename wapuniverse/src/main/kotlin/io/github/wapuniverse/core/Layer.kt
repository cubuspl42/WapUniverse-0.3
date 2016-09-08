package io.github.wapuniverse.core

import io.github.wapuniverse.utils.Signal

interface Layer {
    val entities: Collection<Entity>
    val onEntityAdded: Signal<Entity>
    val onEntityRemoved: Signal<Entity>
}
