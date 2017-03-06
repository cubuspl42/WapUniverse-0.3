package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.wap32.WwdObject

internal class WObjectImpl : WObject {
    override var wwdObject = WwdObject()
        get() = field.clone()
        set(value) {
            field = value.clone()
            onWwdObjectChanged(field.clone())
        }

    override val onWwdObjectChanged = Emitter<WwdObject>()

    override val preRemoved = Emitter<Unit>()
}
