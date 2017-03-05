package io.github.wapuniverse.editor

interface Editor {
    val world: World
}

fun makeEditor(): Editor = EditorImpl()
