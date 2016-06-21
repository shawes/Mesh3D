package mesh.shapes

class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex) {

  val centroid = new Vertex((v1.x+v2.x+v3.x)/3,(v1.y+v2.y+v3.y)/3,0)

  def area3D: Double = {
    val edge1 = v1.distanceToXYZ(v2)
    val edge2 = v2.distanceToXYZ(v3)
    val edge3 = v3.distanceToXYZ(v1)
    getArea(edge1, edge2, edge3)
  }

  private def getArea(edge1: Double, edge2: Double, edge3: Double): Double = {
    val semiperimeter: Double = (edge1 + edge2 + edge3) * 0.5
    val result = math.sqrt(semiperimeter * (semiperimeter - edge1) * (semiperimeter - edge2) * (semiperimeter - edge3))
    if (result == Double.NaN) {
      println("NaN encountered with edges :" + edge1 + ", " + edge2 + ", " + edge3)
    }
    result

  }

  def area2D: Double = {
    val edge1 = v1.distanceToXY(v2)
    val edge2 = v2.distanceToXY(v3)
    val edge3 = v3.distanceToXY(v1)
    getArea(edge1, edge2, edge3)
  }

  override def toString = "centroid is " + centroid
}