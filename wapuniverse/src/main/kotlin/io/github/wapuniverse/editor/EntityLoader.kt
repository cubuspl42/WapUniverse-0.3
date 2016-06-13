package io.github.wapuniverse.editor

import io.github.wapuniverse.wap32.WwdObject

interface EntityLoader {
    val logicName: String
    fun load(levelIndex: Int, wwdObject: WwdObject): Entity
}
