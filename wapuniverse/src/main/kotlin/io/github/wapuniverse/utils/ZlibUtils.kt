package io.github.wapuniverse.utils

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

fun decompress(byteArray: ByteArray, offset: Int, size: Int): ByteArray {
    val inflater = Inflater()
    inflater.setInput(byteArray, offset, size)
    ByteArrayOutputStream(byteArray.size).use { os ->
        val buffer = ByteArray(512 * 1024, { 0 })
        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)
            os.write(buffer, 0, count)
        }
        return os.toByteArray()
    }
}

fun decompress(byteArray: ByteArray, offset: Int): ByteArray {
    return io.github.wapuniverse.utils.decompress(byteArray, offset, byteArray.size - offset)
}

fun compress(byteArray: ByteArray): ByteArray {
    val deflater = Deflater()
    deflater.setInput(byteArray)
    deflater.finish()
    ByteArrayOutputStream(byteArray.size).use { os ->
        val buffer = ByteArray(512 * 1024)
        while (!deflater.finished()) {
            val count = deflater.deflate(buffer)
            os.write(buffer, 0, count)
        }
        return os.toByteArray()
    }
}
