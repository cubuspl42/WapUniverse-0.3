package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Emitter
import io.github.wapuniverse.common.Signal

internal class EditorImpl : Editor {
    private val wObjectsSet = hashSetOf<WObject>()

    private val emitOnWObjectAdded = Emitter<WObject>()

    override val wObjects: Set<WObject> = wObjectsSet

    override val onWObjectAdded: Signal<WObject> = emitOnWObjectAdded
}
