package io.github.wapuniverse.wap32

import com.google.common.io.ByteStreams
import com.google.common.primitives.Bytes
import io.github.wapuniverse.utils.FlagProperty
import io.github.wapuniverse.utils.decompress
import io.github.wapuniverse.utils.flagProperty
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.US_ASCII
import java.util.zip.Inflater
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

val WAP_WWD_HEADER_SIZE = 1524
val WAP_WWD_PLANE_DESCRIPTION_SIZE = 160
val WAP_WWD_OBJECT_DESCRIPTION_SIZE = 284

data class WwdRect(
        var left: Int = 0,
        var top: Int = 0,
        var right: Int = 0,
        var bottom: Int = 0
)

class WwdStream(private val inputStream: InputStream) {
    fun expectInt(expectedValue: Int) {
        val value = readInt()
        assert(value == expectedValue)
    }

    fun readInt(): Int {
        val bytes = ByteArray(4, { 0 })
        inputStream.read(bytes, 0, bytes.size)
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
    }

    fun readFixedString(bufferSize: Int): String {
        val bytes = ByteArray(bufferSize, { 0 })
        inputStream.read(bytes, 0, bytes.size)
        return bytes.toString(StandardCharsets.US_ASCII).replace("\u0000", "")
    }

    fun readStaticString(bufferSize: Int): String {
        val s = readFixedString(bufferSize - 1)
        inputStream.read() // skip '\0'
        return s
    }

    fun skip(n: Long) {
        inputStream.skip(n)
    }

    fun use(function: (WwdStream) -> Unit) {
        return inputStream.use {
            function(this)
        }
    }

    fun readNullTerminatedString(): String {
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(1, { 0 })
        while (true) {
            inputStream.read(buffer)
            if (buffer[0] <= 0) break
            os.write(buffer)
        }
        return os.toByteArray().toString(StandardCharsets.US_ASCII)
    }

    fun readRect(): WwdRect {
        return WwdRect(readInt(), readInt(), readInt(), readInt())
    }
}

private fun subStream(wwdBuffer: ByteArray, offset: Int): WwdStream {
    return WwdStream(ByteArrayInputStream(wwdBuffer, offset, wwdBuffer.size - offset))
}

class Wwd {
    var header = WwdHeader()
    var planes = mutableListOf<WwdPlane>()
    var tileDescriptions = mutableListOf<WwdTileDescription>()
}

class WwdHeader {
    companion object {
        val levelNameBufferSize = 64
        val authorBufferSize = 64
        val birthBufferSize = 64
        val rezFileBufferSize = 256
        val imageDirBufferSize = 128
        val palRezBufferSize = 128
        val launchAppBufferSize = 128
        val imageSetBufferSize = 128
        val prefixBufferSize = 32
    }

    class Flags {
        var dword = 0
        var useZCoords: Boolean by flagProperty(this, Flags::dword, 0)
        var compress: Boolean by flagProperty(this, Flags::dword, 1)
    }

    var flags = Flags()
    var levelName = ""
    var author = ""
    var birth = ""
    var rezFile = ""
    var imageDir = ""
    var palRez = ""
    var startX = 0
    var startY = 0
    var planeCount = 0
    var mainBlockOffset = 0
    var tileDescriptionsOffset = 0
    var decompressedMainBlockSize = 0
    var checksum = 0
    var launchApp = ""
    var imageSet1 = ""
    var imageSet2 = ""
    var imageSet3 = ""
    var imageSet4 = ""
    var prefix1 = ""
    var prefix2 = ""
    var prefix3 = ""
    var prefix4 = ""
}

class WwdPlane {
    companion object {
        val nameBufferSize = 64
    }

    class Flags {
        var dword = 0
        var mainPlane: Boolean by flagProperty(this, Flags::dword, 0)
        var noDraw: Boolean by flagProperty(this, Flags::dword, 1)
        var xWrapping: Boolean by flagProperty(this, Flags::dword, 2)
        var yWrapping: Boolean by flagProperty(this, Flags::dword, 3)
        var autoTileSize: Boolean by flagProperty(this, Flags::dword, 4)
    }

    var flags = Flags()
    var name = ""
    var tileWidth = 0  /* tile's width in pixels */
    var tileHeight = 0 /* tile's height in pixels */
    var movementXPercent = 0
    var movementYPercent = 0
    var fillColor = 0
    var imageSetCount = 0 // *
    var objectCount = 0 // *
    var tilesOffset = 0 // *
    var imageSetsOffset = 0 // *
    var objectsOffset = 0 // *
    var zCoord = 0
    var tilesWide = 0
    var tilesHigh = 0
    var tiles = mutableListOf<Int>()
    var imageSets = mutableListOf<String>()
    var objects = mutableListOf<WwdObject>()
}

class WwdObject {
    class AddFlags {
        var dword = 0
        var difficult: Boolean by flagProperty(this, AddFlags::dword, 0)
        var eyeCandy: Boolean by flagProperty(this, AddFlags::dword, 1)
        var highDetail: Boolean by flagProperty(this, AddFlags::dword, 2)
        var multiplayer: Boolean by flagProperty(this, AddFlags::dword, 3)
        var extraMemory: Boolean by flagProperty(this, AddFlags::dword, 4)
        var fastCpu: Boolean by flagProperty(this, AddFlags::dword, 5)
    }

    class DynamicFlags {
        var dword = 0
        // TODO
    }

    class DrawFlags {
        var dword = 0
        // TODO
    }

    class UserFlags {
        var dword = 0
        // TODO
    }

    var id = 0
    var x = 0
    var y = 0
    var z = 0
    var i = 0
    var addFlags = AddFlags()     /* WAP_OBJECT_ADD_FLAG_ flags */
    var dynamicFlags = DynamicFlags() /* WAP_OBJECT_DYNAMIC_FLAG_ flags */
    var drawFlags = DrawFlags()    /* WAP_OBJECT_DRAW_FLAG_ flags */
    var userFlags = UserFlags()    /* WAP_OBJECT_USER_FLAG_ flags */
    var score = 0
    var points = 0
    var powerup = 0
    var damage = 0
    var smarts = 0
    var health = 0
    var moveRect = WwdRect()
    var hitRect = WwdRect()
    var attackRect = WwdRect()
    var clipRect = WwdRect()
    var userRect1 = WwdRect()
    var userRect2 = WwdRect()
    var userValue1 = 0
    var userValue2 = 0
    var userValue3 = 0
    var userValue4 = 0
    var userValue5 = 0
    var userValue6 = 0
    var userValue7 = 0
    var userValue8 = 0
    var xMin = 0
    var yMin = 0
    var xMax = 0
    var yMax = 0
    var speedX = 0
    var speedY = 0
    var xTweak = 0
    var yTweak = 0
    var counter = 0
    var speed = 0
    var width = 0
    var height = 0
    var direction = 0
    var faceDir = 0
    var timeDelay = 0
    var frameDelay = 0
    var objectType = 0 /* WAP_OBJECT_TYPE_ single value */
    var hitTypeFlags = 0 /* WAP_OBJECT_TYPE_ flags */
    var xMoveRes = 0
    var yMoveRes = 0
    var name = ""
    var logic = ""
    var imageSet = ""
    var animation = ""
}

class WwdTileDescription {
    var type = 0         /* WAP_TILE_TYPE_ single value */
    var width = 0        /* in pixels */
    var height = 0       /* in pixels */
    var insideAttrib = 0 /* WAP_TILE_ATTRIBUTE_ */
    /* outside_attrib and rect only if type == WAP_TILE_TYPE_DOUBLE */
    var outsideAttrib = 0 /* WAP_TILE_ATTRIBUTE_ */
    var rect = WwdRect()
}


fun loadWwd(wwdStream: InputStream): Wwd {
    val wwdBuffer = ByteStreams.toByteArray(wwdStream)
    val headerBuffer = wwdBuffer.sliceArray(0..WAP_WWD_HEADER_SIZE - 1)
    val header = loadWwdHeader(ByteArrayInputStream(headerBuffer))
    if (header.flags.compress) {
        val mainBlockBuffer = decompress(wwdBuffer, header.mainBlockOffset)
        val wwdBuffer2 = Bytes.concat(headerBuffer, mainBlockBuffer)
        return loadMainBlock(header, wwdBuffer2)
    } else {
        return loadMainBlock(header, wwdBuffer)
    }
}

fun loadWwdHeader(inputStream: InputStream): WwdHeader {
    val stream = WwdStream(inputStream)
    val header = WwdHeader()

    stream.expectInt(WAP_WWD_HEADER_SIZE);
    stream.expectInt(0)
    header.flags.dword = stream.readInt()
    stream.expectInt(0)
    header.levelName = stream.readStaticString(WwdHeader.levelNameBufferSize)
    header.author = stream.readStaticString(WwdHeader.authorBufferSize)
    header.birth = stream.readStaticString(WwdHeader.birthBufferSize)
    header.rezFile = stream.readStaticString(WwdHeader.rezFileBufferSize)
    header.imageDir = stream.readStaticString(WwdHeader.imageDirBufferSize)
    header.palRez = stream.readStaticString(WwdHeader.palRezBufferSize)
    header.startX = stream.readInt()
    header.startY = stream.readInt()
    stream.skip(4)
    header.planeCount = stream.readInt()
    header.mainBlockOffset = stream.readInt()
    header.tileDescriptionsOffset = stream.readInt()
    header.decompressedMainBlockSize = stream.readInt()
    header.checksum = stream.readInt()
    header.launchApp = stream.readStaticString(WwdHeader.launchAppBufferSize)
    header.imageSet1 = stream.readStaticString(WwdHeader.imageSetBufferSize)
    header.imageSet2 = stream.readStaticString(WwdHeader.imageSetBufferSize)
    header.imageSet3 = stream.readStaticString(WwdHeader.imageSetBufferSize)
    header.imageSet4 = stream.readStaticString(WwdHeader.imageSetBufferSize)
    header.prefix1 = stream.readStaticString(WwdHeader.prefixBufferSize)
    header.prefix2 = stream.readStaticString(WwdHeader.prefixBufferSize)
    header.prefix3 = stream.readStaticString(WwdHeader.prefixBufferSize)
    header.prefix4 = stream.readStaticString(WwdHeader.prefixBufferSize)

    return header
}

fun loadMainBlock(header: WwdHeader, wwdBuffer: ByteArray): Wwd {
    val wwd = Wwd()
    wwd.header = header
    loadPlanes(wwdBuffer, wwd)
    loadTileDescriptions(wwdBuffer, wwd)
    return wwd
}

fun loadPlanes(wwdBuffer: ByteArray, wwd: Wwd) {
    subStream(wwdBuffer, wwd.header.mainBlockOffset).use { stream ->
        wwd.planes = (1..wwd.header.planeCount).map {
            loadPlaneHeader(stream)
        }.toMutableList()
        wwd.planes.forEach { loadTiles(stream, it) }
        wwd.planes.forEach { loadImageSets(stream, it) }
        wwd.planes.forEach { loadObjects(stream, it) }
    }
}

fun loadPlaneHeader(stream: WwdStream): WwdPlane {
    val plane = WwdPlane()

    stream.expectInt(160)
    stream.expectInt(0)
    plane.flags.dword = stream.readInt()
    stream.expectInt(0)
    plane.name = stream.readStaticString(WwdPlane.nameBufferSize)
    val widthPx = stream.readInt()
    val heightPx = stream.readInt()
    plane.tileWidth = stream.readInt()
    plane.tileHeight = stream.readInt()
    plane.tilesWide = stream.readInt()
    plane.tilesHigh = stream.readInt()
    stream.expectInt(0)
    stream.expectInt(0)
    plane.movementXPercent = stream.readInt()
    plane.movementYPercent = stream.readInt()
    plane.fillColor = stream.readInt()
    plane.imageSetCount = stream.readInt()
    plane.objectCount = stream.readInt()
    plane.tilesOffset = stream.readInt()
    plane.imageSetsOffset = stream.readInt()
    plane.objectsOffset = stream.readInt()
    plane.zCoord = stream.readInt()
    stream.expectInt(0)
    stream.expectInt(0)
    stream.expectInt(0)

    return plane
}

fun loadTiles(stream: WwdStream, plane: WwdPlane) {
    for (i in 0..plane.tilesHigh - 1) {
        for (j in 0..plane.tilesWide - 1) {
            stream.readInt() // TODO: Tile loading
        }
    }
}

fun loadImageSets(stream: WwdStream, plane: WwdPlane) {
    plane.imageSets = (1..plane.imageSetCount).map {
        stream.readNullTerminatedString()
    }.toMutableList()
}

fun loadObjects(stream: WwdStream, plane: WwdPlane) {
    plane.objects = (1..plane.objectCount).map {
        loadObject(stream)
    }.toMutableList()
}

fun loadObject(stream: WwdStream): WwdObject {
    val obj = WwdObject()

    obj.id = stream.readInt()
    val nameLen = stream.readInt()
    val logicLen = stream.readInt()
    val imageSetLen = stream.readInt()
    val animationLen = stream.readInt()

    obj.x = stream.readInt()
    obj.y = stream.readInt()
    obj.z = stream.readInt()
    obj.i = stream.readInt()
    obj.addFlags.dword = stream.readInt()
    obj.dynamicFlags.dword = stream.readInt()
    obj.drawFlags.dword = stream.readInt()
    obj.userFlags.dword = stream.readInt()
    obj.score = stream.readInt()
    obj.points = stream.readInt()
    obj.powerup = stream.readInt()
    obj.damage = stream.readInt()
    obj.smarts = stream.readInt()
    obj.health = stream.readInt()
    obj.moveRect = stream.readRect()
    obj.hitRect = stream.readRect()
    obj.attackRect = stream.readRect()
    obj.clipRect = stream.readRect()
    obj.userRect1 = stream.readRect()
    obj.userRect2 = stream.readRect()
    obj.userValue1 = stream.readInt()
    obj.userValue2 = stream.readInt()
    obj.userValue3 = stream.readInt()
    obj.userValue4 = stream.readInt()
    obj.userValue5 = stream.readInt()
    obj.userValue6 = stream.readInt()
    obj.userValue7 = stream.readInt()
    obj.userValue8 = stream.readInt()
    obj.xMin = stream.readInt()
    obj.yMin = stream.readInt()
    obj.xMax = stream.readInt()
    obj.yMax = stream.readInt()
    obj.speedX = stream.readInt()
    obj.speedY = stream.readInt()
    obj.xTweak = stream.readInt()
    obj.yTweak = stream.readInt()
    obj.counter = stream.readInt()
    obj.speed = stream.readInt()
    obj.width = stream.readInt()
    obj.height = stream.readInt()
    obj.direction = stream.readInt()
    obj.faceDir = stream.readInt()
    obj.timeDelay = stream.readInt()
    obj.frameDelay = stream.readInt()
    obj.objectType = stream.readInt()
    obj.hitTypeFlags = stream.readInt()
    obj.xMoveRes = stream.readInt()
    obj.yMoveRes = stream.readInt()

    obj.name = stream.readFixedString(nameLen)
    obj.logic = stream.readFixedString(logicLen)
    obj.imageSet = stream.readFixedString(imageSetLen)
    obj.animation = stream.readFixedString(animationLen)

    return obj
}

fun loadTileDescriptions(wwdBuffer: ByteArray, wwd: Wwd) {
    // TODO
}
