import scala.collection.mutable.ArrayBuffer

class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex) {

  val centroid = new Vertex((v1.x+v2.x+v3.x)/3,(v1.y+v2.y+v3.y)/3,0)
  var used = false

  val polys = new ArrayBuffer[Polygon]

  def area : Double = {
    val edge1 = v1.distanceTo(v2)
    val edge2 = v2.distanceTo(v3)
    val edge3 = v3.distanceTo(v1)
    val semiperimeter = (edge1+edge2+edge3)/2

    math.sqrt(semiperimeter*(semiperimeter-edge1)*(semiperimeter-edge2)*(semiperimeter-edge3))
  }

  def area2D : Double = {
    val edge1 = v1.distanceXY(v2)
    val edge2 = v2.distanceXY(v3)
    val edge3 = v3.distanceXY(v1)
    val semiperimeter = (edge1+edge2+edge3)/2

    math.sqrt(semiperimeter*(semiperimeter-edge1)*(semiperimeter-edge2)*(semiperimeter-edge3))
  }

  override def toString = "centroid is " + centroid
}