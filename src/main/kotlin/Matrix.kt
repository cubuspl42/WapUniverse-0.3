interface Matrix<out E> {
    operator fun get(i: Int, j: Int): E
    val size: Vec2i
}