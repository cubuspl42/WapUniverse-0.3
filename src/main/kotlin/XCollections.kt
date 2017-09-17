inline fun <E> MutableMatrix(size: Vec2i, init: (i: Int, j: Int) -> E): MutableMatrix<E> {
    val data: MutableList<MutableList<E>> = (0 until size.height).map { i ->
        (0 until size.width).map { j -> init(i, j) }.toMutableList()
    }.toMutableList()
    return MutableMatrixImpl(data)
}
