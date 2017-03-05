package io.github.wapuniverse.editor

import io.github.wapuniverse.common.Signal

interface Editor {
    val world: World
}

fun makeEditor(): Editor = EditorImpl()
