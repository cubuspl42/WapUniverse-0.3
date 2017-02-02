package io.github.wapuniverse.view


class WvScene {
    private val _nodes = mutableSetOf<WvNode>()

    val nodes = _nodes

    fun addNode(node: WvNode) {
        _nodes.add(node)
    }

    fun removeNode(node: WvNode) {
        _nodes.remove(node)
    }
}
