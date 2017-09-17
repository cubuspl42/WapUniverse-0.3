import wap32.Wwd
import wap32.WwdObject
import wap32.WwdPlane

fun buildWorld(wwd: Wwd): World {
    val planes = wwd.planes
            .map { buildPlane(it) }
            .toMutableList()
    return WorldImpl(planes)
}

fun buildPlane(plane: WwdPlane): PlaneImpl {
    val objects = plane.objects
            .map { buildObject(it) }
            .toMutableList()
    val tiles = MutableMatrix(Vec2i(plane.tilesWide, plane.tilesHigh)) { i, j ->
        plane.getTile(i, j)
    }
    return PlaneImpl(objects, tiles)
}

fun buildObject(wwdObject: WwdObject): WObject {
    return WObjectImpl(wwdObject.id, wwdObject.imageSet, wwdObject.x, wwdObject.y)
}
