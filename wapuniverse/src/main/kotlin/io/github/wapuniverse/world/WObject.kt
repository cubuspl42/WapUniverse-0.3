package io.github.wapuniverse.world

import io.github.wapuniverse.common.Signal
import io.github.wapuniverse.common.wap32.WwdObject

interface WObject {
    var wwdObject: WwdObject

    val wwdObjectChanged: Signal<WwdObject>
}
