package io.github.wapuniverse.common

interface XSet<out E> : Set<E> {
    sealed class Change {
        class ElementAdded<E>(val addedElement: E) : Change()
        class ElementRemoved<E>(val removedElement: E) : Change()
    }

    val changed: Signal<Change>
}
