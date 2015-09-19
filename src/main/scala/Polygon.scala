

class Polygon(val leftTop: Vertex, val rightTop: Vertex, val rightBottom: Vertex, val leftBottom: Vertex) {

  val points = List(leftTop, rightTop, rightBottom, leftBottom)

  def contains(vertex: Vertex): Boolean = {
    inBoundingBox(vertex)
  }


  private def inBoundingBox(ver: Vertex): Boolean = {
    if (ver.x < leftTop.x || ver.x > rightBottom.x || ver.y < leftBottom.y || ver.y > rightTop.y) {
      return false
    }
    true
  }

  private def rayCasting(points: List[Vertex], centroid: Vertex): Boolean = {

    var result = 0
    var j = points.size - 1
    for (i <- points.indices) {
      if (points(i).x < centroid.x && points(j).x >= centroid.x ||
        points(j).x < centroid.x && points(i).x >= centroid.x) {
        if (points(i).y + (centroid.x - points(i).x) / (points(j).x - points(i).x) * (points(j).y - points(i).y) < centroid.y) {
          result += 1
        }
      }
      j = i
    }
    result % 2 == 0

  }

  override def toString = points.toString()

}
