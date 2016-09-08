package io.github.wapuniverse.core

import io.github.wapuniverse.wap32.WwdObject

interface EntityLoader {
    val logicName: String
    fun load(levelIndex: Int, wwdObject: WwdObject): EntityRepr
}
