package io.github.wapuniverse.util


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

    fun resize(m: Int, n: Int): Matrix<T> {
        val mm = Math.min(height, m)
        val nn = Math.min(width, n)

        val rows = mutableListOf<MutableList<T>>()
        for (i in 0..mm - 1) {
            val row = mutableListOf<T>()
            for (j in 0..nn - 1) {
                row.add(getElement(i, j))
            }
            rows.add(row)
        }

        return Matrix<T>(rows)
    }
}

fun <T> makeMatrix(m: Int, n: Int, elements: List<T>): Matrix<T> {
    assert(elements.size == m * n)
    val rows = mutableListOf<List<T>>()
    for (i in 0..m - 1) {
        val row = elements.slice(i * n..(i + 1) * n - 1)
        rows.add(row)
    }
    return Matrix(rows)
}

fun <T> makeMatrix(m: Int, n: Int, vararg elements: T): Matrix<T> {
    return makeMatrix(m, n, elements.asList())
}
