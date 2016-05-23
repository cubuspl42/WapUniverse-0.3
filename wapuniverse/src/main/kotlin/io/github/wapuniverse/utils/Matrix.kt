package io.github.wapuniverse.utils


/**
 * Immutable zero-indexed generic matrix.
 */
class Matrix<out T>(private val rows: List<List<T>> = emptyList<List<T>>()) {
    init {
        assert(rows.all { it.size == rows[0].size })
    }

    val width: Int
        get() = when {
            rows.isEmpty() -> 0
            else -> rows[0].size
        }

    val height: Int
        get() {
            return rows.size
        }

    fun getElement(i: Int, j: Int): T = rows[i][j]
}

fun <T> makeMatrix(m: Int, n: Int, vararg  elements: T): Matrix<T> {
    assert(elements.size == m * n)
    val rows = mutableListOf<List<T>>()
    for (i in 0..m-1) {
        val row = elements.slice(i*n..(i+1)*n)
        rows.add(row)
    }
    return Matrix(rows)
}
