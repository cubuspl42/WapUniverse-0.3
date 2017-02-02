package io.github.wapuniverse.world

import io.github.wapuniverse.util.Signal
import io.github.wapuniverse.wap32.WwdObject

class WObjectImpl : WObject {
    override var wwdObject = WwdObject()
        get() = field.clone()
        set(value) {
            field = value.clone()
            wwdObjectChanged._emit(field.clone())
        }

    override val wwdObjectChanged = Signal<WwdObject>()
}
