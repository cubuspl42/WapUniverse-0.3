class MutableMatrixImpl<E>(private val data: MutableList<MutableList<E>>) : MutableMatrix<E> {
    override var size: Vec2i = Vec2i(data.getOrNull(0)?.size ?: 0, data.size)
        private set

    override fun get(i: Int, j: Int): E = data[i][j]

    override fun set(i: Int, j: Int, element: E) {
        data[i][j] = element
    }

    override fun resize(size: Vec2i) {
        TODO()
    }
}