package mesh.shapes

class Vertex(val x: Double, val y:Double, val z:Double) {

  def distanceToXYZ(toVertex: Vertex): Double =
    math.sqrt(math.pow(toVertex.x - x,2)+ math.pow(toVertex.y - y,2) + math.pow(toVertex.z - z,2))

  def distanceToXY(toVertex: Vertex): Double =
    math.sqrt(math.pow(toVertex.x - x,2)+ math.pow(toVertex.y - y,2))

  override def toString = "x=" + x + ", y=" + y + ", z=" + z

  override def equals(that: Any): Boolean = that match {
    case that: Vertex => that.x == this.x && that.y == this.y && that.z == this.z
    case _ => false
  }

  override def hashCode: Int = x.hashCode() + y.hashCode() + z.hashCode()



}