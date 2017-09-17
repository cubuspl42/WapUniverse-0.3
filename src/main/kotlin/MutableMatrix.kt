interface MutableMatrix<E> : Matrix<E> {
    fun set(i: Int, j: Int, element: E)
    fun resize(size: Vec2i)
}