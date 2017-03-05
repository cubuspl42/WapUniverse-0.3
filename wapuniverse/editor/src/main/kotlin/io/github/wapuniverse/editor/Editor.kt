package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Signal

interface Editor {
    val wObjects: Set<WObject>
    val onWObjectAdded: Signal<WObject>
}

fun makeEditor(): Editor = EditorImpl()
