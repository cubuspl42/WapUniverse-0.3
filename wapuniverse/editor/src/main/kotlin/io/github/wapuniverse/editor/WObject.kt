package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.wap32.WwdObject

interface WObject {
    var wwdObject: WwdObject

    val onWwdObjectChanged: Signal<WwdObject>

    val preRemoved: Signal<Unit>
}
