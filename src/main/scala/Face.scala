class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex) {

  val centroid = new Vertex((v1.x+v2.x+v3.x)/3,(v1.y+v2.y+v3.y)/3,(v1.z+v2.z+v3.z)/3)

  def area : Double = {
    val edge1 = v1.distanceTo(v2)
    val edge2 = v2.distanceTo(v3)
    val edge3 = v3.distanceTo(v1)
    val semiperimeter = (edge1+edge2+edge3)/2

    math.sqrt(semiperimeter*(semiperimeter-edge1)*(semiperimeter-edge2)*(semiperimeter-edge3))
  }

  override def toString = "centroid is "+ centroid
}