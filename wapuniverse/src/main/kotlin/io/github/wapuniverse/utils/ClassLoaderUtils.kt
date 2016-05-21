package io.github.wapuniverse.utils

import java.io.InputStream


fun getResourceAsStream(path: String): InputStream {
    val classloader = Thread.currentThread().contextClassLoader
    return classloader.getResourceAsStream(path)
}
