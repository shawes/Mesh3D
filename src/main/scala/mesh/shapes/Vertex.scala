package mesh.shapes

class Vertex(val x: Double, val y:Double, val z:Double) {

  def distanceTo(toVertex: Vertex): Double =
    math.sqrt(math.pow(toVertex.x - x,2)+ math.pow(toVertex.y - y,2) + math.pow(toVertex.z - z,2))

  def distanceXY(toVertex: Vertex): Double =
    math.sqrt(math.pow(toVertex.x - x,2)+ math.pow(toVertex.y - y,2))

  override def toString = "x=" + x + ", y=" + y + ", z=" + z

}