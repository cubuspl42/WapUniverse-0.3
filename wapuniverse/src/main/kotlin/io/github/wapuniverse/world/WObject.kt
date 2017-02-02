package io.github.wapuniverse.world

import io.github.wapuniverse.util.Signal
import io.github.wapuniverse.wap32.WwdObject

interface WObject {
    var wwdObject: WwdObject

    val wwdObjectChanged: Signal<WwdObject>
}
