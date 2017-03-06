package io.github.wapuniverse.editor

import io.github.wapuniverse.common.wap32.Wwd

internal class EditorImpl(wwd: Wwd) : Editor {
    private val worldImpl = WorldImpl(wwd)

    override val world: World = worldImpl

    override fun destroyObject(obj: WObject) {
        worldImpl.removeObject(obj)
    }
}
