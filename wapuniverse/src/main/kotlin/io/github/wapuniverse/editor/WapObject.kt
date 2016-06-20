package io.github.wapuniverse.editor

import io.github.wapuniverse.utils.Signal
import io.github.wapuniverse.utils.Vec2i
import io.github.wapuniverse.wap32.WwdObject

val wapObjectLogicName = "_WU_WapObject"

class WapObject(val wwdObject: WwdObject) : Entity() {
    override fun dump(): WwdObject {
        return wwdObject.clone()
    }

    override var position: Vec2i
        get() = Vec2i(wwdObject.x, wwdObject.y)
        set(value) {
            wwdObject.x = value.x
            wwdObject.y = value.y
            changed._emit(this)
        }

    val changed = Signal<WapObject>()
}
