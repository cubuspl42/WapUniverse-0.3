package io.github.wapuniverse.core

import io.github.wapuniverse.utils.*
import io.github.wapuniverse.wap32.WwdObject

interface Entity {
    var position: Vec2i
    fun dispose()
    val disposed: Signal<Entity>
    fun dump(): WwdObject
}
