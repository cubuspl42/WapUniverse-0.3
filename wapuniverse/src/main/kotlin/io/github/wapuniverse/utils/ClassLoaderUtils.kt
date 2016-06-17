package io.github.wapuniverse.utils

import java.io.InputStream

class ResourceNotFound(path: String) : Exception("Resource not found: $path")

fun getResourceAsStream(path: String): InputStream {
    val classloader = Thread.currentThread().contextClassLoader
    val istr = classloader.getResourceAsStream(path)
    return istr ?: throw ResourceNotFound(path)
}
