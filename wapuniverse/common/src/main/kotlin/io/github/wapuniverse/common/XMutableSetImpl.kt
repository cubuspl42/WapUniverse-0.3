package io.github.wapuniverse.common

internal class XMutableSetImpl<E>(val backingSet: MutableSet<E>) : MutableSet<E> by backingSet, XMutableSet<E> {
    override fun add(element: E): Boolean {
        val wasAdded = backingSet.add(element)
        if (wasAdded) {
            changed(XSet.Change.ElementAdded(element))
        }
        return wasAdded
    }

    override fun remove(element: E): Boolean {
        val wasRemoved = backingSet.remove(element)
        if (wasRemoved) {
            changed(XSet.Change.ElementRemoved(element))
        }
        return wasRemoved
    }

    override val changed = Emitter<XSet.Change>()
}
