package io.github.wapuniverse.utils

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.nio.charset.StandardCharsets.UTF_8


class ZlibUtilsTest {
    val helloString = "Hello"
    val helloBytesList = helloString.toByteArray(UTF_8).toList()
    val expectedCompressedBytes = listOf(120, -100, -13, 72, -51, -55, -55, 7, 0, 5, -116, 1, -11)
    val expectedCompressedBytesList = expectedCompressedBytes.map { it.toByte() }
    val expectedCompressedBytesArray = expectedCompressedBytesList.toByteArray()

    @Test
    fun decompressTest() {
        val decompressedBytes = decompress(expectedCompressedBytesArray, 0).toList()

        assertThat(decompressedBytes, equalTo(helloBytesList))
    }

    @Test
    fun compressTest() {
        val compressedBytes = compress(helloBytesList.toByteArray())

        assertThat(compressedBytes.toList(), equalTo(expectedCompressedBytesList))

        val decompressedBytes = decompress(compressedBytes, 0).toList()
        assertThat(decompressedBytes, equalTo(helloBytesList))
    }
}
