package io.github.wapuniverse.wap32

import com.google.common.io.ByteStreams
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.wapuniverse.utils.getResourceAsStream
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class WwdTest {
    private fun loadReatail01() = getResourceAsStream("RETAIL01.WWD")

    private fun loadReatail01_uncompressed() = getResourceAsStream("RETAIL01_uncompressed.WWD")

    private fun verifyRetail01(wwd: Wwd) {
        val header = wwd.header

//        assertThat(header.flags.compress, equalTo(true))
        assertThat(header.flags.useZCoords, equalTo(true))
        assertThat(header.levelName, equalTo("Claw - Level 1"))
//        assertThat(header.birth, equalTo("October 28, 1997"))
        assertThat(header.startX, equalTo(689))
        assertThat(header.startY, equalTo(4723))
        assertThat(header.planeCount, equalTo(3))
//        assertThat(header.checksum, equalTo(-327002484))
        assertThat(header.prefix2, equalTo("GAME"))

        val plane0 = wwd.planes[0]
        assertThat(plane0.flags.mainPlane, equalTo(false))
        assertThat(plane0.flags.xWrapping, equalTo(true))
        assertThat(plane0.flags.yWrapping, equalTo(true))
        assertThat(plane0.name, equalTo("Background"))
        assertThat(plane0.tileWidth, equalTo(64))
        assertThat(plane0.tileHeight, equalTo(64))
        assertThat(plane0.zCoord, equalTo(-999))

        val plane1 = wwd.planes[1]
        assertThat(plane1.flags.mainPlane, equalTo(true))
        assertThat(plane1.flags.xWrapping, equalTo(false))
        assertThat(plane1.flags.yWrapping, equalTo(false))
        assertThat(plane1.name, equalTo("Action"))
        assertThat(plane1.tileWidth, equalTo(64))
        assertThat(plane1.tileHeight, equalTo(64))
        assertThat(plane1.zCoord, equalTo(0))
        assertThat(plane1.imageSets, equalTo(listOf("ACTION")))

        val plane2 = wwd.planes[2]
        assertThat(plane2.flags.mainPlane, equalTo(false))
        assertThat(plane2.flags.xWrapping, equalTo(true))
        assertThat(plane2.flags.yWrapping, equalTo(false))
        assertThat(plane2.name, equalTo("Front"))
        assertThat(plane2.tileWidth, equalTo(64))
        assertThat(plane2.tileHeight, equalTo(64))
        assertThat(plane2.zCoord, equalTo(9000))

        val obj1 = plane1.objects.find { it.id == 179 }!!
        assertThat(obj1.logic, equalTo("Rat"))
        assertThat(obj1.imageSet, equalTo("LEVEL_RAT"))
        assertThat(obj1.x, equalTo(5377))
        assertThat(obj1.y, equalTo(4374))
        assertThat(obj1.xMin, equalTo(5357))
        assertThat(obj1.xMax, equalTo(5488))

        val obj2 = plane1.objects.find { it.id == 1303 }!!
        assertThat(obj2.logic, equalTo("TogglePeg2"))
        assertThat(obj2.imageSet, equalTo("LEVEL_PEG"))
        assertThat(obj2.x, equalTo(963))
        assertThat(obj2.y, equalTo(2507))
        assertThat(obj2.xMin, equalTo(0))
        assertThat(obj2.xMax, equalTo(0))

        val td0 = wwd.tileDescriptions[13]
        assertThat(td0.type, equalTo(WAP_TILE_TYPE_SINGLE))
        assertThat(td0.width, equalTo(64))
        assertThat(td0.height, equalTo(64))
    }

    @Test
    fun testLoadWwd() {
        loadReatail01().use {
            val wwd = loadWwd(it)
            verifyRetail01(wwd)
        }
    }

    @Test
    fun testDumpWwdHeader() {
        loadReatail01().use {
            val wwd = loadWwd(it)
            val byteArrayStream = ByteArrayOutputStream()
            dumpWwdHeader(WwdOutputStream(byteArrayStream), wwd)
            val dumpedWwdHeaderBuffer = byteArrayStream.toByteArray()

            assertThat(dumpedWwdHeaderBuffer.size, equalTo(WAP_WWD_HEADER_SIZE))
        }
    }

    @Test
    fun testDumpWwd() {
        loadReatail01_uncompressed().use {
            val loadedWwdBuffer = ByteStreams.toByteArray(it)
            val loadedWwd = loadWwd(ByteArrayInputStream(loadedWwdBuffer))
            val byteArrayStream = ByteArrayOutputStream()
            dumpWwd(byteArrayStream, loadedWwd)
            val dumpedWwdBuffer = byteArrayStream.toByteArray()
            val loadedWwd2 = loadWwd(ByteArrayInputStream(dumpedWwdBuffer))

            verifyRetail01(loadedWwd)
            verifyRetail01(loadedWwd2)
        }
    }

    @Test
    fun testCloneWwd() {
        loadReatail01().use {
            val loadedWwd = loadWwd(it)
            val byteArrayStream = ByteArrayOutputStream()
            dumpWwd(byteArrayStream, loadedWwd)

            val clonedWwd = loadedWwd.clone()
            val byteArrayStream2 = ByteArrayOutputStream()
            dumpWwd(byteArrayStream2, clonedWwd)

            val buffer1 = byteArrayStream.toByteArray().toList()
            val buffer2 = byteArrayStream2.toByteArray().toList()

            assertThat(buffer2, equalTo(buffer1))
        }
    }
}
