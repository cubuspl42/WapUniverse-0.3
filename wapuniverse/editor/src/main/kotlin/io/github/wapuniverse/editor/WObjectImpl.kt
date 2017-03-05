package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.wap32.WwdObject

class WObjectImpl : WObject {
    override var wwdObject = WwdObject()
        get() = field.clone()
        set(value) {
            field = value.clone()
            wwdObjectChanged(field.clone())
        }

    override val wwdObjectChanged = Emitter<WwdObject>()
}
