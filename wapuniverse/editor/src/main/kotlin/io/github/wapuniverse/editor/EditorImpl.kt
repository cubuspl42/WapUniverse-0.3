package io.github.wapuniverse.editor

import io.github.wapuniverse.common.wap32.Wwd

internal class EditorImpl(wwd: Wwd) : Editor {
    override val world: World = WorldImpl(wwd)
}
